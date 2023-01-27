package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONObject;

public class TimeService {

    private BrainCloudClient _client;

    public TimeService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Method returns the server time in UTC. This is in UNIX millis time format.
     * For instance 1396378241893 represents 2014-04-01 2:50:41.893 in GMT-4.
     *
     * Server API reference: ServiceName.Time, ServiceOperation.Read
     *
     * Service Name - Time
     * Service Operation - Read
     *
     * @param callback The callback.
     */
    public void readServerTime(IServerCallback callback) {

        JSONObject message = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.time, ServiceOperation.READ, message, callback);
        _client.sendRequest(sc);
    }
}
