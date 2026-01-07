// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.client;

public interface IRTTConnectCallback {
    void rttConnectSuccess();
    void rttConnectFailure(String errorMessage);
}
