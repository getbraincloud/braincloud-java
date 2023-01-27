package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.BrainCloudWrapper;
import com.bitheads.braincloud.client.IRelayCallback;
import com.bitheads.braincloud.client.IRelayConnectCallback;
import com.bitheads.braincloud.client.IRelaySystemCallback;
import com.bitheads.braincloud.client.IRTTCallback;
import com.bitheads.braincloud.client.IRTTConnectCallback;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.RelayConnectionType;
import com.bitheads.braincloud.client.RelayConnectionType;
import com.bitheads.braincloud.client.StatusCodes;

import junit.framework.Assert;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by David St-Louis on 20-01-20.
 */
public class RelayTest extends TestFixtureBase
{
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
        String profileId = _wrapper.getRelayService().getProfileIdForNetId(0); // Just make sure the dictionary returns null and doesn't asserts
        Assert.assertTrue(profileId == null);
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
            RTTConnectionTestResult tr = new RTTConnectionTestResult(_wrapper);
            _wrapper.getClient().getRTTService().enableRTT(tr, true);
            tr.Run();
        }

        // Find or create lobby
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getLobbyService().findOrCreateLobby("READY_START_V2", 0, 1, "{\"strategy\":\"ranged-absolute\",\"alignment\":\"center\",\"ranges\":[1000]}", "{}", null, "{}", true, "{}", "all", tr);
            tr.Run();
            server = lobbyTR.Run();
        }

        // Register callbacks
        RelayConnectSystemCheck systemCallbackReceived = new RelayConnectSystemCheck(_wrapper);
        _wrapper.getRelayService().registerSystemCallback(systemCallbackReceived);
        RelayCheck relayCallbackReceived = new RelayCheck(_wrapper);
        _wrapper.getRelayService().registerRelayCallback(relayCallbackReceived);

        // Connect to relay server
        {
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
            int myNetId = _wrapper.getRelayService().getNetIdForProfileId(_wrapper.getAuthenticationService().getProfileId());
            _wrapper.getRelayService().send("Hello World!".getBytes(StandardCharsets.US_ASCII), myNetId, true, true, RelayService.CHANNEL_HIGH_PRIORITY_1);
        }
        relayCallbackReceived.Run();

        // Check the cx/profile id conversions
        String myProfileId = _wrapper.getAuthenticationService().getProfileId();
        String myCxId = _wrapper.getClient().getRttConnectionId();
        int myNetId = _wrapper.getRelayService().getNetIdForCxId(myCxId);
        Assert.assertTrue(myNetId != RelayService.INVALID_NET_ID);
        String _cxId = _wrapper.getRelayService().getCxIdForNetId(myNetId);
        Assert.assertTrue(_cxId.equals(myCxId));
        String _profileId = _wrapper.getRelayService().getProfileIdForNetId(myNetId);
        Assert.assertTrue(_profileId.equals(myProfileId));
        int _netId = _wrapper.getRelayService().getNetIdForProfileId(myProfileId);
        Assert.assertTrue(_netId == myNetId);

        // Wait 30sec and make sure the Ping kept us alive
        int t = 0;
        while (t < 30000) {
            TestFixtureBase._client.runCallbacks();
            _wrapper.runCallbacks();
            try {
                Thread.sleep(100);
            } catch(InterruptedException ie) {}
            t += 100;
        }
        Assert.assertTrue(_wrapper.getRelayService().isConnected());
        _wrapper.getRelayService().disconnect();
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

    public class RelayConnectSystemCheck implements IRelaySystemCallback {
        public boolean _received = false;

        BrainCloudWrapper _wrapper;

        public RelayConnectSystemCheck(BrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }

        public void relaySystemCallback(JSONObject jsonData) {
            try {
                if (jsonData.getString("op").equals("CONNECT")) {
                    _received = true;

                    System.out.println("relaySystemCallback CONNECT");
                }
            } catch (JSONException e) {}
        }

        public void Run() {
            int t = 0;
            while (!_received) {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try {
                    Thread.sleep(100);
                } catch(InterruptedException ie) {}
                t += 100;
                if (t > 20000) {
                    Assert.assertTrue(false);
                }
            }
        }
    }

    public class RelayCheck implements IRelayCallback {
        public boolean _received = false;

        BrainCloudWrapper _wrapper;

        public RelayCheck(BrainCloudWrapper wrapper) {
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
                } catch(InterruptedException ie) {}
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

        BrainCloudWrapper _wrapper;

        public RTTLobbyResults(BrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }
        
        public void rttCallback(JSONObject eventJson) {
            try {
                JSONObject jsonData = eventJson.getJSONObject("data");
                switch (eventJson.getString("operation")) {
                    case "DISBANDED": {
                        Assert.assertTrue(jsonData.getJSONObject("reason").getInt("code") == ReasonCodes.RTT_ROOM_READY);
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
            } catch(Exception e) {
                e.printStackTrace();
                m_done = true;
            }
        }

        public JSONObject Run() {
            Spin();
            Assert.assertTrue(m_server != null);
            return m_server;
        }

        private void Spin()
        {
            int t = 0;
            while (!m_done)
            {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try
                {
                    Thread.sleep(100);
                } catch(InterruptedException ie)
                {}
                t += 100;
                if (t > 5 * 60 * 1000) {
                    Assert.assertTrue(false);
                }
            }
        }
    }

    public class RelayConnectionTestResult implements IRelayConnectCallback {
        private boolean m_result = false;
        private boolean m_done = false;
        
        BrainCloudWrapper _wrapper;

        public RelayConnectionTestResult(BrainCloudWrapper wrapper) {
            _wrapper = wrapper;
            _wrapper.getClient().enableLogging(true);
        }
        
        public boolean Run() {
            Spin();
            Assert.assertTrue(m_result);
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

        public boolean IsDone()
        {
            return m_done;
        }

        private void Spin()
        {
            while (!m_done)
            {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try
                {
                    Thread.sleep(100);
                } catch(InterruptedException ie)
                {}
            }
        }
    }

    public class RTTConnectionTestResult implements IRTTConnectCallback {
        private boolean m_result = false;
        private boolean m_done = false;
        
        BrainCloudWrapper _wrapper;

        public RTTConnectionTestResult(BrainCloudWrapper wrapper) {
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

        public boolean IsDone()
        {
            return m_done;
        }

        private void Spin()
        {
            while (!m_done)
            {
                TestFixtureBase._client.runCallbacks();
                _wrapper.runCallbacks();
                try
                {
                    Thread.sleep(100);
                } catch(InterruptedException ie)
                {}
            }
        }
    }
}
