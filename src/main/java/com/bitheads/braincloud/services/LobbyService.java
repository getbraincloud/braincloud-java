package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.client.StatusCodes;
import com.bitheads.braincloud.comms.ServerCall;
import com.bitheads.braincloud.comms.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

import java.net.URL;

/**
 * Created by David St-Louis on 2018-07-04
 */
public class LobbyService implements IServerCallback{
 
    private enum Parameter {
        lobbyType,
        rating,
        maxSteps,
        algo,
        filterJson,
        otherUserCxIds,
        settings,
        isReady,
        extraJson,
        teamCode,
        lobbyId,
        cxId,
        signalData,
        toTeamCode,
        roomType,
        lobbyTypes,
        pingData,
        criteriaJson
    }

    class ErrorCallbackEvent {
        public IServerCallback callback;
        public ServiceName serviceName;
        public ServiceOperation serviceOperation;
        public int statusCode;
        public int reasonCode;
        public String jsonError;
    };

    class ActivePing {
        public ActivePing(String regionName, String regionURL, Object sync) {
            _regionName = regionName;
            _regionURL = regionURL;
            _sync = sync;
            _ping = -1;

            _thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Ping as many times as required (MAX_PING_CALLS)
                        ArrayList<Integer> pings = new ArrayList<Integer>();
                        String fullURL = "http://" + _regionURL;
                        for (int i = 0; i < MAX_PING_CALLS; ++i) {
                            pings.add(new Integer(pingHost(fullURL)));
                        }

                        // Sort results from faster to slowest
                        pings.sort(null);
                                
                        // Calculate the average, minus the slowest one (MAX_PING_CALLS - 1)
                        int pingResult = 0;
                        for (int i = 0; i < MAX_PING_CALLS - 1; ++i) {
                            pingResult += pings.get(i);
                        }
                        pingResult /= MAX_PING_CALLS - 1;

                        // Notify
                        _ping = pingResult;
                        synchronized(_sync) {
                            _sync.notify();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });
            _thread.start();
        }

        public String getRegion() {
            return _regionName;
        }

        public int getPing() {
            return _ping;
        }

        public boolean isFinished() {
            return _ping != -1;
        }

