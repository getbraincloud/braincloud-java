// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class TournamentService {

    public enum Parameter {
        afterCount,
        beforeCount,
        data,
        divSetId,
        groupId,
        initialScore,
        leaderboardId,
        roundStartedEpoch,
        score,
        sort,
        tournamentCode,
        versionId
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
     * @param versionId     Version of the tournament. Use -1 for the latest
     *                      version.
     * @param callback      The method to be invoked when the server response is
     *                      received
     */
    public void claimTournamentReward(String leaderboardId, int versionId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.CLAIM_TOURNAMENT_REWARD, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Get the status of a division
     *
     * Service Name - tournament
     * Service Operation - GET_DIVISION_INFO
     *
     * @param divSetId The id for the division
     * @param callback The method to be invoked when the server response is received
     */
    public void getDivisionInfo(String divSetId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.divSetId.name(), divSetId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_DIVISION_INFO, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Essentially the same as GetGroupTournamentStatus(), but takes a division set
     * ID instead of a leaderboard ID as its parameter. Would generally be called
     * before JoinGroupDivision() in the case that there are multiple tournaments,
     * or if the group member is shown information to make an informed choice as to
     * whether to join group in tournament.
     *
     * Service Name - tournament
     * Service Operation - GET_GROUP_DIVISION_INFO
     *
     * @param divSetId The ID for the division.
     * @param groupId  Member's group ID.
     * @param callback The method to be invoked when the server response is
     *                 received.
     */
    public void getGroupDivisionInfo(String divSetId, String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.divSetId.name(), divSetId);
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_GROUP_DIVISION_INFO, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns a list of the member's group's recently active divisions, organized
     * by simplified tournament state: ACTIVE, PENDING, COMPLETE.
     * 
     * Service Name - tournament
     * Service Name - GET_GROUP_DIVISIONS
     * 
     * @param groupId  Member's group id.
     * @param callback The method to be invoked when the server response is
     *                 received.
     */
    public void getGroupDivisions(String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_GROUP_DIVISIONS, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Get tournament status associated with a leaderboard. Option parameter:
     * leaderboard version id 'versionId'. If -1, defaults to current version.
     * 
     * Service Name - tournament
     * Service Operation - GET_GROUP_TOURNAMENT_STATUS
     * 
     * @param leaderboardId The leaderboard for the group tournament.
     * @param groupId       Member's group id.
     * @param versionId     Version of the tournament, use -1 for the latest
     *                      version.
     * @param callback      The method to be invoked when the server response is
     *                      received.
     */
    public void getGroupTournamentStatus(String leaderboardId, String groupId, int versionId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_GROUP_TOURNAMENT_STATUS, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns list of player's recently active divisions
     *
     * Service Name - tournament
     * Service Operation - GET_MY_DIVISIONS
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getMyDivisions(IServerCallback callback) {
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
     * @param versionId     Version of the tournament. Use -1 for the latest
     *                      version.
     * @param callback      The method to be invoked when the server response is
     *                      received
     */
    public void getTournamentStatus(String leaderboardId, int versionId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.GET_TOURNAMENT_STATUS, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Join the specified division.
     * If joining requires a fee, it is possible to fail at joining the division
     *
     * Service Name - tournament
     * Service Operation - JOIN_DIVISION
     *
     * @param divSetId       The id for the division
     * @param tournamentCode Tournament to join
     * @param initialScore   The initial score for players first joining a
     *                       tournament
     *                       Usually 0, unless leaderboard is LOW_VALUE
     * @param callback       The method to be invoked when the server response is
     *                       received
     */
    public void joinDivision(String divSetId, String tournamentCode, long initialScore, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.divSetId.name(), divSetId);
            data.put(Parameter.tournamentCode.name(), tournamentCode);
            data.put(Parameter.initialScore.name(), initialScore);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.JOIN_DIVISION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Similar to JoinGroupTournament(), except requires the division set id instead
     * of the leaderboard id.
     * 
     * Service Name - tournament
     * Service Operation - JOIN_GROUP_DIVISION
     * 
     * @param divSetId       Division set id.
     * @param tournamentCode The code for the group tournament to join.
     * @param groupId        Member's group id.
     * @param initialScore   The initial score to give the group on the group
     *                       leaderboard.
     * @param callback       The method to be invoked when the server response is
     *                       received.
     */
    public void joinGroupDivision(String divSetId, String tournamentCode, String groupId, long initialScore,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.divSetId.name(), divSetId);
            data.put(Parameter.tournamentCode.name(), tournamentCode);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.initialScore.name(), initialScore);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.JOIN_GROUP_DIVISION, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Enrolls a member's group in the group tournament and assigns an initial
     * score.
     * 
     * Service Name - tournament
     * Service Operation - JOIN_GROUP_TOURNAMENT
     * 
     * @param leaderboardId  The leaderboard for the group tournament.
     * @param tournamentCode Group tournament to join.
     * @param groupId        Member's group id.
     * @param initialScore   Initial score for the user.
     * @param callback       The method to be invoked when the server response is
     *                       received.
     */
    public void joinGroupTournament(String leaderboardId, String tournamentCode, String groupId, long initialScore,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.tournamentCode.name(), tournamentCode);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.initialScore.name(), initialScore);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.JOIN_GROUP_TOURNAMENT, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
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
     * @param leaderboardId  The leaderboard for the tournament
     * @param tournamentCode Tournament to join
     * @param initialScore   The initial score for players first joining a
     *                       tournament
     *                       Usually 0, unless leaderboard is LOW_VALUE
     * @param callback       The method to be invoked when the server response is
     *                       received
     */
    public void joinTournament(String leaderboardId, String tournamentCode, long initialScore,
            IServerCallback callback) {

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
     * Removes player from division instance
     * Also removes division instance from player's division list
     *
     * Service Name - tournament
     * Service Operation - LEAVE_DIVISION_INSTANCE
     *
     * @param leaderboardId The leaderboard for the tournament
     * @param callback      The method to be invoked when the server response is
     *                      received
     */
    public void leaveDivisionInstance(String leaderboardId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.LEAVE_DIVISION_INSTANCE, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Similar to LeaveGroupTournament(), but removes member's group from division
     * instance and also ensures that the division instance is removed from the
     * group's division list.
     * 
     * Service Name - tournament
     * Service Operation - LEAVE_GROUP_DIVISION_INSTANCE
     * 
     * @param leaderboardId Id of the division leaderboard the member's group is in.
     * @param groupId       Member's group id.
     * @param callback      The method to be invoked when the server response is
     *                      received.
     */
    public void leaveGroupDivisionInstance(String leaderboardId, String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.LEAVE_GROUP_DIVISION_INSTANCE, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Allows a group member to remove the group's score from the tournament
     * leaderboard.
     * 
     * Service Name - tournament
     * Service Operation - LEAVE_GROUP_TOURNAMENT
     * 
     * @param leaderboardId The leaderboard for the tournament.
     * @param groupId       Member's group id.
     * @param callback
     */
    public void leaveGroupTournament(String leaderboardId, String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.LEAVE_GROUP_TOURNAMENT, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
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
     * @param callback      The method to be invoked when the server response is
     *                      received
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
     * Posts the given score for member's group to the group leaderboard. Group's
     * score is updated, if applicable, based on leaderboard type (best score,
     * latest score, cumulative score).
     * 
     * Service Name - tournament
     * Service Operation - POST_GROUP_TOURNAMENT_SCORE
     * 
     * @param leaderboardId     The leaderboard for the tournament.
     * @param groupId           Member's group id.
     * @param score             The score to post for group.
     * @param jsonData          Optional data attached to the group leaderboard
     *                          entry, if updated.
     * @param roundStartedEpoch UTC timestamp the member started the match resulting
     *                          in the score being posted. (date in millis.)
     * @param callback          The method to be invoked when the server response is
     *                          received.
     */
    public void postGroupTournamentScore(String leaderboardId, String groupId, long score, String jsonData,
            long roundStartedEpoch, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                JSONObject jsonObj = new JSONObject(jsonData);
                data.put(Parameter.data.name(), jsonObj);
            }
            data.put(Parameter.roundStartedEpoch.name(), roundStartedEpoch);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_GROUP_TOURNAMENT_SCORE, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Posts the given score for member's group to the group leaderboard and returns leaderboard results. Group's score is updated, if applicable, based on leaderboard type (best score, latest score, cumulative score).
     * 
     * Service Name - tournament
     * Service Name - POST_GROUP_TOURNAMENT_SCORE_WITH_RESULTS
     * 
     * @param leaderboardId The leaderboard for the tournament.
     * @param groupId 	Member's group id.
     * @param score The score to post for group.
     * @param jsonData Optional data attached to the group leaderboard entry, if updated.
     * @param roundStartedEpoch UTC timestamp the member started the match resulting in the score being posted. (date in millis.)
     * @param sort Sort key for sort order of page. ("HIGH_TO_LOW" or "LOW_TO_HIGH")
     * @param beforeCount The count of groups to include before the current group.
     * @param afterCount 	The count of groups to include after the current group.
     * @param initialScore 	The initial score for group on first joining a tournament, applicable to this call if auto-join supported. Usually 0, unless leaderboard is LOW_VALUE.
     * @param callback The method to be invoked when the server response is received.
     */
    public void postGroupTournamentScoreWithResults(String leaderboardId, String groupId, long score, String jsonData,
            long roundStartedEpoch, SocialLeaderboardService.SortOrder sort, int beforeCount, int afterCount, long initialScore, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                JSONObject jsonObj = new JSONObject(jsonData);
                data.put(Parameter.data.name(), jsonObj);
            }
            data.put(Parameter.roundStartedEpoch.name(), roundStartedEpoch);
            data.put(Parameter.sort.name(), sort);
            data.put(Parameter.beforeCount.name(), beforeCount);
            data.put(Parameter.afterCount.name(), afterCount);
            data.put(Parameter.initialScore.name(), initialScore);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_GROUP_TOURNAMENT_SCORE_WITH_RESULTS, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Post the users score to the leaderboard - UTC time
     *
     * Service Name - tournament
     * Service Operation - POST_TOURNAMENT_SCORE
     *
     * @param leaderboardId       The leaderboard for the tournament
     * @param score               The score to post
     * @param jsonData            Optional data attached to the leaderboard entry
     * @param roundStartedTimeUTC Time the user started the match resulting in the
     *                            score being posted in UTC. Use UTC time in
     *                            milliseconds since epoch
     * @param callback            The method to be invoked when the server response
     *                            is received
     */
    public void postTournamentScoreUTC(String leaderboardId, long score, String jsonData, long roundStartedTimeUTC,
            IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);

            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                JSONObject jsonObj = new JSONObject(jsonData);
                data.put(Parameter.data.name(), jsonObj);
            }

            data.put(Parameter.roundStartedEpoch.name(), roundStartedTimeUTC);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_TOURNAMENT_SCORE, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Post the users score to the leaderboard - UTC time
     *
     * Service Name - tournament
     * Service Operation - POST_TOURNAMENT_SCORE_WITH_RESULTS
     *
     * @param leaderboardId       The leaderboard for the tournament
     * @param score               The score to post
     * @param jsonData            Optional data attached to the leaderboard entry
     * @param roundStartedTimeUTC Time the user started the match resulting in the
     *                            score being posted in UTC. Use UTC time in
     *                            milliseconds since epoch
     * @param sort                Sort key Sort order of page.
     * @param beforeCount         The count of number of players before the current
     *                            player to include.
     * @param afterCount          The count of number of players after the current
     *                            player to include.
     * @param initialScore        The initial score for players first joining a
     *                            tournament
     *                            Usually 0, unless leaderboard is LOW_VALUE
     * @param callback            The method to be invoked when the server response
     *                            is received
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

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.POST_TOURNAMENT_SCORE_WITH_RESULTS,
                    data, callback);
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
     * @param callback      The method to be invoked when the server response is
     *                      received
     */
    public void viewCurrentReward(String leaderboardId, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);

            ServerCall sc = new ServerCall(ServiceName.tournament, ServiceOperation.VIEW_CURRENT_REWARD, data,
                    callback);
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
     * @param versionId     Version of the tournament. Use -1 for the latest
     *                      version.
     * @param callback      The method to be invoked when the server response is
     *                      received
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
