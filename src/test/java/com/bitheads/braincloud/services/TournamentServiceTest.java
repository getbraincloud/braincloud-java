package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;

import org.junit.After;
import org.junit.Test;

import java.util.Date;

/**
 * Created by bradleyh on 1/9/2017.
 */

public class TournamentServiceTest extends TestFixtureBase {

    private String _tournamentCode = "testTournament";
    private String _leaderboardId = "testTournamentLeaderboard";
    private String _divSetId = "testDivSetId";
    private boolean _didJoin;

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

        tr.RunExpectFail(400, ReasonCodes.LEADERBOARD_NOT_DIVISION_SET_INSTANCE);
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
    public void postTournamentScore() throws Exception {
        joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().postTournamentScore(
                _leaderboardId,
                200,
                Helpers.createJsonPair("test", 1),
                new Date(),
                tr);

        tr.Run();
    }

    @Test
    public void postTournamentScoreWithResults() throws Exception {
        joinTestTournament();
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTournamentService().postTournamentScoreWithResults(
                _leaderboardId,
                200,
                Helpers.createJsonPair("test", 1),
                new Date(),
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
