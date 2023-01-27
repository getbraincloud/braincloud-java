package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.BrainCloudWrapper;
import com.bitheads.braincloud.client.IRTTCallback;
import com.bitheads.braincloud.client.IRTTConnectCallback;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by David St-Louis on 18-07-03.
 */
public class RTTTest extends TestFixtureBase
{
    @Test
    public void testRequestClientConnection() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getRTTService().requestClientConnection(tr);
        tr.Run();
    }

    @Test
    public void testEnableRTTWithWS() throws Exception {
        RTTConnectionTestResult tr = new RTTConnectionTestResult(_wrapper);

        _wrapper.getClient().getRTTService().enableRTT(tr, true);
        //we should be able to call this twice without worry
        _wrapper.getClient().getRTTService().enableRTT(tr, true);
        _wrapper.getClient().getRTTService().disableRTT();
        _wrapper.getClient().getRTTService().disableRTT();

        _wrapper.getClient().getRTTService().enableRTT(tr, true);
        tr.Run();
    }

    @Test
    public void testEnableRTTWithTCP() throws Exception {
        RTTConnectionTestResult tr = new RTTConnectionTestResult(_wrapper);

        _wrapper.getClient().getRTTService().enableRTT(tr, false);
        //we should be able to call this twice without worry
        _wrapper.getClient().getRTTService().enableRTT(tr, false);
        _wrapper.getClient().getRTTService().disableRTT();
        _wrapper.getClient().getRTTService().disableRTT();

        _wrapper.getClient().getRTTService().enableRTT(tr, true);
        tr.Run();
    }

    @Test
    public void testChatCallback() throws Exception {
        String channelId;
        String msgId = "";

        // Enable RTT
        {
            RTTConnectionTestResult tr = new RTTConnectionTestResult(_wrapper);
            _wrapper.getClient().getRTTService().enableRTT(tr, true);
            tr.Run();
        }

        // Get channel id
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getChatService().getChannelId("gl", "valid", tr);
            tr.Run();
            channelId = tr.m_response.getJSONObject("data").getString("channelId");
        }

        // Connect to the channel
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getChatService().channelConnect(channelId, 50, tr);
            tr.Run();
        }

        // Post chat message
        synchronized(msgId)
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getChatService().postChatMessage(channelId, "Java RTT test message", null, true, tr);
            tr.Run();
        }

        // Wait for the message
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
