package com.bitheads.braincloud.client;

import org.json.JSONObject;

public interface IRTTConnectCallback {
    void rttConnectSuccess();
    void rttConnectFailure(String errorMessage);
}
