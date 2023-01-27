package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class AsyncMatchService {

    private enum Parameter {
        players,
        matchState,
        currentPlayer,
        status,
        summary,
        pushContent,
        matchId,
        ownerId,
        statistics,
        version
    }

    private BrainCloudClient _client;

    public AsyncMatchService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Creates an instance of an asynchronous match.
     *
     * Service Name - AsyncMatch
     * Service Operation - Create
     *
     * @param jsonOpponentIds  JSON string identifying the opponent platform and id for this match.
     *
     * Platforms are identified as:
     * BC - a brainCloud profile id
     * FB - a Facebook id
     *
     * An exmaple of this string would be:
     * [
     *     {
     *         "platform": "BC",
     *         "id": "some-braincloud-profile"
     *     },
     *     {
     *         "platform": "FB",
     *         "id": "some-facebook-id"
     *     }
     * ]
     *
     * @param pushNotificationMessage Optional push notification message to send to the other party.
     *  Refer to the Push Notification functions for the syntax required.
     * @param callback Optional instance of IServerCallback to call when the server response is received.
     */
    public void createMatch(String jsonOpponentIds,
                            String pushNotificationMessage,
                            IServerCallback callback) {

        createMatchWithInitialTurn(jsonOpponentIds, null, pushNotificationMessage, null, null, callback);
    }

    /**
     * Creates an instance of an asynchronous match with an initial turn.
     *
     * Service Name - AsyncMatch
     * Service Operation - Create
     *
     * @param jsonOpponentIds  JSON string identifying the opponent platform and id for this match.
     *
     * Platforms are identified as:
     * BC - a brainCloud profile id
     * FB - a Facebook id
     *
     * An exmaple of this string would be:
     * [
     *     {
     *         "platform": "BC",
     *         "id": "some-braincloud-profile"
     *     },
     *     {
     *         "platform": "FB",
     *         "id": "some-facebook-id"
     *     }
     * ]
     *
     * @param jsonMatchState    JSON string blob provided by the caller
     * @param pushNotificationMessage Optional push notification message to send to the other party.
     * Refer to the Push Notification functions for the syntax required.
     * @param nextPlayer Optionally, force the next player player to be a specific player
     * @param jsonSummary Optional JSON string defining what the other player will see as a summary of the game when listing their games
     * @param callback Optional instance of IServerCallback to call when the server response is received.
     */
    public void createMatchWithInitialTurn(String jsonOpponentIds, String jsonMatchState, String pushNotificationMessage,
                                           String nextPlayer, String jsonSummary, IServerCallback callback) {

        try {
            JSONArray opponentIdsData = new JSONArray(jsonOpponentIds);
            JSONObject data = new JSONObject();
            data.put(Parameter.players.name(), opponentIdsData);

            if (StringUtil.IsOptionalParameterValid(jsonMatchState)) {
                JSONObject matchStateData = new JSONObject(jsonMatchState);
                data.put(Parameter.matchState.name(), matchStateData);
            }

            if (StringUtil.IsOptionalParameterValid(nextPlayer)) {
                JSONObject currPlayer = new JSONObject();
                currPlayer.put(Parameter.currentPlayer.name(), nextPlayer);
                data.put(Parameter.status.name(), currPlayer);
            }

            if (StringUtil.IsOptionalParameterValid(jsonSummary))
                data.put(Parameter.summary.name(), new JSONObject(jsonSummary));

            if (StringUtil.IsOptionalParameterValid(pushNotificationMessage)) {
                data.put(Parameter.pushContent.name(), pushNotificationMessage);
            }

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.CREATE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a turn for the given match.
     *
     * Service Name - AsyncMatch
     * Service Operation - SubmitTurn
     *
     * @param ownerId Match owner identfier
     * @param matchId Match identifier
     * @param version Game state version to ensure turns are submitted once and in order
     * @param jsonMatchState JSON string provided by the caller
     * @param pushNotificationMessage Optional push notification message to send to the other party.
     *  Refer to the Push Notification functions for the syntax required.
     * @param nextPlayer Optionally, force the next player player to be a specific player
     * @param jsonSummary Optional JSON string that other players will see as a summary of the game when listing their games
     * @param jsonStatistics Optional JSON string blob provided by the caller
     * @param callback Optional instance of IServerCallback to call when the server response is received.
     */
    public void submitTurn(String ownerId, String matchId, BigInteger version, String jsonMatchState, String pushNotificationMessage,
                           String nextPlayer, String jsonSummary, String jsonStatistics,
                           IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);
            data.put(Parameter.version.name(), version.longValue());

            if (StringUtil.IsOptionalParameterValid(jsonMatchState))
                data.put(Parameter.matchState.name(), new JSONObject(jsonMatchState));

            if (StringUtil.IsOptionalParameterValid(jsonMatchState)) {
                JSONObject currPlayer = new JSONObject();
                currPlayer.put(Parameter.currentPlayer.name(), nextPlayer);
                data.put(Parameter.status.name(), currPlayer);
            }

            if (StringUtil.IsOptionalParameterValid(jsonMatchState)) {
                data.put(Parameter.summary.name(), new JSONObject(jsonSummary));
            }

            if (StringUtil.IsOptionalParameterValid(jsonMatchState)) {
                data.put(Parameter.statistics.name(), new JSONObject(jsonStatistics));
            }

            if (StringUtil.IsOptionalParameterValid(jsonMatchState)) {
                data.put(Parameter.pushContent.name(), pushNotificationMessage);
            }

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.SUBMIT_TURN, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows the current player (only) to update Summary data without having to submit a whole turn.
     *
     * Service Name - AsyncMatch
     * Service Operation - UpdateMatchSummary
     *
     * @param ownerId Match owner identfier
     * @param matchId Match identifier
     * @param version Game state version to ensure turns are submitted once and in order
     * @param jsonSummary JSON string that other players will see as a summary of the game when listing their games
     * @param callback Optional instance of IServerCallback to call when the server response is received.
     */
    public void updateMatchSummaryData(String ownerId, String matchId, BigInteger version, String jsonSummary,
                                       IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);
            data.put(Parameter.version.name(), version);

            if (StringUtil.IsOptionalParameterValid(jsonSummary)) {
                data.put(Parameter.summary.name(), new JSONObject(jsonSummary));
            }

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.UPDATE_SUMMARY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks the given match as complete.
     *
     * Service Name - AsyncMatch
     * Service Operation - Complete
     *
     * @param ownerId Match owner identifier
     * @param matchId Match identifier
     * @param callback Optional instance of IServerCallback to call when the server response is received.
     */
    public void completeMatch(String ownerId, String matchId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.COMPLETE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current state of the given match.
     *
     * Service Name - AsyncMatch
     * Service Operation - ReadMatch
     *
     * @param ownerId   Match owner identifier
     * @param matchId   Match identifier
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void readMatch(String ownerId, String matchId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.READ_MATCH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the match history of the given match.
     *
     * Service Name - AsyncMatch
     * Service Operation - ReadMatchHistory
     *
     * @param ownerId   Match owner identifier
     * @param matchId   Match identifier
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void readMatchHistory(String ownerId, String matchId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.READ_MATCH_HISTORY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all matches that are NOT in a COMPLETE state for which the player is involved.
     *
     * Service Name - AsyncMatch
     * Service Operation - FindMatches
     *
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void findMatches(IServerCallback callback) {
        JSONObject data = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.FIND_MATCHES, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Returns all matches that are in a COMPLETE state for which the player is involved.
     *
     * Service Name - AsyncMatch
     * Service Operation - FindMatchesCompleted
     *
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void findCompleteMatches(IServerCallback callback) {
        JSONObject data = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.FIND_MATCHES_COMPLETED, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Marks the given match as abandoned.
     *
     * Service Name - AsyncMatch
     * Service Operation - Abandon
     *
     * @param ownerId   Match owner identifier
     * @param matchId   Match identifier
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void abandonMatch(String ownerId, String matchId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.ABANDON, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the match and match history from the server. DEBUG ONLY, in production it is recommended
     *   the user leave it as completed.
     *
     * Service Name - AsyncMatch
     * Service Operation - Delete
     *
     * @param ownerId   Match owner identifier
     * @param matchId   Match identifier
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void deleteMatch(String ownerId, String matchId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.DELETE_MATCH, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks the given match as abandoned.
     *
     * Service Name - AsyncMatch
     * Service Operation - CompleteMatch
     *
     * @param ownerId   Match owner identifier
     * @param matchId   Match identifier
     * @param pushContent
     * @param summary
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void completeMatchWithSummaryData(String ownerId, String matchId, String pushContent, String summary, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);
            if(pushContent != null)
            {
            data.put(Parameter.pushContent.name(), pushContent);
            }
            JSONObject summaryData = new JSONObject(summary);
            data.put(Parameter.summary.name(), summaryData);

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.COMPLETE_MATCH_WITH_SUMMARY_DATA, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks the given match as abandoned. This call can send a notification message.
     *
     * Service Name - AsyncMatch
     * Service Operation - AbandonMatch
     *
     * @param ownerId   Match owner identifier
     * @param matchId   Match identifier
     * @param pushContent
     * @param summary
     * @param callback  Optional instance of IServerCallback to call when the server response is received.
     */
    public void abandonMatchWithSummaryData(String ownerId, String matchId, String pushContent, String summary, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.ownerId.name(), ownerId);
            data.put(Parameter.matchId.name(), matchId);
            if(pushContent != null)
            {
            data.put(Parameter.pushContent.name(), pushContent);
            }
            JSONObject summaryData = new JSONObject(summary);
            data.put(Parameter.summary.name(), summaryData);

            ServerCall sc = new ServerCall(ServiceName.asyncMatch, ServiceOperation.COMPLETE_MATCH_WITH_SUMMARY_DATA, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
