package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;

import org.junit.After;
import org.junit.Test;

import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;

/**
 * Created by bradleyh on 1/9/2017.
 */

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
