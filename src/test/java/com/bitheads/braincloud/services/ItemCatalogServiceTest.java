package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.junit.Ignore;
import org.junit.Test;

public class ItemCatalogServiceTest extends TestFixtureBase {

    @Test
    public void getCatalogItemDefinition() throws Exception {
        
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getItemCatalogService().getCatalogItemDefinition(
                "sword001",
                tr);
        tr.Run();
    }

    @Test
    public void getCatalogItemsPage() throws Exception {
        String context = "{\"test\": \"Testing\"}";
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getItemCatalogService().getCatalogItemsPage(
                context,
                tr);
        tr.Run();
    }

    @Test
    public void getCatalogItemsPageOffset() throws Exception {
        String context = "eyJzZWFyY2hDcml0ZXJpYSI6eyJnYW1lSWQiOiIyMDAwMSJ9LCJzb3J0Q3JpdGVyaWEiOnt9LCJwYWdpbmF0aW9uIjp7InJvd3NQZXJQYWdlIjoxMDAsInBhZ2VOdW1iZXIiOm51bGx9LCJvcHRpb25zIjpudWxsfQ";
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getItemCatalogService().getCatalogItemsPageOffset(
                context,
                1,
                tr);
        tr.Run();
    }

}