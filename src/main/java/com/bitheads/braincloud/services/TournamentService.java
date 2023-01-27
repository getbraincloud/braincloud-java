package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by bradleyh on 1/9/2017.
 */

public class TournamentService {

    public enum Parameter {
        leaderboardId,
        divSetId,
        versionId,
        tournamentCode,
        initialScore,
        data,
        roundStartedEpoch,
        score,
        sort,
        beforeCount,
        afterCount,
    }

    private BrainCloudClient _client;

    public TournamentService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Processes any outstanding rewards for the given player
     *
     * Service Name - tournament
     * Service Operation - CLAIM_TOURNAMENT_REWARD
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param versionId Version of the tournament. Use -1 for the latest version.
     * @param callback The method to be invoked when the server response is received
     */
    public void claimTournamentReward(String leaderboardId, int versionId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.CLAIM_TOURNAMENT_REWARD, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Get my divisions
     *
     * Service Name - tournament
     * Service Operation - GET_DIVISIONS_INFO
     *
     * @param divSetId The leaderboard for the tournament
     * @param callback The method to be invoked when the server response is received
     */
    public void getDivisionInfo(String divSetId, IServerCallback callback)
    {
        try{
            JSONObject data = new JSONObject();
            data.put(Parameter.divSetId.name(), divSetId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_DIVISION_INFO, data, callback);
            _client.sendRequest(sc);
        }catch (JSONException je)
        {
            je.printStackTrace();
        }
    }
    
    /**
     * Get my divisions
     *
     * Service Name - tournament
     * Service Operation - GET_MY_DIVISIONS
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getMyDivisions(IServerCallback callback)
    {
        ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_MY_DIVISIONS, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Get tournament status associated with a leaderboard
     *
     * Service Name - tournament
     * Service Operation - GET_TOURNAMENT_STATUS
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param versionId Version of the tournament. Use -1 for the latest version.
     * @param callback The method to be invoked when the server response is received
     */
    public void getTournamentStatus(String leaderboardId, int versionId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_TOURNAMENT_STATUS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Join the specified division.
     * If joining tournament requires a fee, it's possible to fail at joining the division
     *
     * Service Name - tournament
     * Service Operation - JOIN_DIVISION
     *
     * @param divSetId The leaderboard for the tournament
     * @param tournamentCode Tournament to join
     * @param initialScore The initial score for players first joining a division
     *						  Usually 0, unless leaderboard is LOW_VALUE
     * @param callback The method to be invoked when the server response is received
     */
    public void joinDivision(String divSetId, String tournamentCode, long initialScore, IServerCallback callback)
    {
        try{
            JSONObject data = new JSONObject();
            data.put(Parameter.divSetId.name(), divSetId);
            data.put(Parameter.tournamentCode.name(), tournamentCode);
            data.put(Parameter.initialScore.name(), initialScore);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.JOIN_DIVISION, data, callback);
            _client.sendRequest(sc);
        }catch (JSONException je)
        {
            je.printStackTrace();
        }
    }

    /**
     * Join the specified tournament.
     * Any entry fees will be automatically collected.
     *
     * Service Name - tournament
     * Service Operation - JOIN_TOURNAMENT
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param tournamentCode Tournament to join
     * @param initialScore The initial score for players first joining a tournament
     *						  Usually 0, unless leaderboard is LOW_VALUE
     * @param callback The method to be invoked when the server response is received
     */
    public void joinTournament(String leaderboardId, String tournamentCode, long initialScore, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.tournamentCode.name(), tournamentCode);
            data.put(Parameter.initialScore.name(), initialScore);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.JOIN_TOURNAMENT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Leave the specified division.
     * Removes player score from tournament leaderboard
     *
     * Service Name - tournament
     * Service Operation - LEAVE_DIVISION_INSTANCE
     *
     * @param leaderboardId The leaderboard for the division
     * @param callback The method to be invoked when the server response is received
     */
    public void leaveDivisionInstance(String leaderboardId, IServerCallback callback)
    {
        try{
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.LEAVE_DIVISION_INSTANCE, data, callback);
            _client.sendRequest(sc);
        }catch (JSONException je)
        {
            je.printStackTrace();
        }
    }

