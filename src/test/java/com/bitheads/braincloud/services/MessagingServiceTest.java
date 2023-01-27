package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.junit.Test;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by David St-Louis on 18-07-17.
 */
public class MessagingServiceTest extends TestFixtureBase
{
    // Those tests for now just test the validity of the calls. For more indepth tests, check out the javascript tests.
    @Test
    public void testDeleteMessages() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> msgIds = new ArrayList<String>();
        msgIds.add("invalidMsgId");
        _wrapper.getMessagingService().deleteMessages("inbox", msgIds, tr);
        tr.Run();
    }

    @Test
    public void testGetMessageboxes() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMessagingService().getMessageboxes(tr);
        tr.Run();
    }

    @Test
    public void testGetMessageCounts() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMessagingService().getMessageCounts(tr);
        tr.Run();
    }

    @Test
    public void testGetMessages() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> msgIds = new ArrayList<String>();
        msgIds.add("invalidMsgId");
        _wrapper.getMessagingService().getMessages("inbox", msgIds, true, tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.MESSAGE_NOT_FOUND);
    }

    @Test
    public void testGetMessagesPage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String profileId = _wrapper.getAuthenticationService().getProfileId();

        _wrapper.getMessagingService().getMessagesPage("{\"pagination\":{\"rowsPerPage\":10,\"pageNumber\":1},\"searchCriteria\":{\"$or\":[{\"message.message.from\":" + profileId + "},{\"message.message.to\":" + profileId + "}]},\"sortCriteria\":{\"mbCr\": 1,\"mbUp\": -1}}", tr);
        tr.Run();
    }

    @Test
    public void testGetMessagesPageOffset() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMessagingService().getMessagesPageOffset("invalidContext", 1, tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.DECODE_CONTEXT);
    }

    @Test
    public void testSendMessage() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String contentJson = "{\"subject\": \"Chat and messaging features are here!\", \"text\": \"hi.\"}";

        ArrayList<String> toProfileIds = new ArrayList<String>();
        toProfileIds.add(_wrapper.getAuthenticationService().getProfileId());
        _wrapper.getMessagingService().sendMessage(toProfileIds, contentJson, tr);
        tr.Run();
    }

    @Test
    public void testSendMessageSimple() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> toProfileIds = new ArrayList<String>();
        toProfileIds.add(_wrapper.getAuthenticationService().getProfileId());
        _wrapper.getMessagingService().sendMessageSimple(toProfileIds, "This is text", tr);
        tr.Run();
    }

    @Test
    public void testMarkMessagesRead() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> msgIds = new ArrayList<String>();
        msgIds.add("invalidMsgId");
        _wrapper.getMessagingService().markMessagesRead("inbox", msgIds, tr);
        tr.Run();
    }
}

