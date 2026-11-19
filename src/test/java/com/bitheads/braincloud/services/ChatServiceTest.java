package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.junit.Test;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by David St-Louis on 18-06-20.
 */
public class ChatServiceTest extends TestFixtureBase
{
    // The global "valid" channel of whichever test app this env uses.
    static private String channelId() { return m_appId + ":gl:valid"; }
    static private String _msgId = "";
    static private int _msgVersion = 1;

    @Test
    public void testGetChannelIdValid() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().getChannelId("gl", "valid", tr);
        tr.Run();

        String id = tr.m_response.getJSONObject("data").getString("channelId");
        if (!id.equals(channelId())) throw new Exception("Expected channel " + channelId() + ", got " + id);
    }

    @Test
    public void testGetChannelIdInValid() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().getChannelId("gl", "invalid", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.CHANNEL_NOT_FOUND);
    }

    @Test
    public void getChannelInfo() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().getChannelInfo(channelId(), tr);
        tr.Run();
    }

    @Test
    public void channelConnect() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().channelConnect(channelId(), 50, tr);
        tr.Run();
    }

    @Test
    public void getSubscribedChannels() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().getSubscribedChannels("gl", tr);
        tr.Run();

        // Verify that our channel is present in the channel list
        JSONArray channels = tr.m_response.getJSONObject("data").getJSONArray("channels");
        int i = 0;
        for (; i < channels.length(); ++i) {
            JSONObject channel = channels.getJSONObject(i);
            if (channel.getString("id").equals(channelId())) {
                break;
            }
        }
        if (i == channels.length()) throw new Exception("Excepted to find " + channelId());
    }

    @Test
    public void postChatMessage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().postChatMessage(channelId(), "Hello World!", null, true, tr);
        tr.Run();

        _msgId = tr.m_response.getJSONObject("data").getString("msgId");
    }

    @Test
    public void postChatMessageSimple() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().postChatMessageSimple(channelId(), "Hello World! SIMPLE", true, tr);
        tr.Run();

        _msgId = tr.m_response.getJSONObject("data").getString("msgId");
    }

    @Test
    public void getChatMessage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().postChatMessage(channelId(), "Hello World!", null, true, tr);
        tr.Run();
        String message = tr.m_response.getJSONObject("data").getString("msgId");

        _wrapper.getChatService().getChatMessage(channelId(), message, tr);
        tr.Run();
    }

    @Test
    public void updateChatMessage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().postChatMessage(channelId(), "Hello World!", null, true, tr);
        tr.Run();
        _msgId = tr.m_response.getJSONObject("data").getString("msgId");
        _msgVersion = 1;

        _wrapper.getChatService().updateChatMessage(channelId(), _msgId, _msgVersion, "Hello World! edited", null, tr);
        tr.Run();

        _wrapper.getChatService().getChatMessage(channelId(), _msgId, tr);
        tr.Run();

        if (!tr.m_response.getJSONObject("data").getJSONObject("content").getString("text").equals("Hello World! edited")) throw new Exception("Wrong message");
        _msgVersion = tr.m_response.getJSONObject("data").getInt("ver");
        if (_msgVersion != 2) throw new Exception("Expected msg version to be 2");
    }

    @Test
    public void getRecentChatMessages() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().getRecentChatMessages(channelId(), 50, tr);
        tr.Run();

        // Verify that our message is present in the history
        JSONArray messages = tr.m_response.getJSONObject("data").getJSONArray("messages");
        int i = 0;
        for (; i < messages.length(); ++i) {
            JSONObject message = messages.getJSONObject(i);
            if (message.getString("msgId").equals(_msgId)) {
                break;
            }
        }
        if (i == messages.length()) throw new Exception("Excepted to find " + _msgId);
    }

    @Test
    public void deleteChatMessage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().postChatMessage(channelId(), "Hello World!", null, true, tr);
        tr.Run();
        String message = tr.m_response.getJSONObject("data").getString("msgId");

        _wrapper.getChatService().deleteChatMessage(channelId(), message, _msgVersion, tr);
        tr.Run();
    }

    @Test
    public void channelDisconnect() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getChatService().channelDisconnect(channelId(), tr);
        tr.Run();
    }
}
