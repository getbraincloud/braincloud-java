package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class BlockchainService {

    private enum Parameter{
        integrationId,
        contextJson
    }

    private BrainCloudClient _client;

    public BlockchainService(BrainCloudClient client){
        _client = client;
    }

    /**
     * Retrieves the blockchain items owned by the caller.
     */
    public void GetBlockchainItems(String in_integrationID,
                                   String in_contextJson,
                                   IServerCallback callback){
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.integrationId.name(), in_integrationID);

            JSONObject jsonData = new JSONObject(in_contextJson);
            data.put(Parameter.contextJson.name(), in_contextJson);

            ServerCall serverCall = new ServerCall(ServiceName.blockchain,
                    ServiceOperation.GET_BLOCKCHAIN_ITEMS, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the uniqs owned by the caller.
     */
    public void GetUniqs(String in_integrationID,
                         String in_contextJson,
                         IServerCallback callback){
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.integrationId.name(), in_integrationID);

            JSONObject jsonData = new JSONObject(in_contextJson);
            data.put(Parameter.contextJson.name(), in_contextJson);

            ServerCall serverCall = new ServerCall(ServiceName.blockchain,
                    ServiceOperation.GET_UNIQS, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}