// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.client;

public interface IRelayCallback {
    void relayCallback(int netId, byte[] bytes);
}
