package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class OneWayMatchService {

    private enum Parameter {
        playerId,
        rangeDelta,
        playbackStreamId
    }

    private BrainCloudClient _client;

    public OneWayMatchService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Starts a match
     *
     * Service Name - OneWayMatch 
     * Service Operation - StartMatch
     *
     * @param otherPlayerId The player to start a match with
     * @param rangeDelta The Range delta used for the initial match search
     * @param callback The callback.
     */
    public void startMatch(String otherPlayerId, long rangeDelta, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playerId.name(), otherPlayerId);
            data.put(Parameter.rangeDelta.name(), rangeDelta);

            ServerCall sc = new ServerCall(ServiceName.onewayMatch, ServiceOperation.START_MATCH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Cancels a match
     *
     * Service Name - OneWayMatch 
     * Service Operation - CancelMatch
     *
     * @param playbackStreamId The playback stream id returned in the start match
     * @param callback The callback.
     */
    public void cancelMatch(String playbackStreamId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playbackStreamId.name(), playbackStreamId);

            ServerCall sc = new ServerCall(ServiceName.onewayMatch, ServiceOperation.CANCEL_MATCH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Completes a match
     *
     * Service Name - OneWayMatch 
     * Service Operation - CompleteMatch
     *
     * @param playbackStreamId The playback stream id returned in the initial start match
     * @param callback The callback.
     */
    public void completeMatch(String playbackStreamId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playbackStreamId.name(), playbackStreamId);

            ServerCall sc = new ServerCall(ServiceName.onewayMatch, ServiceOperation.COMPLETE_MATCH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

}
