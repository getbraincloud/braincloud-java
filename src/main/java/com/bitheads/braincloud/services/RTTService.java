package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;
import com.bitheads.braincloud.client.IRTTCallback;
import com.bitheads.braincloud.client.IRTTConnectCallback;
import com.bitheads.braincloud.comms.RTTComms;

public class RTTService {

    private BrainCloudClient _client;

    public RTTService(BrainCloudClient client) {
        _client = client;
    }
    /**
     * Enables Real Time event for this session.
     * Real Time events are disabled by default. Usually events
     * need to be polled using GET_EVENTS. By enabling this, events will
     * be received instantly when they happen through a TCP connection to an Event Server.
     *
     * This function will first call requestClientConnection, then connect to the address
     *
     * @param callback The callback.
     * @param useWebSocket Use web sockets instead of TCP for the internal connections. Default is true
     */
    public void enableRTT(IRTTConnectCallback callback, boolean useWebSocket) {
        _client.getRTTComms().enableRTT(callback, useWebSocket);
    }
    public void enableRTT(IRTTConnectCallback callback) {
        enableRTT(callback, true);
    }

    /**
     * Disables Real Time event for this session.
     */
    public void disableRTT() {
        _client.getRTTComms().disableRTT();
    }

    /**
     * Returns true is RTT is enabled
     */
    public boolean getRTTEnabled()
    {
        return _client.getRTTComms().isRTTEnabled();
    }

    /**
     * Returns rtt connection status
     */
    public RTTComms.RttConnectionStatus getConnectionStatus()
    {
        return _client.getRTTComms().getConnectionStatus();
    }
    
    public String getRTTConnectionId() {
        return _client.getRTTComms().getConnectionId();
    }

     /**
     * Listen to real time events.
     * 
     * Notes: RTT must be enabled for this app, and enableRTT must have been successfully called.
     * Only one event callback can be registered at a time. Calling this a second time will override the previous callback.
     */
    public void registerRTTEventCallback(IRTTCallback callback) {
        _client.getRTTComms().registerRTTCallback(ServiceName.event.toString(), callback);
    }
    public void deregisterRTTEventCallback() {
        _client.getRTTComms().deregisterRTTCallback(ServiceName.event.toString());
    }

    /**
     * Listen to real time chat messages.
     * 
     * Notes: RTT must be enabled for this app, and enableRTT must have been successfully called.
     * Only one chat callback can be registered at a time. Calling this a second time will override the previous callback.
     */
    public void registerRTTChatCallback(IRTTCallback callback) {
        _client.getRTTComms().registerRTTCallback(ServiceName.chat.toString(), callback);
    }
    public void deregisterRTTChatCallback() {
        _client.getRTTComms().deregisterRTTCallback(ServiceName.chat.toString());
    }

    /**
     * Listen to real time messaging.
     * 
     * Notes: RTT must be enabled for this app, and enableRTT must have been successfully called.
     * Only one messaging callback can be registered at a time. Calling this a second time will override the previous callback.
     */
    public void registerRTTMessagingCallback(IRTTCallback callback) {
        _client.getRTTComms().registerRTTCallback(ServiceName.messaging.toString(), callback);
    }
    public void deregisterRTTMessagingCallback() {
        _client.getRTTComms().deregisterRTTCallback(ServiceName.messaging.toString());
    }

    /**
     * Listen to real time lobby events.
     * 
     * Notes: RTT must be enabled for this app, and enableRTT must have been successfully called.
     * Only one lobby callback can be registered at a time. Calling this a second time will override the previous callback.
     */
    public void registerRTTLobbyCallback(IRTTCallback callback) {
        _client.getRTTComms().registerRTTCallback(ServiceName.lobby.toString(), callback);
    }
    public void deregisterRTTLobbyCallback() {
        _client.getRTTComms().deregisterRTTCallback(ServiceName.lobby.toString());
    }

    /**
     * Listen to real time presence events.
     * 
     * Notes: RTT must be enabled for this app, and enableRTT must have been successfully called.
     * Only one presence callback can be registered at a time. Calling this a second time will override the previous callback.
     */
    public void registerRTTPresenceCallback(IRTTCallback callback) {
        _client.getRTTComms().registerRTTCallback(ServiceName.presence.toString(), callback);
    }
    public void deregisterRTTPresenceCallback() {
        _client.getRTTComms().deregisterRTTCallback(ServiceName.presence.toString());
    }

        /**
     * Listen to real time blockchain events.
     * 
     * Notes: RTT must be enabled for this app, and enableRTT must have been successfully called.
     * Only one presence callback can be registered at a time. Calling this a second time will override the previous callback.
     */
    public void registerRTTBlockchainRefreshCallback(IRTTCallback callback) {
        _client.getRTTComms().registerRTTCallback(ServiceName.userItems.toString(), callback);
    }
    public void deregisterRTTBlockchainRefreshCallback() {
        _client.getRTTComms().deregisterRTTCallback(ServiceName.userItems.toString());
    }

    /**
     * Clear all set RTT callbacks
     */
    public void deregisterAllCallbacks() {
        _client.getRTTComms().deregisterAllCallbacks();
    }

    /**
     * Requests the event server address
     *
     * @param callback The callback.
     */
    public void requestClientConnection(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.rttRegistration, ServiceOperation.REQUEST_CLIENT_CONNECTION, null, callback);
        _client.sendRequest(sc);
    }
}
