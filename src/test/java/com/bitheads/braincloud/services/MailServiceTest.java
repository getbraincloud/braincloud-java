package com.bitheads.braincloud.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class MailServiceTest extends TestFixtureBase {

    @Test
    public void testSendBasicEmail() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMailService().sendBasicEmail(
                getUser(Users.UserC).profileId,
                "Test Subject - TestSendBasicEmail",
                "Test body content message.",
                tr);

        tr.Run();
    }

    @Test
    public void testSendAdvancedEmailSendGrid() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        JSONObject data = new JSONObject();
        data.put("subject", "Test Subject - TestSendAdvancedEmailSendGrid");
        data.put("body", "Test body");
        data.put("categories", new String[]{"unit-test"});

        _wrapper.getMailService().sendAdvancedEmail(
                getUser(Users.UserC).profileId,
                data.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testSendAdvancedEmailByAddress() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        JSONObject data = new JSONObject();
        data.put("subject", "Test Subject - TestSendAdvancedEmailByAddress");
        data.put("body", "Test body");
        data.put("categories", new String[]{"unit-test"});

        _wrapper.getMailService().sendAdvancedEmailByAddress(
                "bc-client-team@bitheads.com",
                data.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testSendAdvancedEmailByAddresses() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String[] emailAddresses = {"bc-client-team@bitheads.com", "anothertest@email.com"};
        String serviceParams = "{\"fromAddress\":\"bc-client-team@bitheads.com\",\"fromName\":\"BC Client Team\",\"replyToAddress\":\"bc-client-team@bitheads.com\",\"replyToName\":\"Optional ReplyTo\",\"templateId\":\"d-www-xxx-yyy-zzz\",\"dynamicData\":{\"user\":{\"firstName\":\"John\",\"lastName\":\"Doe\"},\"resetLink\":\"www.dummuyLink.io\"},\"categories\":[\"category1\",\"category2\"],\"attachments\":[{\"content\":\"VGhpcyBhdHRhY2htZW50IHRleHQ=\",\"filename\":\"attachment.txt\"}]}";

        _wrapper.getMailService().sendAdvancedEmailByAddresses(emailAddresses, serviceParams, tr);

        tr.Run();
    }
}