    /**
     * Removes player's score from tournament leaderboard
     *
     * Service Name - tournament
     * Service Operation - LEAVE_TOURNAMENT
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param callback The method to be invoked when the server response is received
     */
    public void leaveTournament(String leaderboardId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.LEAVE_TOURNAMENT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * @deprecated Use postTournamentScoreUTC instead - Removal September 1, 2021
     */
    public void postTournamentScore(String leaderboardId, long score, String jsonData, Date roundStartedTime, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);

            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                JSONObject jsonObj = new JSONObject(jsonData);
                data.put(Parameter.data.name(), jsonObj);
            }

            data.put(Parameter.roundStartedEpoch.name(), roundStartedTime.getTime());

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_TOURNAMENT_SCORE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Post the users score to the leaderboard
     *
     * Service Name - tournament
     * Service Operation - POST_TOURNAMENT_SCORE
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param score The score to post
     * @param jsonData Optional data attached to the leaderboard entry
     * @param roundStartedTimeUTC Time the user started the match resulting in the score being posted in UTC mmilliseconds time.
     * @param callback The method to be invoked when the server response is received
     */
    public void postTournamentScoreUTC(String leaderboardId, long score, String jsonData, long roundStartedTimeUTC, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);

            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                JSONObject jsonObj = new JSONObject(jsonData);
                data.put(Parameter.data.name(), jsonObj);
            }

            data.put(Parameter.roundStartedEpoch.name(), roundStartedTimeUTC);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_TOURNAMENT_SCORE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * @deprecated Use postTournamentScoreWithResultsUTC instead - Removal September 1, 2021
     */
    public void postTournamentScoreWithResults(
            String leaderboardId,
            long score,
            String jsonData,
            Date roundStartedTime,
            SocialLeaderboardService.SortOrder sort,
            int beforeCount,
            int afterCount,
            long initialScore,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.beforeCount.name(), beforeCount);
            data.put(Parameter.afterCount.name(), afterCount);
            data.put(Parameter.initialScore.name(), initialScore);

            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                JSONObject jsonObj = new JSONObject(jsonData);
                data.put(Parameter.data.name(), jsonObj);
            }

            data.put(Parameter.roundStartedEpoch.name(), roundStartedTime.getTime());

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_TOURNAMENT_SCORE_WITH_RESULTS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Post the users score to the leaderboard
     *
     * Service Name - tournament
     * Service Operation - POST_TOURNAMENT_SCORE_WITH_RESULTS
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param score The score to post
     * @param jsonData Optional data attached to the leaderboard entry
     * @param roundStartedTimeUTC Time the user started the match resulting in the score being posted in UTC mmilliseconds time.
     * @param sort Sort key Sort order of page.
     * @param beforeCount The count of number of players before the current player to include.
     * @param afterCount The count of number of players after the current player to include.
     * @param initialScore The initial score for players first joining a tournament
     *						  Usually 0, unless leaderboard is LOW_VALUE
     * @param callback The method to be invoked when the server response is received
     */
    public void postTournamentScoreWithResultsUTC(
            String leaderboardId,
            long score,
            String jsonData,
            long roundStartedTimeUTC,
            SocialLeaderboardService.SortOrder sort,
            int beforeCount,
            int afterCount,
            long initialScore,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.beforeCount.name(), beforeCount);
            data.put(Parameter.afterCount.name(), afterCount);
            data.put(Parameter.initialScore.name(), initialScore);

            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                JSONObject jsonObj = new JSONObject(jsonData);
                data.put(Parameter.data.name(), jsonObj);
            }

            data.put(Parameter.roundStartedEpoch.name(), roundStartedTimeUTC);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_TOURNAMENT_SCORE_WITH_RESULTS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns the user's expected reward based on the current scores
     *
     * Service Name - tournament
     * Service Operation - VIEW_CURRENT_REWARD
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param callback The method to be invoked when the server response is received
     */
    public void viewCurrentReward(String leaderboardId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.VIEW_CURRENT_REWARD, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns the user's reward from a finished tournament
     *
     * Service Name - tournament
     * Service Operation - VIEW_REWARD
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param versionId Version of the tournament. Use -1 for the latest version.
     * @param callback The method to be invoked when the server response is received
     */
    public void viewReward(String leaderboardId, int versionId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.VIEW_REWARD, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

}
