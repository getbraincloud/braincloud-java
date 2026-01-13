package com.bitheads.braincloud.services;

import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

import com.bitheads.braincloud.client.ReasonCodes;

public class UserItemsServiceTest extends TestFixtureBase {

    @Test
    public void awardUserItem() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().awardUserItem(
                "sword001",
                5,
                true,
                tr);
        tr.Run();
    }

    @Test
    public void awardUserItemWithOptions() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().awardUserItemWithOptions(
                "sword001",
                5,
                true,
                "{\"blockIfExceedItemMaxStackable\": true}",
                tr);
        tr.Run();
    }

    @Test
    public void dropUserItem() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().dropUserItem(
                "invalidForNow",
                1,
                true,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void getItemPromotionDetails() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String shopId = "";
        String defId = "sword001";
        boolean includeDef = true;
        boolean includePromotionDetails = true;

        _wrapper.getUserItemsService().getItemPromotionDetails(defId, shopId, includeDef, includePromotionDetails, tr);

        tr.Run();
    }

    @Test
    public void getItemsOnPromotion() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String shopId = "";
        boolean includeDef = true;
        boolean includePromotionDetails = true;

        _wrapper.getUserItemsService().getItemsOnPromotion(shopId, includeDef, includePromotionDetails,
                "{\"blockIfExceedItemMaxStackable\": true}", tr);

        tr.Run();
    }

    @Test
    public void getUserItemsPage() throws Exception {
        String context = "{\"test\": \"Testing\"}";
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().getUserItemsPage(
                context,
                true,
                tr);
        tr.Run();
    }

    @Test
    public void getUserItemsPageOffset() throws Exception {
        String context = "eyJzZWFyY2hDcml0ZXJpYSI6eyJnYW1lSWQiOiIyMDAwMSIsInBsYXllcklkIjoiNmVhYWU4M2EtYjZkMy00NTM5LWExZjAtZTIxMmMzYjUzMGIwIiwiZ2lmdGVkVG8iOm51bGx9LCJzb3J0Q3JpdGVyaWEiOnt9LCJwYWdpbmF0aW9uIjp7InJvd3NQZXJQYWdlIjoxMDAsInBhZ2VOdW1iZXIiOm51bGx9LCJvcHRpb25zIjpudWxsfQ";
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().getUserItemsPageOffset(
                context,
                1,
                true,
                tr);
        tr.Run();
    }

    @Test
    public void GetUserItem() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().getUserItem(
                "invalidForNow",
                true,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void giveUserItemTo() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().giveUserItemTo(
            getUser(Users.UserB).id, "invalidForNow", 1, 1, true,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void openBundle() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String bundleItemId = "equipmentBundle";
        int quantity = 1;
        boolean includeDef = true;

        _wrapper.getUserItemsService().awardUserItem(bundleItemId, quantity, includeDef, tr);
        tr.Run();

        JSONObject items = tr.m_response.optJSONObject("data").optJSONObject("items");
        System.out.println("Items: " + items.toString());

        assertTrue(items.length() > 0);

        JSONObject item = items.optJSONObject(items.keys().next());
        String itemId = item.optString("itemId");
        int version = -1;
        String optionsJson = "{}";

        _wrapper.getUserItemsService().openBundle(itemId, version, quantity, includeDef, optionsJson, tr);
        tr.Run();
    }

    @Test
    public void purchaseUserItem() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().purchaseUserItem(
                "sword001",
                1,
                null,
                true,
                tr);
        tr.Run();
    }

    @Test
    public void purchaseUserItemWithOptions() throws Exception {

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().purchaseUserItemWithOptions(
                "sword001",
                1,
                null,
                true,
                "{\"blockIfExceedItemMaxStackable\": true}",
                tr);
        tr.Run();
    }

    @Test
    public void receiveUserItemFrom() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().receiveUserItemFrom(
            getUser(Users.UserB).id, "invalidForNow",
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void sellUserItem() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().sellUserItem(
                "invalidForNow",
                1,
                1,
                null,
                true,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void updateUserItemData() throws Exception {
        String newItemData = "{\"test\": \"Testing\"}";
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().updateUserItemData(
                "invalidForNow",
                1,
                newItemData,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void useUserItem() throws Exception {
        String newItemData = "{\"test\": \"Testing\"}";
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().useUserItem(
                "invalidForNow",
                1,
                newItemData,
                true,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void publishUserItemToBlockchain() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().publishUserItemToBlockchain(
                "invalidForNow",
                1,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }

    @Test
    public void refreshBlockchainUserItems() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().refreshBlockchainUserItems(
                tr);
        tr.Run();
    }

    @Test
    public void removeUserItemFromBlockchain() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getUserItemsService().removeUserItemFromBlockchain(
                "invalidForNow",
                1,
                tr);
        tr.RunExpectFail(400, ReasonCodes.ITEM_NOT_FOUND);
    }
}
