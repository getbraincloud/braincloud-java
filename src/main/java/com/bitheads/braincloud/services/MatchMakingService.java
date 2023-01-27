package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class MatchMakingService {

    private enum Parameter {
        playerRating,
        minutes,
        rangeDelta,
        numMatches,
        extraParms,
        attributes,
        playerId
    }

    private BrainCloudClient _client;

    public MatchMakingService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Read match making record
     *
     * Service Name - MatchMaking
     * Service Operation - Read
     *
     * @param callback The callback.
     */
    public void read(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.READ, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Sets player rating
     *
     * Service Name - MatchMaking
     * Service Operation - SetPlayerRating
     *
     * @param playerRating The new player rating.
     * @param callback The callback.
     */
    public void setPlayerRating(long playerRating, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playerRating.name(), playerRating);

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.SET_PLAYER_RATING, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Resets player rating
     *
     * Service Name - MatchMaking
     * Service Operation - ResetPlayerRating
     *
     * @param callback The callback.
     */
    public void resetPlayerRating(IServerCallback callback) {
        BrainCloudClient braincloudClient = _client;
        ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.RESET_PLAYER_RATING, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Increments player rating
     *
     * Service Name - MatchMaking
     * Service Operation - IncrementPlayerRating
     *
     * @param increment The increment amount
     * @param callback The callback.
     */
    public void incrementPlayerRating(long increment, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playerRating.name(), increment);

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.INCREMENT_PLAYER_RATING, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Decrements player rating
     *
     * Service Name - MatchMaking
     * Service Operation - DecrementPlayerRating
     *
     * @param decrement The decrement amount
     * @param callback The callback.
     */
    public void decrementPlayerRating(long decrement, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playerRating.name(), decrement);

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.DECREMENT_PLAYER_RATING, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Turns shield on
     *
     * Service Name - MatchMaking
     * Service Operation - ShieldOn
     *
     * @param callback The callback.
     */
    public void turnShieldOn(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.SHIELD_ON, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Turns shield on for the specified number of minutes
     *
     * Service Name - MatchMaking
     * Service Operation - ShieldOnFor
     *
     * @param minutes Number of minutes to turn the shield on for
     * @param callback The callback.
     */
    public void turnShieldOnFor(int minutes, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.minutes.name(), minutes);

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.SHIELD_ON_FOR, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Increases the shield on time by specified number of minutes
     *
     * Service Name - MatchMaking
     * Service Operation - ShieldOnFor
     *
     * @param minutes Number of minutes to increase the shield turn for
     * @param callback The callback.
     */
    public void incrementShieldOnFor(int minutes, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.minutes.name(), minutes);

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.INCREMENT_SHIELD_ON_FOR, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Turns shield off
     *
     * Service Name - MatchMaking
     * Service Operation - ShieldOff
     *
     * @param callback The callback.
     */
    public void turnShieldOff(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.SHIELD_OFF, null, callback);
        _client.sendRequest(sc);
    }


    /**
     * Gets the shield expiry for the given player id. Passing in a null player id
     * will return the shield expiry for the current player. The value returned is
     * the time in UTC millis when the shield will expire.
     *
     * Service Name - MatchMaking
     * Service Operation - GetShieldExpiry
     *
     * @param playerId The player id or use null to retrieve for the current player
     * @param callback The callback.
     */
    public void getShieldExpiry(String playerId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            if (StringUtil.IsOptionalParameterValid(playerId)) {
                data.put(Parameter.playerId.name(), playerId);
            }

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.GET_SHIELD_EXPIRY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Finds matchmaking enabled players
     *
     * Service Name - MatchMaking
     * Service Operation - FIND_PLAYERS
     *
     * @param rangeDelta The range delta
     * @param numMatches The maximum number of matches to return
     * @param callback The callback.
     */
    public void findPlayers(long rangeDelta, long numMatches, IServerCallback callback) {
        findPlayersWithAttributes(rangeDelta, numMatches, null, callback);
    }

    /**
     * Finds matchmaking enabled players with additional attributes
     *
     * Service Name - MatchMaking
     * Service Operation - FIND_PLAYERS
     *
     * @param rangeDelta The range delta
     * @param numMatches The maximum number of matches to return
     * @param jsonAttributes Attributes match criteria
     * @param callback The method to be invoked when the server response is received
     */
    public void findPlayersWithAttributes(long rangeDelta, long numMatches, String jsonAttributes, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.rangeDelta.name(), rangeDelta);
            data.put(Parameter.numMatches.name(), numMatches);

            if (StringUtil.IsOptionalParameterValid(jsonAttributes)) {
                JSONObject jsonData = new JSONObject(jsonAttributes);
                data.put(Parameter.attributes.name(), jsonData);
            }

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.FIND_PLAYERS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Finds matchmaking enabled players using a cloud code filter
     *
     * Service Name - MatchMaking
     * Service Operation - FIND_PLAYERS_USING_FILTER
     *
     * @param rangeDelta The range delta
     * @param numMatches The maximum number of matches to return
     * @param jsonExtraParms Other parameters
     * @param callback The callback.
     */
    public void findPlayersUsingFilter(long rangeDelta, long numMatches, String jsonExtraParms, IServerCallback callback) {
        findPlayersWithAttributesUsingFilter(rangeDelta, numMatches, null, jsonExtraParms, callback);
    }

    /**
     * Finds matchmaking enabled players using a cloud code filter
     * and additional attributes
     *
     * Service Name - MatchMaking
     * Service Operation - FIND_PLAYERS_USING_FILTER
     *
     * @param rangeDelta The range delta
     * @param numMatches The maximum number of matches to return
     * @param jsonAttributes Attributes match criteria
     * @param jsonExtraParms Parameters to pass to the CloudCode filter script
     * @param callback The method to be invoked when the server response is received
     */
    public void findPlayersWithAttributesUsingFilter(long rangeDelta, long numMatches, String jsonAttributes, String jsonExtraParms, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.rangeDelta.name(), rangeDelta);
            data.put(Parameter.numMatches.name(), numMatches);

            if (StringUtil.IsOptionalParameterValid(jsonExtraParms)) {
                JSONObject jsonData = new JSONObject(jsonExtraParms);
                data.put(Parameter.extraParms.name(), jsonData);
            }

            if (StringUtil.IsOptionalParameterValid(jsonAttributes)) {
                JSONObject jsonData = new JSONObject(jsonAttributes);
                data.put(Parameter.attributes.name(), jsonData);
            }

            ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.FIND_PLAYERS_USING_FILTER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Enables Match Making for the Player
     *
     * Service Name - MatchMaking
     * Service Operation - EnableMatchMaking
     *
     * @param callback The callback.
     */
    public void enableMatchMaking(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.ENABLE_FOR_MATCH, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Disables Match Making for the Player
     *
     * Service Name - MatchMaking
     * Service Operation - EnableMatchMaking
     *
     * @param callback The callback.
     */
    public void disableMatchMaking(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.matchMaking, ServiceOperation.DISABLE_FOR_MATCH, null, callback);
        _client.sendRequest(sc);
    }
}
