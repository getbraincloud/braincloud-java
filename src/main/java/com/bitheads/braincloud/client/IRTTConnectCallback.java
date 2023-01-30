package com.bitheads.braincloud.client;

public interface IRTTConnectCallback {
    void rttConnectSuccess();
    void rttConnectFailure(String errorMessage);
}
