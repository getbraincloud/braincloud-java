// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class AppStoreService {

    private enum Parameter {
        storeId,
        receiptData,
        category,
        priceInfoCriteria,
        userCurrency,
        purchaseData,
        transactionId,
        transactionData,
        iapId,
        payload
    }

    private BrainCloudClient _client;

    public AppStoreService(BrainCloudClient client) {
        _client = client;
    }

    /**
        * Verifies that purchase was properly made at the store.
        *
        * Service Name - AppStore
        * Service Operation - VerifyPurchase
        *
        * @param storeId The store platform. Valid stores are:
        * - itunes
        * - facebook
        * - appworld
        * - steam
        * - windows
        * - windowsPhone
        * - googlePlay
        * @param receiptData the specific store data required
        * @param in_callback The method to be invoked when the server response is received
        */
    public void verifyPurchase(String storeId, String jsonReceiptData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.storeId.name(), storeId);
            data.put(Parameter.receiptData.name(), new JSONObject(jsonReceiptData));

            ServerCall sc = new ServerCall(ServiceName.appStore, ServiceOperation.VERIFY_PURCHASE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
        * Returns the eligible promotions for the player.
        *
        * Service Name - AppStore
        * Service Operation - EligiblePromotions
        *
        * @param in_callback The method to be invoked when the server response is received
        */
    public void getEligiblePromotions(IServerCallback callback) {
        JSONObject data = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.appStore, ServiceOperation.ELIGIBLE_PROMOTIONS, data, callback);
        _client.sendRequest(sc);
    }

    /**
        * Method gets the active sales inventory for the passed-in
        * currency type.
        *
        * Service Name - AppStore
        * Service Operation - GetInventory
        *
        * @param platform The store platform. Valid stores are:
        * - itunes
        * - facebook
        * - appworld
        * - steam
        * - windows
        * - windowsPhone
        * - googlePlay
        * @param userCurrency The currency type to retrieve the sales inventory for.
        * @param in_callback The method to be invoked when the server response is received
        */
    public void getSalesInventory(String storeId, String userCurrency, IServerCallback callback) {
        getSalesInventoryByCategory(storeId, userCurrency, null, callback);
    }

    /**
        * Method gets the active sales inventory for the passed-in
        * currency type.
        *
        * Service Name - AppStore
        * Service Operation - GetInventory
        *
        * @param storeId The store platform. Valid stores are:
        * - itunes
        * - facebook
        * - appworld
        * - steam
        * - windows
        * - windowsPhone
        * - googlePlay
        * @param userCurrency The currency type to retrieve the sales inventory for.
        * @param category The product category
        * @param in_callback The method to be invoked when the server response is received
        */
    public void getSalesInventoryByCategory(String storeId, String userCurrency, String category, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.storeId.name(), storeId);
            data.put(Parameter.category.name(), category);

            JSONObject priceInfoCriteria = new JSONObject();
            if (userCurrency != null)
            {
                priceInfoCriteria.put(Parameter.userCurrency.name(), userCurrency);
            }
            data.put(Parameter.priceInfoCriteria.name(), priceInfoCriteria);

            ServerCall sc = new ServerCall(ServiceName.appStore, ServiceOperation.GET_INVENTORY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
        * Start A Two Staged Purchase Transaction
        *
        * Service Name - AppStore
        * Service Operation - StartPurchase
        *
        * @param storeId The store platform. Valid stores are:
        * - itunes
        * - facebook
        * - appworld
        * - steam
        * - windows
        * - windowsPhone
        * - googlePlay
        * @param purchaseData specific data for purchasing 2 staged purchases
        * @param in_callback The method to be invoked when the server response is received
        */
    public void startPurchase(String storeId, String jsonPurchaseData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.storeId.name(), storeId);
            data.put(Parameter.purchaseData.name(), new JSONObject(jsonPurchaseData));

            ServerCall sc = new ServerCall(ServiceName.appStore, ServiceOperation.START_PURCHASE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /*
        * Before making a purchase with the IAP store, you will need to store the purchase
        * payload context on brainCloud so that the purchase can be verified for the proper IAP product.
        * This payload will be used during the VerifyPurchase method to ensure the
        * user properly paid for the correct product before awarding them the IAP product.
        *
        * Service Name - AppStore
        * Service Operation - CachePurchasePayloadContext
        *
        * @param storeId The store platform. Valid stores are:
        * - itunes
        * - facebook
        * - appworld
        * - steam
        * - windows
        * - windowsPhone
        * - googlePlay
        * @param transactionId the transactionId returned from start Purchase
        * @param transactionData specific data for purchasing 2 staged purchases
        * @param in_callback The method to be invoked when the server response is received
        */
    public void cachePurchasePayloadContext(String storeId, String iapId, String payload, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.storeId.name(), storeId);
            data.put(Parameter.iapId.name(), iapId);
            data.put(Parameter.payload.name(), payload);

            ServerCall sc = new ServerCall(ServiceName.appStore, ServiceOperation.CACHE_PURCHASE_PAYLOAD_CONTEXT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
        * Returns up-to-date eligible 'promotions' for the user and a 'promotionsRefreshed' flag indicating whether the user's promotion info required refreshing.
        *
        * Service Name - AppStore
        * Service Operation - RefreshPromotions
        */
    public void refreshPromotions(IServerCallback callback) {
        JSONObject data = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.appStore, ServiceOperation.REFRESH_PROMOTIONS, data, callback);
        _client.sendRequest(sc);
    }
}
