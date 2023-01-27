package com.bitheads.braincloud.comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IRelayCallback;
import com.bitheads.braincloud.client.IRelayConnectCallback;
import com.bitheads.braincloud.client.IRelaySystemCallback;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.RelayConnectionType;
import com.bitheads.braincloud.client.RelayConnectionType;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.services.AuthenticationService;

public class RelayComms {

    enum RelayCallbackType {
        ConnectSuccess,
        ConnectFailure,
        Relay,
        System
    }

    private final boolean VERBOSE_LOG = true;

    private final int CONTROL_BYTES_SIZE    = 1;
    private final int CHANNEL_COUNT         = 4;
    private final int MAX_PACKET_ID_HISTORY = 60 * 10; // So we last 10 seconds at 60 fps

    private final int MAX_PLAYERS       = 40;
    private final int INVALID_NET_ID    = MAX_PLAYERS;

    // Messages sent from Client to Relay-Server
    private final int CL2RS_CONNECT     = 0;
    private final int CL2RS_DISCONNECT  = 1;
    private final int CL2RS_RELAY       = 2;
    private final int CL2RS_ACK         = 3;
    private final int CL2RS_PING        = 4;
    private final int CL2RS_RSMG_ACK    = 5;

    // Messages sent from Relay-Server to Client
    private final int RS2CL_RSMG        = 0;
    private final int RS2CL_DISCONNECT  = 1;
    private final int RS2CL_RELAY       = 2;
    private final int RS2CL_ACK         = 3;
    private final int RS2CL_PONG        = 4;

    private final int RELIABLE_BIT = 0x8000;
    private final int ORDERED_BIT  = 0x4000;

    private final long CONNECT_RESEND_INTERVAL_MS = 500;

    private final int MAX_PACKET_ID = 0xFFF;
    private final int PACKET_LOWER_THRESHOLD = MAX_PACKET_ID * 25 / 100;
    private final int PACKET_HIGHER_THRESHOLD = MAX_PACKET_ID * 75 / 100;
        
    private final int MAX_PACKET_SIZE = 1024;
    private final int TIMEOUT_MS = 10000;

    private class RelayCallback {
        public RelayCallbackType _type;
        public String _message;
        public JSONObject _json;
        public int _netId;
        public byte[] _data;

        RelayCallback(RelayCallbackType type) {
            _type = type;
        }

        RelayCallback(RelayCallbackType type, String message) {
            _type = type;
            _message = message;
        }

        RelayCallback(RelayCallbackType type, JSONObject json) {
            _type = type;
            _json = json;
        }

        RelayCallback(RelayCallbackType type, int netId, byte[] data) {
            _type = type;
            _netId = netId;
            _data = data;
        }
    }

    private class ConnectInfo {
        public String _passcode;
        public String _lobbyId;

        ConnectInfo(String passcode, String lobbyId) {
            _passcode = passcode;
            _lobbyId = lobbyId;
        }
    }

    private class WSClient extends WebSocketClient {
        public WSClient(String ip) throws Exception {
            super(new URI(ip));
        }
        
        @Override
        public void onMessage(String message) {
            onRecv(ByteBuffer.wrap(message.getBytes()));
        }
        
        @Override
        public void onMessage(ByteBuffer bytes) {
            onRecv(bytes);
        }

