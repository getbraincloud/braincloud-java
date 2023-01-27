package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.junit.Test;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by David St-Louis on 18-09-17.
 */
public class AppStoreServiceTest extends TestFixtureBase
{
    @Test
    public void testVerifyPurchase() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getAppStoreService().verifyPurchase("_invalid_store_id_", "{}", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.INVALID_STORE_ID);
    }

    @Test
    public void testGetEligiblePromotions() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getAppStoreService().getEligiblePromotions(tr);
        tr.Run();
    }

    @Test
    public void testGetSalesInventory() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getAppStoreService().getSalesInventory("_invalid_store_id_", "_invalid_user_currency_", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.INVALID_STORE_ID);
    }

    @Test
    public void testGetSalesInventoryByCategory() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getAppStoreService().getSalesInventoryByCategory("_invalid_store_id_", "_invalid_user_currency_", "_invalid_category_", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.INVALID_STORE_ID);
    }

    @Test
    public void testStartPurchase() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getAppStoreService().startPurchase("_invalid_store_id_", "{}", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.INVALID_STORE_ID);
    }

    @Test
    public void testFinalizePurchase() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getAppStoreService().finalizePurchase("_invalid_store_id_", "_invalid_transaction_id_", "{}", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.INVALID_STORE_ID);
    }

    @Test
    public void testRefreshPromotions() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getAppStoreService().refreshPromotions(tr);
        tr.Run();
    }
}
