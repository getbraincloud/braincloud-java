package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class VirtualCurrencyService {

    private enum Parameter {
        vcId,
        vcAmount,
        levelName,
        peerCode
    }

    private BrainCloudClient _client;

    public VirtualCurrencyService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Retrieve the user's currency account. Optional parameters: vcId (if retrieving all currencies).
     *
     * Service Name - VirtualCurrency
     * Service Operation - GetCurrency
     *
     * @param vcId  The currency type to retrieve or null if all currency types are being requested.
     * @param callback The method to be invoked when the server response is received
     */
    public void getCurrency(String vcId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.vcId.name(), vcId);

            ServerCall sc = new ServerCall(ServiceName.virtualCurrency, ServiceOperation.GET_PLAYER_VC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve the parent user's currency account. Optional parameters: vcId (if retrieving all currencies).
     *
     * Service Name - VirtualCurrency
     * Service Operation - GetParentCurrency
     *
     * @param vcId  The currency type to retrieve or null if all currency types are being requested.
     * @param levelName Name of the parent level.
     * @param callback The method to be invoked when the server response is received
    */
    public void getParentCurrency(String vcId, String levelName, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.vcId.name(), vcId);
            data.put(Parameter.levelName.name(), levelName);

            ServerCall sc = new ServerCall(ServiceName.virtualCurrency, ServiceOperation.GET_PARENT_VC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve the peer user's currency account. Optional parameters: vcId (if retrieving all currencies).
     *
     * Service Name - VirtualCurrency
     * Service Operation - GetPeerCurrency
     *
     * @param vcId  The currency type to retrieve or null if all currency types are being requested.
     * @param peerCode  Name of the peer
     * @param callback The method to be invoked when the server response is received
    */
    public void getPeerCurrency(String vcId, String peerCode, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.vcId.name(), vcId);
            data.put(Parameter.peerCode.name(), peerCode);

            ServerCall sc = new ServerCall(ServiceName.virtualCurrency, ServiceOperation.GET_PEER_VC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
    
    /**
     * Resets the player's currency to 0.
     *
     * Service Name - VirtualCurrency
     * Service Operation - ResetCurrency
     * 
     * @param callback The method to be invoked when the server response is received
     */
    public void resetCurrency(IServerCallback callback) {
            JSONObject data = new JSONObject();
            ServerCall sc = new ServerCall(ServiceName.virtualCurrency, ServiceOperation.RESET_PLAYER_VC, data, callback);
            _client.sendRequest(sc);
    }

    /**
     * Award player the passed-in amount of currency. Returns an object representing the new currency values.
     *
     * Note: Awarding 0 or negative currency will return an error. Use ConsumeCurrency to remove currency values.
     *
     * Service Name - VirtualCurrency
     * Service Operation - GetPeerCurrency
     *
     * Warning, Method is recommended to be used in Cloud Code only for security
     * If you need to use it client side, enable 'Allow Currency Calls from Client' on the brainCloud dashboard
     *
     * @param vcId  The currency type to award.
     * @param vcAmount  The amount of currency to award.
     * @param callback The method to be invoked when the server response is received
     */
    public void awardCurrency(String vcId, int vcAmount, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.vcId.name(), vcId);
            data.put(Parameter.vcAmount.name(), vcAmount);

            ServerCall sc = new ServerCall(ServiceName.virtualCurrency, ServiceOperation.AWARD_VC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Consume the passed-in amount of currency from the player.
     *
     * Note: Consuming 0 or negative currency will return an error. Use AwardCurrency to add currency values.
     *
     * Service Name - VirtualCurrency
     * Service Operation - GetPeerCurrency
     *
     * Warning, Method is recommended to be used in Cloud Code only for security
     * If you need to use it client side, enable 'Allow Currency Calls from Client' on the brainCloud dashboard
     *
     * @param vcId  The currency type to consume.
     * @param vcAmount  The amount of currency to consume.
     * @param callback The method to be invoked when the server response is received
     */
    public void consumeCurrency(String vcId, int vcAmount, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.vcId.name(), vcId);
            data.put(Parameter.vcAmount.name(), vcAmount);

            ServerCall sc = new ServerCall(ServiceName.virtualCurrency, ServiceOperation.CONSUME_VC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
