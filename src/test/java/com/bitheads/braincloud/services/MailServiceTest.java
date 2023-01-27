package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
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
                getUser(Users.UserC).email,
                data.toString(),
                tr);

        tr.Run();
    }
}