// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.client;

import org.json.JSONObject;

public interface IRelayConnectCallback {
    void relayConnectSuccess(JSONObject jsonData);
    void relayConnectFailure(String errorMessage);
}
