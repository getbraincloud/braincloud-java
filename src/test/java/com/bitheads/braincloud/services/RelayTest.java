package com.bitheads.braincloud.services;

import org.junit.Assert;
import static org.junit.Assert.assertTrue;

import com.bitheads.braincloud.client.IBrainCloudWrapper;
import com.bitheads.braincloud.client.IRelayCallback;
import com.bitheads.braincloud.client.IRelayConnectCallback;
import com.bitheads.braincloud.client.IRelaySystemCallback;
import com.bitheads.braincloud.client.IRTTCallback;
import com.bitheads.braincloud.client.IRTTConnectCallback;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.RelayConnectionType;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by David St-Louis on 20-01-20.
 */
public class RelayTest extends TestFixtureBase {
    private boolean endMatch;
    private boolean endMatchReceived;

    @Test
    public void testConnectWithNullOptions() throws Exception {
        RelayConnectionTestResult tr = new RelayConnectionTestResult(_wrapper);
        _wrapper.getRelayService().connect(RelayConnectionType.WEBSOCKET, null, tr);
        tr.RunExpectFail();
    }

    @Test
    public void testConnectWithEmptyOptions() throws Exception {
        RelayConnectionTestResult tr = new RelayConnectionTestResult(_wrapper);
        JSONObject options = new JSONObject();
        _wrapper.getRelayService().connect(RelayConnectionType.WEBSOCKET, options, tr);
        tr.RunExpectFail();
    }

    @Test
    public void testInvalidProfileIdForNetId() throws Exception {
        
        // Just make sure the dictionary returns null and doesn't asserts
        String profileId = _wrapper.getRelayService().getProfileIdForNetId(0); 
        assertTrue(profileId == null);
    }

    @Test
    public void testConnectWithBadURL() throws Exception {
        RelayConnectionTestResult tr = new RelayConnectionTestResult(_wrapper);
        JSONObject options = new JSONObject();
        options.put("ssl", false);
        options.put("host", "ws://192.168.1.0");
        options.put("port", 1234);
        options.put("passcode", "invalid_passcode");
        options.put("lobbyId", "invalid_lobbyId");
        _wrapper.getRelayService().connect(RelayConnectionType.WEBSOCKET, options, tr);
        tr.RunExpectFail();
    }

