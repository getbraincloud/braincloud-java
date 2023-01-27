package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerStatisticsEventService {

    private enum Parameter {
        eventName,
        eventMultiplier,
        events
    }

    private BrainCloudClient _client;

    public PlayerStatisticsEventService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Trigger an event server side that will increase the user statistics.
     * This may cause one or more awards to be sent back to the user -
     * could be achievements, experience, etc. Achievements will be sent by this
     * client library to the appropriate awards service (Apple Game Center, etc).
     *
     * This mechanism supercedes the PlayerStatisticsService API methods, since
     * PlayerStatisticsService API method only update the raw statistics without
     * triggering the rewards.
     *
     * Service Name - PlayerStatisticsEvent
     * Service Operation - Trigger
     *
     * @see PlayerStatisticsService
     * 
     * @deprecated Use triggerStatsEvent instead - removal September 1, 2021
     */
    public void triggerUserStatsEvent(String eventName, int eventMultiplier, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.eventName.name(), eventName);
            data.put(Parameter.eventMultiplier.name(), eventMultiplier);

            ServerCall sc = new ServerCall(ServiceName.playerStatisticsEvent, ServiceOperation.TRIGGER, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException ignored) {
        }
    }

    /**
     * Trigger an event server side that will increase the user statistics.
     * This may cause one or more awards to be sent back to the user -
     * could be achievements, experience, etc. Achievements will be sent by this
     * client library to the appropriate awards service (Apple Game Center, etc).
     *
     * This mechanism supercedes the PlayerStatisticsService API methods, since
     * PlayerStatisticsService API method only update the raw statistics without
     * triggering the rewards.
     *
     * Service Name - PlayerStatisticsEvent
     * Service Operation - Trigger
     *
     * @see PlayerStatisticsService
     */
    public void triggerStatsEvent(String eventName, int eventMultiplier, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.eventName.name(), eventName);
            data.put(Parameter.eventMultiplier.name(), eventMultiplier);

            ServerCall sc = new ServerCall(ServiceName.playerStatisticsEvent, ServiceOperation.TRIGGER, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException ignored) {
        }
    }

    /**
     * @deprecated Use triggerStatsEvents instead - removal September 1, 2021
     */
    public void triggerUserStatsEvents(String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONArray jsonArray = new JSONArray(jsonData);
            data.put(Parameter.events.name(), jsonArray);

            ServerCall sc = new ServerCall(ServiceName.playerStatisticsEvent, ServiceOperation.TRIGGER_MULTIPLE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * See documentation for TriggerPlayerStatisticsEvent for more
     * documentation.
     *
     * Service Name - PlayerStatisticsEvent
     * Service Operation - TriggerMultiple
     *
     * @param jsonData
     *   [
     *     {
     *       "eventName": "event1",
     *       "eventMultiplier": 1
     *     },
     *     {
     *       "eventName": "event2",
     *       "eventMultiplier": 1
     *     }
     *   ]
     */
    public void triggerStatsEvents(String jsonData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONArray jsonArray = new JSONArray(jsonData);
            data.put(Parameter.events.name(), jsonArray);

            ServerCall sc = new ServerCall(ServiceName.playerStatisticsEvent, ServiceOperation.TRIGGER_MULTIPLE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }
}
