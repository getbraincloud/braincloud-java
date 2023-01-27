package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaybackStreamService {

    private enum Parameter {
        targetPlayerId,
        initiatingPlayerId,
        maxNumStreams,
        includeSharedData,
        playbackStreamId,
        eventData,
        summary
    }

    private BrainCloudClient _client;

    public PlaybackStreamService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Starts a stream
     *
     * Service Name - PlaybackStream
     * Service Operation - StartStream
     *
     * @param targetPlayerId    The player to start a stream with
     * @param includeSharedData Whether to include shared data in the stream
     * @param callback The callback.
     */
    public void startStream(
            String targetPlayerId,
            boolean includeSharedData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.targetPlayerId.name(), targetPlayerId);
            data.put(Parameter.includeSharedData.name(), includeSharedData);

            ServerCall sc = new ServerCall(ServiceName.playbackStream, ServiceOperation.START_STREAM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Reads a stream
     *
     * Service Name - PlaybackStream
     * Service Operation - ReadStream
     *
     * @param playbackStreamId Identifies the stream to read
     * @param callback The callback.
     */
    public void readStream(
            String playbackStreamId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playbackStreamId.name(), playbackStreamId);

            ServerCall sc = new ServerCall(ServiceName.playbackStream, ServiceOperation.READ_STREAM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Ends a stream
     *
     * Service Name - PlaybackStream
     * Service Operation - EndStream
     *
     * @param playbackStreamId Identifies the stream to read
     * @param callback The callback.
     */
    public void endStream(
            String playbackStreamId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playbackStreamId.name(), playbackStreamId);

            ServerCall sc = new ServerCall(ServiceName.playbackStream, ServiceOperation.END_STREAM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Deletes a stream
     *
     * Service Name - PlaybackStream
     * Service Operation - DeleteStream
     *
     * @param playbackStreamId Identifies the stream to read
     * @param callback The callback.
     */
    public void deleteStream(
            String playbackStreamId,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playbackStreamId.name(), playbackStreamId);

            ServerCall sc = new ServerCall(ServiceName.playbackStream, ServiceOperation.DELETE_STREAM, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Adds a stream event
     *
     * Service Name - PlaybackStream
     * Service Operation - AddEvent
     *
     * @param playbackStreamId Identifies the stream to read
     * @param eventData Describes the event
     * @param summary Current summary data as of this event
     * @param callback The callback.
     */
    public void addEvent(
            String playbackStreamId,
            String eventData,
            String summary,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playbackStreamId.name(), playbackStreamId);
            data.put(Parameter.eventData.name(), new JSONObject(eventData));
            data.put(Parameter.summary.name(), new JSONObject(summary));

            ServerCall sc = new ServerCall(ServiceName.playbackStream, ServiceOperation.ADD_EVENT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Gets recent stream summaries for initiating player
     *
     * Service Name - PlaybackStream
     * Service Operation - GetRecentStreamsForInitiatingPlayer
     *
     * @param initiatingPlayerId The player that started the stream
     * @param maxNumStreams The max number of streams to query
     * @param callback The callback.
     */
    public void getRecentStreamsForInitiatingPlayer(
            String initiatingPlayerId,
            int maxNumStreams,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.initiatingPlayerId.name(), initiatingPlayerId);
            data.put(Parameter.maxNumStreams.name(), maxNumStreams);

            ServerCall sc = new ServerCall(ServiceName.playbackStream, ServiceOperation.GET_RECENT_STREAMS_FOR_INITIATING_PLAYER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Gets recent stream summaries for target player
     *
     * Service Name - PlaybackStream
     * Service Operation - GetRecentStreamsForTargetPlayer
     *
     * @param targetPlayerId The player that was target of the stream
     * @param maxNumStreams The max number of streams to query
     * @param callback The callback.
     */
    public void getRecentStreamsForTargetPlayer(
            String targetPlayerId,
            int maxNumStreams,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.targetPlayerId.name(), targetPlayerId);
            data.put(Parameter.maxNumStreams.name(), maxNumStreams);

            ServerCall sc = new ServerCall(ServiceName.playbackStream, ServiceOperation.GET_RECENT_STREAMS_FOR_TARGET_PLAYER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

}


