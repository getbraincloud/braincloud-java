package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;

import org.junit.Test;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class FriendServiceTest extends TestFixtureBase
{

    @Test
    public void testGetFriendProfileInfoForExternalId() throws Exception
    {

    }

    @Test
    public void testGetExternalIdForProfileId() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().getExternalIdForProfileId(
                getUser(Users.UserA).profileId,
                "Facebook",
                tr);

        tr.Run();
    }

    @Test
    public void testGetSummaryDataForProfileId() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().getSummaryDataForProfileId(
                getUser(Users.UserA).profileId,
                tr);

        tr.Run();
    }

    @Test
    public void findUsersByExactName() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().findUsersByExactName(
                "search",
                10,
                tr);

        tr.Run();
    }

    @Test 
    public void findUserByExactUniversalId() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().findUserByExactUniversalId(
            "randomUniversalId",
            tr);
        
        //we expect it will search, though it won't find anything.
        tr.Run();
    }

    @Test
    public void findUsersByNameStartingWith() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().findUsersByNameStartingWith(
            "completelyRandomName",
            30,        
            tr);

        tr.Run();
    }

    @Test
    public void findUsersByUniversalIdStartingWith() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().findUsersByUniversalIdStartingWith(
            "completelyRandomUniversalId",
            30,        
            tr);

        tr.Run();
    }

    @Test
    public void findUsersBySubstrName() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().findUsersBySubstrName(
                "search",
                10,
                tr);

        tr.Run();
    }

    @Test
    public void testGetProfileInfoForCredential() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().getProfileInfoForCredential(
                getUser(Users.UserA).id,
                AuthenticationType.Universal,
                tr);

        tr.Run();
    }

    @Test
    public void testGetProfileInfoForExternalAuthId() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().getProfileInfoForExternalAuthId(
                getUser(Users.UserA).id,
                "failType",
                tr);

        tr.RunExpectFail(400, ReasonCodes.INVALID_EXT_AUTH_TYPE);
    }

    @Test
    public void testListFriends() throws Exception
    {
        addFriend();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().listFriends(
                FriendService.FriendPlatform.All, false, tr);

        tr.Run();
    }

    @Test
    public void testGetMySocialInfo() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getFriendService().getMySocialInfo(
                FriendService.FriendPlatform.All, false, tr);

        tr.Run();
    }

    @Test
    public void testAddFriends() throws Exception
    {
        addFriend();
    }

    @Test
    public void testAddFriendsFromPlatform() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        String[] externalIds = { };
        _wrapper.getFriendService().addFriendsFromPlatform(FriendService.FriendPlatform.Facebook, "ADD", externalIds, tr);
        tr.Run();
    }

    @Test
    public void testRemoveFriends() throws Exception
    {
        addFriend();

        TestResult tr = new TestResult(_wrapper);
        String[] ids = { getUser(Users.UserB).profileId };
        _wrapper.getFriendService().removeFriends(ids, tr);
        tr.Run();
    }

    @Test
    public void testGetUsersOnlineStatus() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        String[] ids = { getUser(Users.UserB).profileId };
        _wrapper.getFriendService().getUsersOnlineStatus(ids, tr);
        tr.Run();
    }

    @Test
    public void testReadFriendEntity() throws Exception
    {

    }

    @Test
    public void testReadFriendsEntities() throws Exception
    {

    }

    @Test
    public void testReadFriendPlayerState() throws Exception
    {

    }

    private void addFriend()
    {
        TestResult tr = new TestResult(_wrapper);
        String[] ids = { getUser(Users.UserB).profileId };
        _wrapper.getFriendService().addFriends(ids, tr);
        tr.Run();
    }
}