package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

/**
 * Created by bradleyh on 5/6/2016.
 */
public class GroupServiceTest extends TestFixtureBase {
    private final String _groupType = "test";
    private final String _entityType = "test";

    private String _groupId = null;

    @After
    public void Teardown() throws Exception {
        if (_groupId != null && !_groupId.isEmpty()) {
            deleteGroupAsUserA();
        }

        if (_wrapper.getClient().isAuthenticated()) {
            logout();
        }
    }

    @Test
    public void testAcceptGroupInvitation() throws Exception {
        getUser(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().inviteGroupMember(
                _groupId,
                getUser(Users.UserB).profileId,
                GroupService.Role.ADMIN,
                null,
                tr);
        tr.Run();

        _wrapper.getGroupService().acceptGroupInvitation(
                "invalid_groupId",
                tr);
        tr.RunExpectFail(400, ReasonCodes.MISSING_RECORD);

    }

    @Test
    public void testAutoJoinGroup() throws Exception {
        createGroupAsUserA(true);
        authenticate(Users.UserB);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().autoJoinGroup(
                _groupType,
                GroupService.AutoJoinStrategy.JoinFirstGroup,
                null,
                tr);
        tr.Run();

        logout();
        deleteGroupAsUserA();
    }

    @Test
    public void testAutoJoinGroupMulti() throws Exception {
        createGroupAsUserA(true);
        authenticate(Users.UserB);

        String[] types = new String[1];
        types[0] = _groupType;

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().autoJoinGroupMulti(
                types,
                GroupService.AutoJoinStrategy.JoinFirstGroup,
                null,
                tr);
        tr.Run();

        logout();
        deleteGroupAsUserA();
    }

    @Test
    public void testAddGroupMember() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().addGroupMember(
                _groupId,
                getUser(Users.UserB).profileId,
                GroupService.Role.ADMIN,
                null,
                tr);
        tr.Run();

        deleteGroup();
    }

    @Test
    public void testApproveGroupJoinRequest() throws Exception {
        createGroupAsUserA();
        authenticate(Users.UserB);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().joinGroup(
                _groupId,
                tr);
        tr.Run();

        logout();
        authenticate(Users.UserA);

        _wrapper.getGroupService().approveGroupJoinRequest(
                _groupId,
                getUser(Users.UserB).profileId,
                GroupService.Role.MEMBER,
                null,
                tr);
        tr.Run();

        deleteGroup();
    }

    @Test
    public void testCancelGroupInvitation() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().inviteGroupMember(
                _groupId,
                getUser(Users.UserB).profileId,
                GroupService.Role.ADMIN,
                null,
                tr);
        tr.Run();

        _wrapper.getGroupService().cancelGroupInvitation(
                _groupId,
                getUser(Users.UserB).profileId,
                tr);
        tr.Run();