    private void fullFlow(RelayConnectionType connectionType) throws Exception {
        RTTLobbyResults lobbyTR = new RTTLobbyResults(_wrapper);
        JSONObject server;

        _wrapper.getClient().getRTTService().registerRTTLobbyCallback(lobbyTR);

        // Enable RTT
        {
            System.out.println("Enable RTT...");
            RTTConnectionTestResult tr = new RTTConnectionTestResult(_wrapper);
            _wrapper.getClient().getRTTService().enableRTT(tr, true);
            tr.Run();
        }

        // Find or create lobby
        {
            System.out.println("Find or create lobby...");
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getLobbyService().findOrCreateLobby("READY_START_V2", 0, 1,
                    "{\"strategy\":\"ranged-absolute\",\"alignment\":\"center\",\"ranges\":[1000]}", "{}", null, "{}",
                    true, "{}", "all", tr);
            tr.Run();
            server = lobbyTR.Run();
        }

        // Register callbacks
        System.out.println("Register callbacks...");
        RelayConnectSystemCheck systemCallbackReceived = new RelayConnectSystemCheck(_wrapper);
        _wrapper.getRelayService().registerSystemCallback(systemCallbackReceived);
        RelayCheck relayCallbackReceived = new RelayCheck(_wrapper);
        _wrapper.getRelayService().registerRelayCallback(relayCallbackReceived);

        // Connect to relay server
        {
            System.out.println("Connect to relay server...");
            RelayConnectionTestResult tr = new RelayConnectionTestResult(_wrapper);
            JSONObject options = new JSONObject();
            options.put("ssl", false);
            options.put("host", server.getJSONObject("connectData").getString("address"));
            if (connectionType == RelayConnectionType.WEBSOCKET)
                options.put("port", server.getJSONObject("connectData").getJSONObject("ports").getInt("ws"));
            else if (connectionType == RelayConnectionType.TCP)
                options.put("port", server.getJSONObject("connectData").getJSONObject("ports").getInt("tcp"));
            else if (connectionType == RelayConnectionType.UDP)
                options.put("port", server.getJSONObject("connectData").getJSONObject("ports").getInt("udp"));
            options.put("passcode", server.getString("passcode"));
            options.put("lobbyId", server.getString("lobbyId"));
            _wrapper.getRelayService().connect(connectionType, options, tr);
            tr.Run();
        }

        // Wait for system callback to be received. Wait ~20sec
        systemCallbackReceived.Run();

        // Send a relay message to ourself and wait for callback (ECHO)
        {
            int myNetId = _wrapper.getRelayService()
                    .getNetIdForProfileId(_wrapper.getAuthenticationService().getProfileId());
            _wrapper.getRelayService().send("Hello World!".getBytes(StandardCharsets.US_ASCII), myNetId, true, true,
                    RelayService.CHANNEL_HIGH_PRIORITY_1);
        }

        if (!endMatch) {
            relayCallbackReceived.Run();
        }

        // Check the cx/profile id conversions
        String myProfileId = _wrapper.getAuthenticationService().getProfileId();
        String myCxId = _wrapper.getClient().getRttConnectionId();
        int myNetId = _wrapper.getRelayService().getNetIdForCxId(myCxId);
        assertTrue(myNetId != RelayService.INVALID_NET_ID);
        String _cxId = _wrapper.getRelayService().getCxIdForNetId(myNetId);
        assertTrue(_cxId.equals(myCxId));
        String _profileId = _wrapper.getRelayService().getProfileIdForNetId(myNetId);
        assertTrue(_profileId.equals(myProfileId));
        int _netId = _wrapper.getRelayService().getNetIdForProfileId(myProfileId);
        assertTrue(_netId == myNetId);

        // Wait 30sec and make sure the Ping kept us alive
        int t = 0;
        while (t < 30000) {
            TestFixtureBase._client.runCallbacks();
            _wrapper.runCallbacks();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
            t += 100;
        }

        if (!endMatch) {
            assertTrue(_wrapper.getRelayService().isConnected());

            System.out.println("Disconnecting...");
            _wrapper.getRelayService().disconnect();
        } else {
            assertTrue(endMatchReceived);
        }

    }

    @Test
    public void testFullFlowWS() throws Exception {
        fullFlow(RelayConnectionType.WEBSOCKET);
    }

    @Test
    public void testFullFlowTCP() throws Exception {
        fullFlow(RelayConnectionType.TCP);
    }

    @Test
    public void testFullFlowUDP() throws Exception {
        fullFlow(RelayConnectionType.UDP);
    }

    @Test
    public void testFullFlowWSEndMatch() throws Exception {
        endMatch = true;
        fullFlow(RelayConnectionType.WEBSOCKET);
    }

    public class RelayConnectSystemCheck implements IRelaySystemCallback {
        public boolean _received = false;

        IBrainCloudWrapper _wrapper;

        public RelayConnectSystemCheck(IBrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }

        public void relaySystemCallback(JSONObject jsonData) {
            try {
                if (jsonData.getString("op").equals("CONNECT")) {
                    _received = true;

                    System.out.println("relaySystemCallback CONNECT");

                    if (endMatch) {
                        JSONObject json = new JSONObject();
                        json.put("cxId", _wrapper.getClient().getRttConnectionId());
                        json.put("op", "END_MATCH");

                        System.out.println("Call endMatch()...");
                        _wrapper.getRelayService().endMatch(json);
                    }
                } else if (jsonData.getString("op").equals("END_MATCH")) {
                    System.out.println("relaySystemCallback END_MATCH");
                    endMatchReceived = true;

                    System.out.println("Send a test event...");

                    String profileId = _wrapper.getClient().getAuthenticationService().getProfileId();
                    _wrapper.getEventService().sendEvent(
                            profileId,
                            "test",
                            "{\"testData\":42}",
                            new IServerCallback() {
                                @Override
                                public void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation,
                                        JSONObject jsonData) {
                                    System.out.println("Test event success");
                                }

                                @Override
                                public void serverError(ServiceName serviceName, ServiceOperation serviceOperation,
                                        int statusCode, int reasonCode, String jsonError) {
                                    System.out.println("Test event error: " + jsonError);
                                }
                            });
                }
            } catch (JSONException e) {
            }
        }

