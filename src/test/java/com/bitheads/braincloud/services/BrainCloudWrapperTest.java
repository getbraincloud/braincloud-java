package com.bitheads.braincloud.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.bitheads.braincloud.client.BrainCloudWrapper;
import com.bitheads.braincloud.client.IAutoReconnectCallback;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.client.StatusCodes;

public class BrainCloudWrapperTest extends TestFixtureNoAuth {
    
    @Test
    public void canReconnectTrue(){
        TestResult tr = new TestResult(_wrapper);

        _wrapper.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);

        // Authenticate
        _wrapper.authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        // Log out
        _wrapper.logout(false, tr);
        tr.Run();

        // Check canReconnect()
        assertEquals(true, _wrapper.canReconnect());
    }

    @Test
    public void canReconnectFalse(){
        TestResult tr = new TestResult(_wrapper);

        _wrapper.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);

        // Authenticate
        _wrapper.authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        // Log out
        _wrapper.logout(true, tr);
        tr.Run();

        // Check canReconnect()
        assertEquals(false, _wrapper.canReconnect());
    }
    
    @Test
    public void reconnectExpectSuccess(){
        TestResult tr = new TestResult(_wrapper);

        _wrapper.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);

        // Authenticate
        _wrapper.authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        // Log out
        _wrapper.logout(false, tr);
        tr.Run();

        // Check canReconnect()
        if(_wrapper.canReconnect()){
            _wrapper.reconnect(tr);
            tr.Run();
        }
        else fail("canReconnect returned false but should have been true");
    }

    @Test
    public void reconnectExpectFail(){
        TestResult tr = new TestResult(_wrapper);

        _wrapper.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);

        // Authenticate
        _wrapper.authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        // Log out
        _wrapper.logout(true, tr);
        tr.Run();

        // Check canReconnect()
        if (_wrapper.canReconnect()) {
            fail("canReconnect returned true but should have been false");
        } 
        else {
            System.out.println("Attempting to reconnect to confirm that it shouldn't be possible. . .");
            _wrapper.reconnect(tr);
            tr.RunExpectFail(202, 40208);
        }
    }

    @Test
    public void logOutForgetUser(){
        TestResult tr = new TestResult(_wrapper);

        _wrapper.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);

        // Authenticate
        _wrapper.authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        // Log out
        _wrapper.logout(true, tr);
        tr.Run();

        // Check stored profile ID
        System.out.println("Verifying that the STORED PROFILE ID was cleared (i.e. forgotten)");
        assertEquals("", _wrapper.getStoredProfileId());
    }

    @Test
    public void logOutRememberUser(){
        TestResult tr = new TestResult(_wrapper);

        _wrapper.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);

        // Authenticate
        _wrapper.authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        String profileID = _wrapper.getStoredProfileId();
        System.out.println("STORED PROFILE ID is: " + profileID);
        assertNotNull(profileID);
        assertNotEquals("", profileID);

        // Log out
        _wrapper.logout(false, tr);
        tr.Run();

        // Check stored profile ID
        System.out.println("Verifying that the STORED PROFILE ID was saved (i.e. not forgotten)");
        assertEquals(profileID, _wrapper.getStoredProfileId());
    }

    @Test
    public void ReasonCodeAccess(){        
        int reasonCode = ReasonCodes.MERGE_PROFILES;
        int expectedValue = 40212;

        System.out.println("Reason Code MERGE_PROFILES = " + reasonCode);

        Assert.assertEquals(expectedValue, reasonCode);
    }

    @Test
    public void ServiceNameAccess(){
        String serviceName = ServiceName.appStore.name();
        String expectedValue = "appStore";

        System.out.println("Service Name appStore = " + serviceName);

        Assert.assertEquals(expectedValue, serviceName);
    }

    @Test
    public void ServiceOperationAccess(){
        String serviceOperation = ServiceOperation.ABANDON.name();
        String expectedValue = "ABANDON";

        System.out.println("Service Operation ABANDON = " + serviceOperation);

        Assert.assertEquals(expectedValue, serviceOperation);
    }

    @Test
    public void StatusCodeAccess(){
        int statusCode = StatusCodes.CLIENT_NETWORK_ERROR;
        int expectedValue = 900;

        System.out.println("Status Code CLIENT_NETWORK_ERROR = " + statusCode);

        Assert.assertEquals(expectedValue, statusCode);
    }

    @Test
    public void AutoReconnectEnabled(){
        IAutoReconnectCallback autoReconnectCallback = new IAutoReconnectCallback() {

            @Override
            public void autoReconnectCallbackSuccess(JSONObject jsonData) {

                System.out.println("Auto reconnect SUCCESS");
            }

            @Override
            public void autoReconnectCallbackFailure(JSONObject jsonData) {

                System.out.println("Auto reconnect FAILURE");
            }

        };

        BrainCloudWrapper userWrapper = new BrainCloudWrapper();
        m_secretMap = new HashMap<String, String>();
        m_secretMap.put(m_appId, m_secret);
        m_secretMap.put(m_childAppId, m_childSecret);
        userWrapper.getClient().initializeWithApps(m_serverUrl, m_appId, m_secretMap, m_appVersion);
        userWrapper.getClient().enableLogging(true);

        TestResult userTr = new TestResult(userWrapper);

        userWrapper.authenticateUniversal("primaryUser", "primaryUser", true, userTr);
        userTr.Run();

        _wrapper.getClient().enableLogging(true);
        _wrapper.enableAutoReconnect(true);

        _wrapper.getClient().registerAutoReconnectCallback(autoReconnectCallback);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.authenticateUniversal("secondaryUser", "secondaryUser", true, tr);
        tr.Run();

        // Save Profile and Session IDs so that the session can be ended with a Cloud Code Script
        JSONObject responseData = tr.m_response.optJSONObject("data");
        if(responseData == null){
            fail("Failed to get response data");
        }
        
        String profileId = responseData.optString("profileId");
        String sessionId = responseData.optString("sessionId");

        assertFalse( "profileId empty", profileId.isEmpty());
        assertFalse("sessionId empty", sessionId.isEmpty());

        JSONObject profileSessionObj = new JSONObject();
        profileSessionObj.put("profileId", profileId);
        profileSessionObj.put("sessionId", sessionId);
        String jsonScriptData = profileSessionObj.toString();

        // Verify session is active
        _wrapper.getIdentityService().getIdentities(tr);
        tr.Run();

        // End session via script
        userWrapper.getScriptService().runScript("LogoutSession", jsonScriptData, userTr);
        userTr.Run();

        // Verify session retries via auto reconnect (if auto reconnect isn't enabled, this should fail)
        _wrapper.getIdentityService().getIdentities(tr);
        tr.Run();
    }
}
