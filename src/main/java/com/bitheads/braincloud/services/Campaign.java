package com.bitheads.braincloud.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

public class Campaign {

    private enum Parameter {
        optionsJson
    }

    private BrainCloudClient _client;

    public Campaign(BrainCloudClient client) {
        _client = client;
    }

    /**
	 * Returns the list of campaigns the current player is participating in,
     * providing campaign, campaign scenario, and participation details.
	 *
	 * Service Name - campaign
	 * Service Operation - GET_MY_CAMPAIGNS
	 *
	 * @param optionsJson Optional parameters (reserved for future use).
     * @param callback    The method to be invoked when the server response is
     *                    received
	 */
	public void getMyCampaigns(String optionsJson, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			if (optionsJson != null) {
				data.put(Parameter.optionsJson.name(), new JSONObject(optionsJson));
			}

			ServerCall sc = new ServerCall(ServiceName.campaign, ServiceOperation.GET_MY_CAMPAIGNS, data, callback);
			_client.sendRequest(sc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