        deleteGroup();
    }

    @Test
    public void testcreateGroup() throws Exception {
        authenticate(Users.UserA);
        createGroup();
        deleteGroup();
        logout();
    }

    @Test
    public void testcreateGroupWithSummaryData() throws Exception {
        authenticate(Users.UserA);
        createGroupWithSummaryData(false);
        deleteGroup();
        logout();
    }

    @Test
    public void testcreateGroupEntity() throws Exception {
        authenticate(Users.UserA);
        createGroup();
        createGroupEntity();
        deleteGroup();
        logout();
    }

    @Test
    public void testDeleteGroup() throws Exception {
        authenticate(Users.UserA);
        createGroup();
        deleteGroup();
        logout();
    }


    @Test
    public void testDeleteGroupEntity() throws Exception {
        authenticate(Users.UserA);
        createGroup();
        String entityId = createGroupEntity();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().deleteGroupEntity(
                _groupId,
                entityId,
                1,
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testGetMyGroups() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().getMyGroups(
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testIncrementGroupData() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().incrementGroupData(
                _groupId,
                Helpers.createJsonPair("testInc", 1),
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testIncrementGroupEntityData() throws Exception {
        authenticate(Users.UserA);
        createGroup();
        String id = createGroupEntity();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().incrementGroupEntityData(
                _groupId,
                id,
                Helpers.createJsonPair("testInc", 1),
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testInviteGroupMember() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().inviteGroupMember(
                _groupId,
                getUser(Users.UserB).profileId,
                GroupService.Role.MEMBER,
                null,
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testJoinGroup() throws Exception {
        createGroupAsUserA();
        authenticate(Users.UserB);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().joinGroup(
                _groupId,
                tr);
        tr.Run();

        logout();
        deleteGroupAsUserA();
    }

    @Test
    public void testLeaveGroup() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().leaveGroup(
                _groupId,
                tr);
        tr.Run();

        _wrapper.getGroupService().readGroup(
                _groupId,
                tr);
        tr.RunExpectFail(400, 40345);

        _groupId = null;
        logout();
    }

    @Test
    public void testListGroupsPage() throws Exception {
        authenticate(Users.UserA);

        String context = createContext(10, 1, "groupType", _groupType);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().listGroupsPage(
                context,
                tr);
        tr.Run();

        logout();
    }

    @Test
    public void testListGroupsPageByOffset() throws Exception {
        authenticate(Users.UserA);

        String context = createContext(10, 1, "groupType", _groupType);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().listGroupsPage(
                context,
                tr);
        tr.Run();

        context = tr.m_response.getJSONObject("data").getString("context");

        _wrapper.getGroupService().listGroupsPageByOffset(
                context,
                1,
                tr);

        logout();
    }

    @Test
    public void testListGroupsWithMember() throws Exception {
        authenticate(Users.UserA);
        //createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().listGroupsWithMember(
                getUser(Users.UserA).profileId,
                tr);
        tr.Run();

        logout();
    }

    @Test
    public void testReadGroup() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().readGroup(
                _groupId,
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testReadGroupEntitiesPage() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        String context = createContext(10, 1, "groupId", _groupId);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().readGroupEntitiesPage(
                context,
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testReadGroupEntitiesPageByOffset() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        String context = createContext(10, 1, "groupId", _groupId);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().readGroupEntitiesPage(
                context,
                tr);
        tr.Run();

        context = tr.m_response.getJSONObject("data").getString("context");

        _wrapper.getGroupService().readGroupEntitiesPageByOffset(
                context,
                1,
                tr);

        deleteGroup();
        logout();
    }

    @Test
    public void testReadGroupEntity() throws Exception {
        authenticate(Users.UserA);
        createGroup();
        String id = createGroupEntity();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().readGroupEntity(
                _groupId,
                id,
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testReadGroupMembers() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().readGroupMembers(
                _groupId,
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testRejectGroupInvitation() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().inviteGroupMember(
                _groupId,
                getUser(Users.UserB).profileId,
                GroupService.Role.ADMIN,
                null,
                tr);
        tr.Run();

        _wrapper.getGroupService().rejectGroupInvitation(
                "invalid_groupId",
                tr);
        tr.RunExpectFail(400, ReasonCodes.MISSING_RECORD);
    }

    @Test
    public void testRejectGroupJoinRequest() throws Exception {
        createGroupAsUserA();
        authenticate(Users.UserB);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().joinGroup(
                _groupId,
                tr);
        tr.Run();

        logout();
        authenticate(Users.UserA);

        _wrapper.getGroupService().rejectGroupJoinRequest(
                _groupId,
                getUser(Users.UserB).profileId,
                tr);
        tr.Run();

        deleteGroup();
    }

    @Test
    public void testRemoveGroupMember() throws Exception {
        createGroupAsUserA(true);
        authenticate(Users.UserB);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().joinGroup(
                _groupId,
                tr);
        tr.Run();

        logout();
        authenticate(Users.UserA);

        _wrapper.getGroupService().removeGroupMember(
                _groupId,
                getUser(Users.UserB).profileId,
                tr);
        tr.Run();

        deleteGroup();
    }

    @Test
    public void testSetGroupOpen() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().setGroupOpen(
                _groupId,
                true,
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    
    @Test
    public void testUpdateGroupData() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().updateGroupData(
                _groupId,
                1,
                Helpers.createJsonPair("testUpdate", 1),
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testUpdateGroupEntity() throws Exception {
        authenticate(Users.UserA);
        createGroup();
        String id = createGroupEntity();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().updateGroupEntityData(
                _groupId,
                id,
                1,
                Helpers.createJsonPair("testUpdate", 1),
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testUpdateGroupMember() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().updateGroupMember(
                "invalid_groupid",
                getUser(Users.UserA).profileId,
                null,
                null,
                tr);
        tr.RunExpectFail(400, ReasonCodes.MISSING_RECORD);
    }

    @Test
    public void testUpdateGroupName() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().updateGroupName(
                _groupId,
                "testName",
                tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testReadGroupData() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().readGroupData(
                _groupId, tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testUpdateGroupSummaryData() throws Exception {
        authenticate(Users.UserA);
        createGroup();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().updateGroupSummaryData(
                _groupId, 1, Helpers.createJsonPair("testUpdate", 1), tr);
        tr.Run();

        deleteGroup();
        logout();
    }

    @Test
    public void testGetRandomGroupsMatching() throws Exception {
        createGroupAsUserA(true);
        authenticate(Users.UserB);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().getRandomGroupsMatching(
                "{\"groupType\": \"BLUE\"}",
                0,
                tr);
        tr.Run();

        logout();
        deleteGroupAsUserA();
    }

    ///
    // Helpers
    ///

    private void createGroupAsUserA() throws Exception {
        createGroupAsUserA(false);
    }

    private void createGroupAsUserA(boolean isOpen) throws Exception {
        authenticate(Users.UserA);
        createGroup(isOpen);
        logout();
    }

    private void deleteGroupAsUserA() throws Exception {
        authenticate(Users.UserA);
        deleteGroup();
        logout();
    }

    private void authenticate(Users user) throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(user).id,
                getUser(user).password,
                true,
                tr);
        tr.Run();
    }

    private void logout() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPlayerStateService().logout(
                tr);
        tr.Run();
        _wrapper.getClient().getAuthenticationService().clearSavedProfileId();
    }

    private void createGroup() throws Exception {
        createGroup(false);
    }

    private void createGroup(boolean isOpen) throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupService().createGroup(
                "testGroup",
                _groupType,
                isOpen,
                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                Helpers.createJsonPair("testInc", 123),
                Helpers.createJsonPair("test", "test"),
                Helpers.createJsonPair("test", "test"),
                tr);

        tr.Run();

        JSONObject data = tr.m_response.getJSONObject("data");
        _groupId = data.getString("groupId");
    }

    private void createGroupWithSummaryData(boolean isOpen) throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupService().createGroupWithSummaryData(
                "testGroup",
                _groupType,
                isOpen,
                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                Helpers.createJsonPair("testInc", 123),
                Helpers.createJsonPair("test", "test"),
                Helpers.createJsonPair("test", "test"),
                Helpers.createJsonPair("summary", "data"),
                tr);

        tr.Run();

        JSONObject data = tr.m_response.getJSONObject("data");
        _groupId = data.getString("groupId");
    }

    private String createGroupEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupService().createGroupEntity(
                _groupId,
                _entityType,
                false,
                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                Helpers.createJsonPair("testInc", 123),
                tr);

        tr.Run();

        JSONObject data = tr.m_response.getJSONObject("data");
        return data.getString("entityId");
    }

    private void deleteGroup() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().deleteGroup(
                _groupId,
                -1,
                tr);
        tr.Run();
        _groupId = null;
    }

    private String createContext(int numItemsPerPage, int startPage, String searchKey, String searchValue) throws Exception {
        JSONObject context = new JSONObject();

        JSONObject pagination = new JSONObject();
        pagination.put("rowsPerPage", numItemsPerPage);
        pagination.put("pageNumber", startPage);
        context.put("pagination", pagination);

        JSONObject searchCriteria = new JSONObject();
        searchCriteria.put(searchKey, searchValue);
        context.put("searchCriteria", searchCriteria);

        return context.toString();
    }
}