        public void Run() {
            int t = 0;
            while (!_received) {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
                t += 100;
                if (t > 20000) {
                    assertTrue(false);
                }
            }
        }
    }

    public class RelayCheck implements IRelayCallback {
        public boolean _received = false;

        IBrainCloudWrapper _wrapper;

        public RelayCheck(IBrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }

        public void relayCallback(int netId, byte[] data) {
            String str = new String(data, StandardCharsets.US_ASCII);

            System.out.println("relayCallback: " + str);

            if (str.equals("Hello World!")) {
                _received = true;
            }
        }

        public void Run() {
            int t = 0;
            while (!_received) {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
                t += 100;
                if (t > 20000) {
                    Assert.assertTrue(false);
                }
            }
        }
    }

    public class RTTLobbyResults implements IRTTCallback {
        private boolean m_done = false;
        private JSONObject m_server = null;
        public String ownerCxId = null;

        IBrainCloudWrapper _wrapper;

        public RTTLobbyResults(IBrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }

        public void rttCallback(JSONObject eventJson) {
            try {
                JSONObject jsonData = eventJson.getJSONObject("data");
                switch (eventJson.getString("operation")) {
                    case "DISBANDED": {
                        assertTrue(jsonData.getJSONObject("reason").getInt("code") == ReasonCodes.RTT_ROOM_READY);
                        m_done = true;
                        break;
                    }
                    case "ROOM_ASSIGNED": {
                        _wrapper.getLobbyService().updateReady(jsonData.getString("lobbyId"), true, "{}", null);
                        break;
                    }
                    case "STARTING": {
                        ownerCxId = jsonData.getJSONObject("lobby").getString("ownerCxId");
                        break;
                    }
                    case "ROOM_READY": {
                        m_server = jsonData;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                m_done = true;
            }
        }

        public JSONObject Run() {
            Spin();
            assertTrue(m_server != null);
            return m_server;
        }

        private void Spin() {
            int t = 0;
            while (!m_done) {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
                t += 100;
                if (t > 5 * 60 * 1000) {
                    assertTrue(false);
                }
            }
        }
    }

    public class RelayConnectionTestResult implements IRelayConnectCallback {
        private boolean m_result = false;
        private boolean m_done = false;

        IBrainCloudWrapper _wrapper;

        public RelayConnectionTestResult(IBrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }

        public boolean Run() {
            Spin();
            assertTrue(m_result);
            return m_result;
        }

        public boolean RunExpectFail() {
            Spin();
            Assert.assertFalse(m_result);
            return m_result;
        }

        public void relayConnectSuccess(JSONObject jsonData) {
            m_result = true;
            m_done = true;
        }

        public void relayConnectFailure(String errorMessage) {
            m_result = false;
            m_done = true;
            System.out.println("relayConnectFailure: " + errorMessage);
        }

        public boolean IsDone() {
            return m_done;
        }

        private void Spin() {
            while (!m_done) {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public class RTTConnectionTestResult implements IRTTConnectCallback {
        private boolean m_result = false;
        private boolean m_done = false;

        IBrainCloudWrapper _wrapper;

        public RTTConnectionTestResult(IBrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }

        public boolean Run() {
            Spin();
            Assert.assertTrue(m_result);
            return m_result;
        }

        public void rttConnectSuccess() {
            m_result = true;
            m_done = true;
        }

        public void rttConnectFailure(String errorMessage) {
            m_result = false;
            m_done = true;
        }

        public boolean IsDone() {
            return m_done;
        }

        private void Spin() {
            while (!m_done) {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
            }
        }
    }
}