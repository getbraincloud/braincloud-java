package com.bitheads.braincloud.client;

import org.json.JSONObject;

/**
 * Created by prestonjennings on 15-09-02.
 */
public interface IEventCallback {
    void eventsReceived(JSONObject events);
}
