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
         * Trigger a server-side event that will update the user's statistics.
         * This may cause one or more awards to be sent back to the user,
         * such as achievements, experience, or other rewards. Achievements
         * will be sent by this client library to the appropriate awards service
         * (e.g., Apple Game Center, Google Play Games, etc.).
         *
         * This mechanism supersedes the PlayerStatisticsService API methods,
         * which only update raw statistics without triggering rewards.
         *
         * Service Name - PlayerStatisticsEvent
         * Service Operation - Trigger
         *
         * @param eventName Name of the statistics event to trigger.
         * @param eventMultiplier Optional multiplier to apply to the event.
         * @param callback Callback invoked when the server response is received.
         *                    Defaults to nullptr if no callback is needed.
         * @see BrainCloudPlayerStatistics
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
         * See documentation for TriggerStatisticsEvent for more
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
         * @param callback The method to be invoked when the server response is received
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
