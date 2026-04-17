// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.client;

import org.json.JSONObject;

public interface IAutoReconnectCallback {

    /***
     * The method to be invoked when the auto reconnect re-authentication succeeds.
     * 
     * @param jsonData
     */
    void autoReconnectCallbackSuccess(JSONObject jsonData);

    /***
     * The method to be invoked when the auto reconnect re-authentication fails.
     * 
     * @param jsonData
     */
    void autoReconnectCallbackFailure(JSONObject jsonData);
}
