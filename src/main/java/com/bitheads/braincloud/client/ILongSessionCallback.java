// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.client;

import org.json.JSONObject;

public interface ILongSessionCallback {

    /***
     * The method to be invoked when the long session re-authentication succeeds.
     * 
     * @param jsonData
     */
    void longSessionCallbackSuccess(JSONObject jsonData);

    /***
     * The method to be invoked when the long session re-authentication fails.
     * 
     * @param jsonData
     */
    void longSessionCallbackFailure(JSONObject jsonData);
}