        private String _regionName;
        private String _regionURL;
        private Object _sync;
        private int _ping;
        private Thread _thread;
    }

    private BrainCloudClient _client;
    private boolean _loggingEnabled = false;

    private JSONObject _pingData = null;
    private JSONObject _pingRegions = null;
    private Thread _pingRegionsThread = null;
    private IServerCallback _pingCallback = null;
    private AtomicBoolean _isPingRunning = new AtomicBoolean(false);
    private Object _pingSync = new Object();
    private IServerCallback _getRegionsForLobbiesCallback = null;
    private ArrayList<ErrorCallbackEvent> _errorCallbackQueue = new ArrayList<ErrorCallbackEvent>();

    private final int MAX_PING_CALLS = 4;
    private final int NUM_PING_CALLS_IN_PARALLEL = 2;

    public LobbyService(BrainCloudClient client) {
        _client = client;
    }

    public void enableLogging(boolean isEnabled) {
        _loggingEnabled = isEnabled;
    }

    /**
     * Creates a new lobby.
     * 
     * Sends LOBBY_JOIN_SUCCESS message to the user, with full copy of lobby data Sends LOBBY_MEMBER_JOINED to all lobby members, with copy of member data
     *
     * Service Name - Lobby
     * Service Operation - CREATE_LOBBY
     *
     * @param lobbyType The type of lobby to look for. Lobby types are defined in the portal.
     * @param rating The skill rating to use for finding the lobby. Provided as a separate parameter because it may not exactly match the user's rating (especially in cases where parties are involved).
     * @param otherUserCxIds Array of other users (i.e. party members) to add to the lobby as well. Will constrain things so that only lobbies with room for all players will be considered.
     * @param isReady Initial ready-status of this user.
     * @param extraJson Initial extra-data about this user.
     * @param teamCode Preferred team for this user, if applicable. Send "" or null for automatic assignment.
     * @param settings Configuration data for the room.
     */
    public void createLobby(String lobbyType, int rating, ArrayList<String> otherUserCxIds, Boolean isReady, String extraJson, String teamCode, String settings, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.rating.name(), rating);
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);
            if (StringUtil.IsOptionalParameterValid(settings)) {
                data.put(Parameter.settings.name(), new JSONObject(settings));
            }

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.CREATE_LOBBY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Creates a new lobby with ping data 
     * 
     * Sends LOBBY_JOIN_SUCCESS message to the user, with full copy of lobby data Sends LOBBY_MEMBER_JOINED to all lobby members, with copy of member data
     *
     * Service Name - Lobby
     * Service Operation - CREATE_LOBBY_WITH_PING_DATA
     *
     * @param lobbyType The type of lobby to look for. Lobby types are defined in the portal.
     * @param rating The skill rating to use for finding the lobby. Provided as a separate parameter because it may not exactly match the user's rating (especially in cases where parties are involved).
     * @param otherUserCxIds Array of other users (i.e. party members) to add to the lobby as well. Will constrain things so that only lobbies with room for all players will be considered.
     * @param isReady Initial ready-status of this user.
     * @param extraJson Initial extra-data about this user.
     * @param teamCode Preferred team for this user, if applicable. Send "" or null for automatic assignment.
     * @param settings Configuration data for the room.
     */
    public void createLobbyWithPingData(String lobbyType, int rating, ArrayList<String> otherUserCxIds, Boolean isReady, String extraJson, String teamCode, String settings, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.rating.name(), rating);
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);
            if (StringUtil.IsOptionalParameterValid(settings)) {
                data.put(Parameter.settings.name(), new JSONObject(settings));
            }

            attachPingDataAndSend(data, ServiceOperation.CREATE_LOBBY_WITH_PING_DATA, callback);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Finds a lobby matching the specified parameters. Asynchronous - returns 200 to indicate that matchmaking has started.
     *
     * Service Name - Lobby
     * Service Operation - FIND_LOBBY
     *
     * @param lobbyType The type of lobby to look for. Lobby types are defined in the portal.
     * @param rating The skill rating to use for finding the lobby. Provided as a separate parameter because it may not exactly match the user's rating (especially in cases where parties are involved).
     * @param maxSteps The maximum number of steps to wait when looking for an applicable lobby. Each step is ~5 seconds.
     * @param algo The algorithm to use for increasing the search scope.
     * @param filterJson Used to help filter the list of rooms to consider. Passed to the matchmaking filter, if configured.
     * @param otherUserCxIds Array of other users (i.e. party members) to add to the lobby as well. Will constrain things so that only lobbies with room for all players will be considered.
     * @param isReady Initial ready-status of this user.
     * @param extraJson Initial extra-data about this user.
     * @param teamCode Preferred team for this user, if applicable. Send "" or null for automatic assignment
     */
    public void findLobby(String lobbyType, int rating, int maxSteps, String algo, String filterJson, ArrayList<String> otherUserCxIds, Boolean isReady, String extraJson, String teamCode, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.rating.name(), rating);
            data.put(Parameter.maxSteps.name(), maxSteps);
            if (StringUtil.IsOptionalParameterValid(algo)) {
                data.put(Parameter.algo.name(), new JSONObject(algo));
            }
            if (StringUtil.IsOptionalParameterValid(filterJson)) {
                data.put(Parameter.filterJson.name(), new JSONObject(filterJson));
            }
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.FIND_LOBBY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

        /**
     * Finds a lobby matching the specified parameters. Asynchronous - returns 200 to indicate that matchmaking has started. But adds ping data to the call
     *
     * Service Name - Lobby
     * Service Operation - FIND_LOBBY
     *
     * @param lobbyType The type of lobby to look for. Lobby types are defined in the portal.
     * @param rating The skill rating to use for finding the lobby. Provided as a separate parameter because it may not exactly match the user's rating (especially in cases where parties are involved).
     * @param maxSteps The maximum number of steps to wait when looking for an applicable lobby. Each step is ~5 seconds.
     * @param algo The algorithm to use for increasing the search scope.
     * @param filterJson Used to help filter the list of rooms to consider. Passed to the matchmaking filter, if configured.
     * @param otherUserCxIds Array of other users (i.e. party members) to add to the lobby as well. Will constrain things so that only lobbies with room for all players will be considered.
     * @param isReady Initial ready-status of this user.
     * @param extraJson Initial extra-data about this user.
     * @param teamCode Preferred team for this user, if applicable. Send "" or null for automatic assignment
     */
    public void findLobbyWithPingData(String lobbyType, int rating, int maxSteps, String algo, String filterJson, ArrayList<String> otherUserCxIds, Boolean isReady, String extraJson, String teamCode, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.rating.name(), rating);
            data.put(Parameter.maxSteps.name(), maxSteps);
            if (StringUtil.IsOptionalParameterValid(algo)) {
                data.put(Parameter.algo.name(), new JSONObject(algo));
            }
            if (StringUtil.IsOptionalParameterValid(filterJson)) {
                data.put(Parameter.filterJson.name(), new JSONObject(filterJson));
            }
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);

            attachPingDataAndSend(data, ServiceOperation.FIND_LOBBY_WITH_PING_DATA, callback);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Adds the caller to the lobby entry queue and will create a lobby if none are found.
     *
     * Service Name - Lobby
     * Service Operation - FIND_OR_CREATE_LOBBY
     *
     * @param lobbyType The type of lobby to look for. Lobby types are defined in the portal.
     * @param rating The skill rating to use for finding the lobby. Provided as a separate parameter because it may not exactly match the user's rating (especially in cases where parties are involved).
     * @param maxSteps The maximum number of steps to wait when looking for an applicable lobby. Each step is ~5 seconds.
     * @param algo The algorithm to use for increasing the search scope.
     * @param filterJson Used to help filter the list of rooms to consider. Passed to the matchmaking filter, if configured.
     * @param otherUserCxIds Array of other users (i.e. party members) to add to the lobby as well. Will constrain things so that only lobbies with room for all players will be considered.
     * @param settings Configuration data for the room.
     * @param isReady Initial ready-status of this user.
     * @param extraJson Initial extra-data about this user.
     * @param teamCode Preferred team for this user, if applicable. Send "" or null for automatic assignment.
     */
    public void findOrCreateLobby(String lobbyType, int rating, int maxSteps, String algo, String filterJson, ArrayList<String> otherUserCxIds, String settings, Boolean isReady, String extraJson, String teamCode, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.rating.name(), rating);
            data.put(Parameter.maxSteps.name(), maxSteps);
            if (StringUtil.IsOptionalParameterValid(algo)) {
                data.put(Parameter.algo.name(), new JSONObject(algo));
            }
            if (StringUtil.IsOptionalParameterValid(filterJson)) {
                data.put(Parameter.filterJson.name(), new JSONObject(filterJson));
            }
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }
            if (StringUtil.IsOptionalParameterValid(settings)) {
                data.put(Parameter.settings.name(), new JSONObject(settings));
            }
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.FIND_OR_CREATE_LOBBY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Adds the caller to the lobby entry queue and will create a lobby if none are found.
     *
     * Service Name - Lobby
     * Service Operation - FIND_OR_CREATE_LOBBY_WITH_PING_DATA
     *
     * @param lobbyType The type of lobby to look for. Lobby types are defined in the portal.
     * @param rating The skill rating to use for finding the lobby. Provided as a separate parameter because it may not exactly match the user's rating (especially in cases where parties are involved).
     * @param maxSteps The maximum number of steps to wait when looking for an applicable lobby. Each step is ~5 seconds.
     * @param algo The algorithm to use for increasing the search scope.
     * @param filterJson Used to help filter the list of rooms to consider. Passed to the matchmaking filter, if configured.
     * @param otherUserCxIds Array of other users (i.e. party members) to add to the lobby as well. Will constrain things so that only lobbies with room for all players will be considered.
     * @param settings Configuration data for the room.
     * @param isReady Initial ready-status of this user.
     * @param extraJson Initial extra-data about this user.
     * @param teamCode Preferred team for this user, if applicable. Send "" or null for automatic assignment.
     */
    public void findOrCreateLobbyWithPingData(String lobbyType, int rating, int maxSteps, String algo, String filterJson, ArrayList<String> otherUserCxIds, String settings, Boolean isReady, String extraJson, String teamCode, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.rating.name(), rating);
            data.put(Parameter.maxSteps.name(), maxSteps);
            if (StringUtil.IsOptionalParameterValid(algo)) {
                data.put(Parameter.algo.name(), new JSONObject(algo));
            }
            if (StringUtil.IsOptionalParameterValid(filterJson)) {
                data.put(Parameter.filterJson.name(), new JSONObject(filterJson));
            }
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }
            if (StringUtil.IsOptionalParameterValid(settings)) {
                data.put(Parameter.settings.name(), new JSONObject(settings));
            }
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);

            attachPingDataAndSend(data, ServiceOperation.FIND_OR_CREATE_LOBBY_WITH_PING_DATA, callback);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns the data for the specified lobby, including member data.
     *
     * Service Name - Lobby
     * Service Operation - GET_LOBBY_DATA
     *
     * @param lobbyId Id of chosen lobby.
     */
    public void getLobbyData(String lobbyId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.GET_LOBBY_DATA, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Causes the caller to leave the specified lobby. If the user was the owner, a new owner will be chosen. If user was the last member, the lobby will be deleted.
     *
     * Service Name - Lobby
     * Service Operation - LEAVE_LOBBY
     *
     * @param lobbyId Id of chosen lobby.
     */
    public void leaveLobby(String lobbyId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.LEAVE_LOBBY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Causes the caller to join the specified lobby. 
     *
     * Service Name - Lobby
     * Service Operation - JOIN_LOBBY
     * 
     * @param lobbyId Id of chosen lobby.
     * @param isReady initial ready status of this user
     * @param extraJson Initial extra-data about this user
     * @param teamCode specified team code
     * @param otherUserCxIds Array fo other users (ie party members) to add to the lobby as well. Constrains things so only lobbies with room for all players will be considered. 
     */
    public void joinLobby(String lobbyId, boolean isReady, String extraJson, String teamCode, ArrayList<String> otherUserCxIds, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.JOIN_LOBBY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Causes the caller to join the specified lobby. 
     *
     * Service Name - Lobby
     * Service Operation - JOIN_LOBBY_WITH_PING_DATA
     * 
     * @param lobbyId Id of chosen lobby.
     * @param isReady initial ready status of this user
     * @param extraJson Initial extra-data about this user
     * @param teamCode specified team code
     * @param otherUserCxIds Array fo other users (ie party members) to add to the lobby as well. Constrains things so only lobbies with room for all players will be considered. 
     */
    public void joinLobbyWithPingData(String lobbyId, boolean isReady, String extraJson, String teamCode, ArrayList<String> otherUserCxIds, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
            data.put(Parameter.teamCode.name(), teamCode);
            if (otherUserCxIds != null) {
                data.put(Parameter.otherUserCxIds.name(), new JSONArray(otherUserCxIds));
            }

            attachPingDataAndSend(data, ServiceOperation.JOIN_LOBBY_WITH_PING_DATA, callback);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Evicts the specified user from the specified lobby. The caller must be the owner of the lobby.
     *
     * Service Name - Lobby
     * Service Operation - REMOVE_MEMBER
     *
     * @param lobbyId Id of chosen lobby.
     * @param cxId Specified member to be removed from the lobby.
     */
    public void removeMember(String lobbyId, String cxId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);
            data.put(Parameter.cxId.name(), cxId);

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.REMOVE_MEMBER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends LOBBY_SIGNAL_DATA message to all lobby members.
     *
     * Service Name - Lobby
     * Service Operation - SEND_SIGNAL
     *
     * @param lobbyId Id of chosen lobby.
     * @param signalData Signal data to be sent.
     */
    public void sendSignal(String lobbyId, String signalData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);
            if (StringUtil.IsOptionalParameterValid(signalData)) {
                data.put(Parameter.signalData.name(), new JSONObject(signalData));
            }

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.SEND_SIGNAL, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Switches to the specified team (if allowed.)
     * 
     * Sends LOBBY_MEMBER_UPDATED to all lobby members, with copy of member data
     *
     * Service Name - Lobby
     * Service Operation - SWITCH_TEAM
     *
     * @param lobbyId Id of chosen lobby.
     * @param toTeamCode Specified team code.
     */
    public void switchTeam(String lobbyId, String toTeamCode, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);
            data.put(Parameter.toTeamCode.name(), toTeamCode);

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.SWITCH_TEAM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Updates the ready status and extra json for the given lobby member.
     *
     * Service Name - Lobby
     * Service Operation - UPDATE_READY
     *
     * @param lobbyId The type of lobby to look for. Lobby types are defined in the portal.
     * @param isReady Initial ready-status of this user.
     * @param extraJson Initial extra-data about this user.
     */
    public void updateReady(String lobbyId, Boolean isReady, String extraJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);
            data.put(Parameter.isReady.name(), isReady);
            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.UPDATE_READY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Updates the ready status and extra json for the given lobby member.
     *
     * Service Name - Lobby
     * Service Operation - UPDATE_SETTINGS
     *
     * @param lobbyId Id of the specfified lobby.
     * @param settings Configuration data for the room.
     */
    public void updateSettings(String lobbyId, String settings, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyId.name(), lobbyId);
            if (StringUtil.IsOptionalParameterValid(settings)) {
                data.put(Parameter.settings.name(), new JSONObject(settings));
            }

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.UPDATE_SETTINGS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets a map keyed by rating of the visible lobby instances matching the given type and rating range.
     *
     * Service Name - Lobby
     * Service Operation - GET_LOBBY_INSTANCES
     *
     * @param lobbyType The type of lobby to look for.
     * @param criteriaJson A JSON string used to describe filter criteria.
     */
    public void getLobbyInstances(String lobbyType, String criteriaJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.criteriaJson.name(), new JSONObject(criteriaJson));

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.GET_LOBBY_INSTANCES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets a map keyed by rating of the visible lobby instances matching the given type and rating range.
     * Only lobby instances in the regions that satisfy the ping portion of the criteriaJson (based on the values provided in pingData) will be returned.
     *
     * Service Name - Lobby
     * Service Operation - GET_LOBBY_INSTANCES_WITH_PING_DATA
     *
     * @param lobbyType The type of lobby to look for.
     * @param criteriaJson A JSON string used to describe filter criteria.
     */
    public void getLobbyInstancesWithPingData(String lobbyType, String criteriaJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.criteriaJson.name(), new JSONObject(criteriaJson));

            attachPingDataAndSend(data, ServiceOperation.GET_LOBBY_INSTANCES_WITH_PING_DATA, callback);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /// <summary>
    /// Cancel this members Find, Join and Searching of Lobbies
    /// </summary>
    public void cancelFindRequest(String lobbyType, String cxId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyType.name(), lobbyType);
            data.put(Parameter.cxId.name(), cxId);

            ServerCall sc = new ServerCall(ServiceName.lobby,
                    ServiceOperation.CANCEL_FIND_REQUEST, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public void getRegionsForLobbies(String[] in_lobbyTypes, IServerCallback callback) {
        try {
            _pingData = null;
            _pingRegions = null;
            _getRegionsForLobbiesCallback = callback;
            JSONObject data = new JSONObject();
            data.put(Parameter.lobbyTypes.name(), in_lobbyTypes);

            ServerCall sc = new ServerCall(ServiceName.lobby,
            ServiceOperation.GET_REGIONS_FOR_LOBBIES, data, this);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private void startPingThread() {
        _pingData = new JSONObject();
        
        // Run the thread
        _isPingRunning.set(true);
        _pingRegionsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Build a map of regions to ping
                    Map<String, String> regionsToPing = new HashMap<String, String>();
                    for (int i = 0; i < _pingRegions.names().length(); ++i) {
                        String regionName = _pingRegions.names().getString(i);
                        JSONObject jsonRegion = _pingRegions.getJSONObject(regionName);
                        String type = jsonRegion.getString("type");
                        String target = jsonRegion.getString("target");
                        if (type != null && type.equals("PING")) {
                            regionsToPing.put(regionName, target);
                        }
                    }

                    ArrayList<ActivePing> activePings = new ArrayList<ActivePing>();

                    synchronized(_pingSync) {
                        while (_isPingRunning.get()) {
                            // Make sure we have the desired active pings count in parrallel
                            while (!regionsToPing.isEmpty() && activePings.size() < NUM_PING_CALLS_IN_PARALLEL) {
                                String regionName = (String)regionsToPing.keySet().toArray()[0];
                                String regionURL = regionsToPing.get(regionName);
                                ActivePing activePing = new ActivePing(regionName, regionURL, _pingSync);
                                activePings.add(activePing);
                                regionsToPing.remove(regionName);
                            }

                            // Check for completed active pings
                            for (int i = 0; i < activePings.size(); ++i) {
                                ActivePing activePing = activePings.get(i);
                                if (activePing.isFinished()) {
                                    _pingData.put(activePing.getRegion(), activePing.getPing());
                                    activePings.remove(i);
                                    --i;
                                }
                            }

                            // Check if we completed all the regions
                            if (regionsToPing.isEmpty() && activePings.isEmpty()) {
                                _isPingRunning.set(false);
                                break;
                            }
				
				            // Otherwise, wait for an active ping to complete
                            if (!activePings.isEmpty()) {
                                _pingSync.wait();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    queueErrorEvent(_pingCallback, ServiceName.lobby, ServiceOperation.PING_REGIONS, StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_REQUIRED_PARAMETER, "Required message parameter 'pingData' is missing. Please ensure PingData exists by first calling GetRegionsForLobbies and PingRegions, and waiting for response before proceeding.");
                    return;
                }
            }
        });
        _pingRegionsThread.start();
    }

    private void stopPingThread() {
        if (_pingRegionsThread != null) {
            _isPingRunning.set(false);
            synchronized(_pingSync) {
                _pingSync.notify();
            }
            try {
                _pingRegionsThread.join();
            } catch(InterruptedException e) {}
            _pingRegionsThread = null;
        }
    }

    public void pingRegions(IServerCallback callback) {
        if (_pingRegions == null) {
            if (callback != null) {
                queueErrorEvent(callback, ServiceName.lobby, ServiceOperation.PING_REGIONS, StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_REQUIRED_PARAMETER, "Required message parameter 'pingData' is missing. Please ensure PingData exists by first calling GetRegionsForLobbies and PingRegions, and waiting for response before proceeding.");
            }
            return;
        }
        if (_pingRegionsThread == null) {
            _pingCallback = callback;
            startPingThread();
        }
        else if (callback != null) {
            queueErrorEvent(callback, ServiceName.lobby, ServiceOperation.PING_REGIONS, StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_REQUIRED_PARAMETER, "'pingRegions' is already running. Please wait for callback before calling this again.");
        }
    }

    private int pingHost(String targetURL)
    {
        // Make http request
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(targetURL).openConnection();
            try {
                connection.setRequestMethod("GET");
            } catch(java.net.ProtocolException pe) {
                return 999;
            }

            long timeStart = System.currentTimeMillis();
            if (connection.getResponseCode() == 200) {
                long timeEnd = System.currentTimeMillis();
                int resultPing = (int)(timeEnd - timeStart);
                if (resultPing > 999) {
                    resultPing = 999;
                }
                return resultPing;
            }

            return 999;
        } catch(java.io.IOException io) {
            return 999;
        }
    }

    private void attachPingDataAndSend(JSONObject in_data, ServiceOperation in_operation, IServerCallback callback)
    {
        if (_pingData != null && _pingData.length() > 0) {
            try {
                in_data.put(Parameter.pingData.name(), _pingData);
                ServerCall sc = new ServerCall(ServiceName.lobby, in_operation, in_data, callback);
                _client.sendRequest(sc);
            } catch(JSONException je) {
                je.printStackTrace();
            }
        } else {
            queueErrorEvent(callback, ServiceName.lobby, in_operation, StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_REQUIRED_PARAMETER, "Required parameter 'pingData' is missing. Please ensure 'pingData' exists by first calling GetRegionsForLobbies, then wait for the response and then call PingRegions");
        }
    }

    public void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject jsonData)
    {
        if(serviceName.toString().equals("lobby") && serviceOperation.toString().equals("GET_REGIONS_FOR_LOBBIES"))
        {
            try {
                _pingRegions = jsonData.getJSONObject("data").getJSONObject("regionPingData");
            }
            catch (JSONException je)
            {}

            if(_getRegionsForLobbiesCallback != null)
            {
                _getRegionsForLobbiesCallback.serverCallback(serviceName, serviceOperation, jsonData);
            }
        }
    }

    public void serverError(ServiceName serviceName, ServiceOperation serviceOperation, int statusCode, int reasonCode, String jsonError)
    {
        if(serviceName.toString().equals("lobby") && serviceOperation.toString().equals("GET_REGIONS_FOR_LOBBIES"))
        {
            _getRegionsForLobbiesCallback.serverError(serviceName, serviceOperation, statusCode, reasonCode, jsonError);
        }
    }

    public void runPingCallbacks() {
		// pingRegions callback
		if (!_isPingRunning.get() && _pingCallback != null)
		{
			if (_loggingEnabled)
			{
                String dataStr = _pingData.toString();
                System.out.println("#PING RESULTS " + dataStr);
            }
			_pingCallback.serverCallback(ServiceName.lobby, ServiceOperation.PING_REGIONS, _pingData);
            _pingCallback = null;
            stopPingThread();
		}

        // Trigger delayed events
        synchronized(_errorCallbackQueue) {
            for (int i = 0; i < _errorCallbackQueue.size(); ++i) {
                ErrorCallbackEvent evt = _errorCallbackQueue.get(i);
                evt.callback.serverError(evt.serviceName, evt.serviceOperation, evt.statusCode, evt.reasonCode, evt.jsonError);
            }
            _errorCallbackQueue.clear();
        }
    }

    private void queueErrorEvent(IServerCallback callback, ServiceName serviceName, ServiceOperation serviceOperation, int statusCode, int reasonCode, String jsonError) {
        ErrorCallbackEvent evt = new ErrorCallbackEvent();
        evt.callback = callback;
        evt.serviceName = serviceName;
        evt.serviceOperation = serviceOperation;
        evt.statusCode = statusCode;
        evt.reasonCode = reasonCode;
        evt.jsonError = jsonError;
        synchronized(_errorCallbackQueue) {
            _errorCallbackQueue.add(evt);
        }
    }

    public JSONObject getPingData() {
        return _pingData;
    }
}
