// Copyright 2026 bitHeads, Inc. All Rights Reserved.
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
        numDaysToRotate,
        scoreData,
        configJson
    }

    private BrainCloudClient _client;

    public SocialLeaderboardService(BrainCloudClient client) {
        _client = client;
    }

    /**
		 * Method returns the social leaderboard. A player's social leaderboard is
		 * comprised of players who are recognized as being your friend.
		 *
		 * The getSocialLeaderboard will retrieve all friends from all friend platforms, so
		 * - all external friends (Facebook, Steam, PlaystationNetwork)
		 * - all internal friends (brainCloud)
		 * - plus "self".
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score. The currently logged in player will also
		 * be returned in the social leaderboard.
		 *
		 * Note: If no friends have played the game, the bestScore, createdAt, updatedAt
		 * will contain NULL.
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve
		 * @param replaceName If true, the currently logged in player's name will be replaced
		 * by the string "You".
		 * @param callback The method to be invoked when the server response is received
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
		 * Method returns the social leaderboard. A player's social leaderboard is
		 * comprised of players who are recognized as being your friend.
		 *
		 * This method returns the same data as getSocialLeaderboard, but it will not return an error if the leaderboard is not found.
		 *
		 * The method will retrieve all friends from all friend platforms, so
		 * - all external friends (Facebook, Steam, PlaystationNetwork)
		 * - all internal friends (brainCloud)
		 * - plus "self".
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score. The currently logged in player will also
		 * be returned in the social leaderboard.
		 *
		 * Note: If no friends have played the game, the bestScore, createdAt, updatedAt
		 * will contain NULL.
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve
		 * @param replaceName If true, the currently logged in player's name will be replaced
		 * by the string "You".
		 * @param callback The method to be invoked when the server response is received
		 *
		 */
    public void getSocialLeaderboardIfExists(String leaderboardId, boolean replaceName, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.replaceName.name(), replaceName);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_SOCIAL_LEADERBOARD_IF_EXISTS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns the social leaderboard by its version. A player's social leaderboard is
		 * comprised of players who are recognized as being your friend.
		 *
		 * The getSocialLeaderboard will retrieve all friends from all friend platforms, so
		 * - all external friends (Facebook, Steam, PlaystationNetwork)
		 * - all internal friends (brainCloud)
		 * - plus "self".
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score. The currently logged in player will also
		 * be returned in the social leaderboard.
		 *
		 * Note: If no friends have played the game, the bestScore, createdAt, updatedAt
		 * will contain NULL.
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve
		 * @param replaceName If true, the currently logged in player's name will be replaced
		 * by the string "You".
		 * @param versionId the version of the leaderboard
		 * @param callback The method to be invoked when the server response is received
		 *
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
		 * Method returns the social leaderboard by its version. A player's social leaderboard is
		 * comprised of players who are recognized as being your friend.
		 *
		 * This method returns the same data as getSocialLeaderboardByVersion, but it will not return an error if the leaderboard is not found.
		 *
		 * The method will retrieve all friends from all friend platforms, so
		 * - all external friends (Facebook, Steam, PlaystationNetwork)
		 * - all internal friends (brainCloud)
		 * - plus "self".
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score. The currently logged in player will also
		 * be returned in the social leaderboard.
		 *
		 * Note: If no friends have played the game, the bestScore, createdAt, updatedAt
		 * will contain NULL.
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve
		 * @param replaceName If true, the currently logged in player's name will be replaced
		 * by the string "You".
		 * @param versionId the version of the leaderboard
		 * @param callback The method to be invoked when the server response is received
		 *
		 */
    public void getSocialLeaderboardByVersionIfExists(String leaderboardId, boolean replaceName,
            int versionId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.replaceName.name(), replaceName);
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_SOCIAL_LEADERBOARD_BY_VERSION_IF_EXISTS, data, callback);
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
		 * Method returns a page of global leaderboard results.
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score.
		 *
		 * Note: This method allows the client to retrieve pages from within the global leaderboard list
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GetGlobalLeaderboardPage
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param startIndex The index at which to start the page.
		 * @param endIndex The index at which to end the page.
		 * @param callback The method to be invoked when the server response is received
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_PAGE, data,
                    callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns a page of global leaderboard results.
		 * Returns the same data as getGlobalLeaderboardPage, but does not return an error if the leaderboard does not exist.
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score.
		 *
		 * Note: This method allows the client to retrieve pages from within the global leaderboard list
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GET_GLOBAL_LEADERBOARD_PAGE_IF_EXISTS
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param startIndex The index at which to start the page.
		 * @param endIndex The index at which to end the page.
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getGlobalLeaderboardPageIfExists(
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GLOBAL_LEADERBOARD_PAGE_IF_EXISTS, data,
                    callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns a page of global leaderboard results.
		 * By using a non-current version id, the user can retrieve a historical leaderboard.
		 * See GetGlobalLeaderboardVersions method to retrieve the version id.
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GET_GLOBAL_LEADERBOARD_PAGE_BY_VERSION
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param startIndex The index at which to start the page.
		 * @param endIndex The index at which to end the page.
		 * @param versionId The historical version to retrieve.
		 * @param callback The method to be invoked when the server response is received
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_PAGE, data,
                    callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns a page of global leaderboard results.
		 * By using a non-current version id, the user can retrieve a historical leaderboard.
		 * See GetGlobalLeaderboardVersions method to retrieve the version id.
		 *
		 * This method returns the same data as getGlobalLeaderboardPageByVersion, but it will not return an error if the leaderboard does not exist
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GET_GLOBAL_LEADERBOARD_PAGE_BY_VERSION_IF_EXISTS
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param startIndex The index at which to start the page.
		 * @param endIndex The index at which to end the page.
		 * @param versionId The historical version to retrieve.
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getGlobalLeaderboardPageByVersionIfExists(
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GLOBAL_LEADERBOARD_PAGE_IF_EXISTS, data,
                    callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns a view of global leaderboard results that centers on the current player.
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score.
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GetGlobalLeaderboardView
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param beforeCount The count of number of players before the current player to include.
		 * @param afterCount The count of number of players after the current player to include.
		 * @param callback The method to be invoked when the server response is received
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_VIEW, data,
                    callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns a view of global leaderboard results that centers on the current player.
		 * Returns the same data as getGlobalLeaderboardView, but will not return an error if the leaderboard does not exist.
		 *
		 * Leaderboards entries contain the player's score and optionally, some user-defined
		 * data associated with the score.
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GET_GLOBAL_LEADERBOARD_VIEW_IF_EXISTS
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param beforeCount The count of number of players before the current player to include.
		 * @param afterCount The count of number of players after the current player to include.
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getGlobalLeaderboardViewIfExists(
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GLOBAL_LEADERBOARD_VIEW_IF_EXISTS, data,
                    callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns a view of global leaderboard results that centers on the current player.
		 * By using a non-current version id, the user can retrieve a historical leaderboard.
		 * See GetGlobalLeaderboardVersions method to retrieve the version id.
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GetGlobalLeaderboardView
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param beforeCount The count of number of players before the current player to include.
		 * @param afterCount The count of number of players after the current player to include.
		 * @param versionId The historical version to retrieve.
		 * @param callback The method to be invoked when the server response is received
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_VIEW, data,
                    callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Method returns a view of global leaderboard results that centers on the current player.
		 * By using a non-current version id, the user can retrieve a historical leaderboard.
		 * See GetGlobalLeaderboardVersions method to retrieve the version id.
		 *
		 * This method returns the same data as getGlobalLeaderboardViewByVersion, but it will not return an error if the leaderboard does not exist.
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - GET_GLOBAL_LEADERBOARD_VIEW_IF_EXISTS
		 *
		 * @param leaderboardId The id of the leaderboard to retrieve.
		 * @param sort Sort key Sort order of page.
		 * @param beforeCount The count of number of players before the current player to include.
		 * @param afterCount The count of number of players after the current player to include.
		 * @param versionId The historical version to retrieve.
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getGlobalLeaderboardViewByVersionIfExists(
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_GLOBAL_LEADERBOARD_VIEW_IF_EXISTS, data,
                    callback);
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_VERSIONS,
                    data, callback);
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_ENTRY_COUNT,
                    data, callback);
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
		 * @param versionId The version of the leaderboard. Use -1 for current.
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

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.GET_GLOBAL_LEADERBOARD_ENTRY_COUNT,
                    data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Post the players score to the given social leaderboard.
		 * You can optionally send a user-defined json string of data
		 * with the posted score. This string could include information
		 * relevant to the posted score.
		 *
		 * Note that the behaviour of posting a score can be modified in
		 * the brainCloud portal. By default, the server will only keep
		 * the player's best score.
		 *
		 * @param leaderboardId The leaderboard to post to
		 * @param score The score to post
		 * @param data Optional user-defined data to post with the score
		 * @param callback The method to be invoked when the server response is received
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
		 * Post the player's score to the given social leaderboard,
		 * dynamically creating the leaderboard if it does not exist yet.
		 * To create new leaderboard, configJson must specify leaderboardType, rotationType, resetAt, and retainedCount, at a minimum, with support to optionally specify an expiry in minutes.
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - POST_SCORE_DYNAMIC_USING_CONFIG
		 *
		 * @param leaderboardId The leaderboard to post to.
		 * @param score A score to post.
		 * @param scoreData Optional user-defined data to post with the score.
		 * @param configJson Configuration for the leaderboard if it does not exist yet, specified as JSON object.
		 *                      Configuration fields supported are:
		 *                          'leaderboardType': Required. Type of leaderboard. Valid values are:
		 *                              'LAST_VALUE',
		 *                              'HIGH_VALUE',
		 *                              'LOW_VALUE',
		 *                              'CUMULATIVE',
		 *                              'ARCADE_HIGH',
		 *                              'ARCADE_LOW';
		 *                          'rotationType': Required. Type of rotation. Valid values are:
		 *                              'NEVER',
		 *                              'DAILY',
		 *                              'DAYS',
		 *                              'WEEKLY',
		 *                              'MONTHLY',
		 *                              'YEARLY';
		 *                          'numDaysToRotate': Required if 'DAYS' rotation type, with valid values between 2 and 14; otherwise, null;
		 *                          'resetAt': UTC timestamp, in milliseconds, at which to rotate the period. Always null if 'NEVER' rotation type;
		 *                          'retainedCount': Required. Number of rotations (versions) of the leaderboard to retain;
		 *                          'expireInMins': Optional. Duration, in minutes, before the leaderboard is to automatically expire.
		 * @param callback The method to be invoked when the server response is received.
		 */
    public void postScoreToDynamicLeaderboardUsingConfig(String leaderboardId, int score, String scoreData,
            String configJson, IServerCallback callback) {
        try {
            JSONObject requestData = new JSONObject();
            requestData.put(Parameter.leaderboardId.name(), leaderboardId);
            requestData.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(scoreData)) {
                requestData.put(Parameter.scoreData.name(), new JSONObject(scoreData));
            }
            requestData.put(Parameter.configJson.name(), new JSONObject(configJson));

            ServerCall sc = new ServerCall(ServiceName.leaderboard, ServiceOperation.POST_SCORE_DYNAMIC_USING_CONFIG,
                    requestData, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Post the players score to the given social leaderboard.
		 * Pass leaderboard config data to dynamically create if necessary.
		 * You can optionally send a user-defined json string of data
		 * with the posted score. This string could include information
		 * relevant to the posted score. Uses UTC time in milliseconds since epoch
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - PostScoreDynamic
		 *
		 * @param leaderboardId The leaderboard to post to
		 * @param score The score to post
		 * @param data Optional user-defined data to post with the score
		 * @param leaderboardType leaderboard type
		 * @param rotationType Type of rotation
		 * @param rotationResetUTC Date to start rotation calculations. uses UTC time in milliseconds since epoch
		 * @param retainedCount How many rotations to keep
		 * @param callback The method to be invoked when the server response is received
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
		 * Service Name - leaderboard
		 * Service Operation - POST_GROUP_SCORE_DYNAMIC
		 *
		 * @param leaderboardId the leaderboard to post to
		 * @param groupId the group's id
		 * @param score the score to post
		 * @param data optional user defined datat to post with scor
		 * @param leaderboardType type of leaderboard
		 * @param rotationResetUTC uses UTC time in milliseconds since epoch
		 * @param retainedCount how long to keep rotation
		 * @param numDaysToRotate How many days between each rotation
		 * @param callback The method to be invoked when the server response is received
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
		 * Post the group's score to the given social leaderboard, dynamically creating the group leaderboard if it does not exist yet.
		 * To create new leaderboard, configJson must specify leaderboardType, rotationType, resetAt, and retainedCount, at a minimum, with support to optionally specify an expiry in minutes.
		 *
		 * Service Name - Leaderboard
		 * Service Operation - POST_GROUP_SCORE_DYNAMIC_USING_CONFIG
		 *
		 * @param leaderboard The leaderboard to post to
		 * @param groupId The ID of the group
		 * @param score A score to post
		 * @param configJson Configuration for the leaderboard if it does not exist yet, specified as JSON object. The supporting configuration fields are listed in the following table of configJson fields.
		 * @param callback The method to be invoked when the server response is received
		 */
    public void postScoreToDynamicGroupLeaderboardUsingConfig(String leaderboardId, String groupId, long score,
            String scoreData, String configJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.score.name(), score);
            if (StringUtil.IsOptionalParameterValid(scoreData)) {
                data.put(Parameter.scoreData.name(), new JSONObject(scoreData));
            }
            data.put(Parameter.configJson.name(), new JSONObject(configJson));

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.POST_GROUP_SCORE_DYNAMIC_USING_CONFIG, data, callback);

            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Post the players score to the given social leaderboard.
		 * Pass leaderboard config data to dynamically create if necessary.
		 * You can optionally send a user-defined json string of data
		 * with the posted score. This string could include information
		 * relevant to the posted score. uses UTC time in milliseconds since epoch
		 *
		 * Service Name - SocialLeaderboard
		 * Service Operation - PostScoreDynamic
		 *
		 * @param leaderboardId The leaderboard to post to
		 * @param score The score to post
		 * @param data Optional user-defined data to post with the score
		 * @param leaderboardType leaderboard type
		 * @param rotationResetUTC Date to start rotation calculations, uses UTC time in milliseconds since epoch
		 * @param retainedCount How many rotations to keep
		 * @param numDaysToRotate How many days between each rotation
		 * @param callback The method to be invoked when the server response is received
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
		 * Retrieve the social leaderboard for a group by its version.
		 *
		 * Service Name - leaderboard
		 * Service Operation - GET_GROUP_SOCIAL_LEADERBOARD
		 *
		 * @param leaderboardId The leaderboard to retrieve
		 * @param groupId The ID of the group
		 * @param versionId the version of the leaderboard
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getGroupSocialLeaderboardByVersion(String leaderboardId, String groupId, int versionId,
            IServerCallback callback) {
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
		 * Retrieve the social leaderboard for a list of players.
		 * This method returns the same data as getPlayersSocialLeaderboard, but it will not return an error if the leaderboard is not found.
		 *
		 * Service Name - leaderboard
		 * Service Operation - GET_PLAYERS_SOCIAL_LEADERBOARD_IF_EXISTS
		 *
		 * @param leaderboardId The leaderboard to retrieve
		 * @param profileIds The IDs of the players
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getPlayersSocialLeaderboardIfExists(String leaderboardId, String[] profileIds,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.profileIds.name(), new JSONArray(profileIds));

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYERS_SOCIAL_LEADERBOARD_IF_EXISTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Retrieve the social leaderboard for a list of players by its version.
		 *
		 * Service Name - leaderboard
		 * Service Operation - GET_PLAYERS_SOCIAL_LEADERBOARD
		 *
		 * @param leaderboardId The leaderboard to retrieve
		 * @param profileIds The IDs of the players
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getPlayersSocialLeaderboardByVersion(String leaderboardId, String[] profileIds, int versionId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYERS_SOCIAL_LEADERBOARD, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Retrieve the social leaderboard for a list of players by its version.
		 * This method returns the same data as getPlayersSocialLeaderboardByVersion, but it will not return an error if the leaderboard is not found.
		 *
		 * Service Name - leaderboard
		 * Service Operation - GET_PLAYERS_SOCIAL_LEADERBOARD
		 *
		 * @param leaderboardId The leaderboard to retrieve
		 * @param profileIds The IDs of the players
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getPlayersSocialLeaderboardByVersionIfExists(String leaderboardId, String[] profileIds, int versionId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.leaderboardId.name(), leaderboardId);
            data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
            data.put(Parameter.versionId.name(), versionId);

            ServerCall sc = new ServerCall(ServiceName.leaderboard,
                    ServiceOperation.GET_PLAYERS_SOCIAL_LEADERBOARD_BY_VERSION_IF_EXISTS, data, callback);
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
		 * Gets a player's score from a leaderboard
		 *
		 * Service Name - leaderboard
		 * Service Operation - GET_PLAYER_SCORE
		 *
		 * @param leaderboardId The leaderboard ID
		 * @param versionId The version of the leaderboard. Use -1 for current.
		 * @param maxResults The max number of returned results
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
		 * @param type A collection of leaderboardIds to retrieve scores from
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
		 * Posts score to groups leaderboard - Note the user must be a member of the group
		 *
		 * Service Name - leaderboard
		 * Service Operation - POST_GROUP_SCORE
		 *
		 * @param leaderboardId A collection of leaderboardIds to retrieve scores from
		 * @param groupId the groups Id
		 * @param score the score you wish to post
		 * @param jsonData extra json Data
		 * @param callback The method to be invoked when the server response is received
		 */
    public void postScoreToGroupLeaderboard(String leaderboardId, String groupId, int score, String jsonData,
            IServerCallback callback) {
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
		 * Posts score to group leaderbopard and dynamically creates if necessary. leaderboardType, rotationReset, retainedCount and rotationType are required. uses UTC time in milliseconds since epoch
		 *
		 * Service Name - leaderboard
		 * Service Operation - POST_GROUP_SCORE_DYNAMIC
		 *
		 * @param leaderboardId the leaderboard to post to
		 * @param groupId the group's id
		 * @param score the score to post
		 * @param data optional user defined datat to post with scor
		 * @param leaderboardType type of leaderboard
		 * @param rotationType type of rotation
		 * @param rotationResetUTC uses UTC time in milliseconds since epoch
		 * @param retainedCount how long to keep rotation
		 * @param callback The method to be invoked when the server response is received
		 */
    public void postScoreToDynamicGroupLeaderboardUTC(String leaderboardId, String groupId, long score, String data,
            String leaderboardType, String rotationType, long rotationResetUTC, int retainedCount,
            IServerCallback callback) {
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
		 * @param leaderboardId A collection of leaderboardIds to retrieve scores from
		 * @param groupId the groups Id
		 * @param versionId the score you wish to post
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
		 * Retrieve a view of the group leaderboardsurrounding the current group.
		 *
		 * Service Name - leaderboard
		 * Service Operation - GET_GROUP_LEADERBOARD_VIEW
		 *
		 * @param leaderboardId A collection of leaderboardIds to retrieve scores from
		 * @param groupId the groups Id
		 * @param sortOrder the sort order
		 * @param beforeCount count of players before current player to include
		 * @param afterCount count of players after current player to include
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getGroupLeaderboardView(String leaderboardId, String groupId, SortOrder sort, int beforeCount,
            int afterCount, IServerCallback callback) {
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
		 * Retrieve a view of the group leaderboard surrounding the current group by the version
		 *
		 * Service Name - leaderboard
		 * Service Operation - GET_GROUP_LEADERBOARD_VIEW
		 *
		 * @param leaderboardId A collection of leaderboardIds to retrieve scores from
		 * @param groupId the groups Id
		 * @param versionId the version
		 * @param sortOrder the sort order
		 * @param beforeCount count of players before current player to include
		 * @param afterCount count of players after current player to include
		 * @param callback The method to be invoked when the server response is received
		 */
    public void getGroupLeaderboardViewByVersion(String leaderboardId, String groupId, int versionId, SortOrder sort,
            int beforeCount, int afterCount, IServerCallback callback) {
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
