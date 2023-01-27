package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerStatisticsService {

    private enum Parameter {
        statistics,
        category,
        xp_points
    }

    private BrainCloudClient _client;

    public PlayerStatisticsService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Read all available user statistics.
     *
     * Service Name - PlayerStatistics
     * Service Operation - Read
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void readAllUserStats(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                ServiceOperation.READ, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Reads a subset of user statistics as defined by the input collection.
     *
     * Service Name - PlayerStatistics
     * Service Operation - ReadSubset
     *
     * @param statistics A collection containing the subset of statistics to read:
     * ex. [ "pantaloons", "minions" ]
     * @param callback The method to be invoked when the server response is received
     */
    public void readUserStatsSubset(String[] statistics, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONArray jsonData = new JSONArray();
            for (String att : statistics) {
                jsonData.put(att);
            }
            data.put(Parameter.statistics.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                    ServiceOperation.READ_SUBSET, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method retrieves the user statistics for the given category.
     *
     * Service Name - PlayerStatistics
     * Service Operation - READ_FOR_CATEGORY
     *
     * @param category The user statistics category
     * @param callback Method to be invoked when the server response is received.
     */
    public void readUserStatsForCategory(String category, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.category.name(), category);

            ServerCall sc = new ServerCall(ServiceName.playerStatistics, ServiceOperation.READ_FOR_CATEGORY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Reset all of the statistics for this user back to their initial value.
     *
     * Service Name - PlayerStatistics
     * Service Operation - Reset
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void resetAllUserStats(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                ServiceOperation.RESET, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Atomically increment (or decrement) user statistics.
     * Any rewards that are triggered from user statistic increments
     * will be considered. User statistics are defined through the brainCloud portal.
     * Note also that the "xpCapped" property is returned (true/false depending on whether
     * the xp cap is turned on and whether the user has hit it).
     *
     * Service Name - PlayerStatistics
     * Service Operation - Update
     *
     * @param jsonData The JSON encoded data to be sent to the server as follows:
     * {
     *   stat1: 10,
     *   stat2: -5.5,
     * }
     * would increment stat1 by 10 and decrement stat2 by 5.5.
     * For the full statistics grammer see the api.braincloudservers.com site.
     * There are many more complex operations supported such as:
     * {
     *   stat1:INC_TO_LIMIT#9#30
     * }
     * which increments stat1 by 9 up to a limit of 30.
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void incrementUserStats(String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONObject jsonDataObj = new JSONObject(jsonData);
            data.put(Parameter.statistics.name(), jsonDataObj);

            /*
             * To be implemented for Android platform
             * 
             * SuccessCallback successCallbacks =
             * braincloudClient.GetGamificationService
             * ().CheckForAchievementsToAward; if (success != null) {
             * successCallbacks += success; }
             */

            ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                    ServiceOperation.UPDATE_INCREMENT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Increments the user's experience. If the user goes up a level,
     * the new level details will be returned along with a list of rewards.
     *
     * Service Name - PlayerStatistics
     * Service Operation - UpdateIncrement
     *
     * @param xpValue The amount to increase the user's experience by
     * @param callback The method to be invoked when the server response is received
     */
    public void incrementExperiencePoints(
            int xpValue,
            IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.xp_points.toString(), xpValue);
        } catch (JSONException je) {
            je.printStackTrace();
        }

        // TODO: 15-09-03 need to check for any achievements awarded and notify gamification service
        //SuccessCallback successCallbacks = _client.getGamificationService().checkForAchievementsToAward;
        //if (success != null)
        //{
        //     successCallbacks += success;
        //
        // }

        ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                ServiceOperation.UPDATE, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Returns JSON representing the next experience level for the user.
     *
     * Service Name - PlayerStatistics
     * Service Operation - ReadNextXpLevel
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getNextExperienceLevel(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                ServiceOperation.READ_NEXT_XPLEVEL, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Sets the user's experience to an absolute value. Note that this
     * is simply a set and will not reward the user if their level changes
     * as a result.
     *
     * Service Name - PlayerStatistics
     * Service Operation - SetXpPoints
     *
     * @param xpValue The amount to set the the user's experience to
     * @param callback The method to be invoked when the server response is received
     */
    public void setExperiencePoints(int xpValue,
                                    IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.xp_points.name(), xpValue);

            ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                    ServiceOperation.SET_XPPOINTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }

    /**
     * Apply statistics grammar to a partial set of statistics.
     *
     * Service Name - PlayerStatistics
     * Service Operation - PROCESS_STATISTICS
     *
     * @param jsonData The JSON format is as follows:
     * {
     *     "DEAD_CATS": "RESET",
     *     "LIVES_LEFT": "SET#9",
     *     "MICE_KILLED": "INC#2",
     *     "DOG_SCARE_BONUS_POINTS": "INC#10",
     *     "TREES_CLIMBED": 1
     * }
     * @param callback Method to be invoked when the server response is received.
     */
    public void processStatistics(String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONObject jsonDataObj = new JSONObject(jsonData);
            data.put(Parameter.statistics.name(), jsonDataObj);

            ServerCall sc = new ServerCall(ServiceName.playerStatistics,
                    ServiceOperation.PROCESS_STATISTICS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
