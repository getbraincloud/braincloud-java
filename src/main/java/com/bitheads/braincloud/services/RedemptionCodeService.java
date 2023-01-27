package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prestonjennings on 2015-10-27.
 */
public class RedemptionCodeService {

    private enum Parameter {
        scanCode,
        codeType,
        customRedemptionInfo
    }

    private BrainCloudClient _client;

    public RedemptionCodeService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Redeem a code.
     *
     * Service Name - RedemptionCode
     * Service Operation - REDEEM_CODE
     *
     * @param scanCode The code to redeem
     * @param codeType The type of code
     * @param jsonCustomRedemptionInfo Optional - A JSON string containing custom redemption data
     * @param callback The method to be invoked when the server response is received
     */
    public void redeemCode(String scanCode, String codeType, String jsonCustomRedemptionInfo, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scanCode.name(), scanCode);
            data.put(Parameter.codeType.name(), codeType);
            if (StringUtil.IsOptionalParameterValid(jsonCustomRedemptionInfo)) {
                JSONObject infoObj = new JSONObject(jsonCustomRedemptionInfo);
                data.put(Parameter.customRedemptionInfo.name(), infoObj);
            }

            ServerCall sc = new ServerCall(ServiceName.redemptionCode, ServiceOperation.REDEEM_CODE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve the codes already redeemed by player.
     *
     * Service Name - RedemptionCode
     * Service Operation - GET_REDEEMED_CODES
     *
     * @param codeType Optional - The type of codes to retrieve. Returns all codes if left unspecified.
     * @param callback The method to be invoked when the server response is received
     */
    public void getRedeemedCodes(String codeType, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            if (StringUtil.IsOptionalParameterValid(codeType)) {
                data.put(Parameter.codeType.name(), codeType);
            }

            ServerCall sc = new ServerCall(ServiceName.redemptionCode, ServiceOperation.GET_REDEEMED_CODES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
