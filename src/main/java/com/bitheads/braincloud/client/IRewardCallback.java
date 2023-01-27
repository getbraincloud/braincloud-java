package com.bitheads.braincloud.client;

import org.json.JSONObject;

/**
 * Created by prestonjennings on 2015-11-03.
 */
public interface IRewardCallback {
    void rewardCallback(JSONObject events);
}
