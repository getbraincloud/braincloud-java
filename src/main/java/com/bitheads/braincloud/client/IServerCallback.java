package com.bitheads.braincloud.client;

import org.json.JSONObject;

public interface IServerCallback {
    /**
     * The serverCallback() method returns server data back to the layer
     * interfacing with the BrainCloud library.
     *
     * @param serviceName - name of the requested service
     * @param serviceOperation - requested operation
     * @param jsonData - returned data from the server
     */
    void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject jsonData);

    /**
     * Errors are returned back to the layer which is interfacing with the
     * BrainCloud library through the serverError() callback.
     *
     * A server error might indicate a failure of the client to communicate
     * with the server after N retries.
     *
     * @param serviceName - name of the requested service
     * @param serviceOperation - requested operation
     * @param statusCode The error status return code (400, 403, 500, etc)
     * @param reasonCode The brainCloud reason code (see reason codes on apidocs site)
     * @param jsonError The error json string
     */
    void serverError(ServiceName serviceName, ServiceOperation serviceOperation, int statusCode, int reasonCode, String jsonError);
}
