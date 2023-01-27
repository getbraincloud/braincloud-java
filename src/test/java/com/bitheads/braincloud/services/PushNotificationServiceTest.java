package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.Platform;
import com.bitheads.braincloud.client.ReasonCodes;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class PushNotificationServiceTest extends TestFixtureBase
{
    @Test
    public void testDeregisterAllPushNotificationDeviceTokens() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().deregisterAllPushNotificationDeviceTokens(tr);

        tr.Run();
    }

    @Test
    public void testDeregisterPushNotificationDeviceToken() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPushNotificationService().registerPushNotificationToken(
                Platform.GooglePlayAndroid, "GARBAGE_TOKEN", tr);

        tr.Reset();
        _wrapper.getPushNotificationService().deregisterPushNotificationDeviceToken(
                Platform.GooglePlayAndroid, "GARBAGE_TOKEN", tr);
        tr.Run();
    }

    @Test
    public void testRegisterPushNotificationToken() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().registerPushNotificationToken(
                Platform.GooglePlayAndroid, "GARBAGE_TOKEN", tr);

        tr.Run();
    }

    @Test
    public void testSendSimplePushNotification() throws Exception
    {

    }

    @Test
    public void testSendRichPushNotification() throws Exception
    {

    }

    @Test
    public void testSendTemplatedPushNotificationToGroup() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupService().createGroup(
                "testGroup",
                "test",
                false,
                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                Helpers.createJsonPair("testInc", 123),
                Helpers.createJsonPair("test", "test"),
                Helpers.createJsonPair("test", "test"),
                tr);

        tr.Run();

        JSONObject data = tr.m_response.getJSONObject("data");
        String groupId = data.getString("groupId");

        _wrapper.getPushNotificationService().sendTemplatedPushNotificationToGroup(
                groupId,
                1,
                Helpers.createJsonPair("1", "asdf"),
                tr);
        tr.Run();

        _wrapper.getGroupService().deleteGroup(
                groupId,
                -1,
                tr);
        tr.Run();
    }

    @Test
    public void testSendNormalizedPushNotificationToGroup() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupService().createGroup(
                "testGroup",
                "test",
                false,
                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                Helpers.createJsonPair("testInc", 123),
                Helpers.createJsonPair("test", "test"),
                Helpers.createJsonPair("test", "test"),
                tr);

        tr.Run();

        JSONObject data = tr.m_response.getJSONObject("data");
        String groupId = data.getString("groupId");

        _wrapper.getPushNotificationService().sendNormalizedPushNotificationToGroup(
                groupId,
                "{ \"body\": \"content of message\", \"title\": \"message title\" }",
                Helpers.createJsonPair("1", "asdf"),
                tr);
        tr.Run();

        _wrapper.getGroupService().deleteGroup(
                groupId,
                -1,
                tr);
        tr.Run();
    }

    @Test
    public void testScheduleRawPushNotificationUTC() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String fcmContent = "{ \"notification\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"data\": { \"customfield1\": \"customValue1\", \"customfield2\": \"customValue2\" }, \"priority\": \"normal\" }";
        String iosContent = "{ \"aps\": { \"alert\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"badge\": 0, \"sound\": \"gggg\" } }";
        String facebookContent = "{\"template\": \"content of message\"}";

        _wrapper.getPushNotificationService().scheduleRawPushNotificationUTC(
                getUser(Users.UserA).profileId,
                fcmContent,
                iosContent,
                facebookContent,
                0,
                tr);

        tr.Run();
    }

    @Test
    public void testScheduleRawPushNotificationMinutes() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String fcmContent = "{ \"notification\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"data\": { \"customfield1\": \"customValue1\", \"customfield2\": \"customValue2\" }, \"priority\": \"normal\" }";
        String iosContent = "{ \"aps\": { \"alert\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"badge\": 0, \"sound\": \"gggg\" } }";
        String facebookContent = "{\"template\": \"content of message\"}";

        _wrapper.getPushNotificationService().scheduleRawPushNotificationUTC(
                getUser(Users.UserA).profileId,
                fcmContent,
                iosContent,
                facebookContent,
                42,
                tr);

        tr.Run();
    }

    @Test
    public void testSendRawPushNotification() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String fcmContent = "{ \"notification\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"data\": { \"customfield1\": \"customValue1\", \"customfield2\": \"customValue2\" }, \"priority\": \"normal\" }";
        String iosContent = "{ \"aps\": { \"alert\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"badge\": 0, \"sound\": \"gggg\" } }";
        String facebookContent = "{\"template\": \"content of message\"}";

        _wrapper.getPushNotificationService().sendRawPushNotification(
                "invalid_profileId",
                fcmContent,
                iosContent,
                facebookContent,
                tr);

        tr.RunExpectFail(400, ReasonCodes.INVALID_PLAYER_ID);
    }

    @Test
    public void testSendRawPushNotificationBatch() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String fcmContent = "{ \"notification\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"data\": { \"customfield1\": \"customValue1\", \"customfield2\": \"customValue2\" }, \"priority\": \"normal\" }";
        String iosContent = "{ \"aps\": { \"alert\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"badge\": 0, \"sound\": \"gggg\" } }";
        String facebookContent = "{\"template\": \"content of message\"}";


        _wrapper.getPushNotificationService().sendRawPushNotificationBatch(
                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                fcmContent,
                iosContent,
                facebookContent,
                tr);

        tr.Run();
    }

    @Test
    public void testSendRawPushNotificationToGroup() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupService().createGroup(
                "testGroup",
                "test",
                false,
                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                Helpers.createJsonPair("testInc", 123),
                Helpers.createJsonPair("test", "test"),
                Helpers.createJsonPair("test", "test"),
                tr);

        tr.Run();

        JSONObject data = tr.m_response.getJSONObject("data");
        String groupId = data.getString("groupId");

        String fcmContent = "{ \"notification\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"data\": { \"customfield1\": \"customValue1\", \"customfield2\": \"customValue2\" }, \"priority\": \"normal\" }";
        String iosContent = "{ \"aps\": { \"alert\": { \"body\": \"content of message\", \"title\": \"message title\" }, \"badge\": 0, \"sound\": \"gggg\" } }";
        String facebookContent = "{\"template\": \"content of message\"}";


        _wrapper.getPushNotificationService().sendRawPushNotificationToGroup(
                groupId,
                fcmContent,
                iosContent,
                facebookContent,
                tr);
        tr.Run();

        _wrapper.getGroupService().deleteGroup(
                groupId,
                -1,
                tr);
        tr.Run();
    }

    @Test
    public void testScheduleNormalizedPushNotificationUTC() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().scheduleNormalizedPushNotificationUTC(
                getUser(Users.UserA).profileId,
                "{ \"body\": \"content of message\", \"title\": \"message title\" }",
                Helpers.createJsonPair("1", "asdf"),
                0,
                tr);

        tr.Run();
    }

    @Test
    public void testScheduleNormalizedPushNotificationMinutes() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().scheduleNormalizedPushNotificationMinutes(
                getUser(Users.UserA).profileId,
                "{ \"body\": \"content of message\", \"title\": \"message title\" }",
                Helpers.createJsonPair("1", "asdf"),
                42,
                tr);

        tr.Run();
    }

    @Test
    public void testScheduleRichPushNotificationUTC() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().scheduleRichPushNotificationUTC(
                getUser(Users.UserA).profileId,
                1,
                Helpers.createJsonPair("1", "asdf"),
                0,
                tr);

        tr.Run();
    }

    @Test
    public void testScheduleRichPushNotificationMinutes() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().scheduleRichPushNotificationMinutes(
                getUser(Users.UserA).profileId,
                1,
                Helpers.createJsonPair("1", "asdf"),
                42,
                tr);

        tr.Run();
    }


    @Test
    public void testSendNormalizedPushNotification() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().sendNormalizedPushNotification(
                "invalid_profileId",
                "{ \"body\": \"content of message\", \"title\": \"message title\" }",
                Helpers.createJsonPair("1", "asdf"),
                tr);

        tr.RunExpectFail(400, ReasonCodes.INVALID_PLAYER_ID);
    }

    @Test
    public void testSendNormalizedPushNotificationBatch() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPushNotificationService().sendNormalizedPushNotificationBatch(
                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                "{ \"body\": \"content of message\", \"title\": \"message title\" }",
                Helpers.createJsonPair("1", "asdf"),
                tr);

        tr.Run();
    }
}