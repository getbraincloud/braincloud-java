package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class SocialLeaderboardService {

    public enum SocialLeaderboardType {
        HIGH_VALUE, CUMULATIVE, LOW_VALUE, LAST_VALUE
    }

    public enum RotationType {
        NEVER, DAILY, WEEKLY, MONTHLY, YEARLY
    }

    public enum SortOrder {
        HIGH_TO_LOW, LOW_TO_HIGH
    }

    private enum Parameter {
        leaderboardId,
        leaderboardIds,
        maxResults,
        replaceName,
        score,
        data,
        eventName,
        eventMultiplier,
        leaderboardType,
        rotationType,
        rotationReset,
        rotationResetTime,
        retainedCount,
        sort,
        startIndex,
        endIndex,
        beforeCount,
        afterCount,
        includeLeaderboardSize,
        versionId,
        leaderboardResultCount,
        groupId,
        profileIds,
        numDaysToRotate
    }

    private BrainCloudClient _client;

    public SocialLeaderboardService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Method returns the social leaderboard. A player's social leaderboard is
     * comprised of players who are recognized as being your friend. For now,
     * 
     * The getSocialLeaderboard will retrieve all friends from all friend platforms, so
     * - all external friends (Facebook, Steam, PlaystationNetwork)
     * - all internal friends (brainCloud)
     * - plus "self".
     * 
     * Leaderboards entries contain the player's score and optionally, some
     * user-defined data associated with the score. The currently logged in
     * player will also be returned in the social leaderboard.
     *
     * Note: If no friends have played the game, the bestScore, createdAt,
     * updatedAt will contain NULL.
     *
     * @param leaderboardId
     *            The id of the leaderboard to retrieve
     * @param replaceName
     *            If true, the currently logged in player's name will be
     *            replaced by the String "You".
     *
     */
    public void getSocialLeaderboard(String leaderboardId, boolean replaceName,
                                     IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.replaceName.name(), replaceName);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_SOCIAL_LEADERBOARD, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method returns the social leaderboard by the. A player's social leaderboard is
     * comprised of players who are recognized as being your friend.
     * 
     * The getSocialLeaderboard will retrieve all friends from all friend platforms, so
     * - all external friends (Facebook, Steam, PlaystationNetwork)
     * - all internal friends (brainCloud)
     * - plus "self".
     *
     * Leaderboards entries contain the player's score and optionally, some
     * user-defined data associated with the score. The currently logged in
     * player will also be returned in the social leaderboard.
     *
     * Note: If no friends have played the game, the bestScore, createdAt,
     * updatedAt will contain NULL.
     *
     * @param leaderboardId
     *            The id of the leaderboard to retrieve
     * @param replaceName
     *            If true, the currently logged in player's name will be
     *            replaced by the String "You".
     * @param versionId the version of the leaderboard
     * @param callback The method to be invoked when the server response is received
     */
    public void getSocialLeaderboardByVersion(String leaderboardId, boolean replaceName,
                                                int versionId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.replaceName.name(), replaceName);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_SOCIAL_LEADERBOARD_BY_VERSION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Reads multiple social leaderboards.
     *
     * @param leaderboardIds Collection of leaderboard IDs.
     * @param leaderboardResultCount Maximum count of entries to return for each leaderboard.
     * @param replaceName If true, the currently logged in player's name will be replaced
     * by the string "You".
     * @param callback The method to be invoked when the server response is received
     */
    public void getMultiSocialLeaderboard(String[] leaderboardIds,
                                          int leaderboardResultCount,
                                          boolean replaceName,
                                          IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardIds.name(), leaderboardIds);
            data.put(Parameter.leaderboardResultCount.name(), leaderboardResultCount);
            data.put(Parameter.replaceName.name(), replaceName);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_MULTI_SOCIAL_LEADERBOARD, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method returns a page of results of the global leaderboard.
     *
     * Leaderboards entries contain the player's score and optionally, some user-defined
     * data associated with the score.
     *
     * Note: If no leaderboard records exist then this method will empty list.
     *
     * Service Name - SocialLeaderboard
     * Service Operation - GetGlobalLeaderboardPage
     *
     * @param leaderboardId The id of the leaderboard to retrieve
     * @param sort Sort order of the returned list.
     * @param startIndex The index at which to start the page.
     * @param endIndex The index at which to end the page.
     */
    public void getGlobalLeaderboardPage(
            String leaderboardId,
            SortOrder sort,
            int startIndex,
            int endIndex,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.startIndex.name(), startIndex);
            data.put(Parameter.endIndex.name(), endIndex);

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_PAGE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method returns a page of results of the global leaderboard.
     * By using a non-current version id, the user can retrieve a historial leaderboard.
     * See GetGlobalLeaderboardVersions method to retrieve the version id.
     *
     * Service Name - SocialLeaderboard
     * Service Operation - GetGlobalLeaderboardPage
     *
     * @param leaderboardId The id of the leaderboard to retrieve
     * @param sort Sort order of the returned list.
     * @param startIndex The index at which to start the page.
     * @param endIndex The index at which to end the page.
     * @param versionId The historical version to retrieve
     */
    public void getGlobalLeaderboardPageByVersion(
            String leaderboardId,
            SortOrder sort,
            int startIndex,
            int endIndex,
            int versionId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.startIndex.name(), startIndex);
            data.put(Parameter.endIndex.name(), endIndex);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_PAGE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method returns a page of results of the global leaderboard.
     *
     * Leaderboards entries contain the player's score and optionally, some user-defined
     * data associated with the score.
     *
     * Note: If no leaderboard records exist then this method will empty list.
     *
     * Service Name - SocialLeaderboard
     * Service Operation - GetGlobalLeaderboardPage
     *
     * @param leaderboardId The id of the leaderboard to retrieve
     * @param sort Sort order of the returned list.
     * @param beforeCount The count of number of players before the current player to include.
     * @param afterCount The count of number of players after the current player to include.
     */
    public void getGlobalLeaderboardView(
            String leaderboardId,
            SortOrder sort,
            int beforeCount,
            int afterCount,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.beforeCount.name(), beforeCount);
            data.put(Parameter.afterCount.name(), afterCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_VIEW, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method returns a page of results of the global leaderboard.
     * By using a non-current version id, the user can retrieve a historial leaderboard.
     * See GetGlobalLeaderboardVersions method to retrieve the version id.
     *
     * Service Name - SocialLeaderboard
     * Service Operation - GetGlobalLeaderboardPage
     *
     * @param leaderboardId The id of the leaderboard to retrieve
     * @param sort Sort order of the returned list.
     * @param beforeCount The count of number of players before the current player to include.
     * @param afterCount The count of number of players after the current player to include.
     * @param versionId The historical version id
     * @returns JSON String representing the entries in the leaderboard.
     * See GetGlobalLeaderboardView documentation. Note that historial leaderboards do not
     * include the 'timeBeforeReset' parameter.
     */
    public void getGlobalLeaderboardViewByVersion(
            String leaderboardId,
            SortOrder sort,
            int beforeCount,
            int afterCount,
            int versionId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.beforeCount.name(), beforeCount);
            data.put(Parameter.afterCount.name(), afterCount);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_VIEW, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /** Gets the global leaderboard versions.
     *
     * Service Name - SocialLeaderboard
     * Service Operation - GetGlobalLeaderboardVersions
     *
     * @param leaderboardId The leaderboard
     * @param callback The method to be invoked when the server response is received
     */
    public void getGlobalLeaderboardVersions(
            String leaderboardId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_VERSIONS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets the number of entries in a global leaderboard
     *
     * Service Name - leaderboard
     * Service Operation - GET_GLOBAL_LEADERBOARD_ENTRY_COUNT
     *
     * @param leaderboardId The leaderboard ID
     * @param callback The method to be invoked when the server response is received
     */
    public void getGlobalLeaderboardEntryCount(
            String leaderboardId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_ENTRY_COUNT, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets the number of entries in a global leaderboard
     *
     * Service Name - leaderboard
     * Service Operation - GET_GLOBAL_LEADERBOARD_ENTRY_COUNT
     *
     * @param leaderboardId The leaderboard ID
     * @param versionId The version of the leaderboard
     * @param callback The method to be invoked when the server response is received
     */
    public void getGlobalLeaderboardEntryCountByVersion(
            String leaderboardId,
            int versionId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_ENTRY_COUNT, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Post the players score to the given social leaderboard. You can
     * optionally send a user-defined json String of data with the posted score.
     * This String could include information relevant to the posted score.
     *
     * Note that the behaviour of posting a score can be modified in the
     * brainCloud portal. By default, the server will only keep the player's
     * best score.
     *
     * @param leaderboardId The leaderboard to post to
     * @param score The score to post
     * @param jsonData Optional user-defined data to post with the score
     * @param callback The callback.
     */
    public void postScoreToLeaderboard(String leaderboardId, long score,
                                       String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            }

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_SCORE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * @deprecated Use postScoreToDynamicLeaderboardUTC instead - Removal September 1, 2021
     */
    public void postScoreToDynamicLeaderboard(
            String leaderboardId,
            long score,
            String jsonData,
            String leaderboardType,
            String rotationType,
            Date rotationReset,
            int retainedCount,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            }
            data.put(Parameter.leaderboardType.name(), leaderboardType);
            data.put(Parameter.rotationType.name(), rotationType);

            if (rotationReset != null) {
                data.put(Parameter.rotationResetTime.name(), rotationReset.getTime());
            }

            data.put(Parameter.retainedCount.name(), retainedCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_SCORE_DYNAMIC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Post the players score to the given social leaderboard. Pass leaderboard
     * config data to dynamically create if necessary. You can optionally send a
     * user-defined json String of data with the posted score. This String could
     * include information relevant to the posted score.
     *
     * @param leaderboardId The leaderboard to post to
     * @param score The score to post
     * @param jsonData Optional user-defined data to post with the score
     * @param leaderboardType leaderboard type
     * @param rotationType Type of rotation
     * @param rotationResetUTC Date to reset the leaderboard - in UTC milliseconds since epoch
     * @param retainedCount How many rotations to keep
     * @param callback The callback.
     */
    public void postScoreToDynamicLeaderboardUTC(
            String leaderboardId,
            long score,
            String jsonData,
            String leaderboardType,
            String rotationType,
            long rotationResetUTC,
            int retainedCount,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            }
            data.put(Parameter.leaderboardType.name(), leaderboardType);
            data.put(Parameter.rotationType.name(), rotationType);

            data.put(Parameter.rotationResetTime.name(), rotationResetUTC);

            data.put(Parameter.retainedCount.name(), retainedCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_SCORE_DYNAMIC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
 
    /**
     * Posts score to group leaderbopard and dynamically creates if necessary. leaderboardType, rotationReset, retainedCount and rotationType are required. uses UTC time in milliseconds since epoch
     *
     * @param leaderboardId The leaderboard to post to
     * @param groupId the group's id
     * @param score The score to post
     * @param jsonData Optional user-defined data to post with the score
     * @param leaderboardType leaderboard type
     * @param rotationResetUTC Date to reset the leaderboard - in UTC milliseconds since epoch
     * @param retainedCount How many rotations to keep
     * @param numDaysToRotate How many days between each rotation
     * @param callback The callback.
     */
    public void postScoreToDynamicGroupLeaderboardDaysUTC(
            String leaderboardId,
            String groupId,
            long score,
            String jsonData,
            String leaderboardType,
            long rotationResetUTC,
            int retainedCount,
            int numDaysToRotate,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            }
            data.put(Parameter.leaderboardType.name(), leaderboardType);
            data.put(Parameter.rotationType.name(), "DAYS");

            data.put(Parameter.rotationResetTime.name(), rotationResetUTC);

            data.put(Parameter.retainedCount.name(), retainedCount);
            data.put(Parameter.numDaysToRotate.name(), numDaysToRotate);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_GROUP_SCORE_DYNAMIC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * @deprecated Use postScoreToDynamicLeaderboardDays instead - Removal September 1, 2021
     */
    public void postScoreToDynamicLeaderboardDays(
            String leaderboardId,
            long score,
            String jsonData,
            String leaderboardType,
            Date rotationReset,
            int retainedCount,
            int numDaysToRotate,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            }
            data.put(Parameter.leaderboardType.name(), leaderboardType);
            data.put(Parameter.rotationType.name(), "DAYS");
            data.put(Parameter.numDaysToRotate.name(), numDaysToRotate);

            if (rotationReset != null) {
                data.put(Parameter.rotationResetTime.name(), rotationReset.getTime());
            }

            data.put(Parameter.retainedCount.name(), retainedCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_SCORE_DYNAMIC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

        /**
     * Post the players score to the given social leaderboard. Pass leaderboard
     * config data to dynamically create if necessary. You can optionally send a
     * user-defined json String of data with the posted score. This String could
     * include information relevant to the posted score.
     *
     * @param leaderboardId The leaderboard to post to
     * @param score The score to post
     * @param jsonData Optional user-defined data to post with the score
     * @param leaderboardType leaderboard type
     * @param rotationResetUTC Date to reset the leaderboard
     * @param retainedCount How many rotations to keep
     * @param numDaysToRotate How many days between each rotation
     * @param callback The callback.
     */
    public void postScoreToDynamicLeaderboardDaysUTC(
            String leaderboardId,
            long score,
            String jsonData,
            String leaderboardType,
            long rotationResetUTC,
            int retainedCount,
            int numDaysToRotate,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            }
            data.put(Parameter.leaderboardType.name(), leaderboardType);
            data.put(Parameter.rotationType.name(), "DAYS");
            data.put(Parameter.numDaysToRotate.name(), numDaysToRotate);

            data.put(Parameter.rotationResetTime.name(), rotationResetUTC);

            data.put(Parameter.retainedCount.name(), retainedCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_SCORE_DYNAMIC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Removes a player's score from the leaderboard
     *
     * Service Name - leaderboard
     * Service Operation - REMOVE_PLAYER_SCORE
     *
     * @param leaderboardId The leaderboard ID
     * @param versionId The version of the leaderboard. Use -1 to specifiy the currently active leaderboard version
     * @param callback The method to be invoked when the server response is received
     */
    public void removePlayerScore(String leaderboardId, int versionId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.REMOVE_PLAYER_SCORE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve the social leaderboard for a group.
     *
     * Service Name - leaderboard
     * Service Operation - GET_GROUP_SOCIAL_LEADERBOARD
     *
     * @param leaderboardId The leaderboard to retrieve
     * @param groupId The ID of the group
     * @param callback The method to be invoked when the server response is received
     */
    public void getGroupSocialLeaderboard(String leaderboardId, String groupId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GROUP_SOCIAL_LEADERBOARD, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve the social leaderboard for a group by version.
     *
     * Service Name - leaderboard
     * Service Operation - GET_GROUP_SOCIAL_LEADERBOARD_BY_VERSION
     *
     * @param leaderboardId The leaderboard to retrieve
     * @param groupId The ID of the group
     * @param versionId The ID of the group
     * @param callback The method to be invoked when the server response is received
     */
    public void getGroupSocialLeaderboardByVersion(String leaderboardId, String groupId, int versionId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GROUP_SOCIAL_LEADERBOARD_BY_VERSION, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve the social leaderboard for a list of players.
     *
     * Service Name - leaderboard
     * Service Operation - GET_PLAYERS_SOCIAL_LEADERBOARD
     *
     * @param leaderboardId The leaderboard to retrieve
     * @param profileIds The IDs of the players
     * @param callback The method to be invoked when the server response is received
     */
    public void getPlayersSocialLeaderboard(String leaderboardId, String[] profileIds, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.profileIds.name(), new JSONArray(profileIds));

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYERS_SOCIAL_LEADERBOARD, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve the social leaderboard for a list of players by version.
     *
     * Service Name - leaderboard
     * Service Operation - GET_PLAYERS_SOCIAL_LEADERBOARD_BY_VERSION
     *
     * @param leaderboardId The leaderboard to retrieve
     * @param profileIds The IDs of the players
     * @param versionId The IDs of the players
     * @param callback The method to be invoked when the server response is received
     */
    public void getPlayersSocialLeaderboardByVersion(String leaderboardId, String[] profileIds, int versionId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYERS_SOCIAL_LEADERBOARD_BY_VERSION, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve a list of all leaderboards
     *
     * Service Name - leaderboard
     * Service Operation - LIST_ALL_LEADERBOARDS
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void listAllLeaderboards(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.LIST_ALL_LEADERBOARDS, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Gets a player's score from a leaderboard
     *
     * Service Name - leaderboard
     * Service Operation - GET_PLAYER_SCORE
     *
     * @param leaderboardId The leaderboard ID
     * @param versionId The version of the leaderboard. Use -1 for current.
     * @param callback The method to be invoked when the server response is received
     */
    public void getPlayerScore(String leaderboardId, int versionId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYER_SCORE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets a player's highest scores from a leaderboard
     *
     * Service Name - leaderboard
     * Service Operation - GET_PLAYER_SCORES
     *
     * @param leaderboardId The leaderboard ID
     * @param versionId The version of the leaderboard. Use -1 for current.
     * @param maxResults The maximum number of returned results
     * @param callback The method to be invoked when the server response is received
     */
    public void getPlayerScores(String leaderboardId, int versionId, int maxResults, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.versionId.name(), versionId);
            data.put(Parameter.maxResults.name(), maxResults);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYER_SCORES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets a player's score from multiple leaderboards
     *
     * Service Name - leaderboard
     * Service Operation - GET_PLAYER_SCORES_FROM_LEADERBOARDS
     *
     * @param leaderboardIds A collection of leaderboardIds to retrieve scores from
     * @param callback The method to be invoked when the server response is received
     */
    public void getPlayerScoresFromLeaderboards(String[] leaderboardIds, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardIds.name(), leaderboardIds);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYER_SCORES_FROM_LEADERBOARDS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Posts score to Group's leaderboard - Note the user must be a member of the group
     *
     * Service Name - leaderboard
     * Service Operation - POST_GROUP_SCORE
     *
     * @param leaderboardId the leaderboard
     * @param groupId the groups id
     * @param score the score you want to post
     * @param jsonData extra json data
     * @param callback The method to be invoked when the server response is received
     */
    public void postScoreToGroupLeaderboard(String leaderboardId, String groupId, int score, String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(jsonData)) {
                data.put(Parameter.data.name(), new JSONObject(jsonData));
            }
            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_GROUP_SCORE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * @deprecated Use postScoreToDynamicGroupLeaderboardUTC instead - Removal September 1, 2021
     */
    public void postScoreToDynamicGroupLeaderboard(String leaderboardId, String groupId, long score, String data, String leaderboardType, String rotationType, Date rotationReset, int retainedCount, IServerCallback callback) {
        try {
            JSONObject message = new JSONObject();
            message.put(Parameter.leaderboardId.name(), leaderboardId);
            message.put(Parameter.groupId.name(), groupId);
            message.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(data)) {
                message.put(Parameter.data.name(), new JSONObject(data));
            }
            message.put(Parameter.leaderboardType.name(), leaderboardType);
            message.put(Parameter.rotationType.name(), rotationType);

            if (rotationReset != null) {
                message.put(Parameter.rotationResetTime.name(), rotationReset.getTime());
            }

            message.put(Parameter.retainedCount.name(), retainedCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_GROUP_SCORE_DYNAMIC, message, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Post the group score to the given group leaderboard and dynamically create if necessary. LeaderboardType, rotationType, rotationReset, and retainedCount are required.	 *
     *
     * Service Name - leaderboard
     * Service Operation - POST_GROUP_SCORE_DYNAMIC
     *
     * @param leaderboardId the leaderboard
     * @param groupId the groups id
     * @param score the score you want to post
     * @param data
     * @param leaderboardType
     * @param rotationType
     * @param rotationResetUTC time that rotation resets in UTC mmilliseconds time
     * @param retainedCount
     * @param callback The method to be invoked when the server response is received
     */
    public void postScoreToDynamicGroupLeaderboardUTC(String leaderboardId, String groupId, long score, String data, String leaderboardType, String rotationType, long rotationResetUTC, int retainedCount, IServerCallback callback) {
        try {
            JSONObject message = new JSONObject();
            message.put(Parameter.leaderboardId.name(), leaderboardId);
            message.put(Parameter.groupId.name(), groupId);
            message.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(data)) {
                message.put(Parameter.data.name(), new JSONObject(data));
            }
            message.put(Parameter.leaderboardType.name(), leaderboardType);
            message.put(Parameter.rotationType.name(), rotationType);

            message.put(Parameter.rotationResetTime.name(), rotationResetUTC);

            message.put(Parameter.retainedCount.name(), retainedCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_GROUP_SCORE_DYNAMIC, message, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Removes score from group leaderboard
     * 
     * Service Name - leaderboard
     * Service Operation - REMOVE_GROUP_SCORE
     *
     * @param leaderboardId the leaderboard
     * @param groupId the groups id
     * @param versionId the version
     * @param callback The method to be invoked when the server response is received
     */
    public void removeGroupScore(String leaderboardId, String groupId, int versionId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.REMOVE_GROUP_SCORE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve a view of the group leaderbaord surrounding the current group
     * 
     * Service Name - leaderboard
     * Service Operation - GET_GROUP_LEADERBOARD_VIEW
     *
     * @param leaderboardId the leaderboard
     * @param groupId the groups id
     * @param sort the sort order
     * @param beforeCount count of players before current player to include
     * @param afterCount count of the players after current player to include
     * @param callback The method to be invoked when the server response is received
     */
    public void getGroupLeaderboardView(String leaderboardId, String groupId, SortOrder sort, int beforeCount, int afterCount, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.beforeCount.name(), beforeCount);
            data.put(Parameter.afterCount.name(), afterCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GROUP_LEADERBOARD_VIEW, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve a view of the group leaderbaord surrounding the current group by version
     * 
     * Service Name - leaderboard
     * Service Operation - GET_GROUP_LEADERBOARD_VIEW
     *
     * @param leaderboardId the leaderboard
     * @param groupId the groups id
     * @param versionId
     * @param sort the sort order
     * @param beforeCount count of players before current player to include
     * @param afterCount count of the players after current player to include
     * @param callback The method to be invoked when the server response is received
     */
    public void getGroupLeaderboardViewByVersion(String leaderboardId, String groupId, int versionId, SortOrder sort, int beforeCount, int afterCount, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.versionId.name(), versionId);
            data.put(Parameter.sort.name(), sort.name());
            data.put(Parameter.beforeCount.name(), beforeCount);
            data.put(Parameter.afterCount.name(), afterCount);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GROUP_LEADERBOARD_VIEW, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
