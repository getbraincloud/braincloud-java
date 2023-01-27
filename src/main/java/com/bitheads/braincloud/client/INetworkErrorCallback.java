package com.bitheads.braincloud.client;

/**
 * Created by prestonjennings on 16-02-25.
 */
public interface INetworkErrorCallback {
    /**
     * The networkError method is invoked whenever a network error is encountered
     * communicating to the brainCloud server.
     *
     * Note this method is *not* invoked when FlushCachedMessages(true) is called.
     */
    void networkError();
}