        @Override
        public void onOpen(ServerHandshake handshake) {
            System.out.println("Relay WS Connected");
            onWSConnected();
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Relay WS onClose: " + reason + ", code: " + Integer.toString(code) + ", remote: " + Boolean.toString(remote));
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "webSocket onClose: " + reason));
            }
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "webSocket onError"));
            }
        }
    }

    public class UdpRsmgPacket {
        public int id;
        public JSONObject json;
    }

    class RelayPacket {
        public int packetId;
        public int netId;
        public byte[] data;

        public RelayPacket(int _packetId, int _netId, byte[] _data) {
            packetId = _packetId;
            netId = _netId;
            data = _data;
        }
    }

    class Reliable {
        public ByteBuffer buffer;
        public int packetId;
        public long ackId;
        public long sendTimeMs;
        public long resendTimeMs;
        public long waitTimeMs;

        public Reliable(ByteBuffer _buffer, long _ackId, int _packetId, int channel) {
            buffer = _buffer;
            ackId = _ackId;
            packetId = _packetId;
            sendTimeMs = System.currentTimeMillis();
            resendTimeMs = sendTimeMs;
            waitTimeMs = channel <= 1 ? 50 : channel == 2 ? 150 : 250;
        }
    }

    private ArrayList<Reliable> _reliables = new ArrayList<Reliable>();
    private ArrayList<UdpRsmgPacket> _udpRsmgPackets = new ArrayList<UdpRsmgPacket>();
    private ArrayList<Integer> _rsmgHistory = new ArrayList<Integer>();
    private int _nextExpectedUdpRsmgPacketId = 0;

    private BrainCloudClient _client;
    private boolean _loggingEnabled = false;
    private IRelayConnectCallback _connectCallback = null;
    private ArrayList<RelayCallback> _callbackEventQueue = new ArrayList<RelayCallback>();

    private boolean _isConnected = false;
    private boolean _isConnecting = false;
    private long _lastConnectTryTime = 0;
    private int _netId = -1;
    private String _ownerCxId = null;
    private ConnectInfo _connectInfo = null;

    private HashMap<Integer, String> _netIdToCxId = new HashMap<Integer, String>();
    private HashMap<String, Integer> _cxIdToNetId = new HashMap<String, Integer>();

    private HashMap<Long, Integer> _sendPacketId = new HashMap<Long, Integer>();
    private HashMap<Long, Integer> _recvPacketId = new HashMap<Long, Integer>();
    private HashMap<Long, ArrayList<RelayPacket>> _orderedReliablePackets = new HashMap<Long, ArrayList<RelayPacket>>();
    
    private int _ping = 999;
    private int _pingIntervalMS = 1000;
    private long _lastPingTime = 0;
    private long _lastRecvTime = 0;
    
    private RelayConnectionType _connectionType = RelayConnectionType.WEBSOCKET;
    private WSClient _webSocketClient;
    private Socket _tcpSocket;
    private DatagramSocket _udpSocket;
    private InetAddress _udpAddr;
    private int _udpPort;
    private Object _lock = new Object();

    private IRelayCallback _relayCallback = null;
    private IRelaySystemCallback _relaySystemCallback = null;
    
    public RelayComms(BrainCloudClient client) {
        _client = client;
    }

    public boolean getLoggingEnabled() {
        return _loggingEnabled;
    }

    public void enableLogging(boolean isEnabled) {
        _loggingEnabled = isEnabled;
    }

    public void connect(RelayConnectionType connectionType, JSONObject options, IRelayConnectCallback callback) {
        if (_isConnected) {
            disconnect();
        }

        _connectionType = connectionType;
        _isConnected = false;
        _connectCallback = callback;
        _ping = 999;
        _netIdToCxId.clear();
        _cxIdToNetId.clear();
        _netId = -1;
        _ownerCxId = null;
        _sendPacketId.clear();
        _recvPacketId.clear();
        _reliables.clear();
        _orderedReliablePackets.clear();
        _udpRsmgPackets.clear();
        _rsmgHistory.clear();

        if (options == null) {
            _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Invalid arguments"));
            return;
        }

        final boolean ssl;
        final String host;
        final int port;
        final String passcode;
        final String lobbyId;

        try {
            ssl = options.has("ssl") ? options.getBoolean("ssl") : false;
            host = options.getString("host");
            port = options.getInt("port");
            passcode = options.getString("passcode");
            lobbyId = options.getString("lobbyId");
        } catch (JSONException e) {
            e.printStackTrace();
            _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Invalid arguments"));
            return;
        }

        _connectInfo = new ConnectInfo(passcode, lobbyId);

        // connect...
        switch (_connectionType) {
            case WEBSOCKET: {
                try {
                    String uri = (ssl ? "wss://" : "ws://") + host + ":" + port;

                    _webSocketClient = new WSClient(uri);
        
                    if (ssl) {
                        setupSSL();
                    }
                    
                    _webSocketClient.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Failed to connect"));
                    disconnect();
                    return;
                }
                break;
            }
            case TCP: {
                Thread connectionThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            _tcpSocket = new Socket(InetAddress.getByName(host), port);
                            _tcpSocket.setTcpNoDelay(true);
                            if (_loggingEnabled) {
                                System.out.println("RELAY TCP: Connected");
                            }
                            onTCPConnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                            _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Failed to connect"));
                            disconnect();
                            return;
                        }
                    }
                });
                connectionThread.start();
                break;
            }
            case UDP: {
                try {
                    _lastRecvTime = System.currentTimeMillis();
                    _udpAddr = InetAddress.getByName(host);
                    _udpSocket = new DatagramSocket();
                    _udpPort = port;
                    if (_loggingEnabled) {
                        System.out.println("RELAY UDP: Socket Open");
                    }
                    onUDPConnected();
                } catch (Exception e) {
                    e.printStackTrace();
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Failed to connect"));
                    disconnect();
                    return;
                }
                break;
            }
            default: {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Protocol Unimplemented"));
            }
        }
    }

    public void disconnect() {
        if (_isConnected && _connectionType == RelayConnectionType.UDP) {
            ByteBuffer buffer = ByteBuffer.allocate(3);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putShort((short)3);
            buffer.put((byte)CL2RS_DISCONNECT);
            send(buffer);
        }

        synchronized(_lock) {
            _isConnected = false;
            _isConnecting = false;

            if (_webSocketClient != null) {
                _webSocketClient.close();
                _webSocketClient = null;
            }

            try {
                if (_tcpSocket != null) {
                    _tcpSocket.close();
                    _tcpSocket = null;
                }
            } catch (Exception e) {
                _tcpSocket = null;
            }

            try {
                if (_udpSocket != null) {
                    _udpSocket.close();
                    _udpSocket = null;
                }
            } catch (Exception e) {
                _udpSocket = null;
            }

            _connectInfo = null;
            
            _reliables.clear();
            _udpRsmgPackets.clear();
            _rsmgHistory.clear();
            _nextExpectedUdpRsmgPacketId = 0;
            _sendPacketId.clear();
            _recvPacketId.clear();
            _reliables.clear();
            _orderedReliablePackets.clear();
        }
    }

    public boolean isConnected() {
        boolean ret;
        synchronized(_lock) {
            ret = _isConnected;
        }
        return ret;
    }

    public int getPing() {
        return _ping;
    }

    public void setPingInterval(int intervalMS) {
        _pingIntervalMS = intervalMS;
    }

    public String getOwnerProfileId() {
        if (_ownerCxId == null) return null;
        String[] splits = _ownerCxId.split(":");
        if (splits.length != 3) return null;
        return splits[1];
    }

    public String getProfileIdForNetId(int netId) {
        if (!_netIdToCxId.containsKey(netId)) return null;
        String[] splits = _netIdToCxId.get(netId).split(":");
        if (splits.length != 3) return null;
        return splits[1];
    }

    public int getNetIdForProfileId(String profileId) {
        for (Map.Entry<String, Integer> entry : _cxIdToNetId.entrySet()) {
            String[] splits = entry.getKey().split(":");
            if (splits.length != 3) continue; // Invalid cxId
            String id = splits[1];
            if (id.equals(profileId)) {
                return entry.getValue();
            }
        }
        return INVALID_NET_ID;
    }

    public String getOwnerCxId() {
        return _ownerCxId;
    }

    public String getCxIdForNetId(int netId) {
        return _netIdToCxId.get(netId);
    }

    public int getNetIdForCxId(String cxId) {
        if (_cxIdToNetId.containsKey(cxId))
        {
            return _cxIdToNetId.get(cxId);
        }
        return INVALID_NET_ID;
    }
    
    public void registerRelayCallback(IRelayCallback callback) {
        _relayCallback = callback;
    }
    public void deregisterRelayCallback() {
        _relayCallback = null;
    }
    
    public void registerSystemCallback(IRelaySystemCallback callback) {
        _relaySystemCallback = callback;
    }
    public void deregisterSystemCallback() {
        _relaySystemCallback = null;
    }

    private void setupSSL() throws Exception {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) 
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) 
                    throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        SSLSocketFactory factory = sc.getSocketFactory();

        _webSocketClient.setSocket(factory.createSocket());
    }

    private void onWSConnected() {
        try {
            send(CL2RS_CONNECT, buildConnectionRequest());
        } catch(Exception e) {
            e.printStackTrace();
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Failed to build Connection Request"));
            }
        }
    }

    private void onTCPConnected() {
        try {
            startTCPReceivingThread();
            send(CL2RS_CONNECT, buildConnectionRequest());
        } catch(Exception e) {
            e.printStackTrace();
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Failed to build Connection Request"));
            }
        }
    }

    private void onUDPConnected() {
        try {
            startUDPReceivingThread();
            _isConnecting = true;
            _lastConnectTryTime = System.currentTimeMillis();
            send(CL2RS_CONNECT, buildConnectionRequest());
        } catch(Exception e) {
            e.printStackTrace();
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Failed to build Connection Request"));
            }
        }
    }

    private JSONObject buildConnectionRequest() throws Exception {
        JSONObject json = new JSONObject();

        json.put("lobbyId", _connectInfo._lobbyId);
        json.put("cxId", _client.getRttConnectionId());
        json.put("passcode", _connectInfo._passcode);
        json.put("version", _client.getBrainCloudVersion());

        return json;
    }

    private void send(int netId, JSONObject json) {
        send(netId, json.toString());
    }

    private void send(int netId, String text) {
        // if (_loggingEnabled && VERBOSE_LOG) {
        //     System.out.println("RELAY SEND: " + text);
        // }

        byte[] textBytes = text.getBytes(StandardCharsets.US_ASCII);
        int bufferSize = textBytes.length + 3;
        
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort((short)bufferSize);
        buffer.put((byte)netId);
        buffer.put(textBytes, 0, textBytes.length);
        buffer.rewind();

        send(buffer);
    }

    private void send(ByteBuffer buffer) {
        buffer.rewind();

        try {
            synchronized(_lock) {
                switch (_connectionType) {
                    case WEBSOCKET: {
                        if (_webSocketClient != null) {
                            _webSocketClient.send(buffer);
                        }
                        break;
                    }
                    case TCP: {
                        if (_tcpSocket != null) {
                            byte[] bytes = buffer.array();
                            _tcpSocket.getOutputStream().write(bytes, 0, bytes.length);
                            _tcpSocket.getOutputStream().flush();
                        }
                        break;
                    }
                    case UDP: {
                        if (_udpSocket != null) {
                            byte[] bytes = buffer.array();
                            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, _udpAddr, _udpPort);
                            _udpSocket.send(packet);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "RELAY Send Failed"));
            }
        }
    }

    public void sendRelay(byte[] data, long in_playerMask, boolean reliable, boolean ordered, int channel) {
        if (!isConnected()) return;

        if (data.length > MAX_PACKET_SIZE)
        {
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Packet too big " + data.length + " > max " + MAX_PACKET_SIZE));
            }
            return;
        }

        // Allocate buffer
        int bufferSize = data.length + 11;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        buffer.order(ByteOrder.BIG_ENDIAN);

        // Size
        buffer.putShort((short)bufferSize);

        // Control byte
        buffer.put((byte)CL2RS_RELAY);

        // Relay Header
        int rh = 0;
        if (reliable) rh |= RELIABLE_BIT;
        if (ordered) rh |= ORDERED_BIT;
        rh |= channel << 12;

        // Store inverted player mask
        long playerMask = 0;
        for (long i = 0; i < (long)MAX_PLAYERS; ++i) {
            playerMask |= ((in_playerMask >> ((long)MAX_PLAYERS - i - 1L)) & 1L) << i;
        }
        playerMask = ((playerMask << 8L) & 0x0000FFFFFFFFFF00L);

        // AckId without packet id
        long ackIdWithoutPacketId = ((((long)rh << 48L) & 0xF000000000000000L) | playerMask);

        // Packet Id
        int packetId = 0;
        Integer it = _sendPacketId.get(ackIdWithoutPacketId);
        if (it != null)
        {
            packetId = it;
        }
        int nextPacketId = ((packetId + 1) & MAX_PACKET_ID);
        _sendPacketId.put(ackIdWithoutPacketId, nextPacketId);

        // Add packet id to the header, then encode
        rh |= packetId;

        buffer.putShort((short)rh);
        buffer.putShort((short)((playerMask >> 32L) & 0xFFFFL));
        buffer.putShort((short)((playerMask >> 16L) & 0xFFFFL));
        buffer.putShort((short)((playerMask)        & 0xFFFFL));

        // Rest of data
        buffer.put(data, 0, data.length);

        // Send
        send(buffer);

        // UDP, store reliable in send map
        if (reliable && _connectionType == RelayConnectionType.UDP) {
            long ackId = (((long)rh << 48L) & 0xFFFF000000000000L) | playerMask; // We have the packet id now in the rh
            synchronized(_lock) {
                _reliables.add(new Reliable(buffer, ackId, packetId, channel));
            }
        }
    }

    private void startTCPReceivingThread() {
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream in;
                try {
                    synchronized(_lock) {
                        in = new DataInputStream(_tcpSocket.getInputStream());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    disconnect();
                    synchronized(_callbackEventQueue) {
                        _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "TCP Connect Failed"));
                    }
                    return;
                }
                while (true) {
                    try {
                        int len = in.readShort() & 0xFFFF;
                        byte[] bytes = new byte[len - 2];
                        in.readFully(bytes);

                        ByteBuffer buffer = ByteBuffer.allocate(len);
                        buffer.order(ByteOrder.BIG_ENDIAN);
                        buffer.putShort((short)len);
                        buffer.put(bytes, 0, bytes.length);
                        buffer.rewind();
                        
                        onRecv(buffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        disconnect();
                        synchronized(_callbackEventQueue) {
                            _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "TCP Connect Failed"));
                        }
                        return;
                    }
                }
            }
        });

        receiveThread.start();
    }

    private void startUDPReceivingThread() {
        Thread receivingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] receiveData = new byte[1400];
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

                try {
                    while (true) {
                        DatagramSocket socket = _udpSocket;
                        if (socket == null) {
                            break;
                        }
                        try {
                            socket.receive(packet);
                        } catch (SocketTimeoutException e) {
                            continue;
                        } catch (SocketException e) {
                            if (e.getMessage().equals("socket closed")) {
                                break; // Leave peacefully
                            }
                            throw e;
                        }

                        ByteBuffer buffer = ByteBuffer.allocate(packet.getLength());
                        buffer.order(ByteOrder.BIG_ENDIAN);
                        buffer.put(packet.getData(), 0, packet.getLength());
                        buffer.rewind();
                        
                        _lastRecvTime = System.currentTimeMillis();
                        onRecv(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    disconnect();
                    synchronized(_callbackEventQueue) {
                        _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "UDP Receive Failed"));
                    }
                    return;
                }
            }
        });

        receivingThread.start();
    }

    private void sendPing() {
        if (_loggingEnabled && VERBOSE_LOG) {
            System.out.println("RELAY SEND: PING");
        }

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort((short)5);
        buffer.put((byte)CL2RS_PING);
        buffer.putShort((short)_ping);
        send(buffer);
        
        _lastPingTime = System.currentTimeMillis();
    }

    private void onRecv(ByteBuffer buffer) {
        int len = buffer.limit();
        if (len < 3) {
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay Recv Error: packet cannot be smaller than 3 bytes"));
            }
            return;
        }

        buffer.rewind();
        buffer.order(ByteOrder.BIG_ENDIAN);

        int size = buffer.getShort() & 0xFFFF;
        int controlByte = buffer.get() & 0xFF;

        if (len < size) {
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay Recv Error: Packet is smaller than header's size"));
            }
            return;
        }

        if (controlByte == RS2CL_RSMG) {
            if (size < 5) {
                disconnect();
                synchronized(_callbackEventQueue) {
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay Recv Error: RSMG cannot be smaller than 5 bytes"));
                }
                return;
            }
            onRSMG(buffer, size - 3);
        }
        else if (controlByte == RS2CL_DISCONNECT) {
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay: Disconnected by server"));
            }
            return;
        }
        else if (controlByte == RS2CL_PONG) {
            onPONG();
        }
        else if (controlByte == RS2CL_ACK) {
            if (size < 11) {
                disconnect();
                synchronized(_callbackEventQueue) {
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay Recv Error: ack packet cannot be smaller than 11 bytes"));
                }
                return;
            }
            if (_connectionType == RelayConnectionType.UDP) {
                onACK(buffer, size - 3);
            }
        }
        else if (controlByte == RS2CL_RELAY) {
            if (size < 11) {
                disconnect();
                synchronized(_callbackEventQueue) {
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay Recv Error: relay packet cannot be smaller than 11 bytes"));
                }
                return;
            }
            onRelay(buffer, size - 3);
        }
        else {
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay Recv Error: Unknown control byte: " + controlByte));
            }
            return;
        }
    }

    private void onACK(ByteBuffer buffer, int size) {
        short rh          = buffer.getShort();
        short playerMask0 = buffer.getShort();
        short playerMask1 = buffer.getShort();
        short playerMask2 = buffer.getShort();
        long ackId = (((long)rh << 48L)          & 0xFFFF000000000000L) | 
                     (((long)playerMask0 << 32L) & 0x0000FFFF00000000L) |
                     (((long)playerMask1 << 16L) & 0x00000000FFFF0000L) |
                     (((long)playerMask2)        & 0x000000000000FFFFL);

        // if (_loggingEnabled && VERBOSE_LOG) {
        //     int packetId = (int)(rh & 0xFFFL);
        //     System.out.println("ON ACK: " + packetId + ", " + ackId + ", " + rh + ", " + playerMask0 + ", " + playerMask1 + ", " + playerMask2);
        // }

        synchronized(_lock) {
            for (int i = 0; i < _reliables.size(); ++i) {
                Reliable reliable = _reliables.get(i);
                if (reliable.ackId == ackId) {
                    _reliables.remove(i);
                    // if (_loggingEnabled && VERBOSE_LOG) {
                    //     int packetId = (int)(rh & 0xFFFL);
                    //     System.out.println("RELAY ACKED: " + packetId + ", " + ackId);
                    // }
                    break;
                }
            }
        }
    }

    private boolean packetLE(int a, int b) {
        if (a > PACKET_HIGHER_THRESHOLD && b <= PACKET_LOWER_THRESHOLD) {
            return true;
        }
        if (b > PACKET_HIGHER_THRESHOLD && a <= PACKET_LOWER_THRESHOLD)
        {
            return false;
        }
        return a <= b;
    }
    
    private void onRelay(ByteBuffer buffer, int size) {
        short rh          = buffer.getShort();
        short playerMask0 = buffer.getShort();
        short playerMask1 = buffer.getShort();
        short playerMask2 = buffer.getShort();
        long ackId = (((long)rh << 48L)          & 0xFFFF000000000000L) | 
                     (((long)playerMask0 << 32L) & 0x0000FFFF00000000L) |
                     (((long)playerMask1 << 16L) & 0x00000000FFFF0000L) |
                     (((long)playerMask2)        & 0x000000000000FFFFL);

        long ackIdWithoutPacketId = (ackId & 0xF000FFFFFFFFFFFFL);
        boolean reliable = (rh & RELIABLE_BIT) == 0 ? false : true;
        boolean ordered = (rh & ORDERED_BIT) == 0  ? false : true;
        int channel = (int)((rh >> 12L) & 0x3L);
        int packetId = (int)(rh & 0x0FFFL);
        int netId = (int)(playerMask2 & 0x00FFL);

        // Create the packet data
        byte[] eventBuffer = new byte[size - 8];
        buffer.position(11);
        buffer.get(eventBuffer, 0, size - 8);

        // Ack reliables, always. An ack might have been previously dropped.
        if (_connectionType == RelayConnectionType.UDP) {
            if (reliable) {
                // Ack
                ByteBuffer ack = ByteBuffer.allocate(11);
                ack.order(ByteOrder.BIG_ENDIAN);
                ack.putShort((short)11);
                ack.put((byte)CL2RS_ACK);
                ack.putShort((short)rh);
                ack.putShort((short)playerMask0);
                ack.putShort((short)playerMask1);
                ack.putShort((short)playerMask2);
                send(ack);
            }

            synchronized(_lock) {
                if (ordered) {
                    int prevPacketId = MAX_PACKET_ID;
                    Integer it = _recvPacketId.get(ackIdWithoutPacketId);
                    if (it != null) {
                        prevPacketId = it;
                    }
                    if (reliable) {
                        // We already received that packet if it's lower than the last confirmed
                        // packetId. This must be a duplicate
                        if (packetLE(packetId, prevPacketId)) {
                            if (_loggingEnabled && VERBOSE_LOG) {
                                System.out.println("Duplicated packet from: " + netId + ", got: " + packetId);
                            }
                            return;
                        }

                        if (!_orderedReliablePackets.containsKey(ackIdWithoutPacketId)) {
                            _orderedReliablePackets.put(ackIdWithoutPacketId, new ArrayList<RelayPacket>());
                        }

                        // Check if it's out of order, then save it for later
                        ArrayList<RelayPacket> orderedReliablePackets = _orderedReliablePackets.get(ackIdWithoutPacketId);
                        if (packetId != ((prevPacketId + 1) & MAX_PACKET_ID)) {
                            if (orderedReliablePackets.size() > MAX_PACKET_ID_HISTORY) {
                                disconnect();
                                synchronized(_callbackEventQueue) {
                                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay disconnected, too many queued out of order packets."));
                                }
                                return;
                            }

                            int insertIdx = 0;
                            for (; insertIdx < orderedReliablePackets.size(); ++insertIdx) {
                                RelayPacket packet = orderedReliablePackets.get(insertIdx);
                                if (packet.packetId == packetId) {
                                    if (_loggingEnabled && VERBOSE_LOG) {
                                        System.out.println("Duplicated packet: " + packetId);
                                    }
                                    return;
                                }
                                if (packetLE(packetId, packet.packetId)) break;
                            }
                            if (_loggingEnabled && VERBOSE_LOG) {
                                System.out.println("Queuing out of order packet: " + packetId);
                            }
                            orderedReliablePackets.add(insertIdx, new RelayPacket(packetId, netId, eventBuffer));
                            return;
                        }

                        // If it's in order, queue event
                        _recvPacketId.put(ackIdWithoutPacketId, packetId);
                        synchronized(_callbackEventQueue) {
                            _callbackEventQueue.add(new RelayCallback(RelayCallbackType.Relay, netId, eventBuffer));
                        }

                        // Empty previously queued packets if they follow this one
                        while (orderedReliablePackets.size() > 0) {
                            RelayPacket packet = orderedReliablePackets.get(0);
                            if (packet.packetId == ((packetId + 1) & MAX_PACKET_ID)) {
                                synchronized(_callbackEventQueue) {
                                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.Relay, packet.netId, packet.data));
                                }
                                orderedReliablePackets.remove(0);
                                packetId = packet.packetId;
                                _recvPacketId.put(ackIdWithoutPacketId, packetId);
                                continue;
                            }
                            break; // Out of order
                        }
                        return;
                    }
                    else {
                        // Just drop out of order packets for unreliables
                        if (packetLE(packetId, prevPacketId)) {
                            if (_loggingEnabled && VERBOSE_LOG) {
                                System.out.println("RELAY Packet our of order: " + packetId + ", expected: " + ((prevPacketId + 1) & MAX_PACKET_ID));
                            }
                            return;
                        }
                        _recvPacketId.put(ackIdWithoutPacketId, packetId);
                    }
                }
            }
            // else - If not ordered, we don't care if it's out of order.
        }

        // Queue the packet callback
        synchronized(_callbackEventQueue) {
            _callbackEventQueue.add(new RelayCallback(RelayCallbackType.Relay, netId, eventBuffer));
        }
    }
    private void onPONG() {
        _ping = (int)Math.min((long)999, System.currentTimeMillis() - _lastPingTime);
        if (_loggingEnabled && VERBOSE_LOG) {
            System.out.println("RELAY PONG: " + _ping);
        }
    }

    private void ackRSMG(int packetId) {
        if (_loggingEnabled && VERBOSE_LOG) {
            System.out.println("RELAY RSMG ACK: " + packetId);
        }
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort((short)5);
        buffer.put((byte)CL2RS_RSMG_ACK);
        buffer.putShort((short)packetId);
        send(buffer);
    }

    private void onRSMG(ByteBuffer buffer, int size) {
        try {
            int rsmgPacketId = buffer.getShort() & 0xFFFF;

            if (_connectionType == RelayConnectionType.UDP) {
                // Always ack
                ackRSMG(rsmgPacketId);

                // Is it duplicate?
                for (int i = 0; i < _rsmgHistory.size(); ++i) {
                    if (_rsmgHistory.get(i) == rsmgPacketId) {
                        return; // Just ignore it
                    }
                }

                // Record in history
                _rsmgHistory.add(rsmgPacketId);

                // Crop to max history
                while (_rsmgHistory.size() > MAX_PACKET_ID_HISTORY) {
                    _rsmgHistory.remove(0);
                }
            }

            size -= 2;
            byte[] bytes = new byte[size];
            buffer.get(bytes, 0, size);
            String jsonString = new String(bytes, StandardCharsets.US_ASCII);
            JSONObject json = new JSONObject(jsonString);
            
            if (_loggingEnabled) {
                System.out.println("RELAY System Msg: " + jsonString);
            }

            switch (json.getString("op")) {
                case "CONNECT": {
                    int netId = json.getInt("netId");
                    String cxId = json.getString("cxId");
                    _netIdToCxId.put(netId, cxId);
                    _cxIdToNetId.put(cxId, netId);
                    if (cxId.equals(_client.getRttConnectionId())) {
                        synchronized(_lock) {
                            if (!_isConnected) {
                                _isConnected = true;
                                _isConnecting = false;
                                _lastPingTime = System.currentTimeMillis();
                                _netId = netId;
                                _ownerCxId = json.getString("ownerCxId");

                                synchronized(_callbackEventQueue) {
                                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectSuccess, json));
                                }
                            }
                        }
                    }
                    break;
                }
                case "NET_ID": {
                    int netId = json.getInt("netId");
                    String cxId = json.getString("cxId");
                    _netIdToCxId.put(netId, cxId);
                    _cxIdToNetId.put(cxId, netId);
                    break;
                }
                case "MIGRATE_OWNER": {
                    _ownerCxId = json.getString("cxId");
                    break;
                }
            }

            if (_connectionType == RelayConnectionType.UDP) {
                if (rsmgPacketId == _nextExpectedUdpRsmgPacketId) {
                    ++_nextExpectedUdpRsmgPacketId;
                    synchronized(_callbackEventQueue) {
                        _callbackEventQueue.add(new RelayCallback(RelayCallbackType.System, json));
                    }
                    for (int i = 0; i < _udpRsmgPackets.size(); ++i) {
                        UdpRsmgPacket packet = _udpRsmgPackets.get(i);
                        if (packet.id == _nextExpectedUdpRsmgPacketId) {
                            ++_nextExpectedUdpRsmgPacketId;
                            synchronized(_callbackEventQueue) {
                                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.System, packet.json));
                            }
                            _udpRsmgPackets.remove(i);
                            --i;
                        }
                        else {
                            break;
                        }
                    }
                } else {
                    int insertId = 0;
                    for (; insertId < _udpRsmgPackets.size(); ++insertId) {
                        UdpRsmgPacket packet = _udpRsmgPackets.get(insertId);
                        if (packet.id == rsmgPacketId) return; // Already in queue, it's a duplicate, ignore it
                        if (packet.id > rsmgPacketId) break;
                    }
                    UdpRsmgPacket packet = new UdpRsmgPacket();
                    packet.id = insertId;
                    packet.json = json;
                    _udpRsmgPackets.add(insertId, packet);
                }
            } else {
                synchronized(_callbackEventQueue) {
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.System, json));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            disconnect();
            synchronized(_callbackEventQueue) {
                _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay System Msg error"));
            }
        }
    }

    public void runCallbacks() {
        synchronized(_callbackEventQueue) {
            while (!_callbackEventQueue.isEmpty()) {
                RelayCallback relayCallback = _callbackEventQueue.remove(0);
                switch (relayCallback._type) {
                    case ConnectSuccess: {
                        if (_connectCallback != null) {
                            _connectCallback.relayConnectSuccess(relayCallback._json);
                        }
                        break;
                    }
                    case ConnectFailure: {
                        if (_connectCallback != null) {
                            _connectCallback.relayConnectFailure(relayCallback._message);
                        }
                        break;
                    }
                    case Relay: {
                        if (_relayCallback != null) {
                            _relayCallback.relayCallback(relayCallback._netId, relayCallback._data);
                        }
                        break;
                    }
                    case System: {
                        if (_relaySystemCallback != null) {
                            _relaySystemCallback.relaySystemCallback(relayCallback._json);
                        }
                        break;
                    }
                }
            }
        }

        if (_isConnecting) {
            long timeMs = System.currentTimeMillis();
            if (timeMs - _lastConnectTryTime > CONNECT_RESEND_INTERVAL_MS) {
                _lastConnectTryTime = timeMs;
                try {
                    send(CL2RS_CONNECT, buildConnectionRequest());
                } catch(Exception e) {
                    e.printStackTrace();
                    disconnect();
                    synchronized(_callbackEventQueue) {
                        _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay System Fail to build connection request"));
                    }
                    return;
                }
            }
        }

        // Resend reliable
        long nowMs = System.currentTimeMillis();
        if (_connectionType == RelayConnectionType.UDP && _isConnected) {
            boolean resendTimedOut = false;
            synchronized(_lock) {
                for (int i = 0; i < _reliables.size(); ++i) {
                    Reliable reliable = _reliables.get(i);
                    long elapsedMs = nowMs - reliable.resendTimeMs;
                    if (elapsedMs >= reliable.waitTimeMs) {
                        // Did we timeout?
                        if (nowMs - reliable.sendTimeMs >= 10000) {
                            resendTimedOut = true;
                            break;
                        }

                        // Resend
                        reliable.waitTimeMs = Math.min(500, (reliable.waitTimeMs * 125) / 100);
                        reliable.resendTimeMs = nowMs;
                        send(reliable.buffer);

                        if (_loggingEnabled && VERBOSE_LOG) {
                            System.out.println("RELAY RESEND: " + reliable.packetId + ", " + reliable.ackId + ", Next resend in: " + reliable.waitTimeMs + "ms");
                        }
                    }
                }
            }
            if (resendTimedOut) {
                disconnect();
                synchronized(_callbackEventQueue) {
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Timed out. Too many packet drops."));
                }
                return;
            }
        }

        // Check if we timeout (no response for 10 seconds).
        // For UDP only since we don't have a connection.
        if (_connectionType == RelayConnectionType.UDP &&
            (_isConnecting || _isConnected)) {
            if (nowMs - _lastRecvTime > TIMEOUT_MS) {
                disconnect();
                synchronized(_callbackEventQueue) {
                    _callbackEventQueue.add(new RelayCallback(RelayCallbackType.ConnectFailure, "Relay Socket Timeout."));
                }
                return;
            }
        }

        // Ping. Which also works as a heartbeat
        if (_isConnected) {
            if (System.currentTimeMillis() - _lastPingTime >= _pingIntervalMS) {
                sendPing();
            }
        }
    }
}
