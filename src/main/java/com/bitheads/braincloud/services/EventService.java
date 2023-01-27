package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventService {

    private enum Parameter {
        toId,
        eventType,
        eventData,
        recordLocally,
        fromId,
        eventId,
        includeIncomingEvents,
        includeSentEvents,
        evId,
        evIds,
        dateMillis
    }

    private BrainCloudClient _client;

    public EventService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Sends an event to the designated player id with the attached json data.
     * Any events that have been sent to a player will show up in their incoming
     * event mailbox. If the recordLocally flag is set to true, a copy of
     * this event (with the exact same event id) will be stored in the sending
     * player's "sent" event mailbox.
     *
     * Note that the list of sent and incoming events for a player is returned
     * in the "ReadPlayerState" call (in the BrainCloudPlayer module).
     *
     * Service Name - event
     * Service Operation - SEND
     *
     * @param toProfileId The id of the user who is being sent the event
     * @param eventType The user-defined type of the event.
     * @param jsonEventData The user-defined data for this event encoded in JSON.
     * @param callback The callback.
     */
    public void sendEvent(String toProfileId, String eventType, String jsonEventData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.toId.name(), toProfileId);
            data.put(Parameter.eventType.name(), eventType);

            JSONObject jsonData = new JSONObject(jsonEventData);
            data.put(Parameter.eventData.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.SEND, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
        }
    }

    /**
     * Updates an event in the player's incoming event mailbox.
     *
     * Service Name - event
     * Service Operation - UPDATE_EVENT_DATA
     *
     * @param evId The event id
     * @param jsonEventData The user-defined data for this event encoded in JSON.
     * @param callback The  callback.
     */
    public void updateIncomingEventData(String evId, String jsonEventData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.evId.name(), evId);

            JSONObject jsonData = new JSONObject(jsonEventData);
            data.put(Parameter.eventData.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.UPDATE_EVENT_DATA, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
        }
    }

    /**
     * Delete an event out of the player's incoming mailbox.
     *
     * Service Name - event
     * Service Operation - DELETE_INCOMING
     *
     * @param evId The event id
     * @param callback The callback.
     */
    public void deleteIncomingEvent(String evId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.evId.name(), evId);

            ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.DELETE_INCOMING, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
        }
    }

    /**
     * Delete a list of events out of the user's incoming mailbox.
     *
     * Service Name - event
     * Service Operation - DELETE_INCOMING_EVENTS
     *
     * @param evIds Collection of event ids
     * @param callback The callback.
     */
    public void deleteIncomingEvents(String[] evIds, IServerCallback callback)
    {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.evIds.name(), evIds);

            ServerCall serverCall = new ServerCall(ServiceName.event, ServiceOperation.DELETE_INCOMING_EVENTS, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete any events of the given type older than the given date out of the user's incoming mailbox.
     *
     * Service Name - event
     * Service Operation - DELETE_INCOMING_EVENTS_BY_TYPE_OLDER_THAN
     *
     * @param eventType The user-defined type of the event
     * @param dateMillis createdAt cut-off time whereby older events will be deleted
     * @param callback The callback.
     */
    public void deleteIncomingEventsByTypeOlderThan(String eventType, long dateMillis, IServerCallback callback)
    {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.eventType.name(), eventType);
            data.put(Parameter.dateMillis.name(), dateMillis);

            ServerCall serverCall = new ServerCall(ServiceName.event, ServiceOperation.DELETE_INCOMING_EVENTS_BY_TYPE_OLDER_THAN, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete any events older than the given date out of the user's incoming mailbox.
     *
     * Service Name - event
     * Service Operation - DELETE_INCOMING_EVENTS_OLDER_THAN
     *
     * @param dateMillis createdAt cut-off time whereby older events will be deleted
     * @param callback The callback.
     */
    public void deleteIncomingEventsOlderThan(long dateMillis, IServerCallback callback)
    {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.dateMillis.name(), dateMillis);

            ServerCall serverCall = new ServerCall(ServiceName.event, ServiceOperation.DELETE_INCOMING_EVENTS_OLDER_THAN, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the events currently queued for the player.
     *
     * Service Name - event
     * Service Operation - GET_EVENTS
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getEvents(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.GET_EVENTS, null, callback);
        _client.sendRequest(sc);
    }
}
