package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class PlayerStateServiceTest extends TestFixtureBase
{

    @Test
    public void testDeletePlayer() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().deleteUser(
                tr);
        tr.Run();
        _wrapper.getClient().getAuthenticationService().clearSavedProfileId();
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(Users.UserA).id,
                getUser(Users.UserA).password,
                true,
                tr);
        tr.Run();
        getUser(Users.UserA).profileId = _wrapper.getClient().getAuthenticationService().getProfileId();
    }

    @Test
    public void testGetAttributes() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().getAttributes(
                tr);

        tr.Run();
    }

    @Test
    public void testLogout() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().logout(
                tr);

        tr.Run();
    }

    @Test
    public void testReadPlayerState() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().readUserState(
                tr);

        tr.Run();
    }

    @Test
    public void testRemoveAttributes() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().removeAttributes(
                new String[]{"testAttrib1", "testAttrib2"},
                tr);

        tr.Run();
    }

    @Test
    public void testResetPlayer() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().resetUser(
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateAttributes() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        JSONObject stats = new JSONObject();
        stats.put("testAttrib1", "value1");
        stats.put("testAttrib2", "value2");

        _wrapper.getPlayerStateService().updateAttributes(
                stats.toString(),
                false,
                tr);

        tr.Run();
    }

    @Test
    public void testUpdatePlayerName() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().updateName(
                "ABC",
                tr);

        tr.Run();
    }

    @Test
    public void testUpdatePlayerNameWithEmoji() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().updateName(
                "\uD83D\uDE0A \uD83D\uDE0A \uD83D\uDE0A",
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateSummaryFriendData() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        JSONObject friendData = new JSONObject();
        friendData.put("field", "value");
        _wrapper.getPlayerStateService().updateSummaryFriendData(
                friendData.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testUpdatePlayerPictureUrl() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().updateUserPictureUrl(
                "https://some.domain.com/mypicture.jpg",
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateContactEmail() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().updateContactEmail(
                "something@bctestdomain.com",
                tr);

        tr.Run();
    }

    @Test
    public void testClearUserStatus() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().clearUserStatus(
                "name",
                tr);

        tr.Run();
    }

    @Test
    public void testExtendUserStatus() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().extendUserStatus(
                "name",
                0,
                "{\"test\": \"Testing\"}",
                tr);

        tr.Run();
    }

    @Test
    public void testGetUserStatus() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().getUserStatus(
                "name",
                tr);

        tr.Run();
    }

    @Test
    public void testSetUserStatus() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().setUserStatus(
                "name",
                0,
                "{\"test\": \"Testing\"}",
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateTimeZoneOffset() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().updateTimeZoneOffset(
                1,
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateLanguageCode() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().updateLanguageCode(
                "fr",
                tr);

        tr.Run();
    }
}