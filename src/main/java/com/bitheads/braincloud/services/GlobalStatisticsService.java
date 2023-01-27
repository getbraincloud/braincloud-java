package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlobalStatisticsService {

    private enum Parameter {
        statistics,
        category
    }

    private BrainCloudClient _client;

    public GlobalStatisticsService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Method returns all of the global statistics.
     *
     * @param callback The callback.
     */
    public void readAllGlobalStats(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.globalGameStatistics,
                ServiceOperation.READ, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Reads a subset of global statistics.
     *
     * @param globalStats The array of statistics to read: [ "Level01_TimesBeaten", "Level02_TimesBeaten" ]
     * @param callback The callback.
     */
    public void readGlobalStatsSubset(String[] globalStats,
                                      IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONArray jsonData = new JSONArray();
            for (String att : globalStats) {
                jsonData.put(att);
            }
            data.put(Parameter.statistics.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.globalGameStatistics,
                    ServiceOperation.READ_SUBSET, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method retrieves the global statistics for the given category.
     *
     * Service Name - GlobalStatistics
     * Service Operation - READ_FOR_CATEGORY
     *
     * @param category The global statistics category
     *
     * @param callback Callback.
     */
    public void readGlobalStatsForCategory(
            String category,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.category.name(), category);

            ServerCall sc = new ServerCall(ServiceName.globalGameStatistics, ServiceOperation.READ_FOR_CATEGORY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Atomically increment (or decrement) global statistics. Global statistics
     * are defined through the brainCloud portal.
     *
     * @param jsonData The JSON encoded data to be sent to the server.For the full
     *            statistics grammer see the http://getbraincloud.com/apidocs site.
     * @param callback The callback.
     */
    public void incrementGlobalStats(String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONObject jsonDataObj = new JSONObject(jsonData);
            data.put(Parameter.statistics.name(), jsonDataObj);

            ServerCall sc = new ServerCall(ServiceName.globalGameStatistics,
                    ServiceOperation.UPDATE_INCREMENT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Apply statistics grammar to a partial set of statistics.
     *
     * Service Name - GlobalStatistics
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

            ServerCall sc = new ServerCall(ServiceName.globalGameStatistics,
                    ServiceOperation.PROCESS_STATISTICS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
