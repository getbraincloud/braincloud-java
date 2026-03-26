package com.bitheads.braincloud.services;

import static org.junit.Assert.fail;

import java.util.Date;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bitheads.braincloud.client.ReasonCodes;

public class TournamentServiceTest extends TestFixtureBase {

    private String _tournamentCode = "testTournament";
    private String _leaderboardId = "testTournamentLeaderboard";
    private String _groupLeaderboardId = "groupTournament";
    private boolean _didJoin;
    private String _groupId;
    private int _createGroupReasonCode;
    private String _createGroupStatusMessage;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        _groupId = null;
        _createGroupReasonCode = 0;
        _createGroupStatusMessage = "unknown";
    }
    
    @After
    public void Teardown() throws Exception {
        if (_didJoin) {
            leaveTestTournament();
        }
    }

    @Test
    public void claimTournamentReward() throws Exception {
        int version = joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().claimTournamentReward(
                _leaderboardId,
                version,
                tr);

        tr.RunExpectFail(400, ReasonCodes.VIEWING_REWARD_FOR_NON_PROCESSED_TOURNAMENTS);
    }

    @Test
    public void getDivisionInfo() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().getDivisionInfo(
            "Invalid_Id",
            tr
        );
        tr.RunExpectFail(400, ReasonCodes.DIVISION_SET_DOESNOT_EXIST);
    }

    @Test
    public void getMyDivisions() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getTournamentService().getMyDivisions(tr);
        tr.Run();
    }

    @Test
    public void joinDivision() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().joinDivision(
                "Invalid_Id",
                _tournamentCode,
                0,
                tr);

        tr.RunExpectFail(400, ReasonCodes.DIVISION_SET_DOESNOT_EXIST);
    }

    @Test
    public void leaveDivisionInstance() { 
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().leaveDivisionInstance(
                "Invalid_Id",
                tr);

        tr.RunExpectFail(500, ReasonCodes.NO_LEADERBOARD_FOUND);
    }

    @Test
    public void getTournamentStatus() throws Exception {
        int version = joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().getTournamentStatus(
                _leaderboardId,
                version,
                tr);

        tr.Run();
    }

    @Test
    public void joinTournament() throws Exception {
        joinTestTournament();
    }

    @Test
    public void leaveTournament() throws Exception {
        joinTestTournament();
        leaveTestTournament();
    }

    @Test
    public void postTournamentScoreUTC() throws Exception {
        joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().postTournamentScoreUTC(
                _leaderboardId,
                200,
                Helpers.createJsonPair("test", 1),
                new Date().getTime(),
                tr);

        tr.Run();
    }

    @Test
    public void postTournamentScoreWithResultsUTC() throws Exception {
        joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        long score = 200;

        _wrapper.getTournamentService().postTournamentScoreWithResultsUTC(
                _leaderboardId,
                score,
                Helpers.createJsonPair("test", 1),
                new Date().getTime(),
                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                10,
                10,
                0,
                tr);

        tr.Run();
    }

    @Test
    public void viewCurrentReward() throws Exception {
        joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().viewCurrentReward(
                _leaderboardId,
                tr);

        tr.Run();
    }

    @Test
    public void viewReward() throws Exception {
        joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().viewReward(
                _leaderboardId,
                -1,
                tr);

        tr.RunExpectFail(400, ReasonCodes.PLAYER_NOT_ENROLLED_IN_TOURNAMENT);
    }

    @Test
    public void getGroupDivisionInfo() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        if (!createTestGroup()) {
            fail("Failed to create group.\nReason Code: " + _createGroupReasonCode + "\nStatus Message: "
                    + _createGroupStatusMessage + "\n");
        }

        try {
            _wrapper.getTournamentService().getGroupDivisionInfo("bronzeGroup", _groupId, tr);
            tr.Run();
        } finally {
            deleteTestGroup();
        }
    }

    @Test
    public void getGroupDivisions() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        if (!createTestGroup()) {
            fail("Failed to create group.\nReason Code: " + _createGroupReasonCode + "\nStatus Message: "
                    + _createGroupStatusMessage + "\n");
        }

        try {
            _wrapper.getTournamentService().getGroupDivisions(_groupId, tr);
            tr.Run();
        } finally {
            deleteTestGroup();
        }
    }

    @Test
    public void getGroupTournamentStatus() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        if (!createTestGroup()) {
            fail("Failed to create group.\nReason Code: " + _createGroupReasonCode + "\nStatus Message: "
                    + _createGroupStatusMessage + "\n");
        }

        try {
            _wrapper.getTournamentService().getGroupTournamentStatus(_groupLeaderboardId, _groupId, -1, tr);
            tr.Run();
        } finally {
            deleteTestGroup();
        }
    }

    @Test
    public void joinAndLeaveGroupDivision() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        if (!createTestGroup()) {
            fail("Failed to create group.\nReason Code: " + _createGroupReasonCode + "\nStatus Message: "
                    + _createGroupStatusMessage + "\n");
        }

        try {
            _wrapper.getTournamentService().joinGroupDivision("bronzeGroup", "testGroupTournament", _groupId, 0, tr);
            tr.Run();

            String leaderboardId = tr.m_response.optJSONObject("data").optString("leaderboardId");
            if (leaderboardId.isEmpty())
                fail("Error reading JOIN_GROUP_DIVISION response...");

            _wrapper.getTournamentService().leaveGroupDivisionInstance(leaderboardId, _groupId, tr);
            tr.Run();
        } finally {
            deleteTestGroup();
        }
    }

    @Test
    public void joinPostLeaveGroupTournament() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        if (!createTestGroup()) {
            fail("Failed to create group.\nReason Code: " + _createGroupReasonCode + "\nStatus Message: "
                    + _createGroupStatusMessage + "\n");
        }

        try {
            _wrapper.getTournamentService().joinGroupTournament(_groupLeaderboardId, "testGroupTournament", _groupId, 0,
                    tr);
            tr.Run();

            _wrapper.getTournamentService().postGroupTournamentScore(_groupLeaderboardId, _groupId, 10, "{}", new Date().getTime(), tr);
            tr.Run();

            _wrapper.getTournamentService().postGroupTournamentScoreWithResults(_groupLeaderboardId, _groupId, 100, "{}", new Date().getTime(), SocialLeaderboardService.SortOrder.HIGH_TO_LOW, 10, 10, 0, tr);
            tr.Run();

            _wrapper.getTournamentService().leaveGroupTournament(_groupLeaderboardId, _groupId, tr);
            tr.Run();
        } finally {
            deleteTestGroup();
        }
    }

    private boolean createTestGroup() {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().createGroup(
                "JavaTestGroup",
                "csharpTest",
                true,
                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                "{}",
                Helpers.createJsonPair("testInc", 123),
                Helpers.createJsonPair("test", "test"),
                tr);
        tr.Run(true);

        if (!tr.m_result) {
            _createGroupReasonCode = tr.m_reasonCode;
            _createGroupStatusMessage = tr.m_statusMessage;
            
            return false;
        }

        JSONObject data = tr.m_response.optJSONObject("data");
        if (data == null) return false;

        _groupId = data.optString("groupId");
        
        return !_groupId.isEmpty();
    }

    private void deleteTestGroup() {
        if (_groupId == null || _groupId.isEmpty()) return;
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupService().deleteGroup(_groupId, -1, tr);
        tr.Run();
    }

    private int joinTestTournament() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().joinTournament(
                _leaderboardId,
                _tournamentCode,
                0,
                tr);

        tr.Run();
        _didJoin = true;

        _wrapper.getTournamentService().getTournamentStatus(
                _leaderboardId,
                -1,
                tr);
        tr.Run();

        int version = tr.m_response.getJSONObject("data").getInt("versionId");
        return version;
    }

    private void leaveTestTournament() {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().leaveTournament(
                _leaderboardId,
                tr);

        tr.Run();

        _didJoin = false;
    }
}
