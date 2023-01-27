package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class BlockchainServiceTest extends TestFixtureBase {
    private String _defaultIntegrationId = "default";
    private String _defaultContextJson = "{}";
     
    /*
     * FL: While writing this test, for some reason Setup() didn't authenticate a user for us to test with
     * So I added the authenticate and logout for these tests.
     * Also, I don't know how to add this test suite to the filters list for client master tool.
     */

    @Test
    public void testGetBlockchainItems() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);

        _wrapper.getClient().getBlockchainService().GetBlockchainItems(_defaultIntegrationId, _defaultContextJson, tr);
        tr.Run();

        _wrapper.getPlayerStateService().logout(tr);
    }

    @Test
    public void testGetUniqs() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);

        _wrapper.getClient().getBlockchainService().GetUniqs(_defaultIntegrationId, _defaultContextJson, tr);
        tr.Run();
        _wrapper.getPlayerStateService().logout(tr);
    }
    
}
