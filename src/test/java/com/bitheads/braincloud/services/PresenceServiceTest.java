package com.bitheads.braincloud.services;

import java.util.Arrays;
import java.util.ArrayList;

import org.json.JSONObject;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.Platform;
import com.bitheads.braincloud.client.ReasonCodes;

import org.junit.Test;

public class PresenceServiceTest extends TestFixtureBase
{
    @Test
    public void testForcePush() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().forcePush(
            tr
        );
        tr.Run();
    }

    @Test
    public void testGetPresenceOfFriends() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().getPresenceOfFriends(
            "aaa-bbb-ccc",
            true,
            tr
        );
        tr.Run();
    }

    @Test
    public void testGetPresenceOfGroup() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().getPresenceOfGroup(
            "Test",
            true,
            tr
        );
        tr.RunExpectFail(400, ReasonCodes.INVALID_GROUP_ID);
    }

    @Test
    public void testGetPresenceOfUsers() throws Exception
    {
        ArrayList<String> users = new ArrayList<String>(
            Arrays.asList("aaa-bbb-ccc-ddd", "bbb-ccc-ddd-eee")
        );

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().getPresenceOfUsers(
            users,
            true,
            tr
        );
        tr.Run();
    }

    @Test
    public void testRegisterListenersForFriends() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().registerListenersForFriends(
            "Test",
            true,
            tr
        );
        tr.Run();
    }

    @Test
    public void testRegisterListenersForGroup() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().registerListenersForGroup(
            "Test",
            true,
            tr
        );
        tr.RunExpectFail(400, ReasonCodes.INVALID_GROUP_ID);
    }

    @Test
    public void testRegisterListenersForProfiles() throws Exception
    {
        ArrayList<String> users = new ArrayList<String>(
            Arrays.asList("aaa-bbb-ccc-ddd", "bbb-ccc-ddd-eee")
        );

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().registerListenersForProfiles(
            users,
            true,
            tr
        );
        tr.Run();
    }

    @Test
    public void testSetVisibility() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().setVisibility(
            true,
            tr
        );
        tr.RunExpectFail(400, ReasonCodes.PRESENCE_NOT_INITIALIZED);
    }

    @Test
    public void testStopListening() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().stopListening(
            tr
        );
        tr.RunExpectFail(400, ReasonCodes.PRESENCE_NOT_INITIALIZED);
    }

    @Test
    public void testUpdateActivity() throws Exception
    {
        JSONObject jsonActivity = new JSONObject();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPresenceService().updateActivity(
            "{ \"LOCATION\": \"POKER_TABLE\", \"STATUS\": \"PLAYING_GAME\"}",
            tr
        );
        tr.RunExpectFail(400, ReasonCodes.PRESENCE_NOT_INITIALIZED);
    }
}