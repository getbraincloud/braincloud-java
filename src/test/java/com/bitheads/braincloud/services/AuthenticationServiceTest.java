package com.bitheads.braincloud.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.bitheads.braincloud.client.AuthenticationIds;
import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

/**
 * Created by prestonjennings on 15-08-31.
 */
public class AuthenticationServiceTest extends TestFixtureNoAuth {

    long mostRecentPacket = -1000000;
    long secondMostRecentPacket = -1000000;

    @Test
    public void testAuthManualRedirect() throws Exception {
        _client.initialize(m_serverUrl, m_redirectAppId, m_secret, m_appVersion);

        TestResult tr = new TestResult(_wrapper);
        String anonId = _client.getAuthenticationService().generateAnonymousId();
        
        _client.getAuthenticationService().authenticateAnonymous(anonId, true, tr);
        tr.RunExpectFail(202, ReasonCodes.MANUAL_REDIRECT /* 40308 */);
    }

    @Test
    public void testAuthenticateAnonymous() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String anonId = _client.getAuthenticationService().generateAnonymousId();

        _client.getAuthenticationService().authenticateAnonymous(anonId, true, tr);
        tr.Run();
    }

    @Test
    public void testAuthenticateUniversalInstance() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(Users.UserA).id,
                getUser(Users.UserA).password, 
                true, 
                tr);
        tr.Run();
    }

    @Test
    public void testAuthenticateAdvanced() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        // Call it directly on the wrapper. This way we test both the wrapper and the
        // service
        _wrapper.getClient().getAuthenticationService().authenticateAdvanced(
                AuthenticationType.Universal,
                new AuthenticationIds("authAdvancedUser", "authAdvancedPass"),
                true,
                "{\"AnswerToEverything\":42}",
                tr);
        tr.Run();
    }

    @Test
    public void testAuthenticateEmailPassword() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateEmailPassword(
                getUser(Users.UserA).email,
                getUser(Users.UserA).password,
                true,
                tr);
        tr.Run();
    }

    @Test
    public void testAuthenticateHandoff() throws Exception {
        String handoffId;
        String handoffToken;
        TestResult tr = new TestResult(_wrapper);
        String anonId = _client.getAuthenticationService().generateAnonymousId();

        _client.getAuthenticationService().authenticateAnonymous(anonId, true, tr);
        tr.Run();

        _client.getScriptService().runScript("createHandoffId",
                Helpers.createJsonPair("", ""),
                tr);
        tr.Run();
        
        handoffId = tr.m_response.getJSONObject("data").getJSONObject("response").getString("handoffId");
        handoffToken = tr.m_response.getJSONObject("data").getJSONObject("response").getString("securityToken");

        _client.getAuthenticationService().authenticateHandoff(handoffId, handoffToken, tr);
        tr.Run();
    }

    @Test
    public void testAuthenticateSettopHandoff() throws Exception {
        String handoffCode;
        TestResult tr = new TestResult(_wrapper);
        String anonId = _client.getAuthenticationService().generateAnonymousId();

        _client.getAuthenticationService().authenticateAnonymous(anonId, true, tr);
        tr.Run();

        _client.getScriptService().runScript(
                "CreateSettopHandoffCode",
                Helpers.createJsonPair("", ""),
                tr);
        tr.Run();
        
        handoffCode = tr.m_response.getJSONObject("data").getJSONObject("response").getString("handoffCode");

        _client.getAuthenticationService().authenticateSettopHandoff(handoffCode, tr);
        tr.Run();
    }
    
    @Test
    public void testAuthenticateUniversal() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal("abc", "abc", true, tr);
        tr.Run();
    }

    @Test
    public void testResetEmailPassword() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().resetEmailPassword(emailAddress, tr);
        tr.Run();
    }

    @Test
    public void testResetEmailPasswordAdvanced() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        String serviceParams = "{\"fromAddress\": \"fromAddress\",\"fromName\": \"fromName\",\"replyToAddress\": \"replyToAddress\",\"replyToName\": \"replyToName\", \"templateId\": \"8f14c77d-61f4-4966-ab6d-0bee8b13d090\",\"subject\": \"subject\",\"body\": \"Body goes here\", \"substitutions\": { \":name\": \"John Doe\",\":resetLink\": \"www.dummuyLink.io\"}, \"categories\": [\"category1\",\"category2\" ]}";
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().resetEmailPasswordAdvanced(
                emailAddress,
                serviceParams,
                tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.INVALID_FROM_ADDRESS);
    }

    @Test
    public void testResetEmailPasswordWithExpiry() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal("abc", "abc", true, tr);
        tr.Run();

        _wrapper.getClient().getAuthenticationService().resetEmailPasswordWithExpiry(emailAddress, 1, tr);
        tr.Run();
    }

    @Test
    public void testResetEmailPasswordAdvancedWithExpiry() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        String serviceParams = "{\"fromAddress\": \"fromAddress\",\"fromName\": \"fromName\",\"replyToAddress\": \"replyToAddress\",\"replyToName\": \"replyToName\", \"templateId\": \"8f14c77d-61f4-4966-ab6d-0bee8b13d090\",\"subject\": \"subject\",\"body\": \"Body goes here\", \"substitutions\": { \":name\": \"John Doe\",\":resetLink\": \"www.dummuyLink.io\"}, \"categories\": [\"category1\",\"category2\" ]}";
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal("abc", "abc", true, tr);
        tr.Run();

        _wrapper.getClient().getAuthenticationService().resetEmailPasswordAdvancedWithExpiry(
                emailAddress,
                serviceParams,
                1,
                tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.INVALID_FROM_ADDRESS);
    }

    @Test
    public void testResetUniversalIdPassword() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(Users.UserB).id,
                getUser(Users.UserB).password,
                true,
                tr);
        tr.Run();

        _wrapper.getClient().getPlayerStateService().updateContactEmail(
                emailAddress,
                tr);
        tr.Run();

        _wrapper.getClient().getAuthenticationService().resetUniversalIdPassword(
                getUser(Users.UserB).id,
                tr);
        tr.Run();
    }

    @Test
    public void testResetUniversalIdPasswordAdvanced() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        String serviceParams = "{\"templateId\": \"8f14c77d-61f4-4966-ab6d-0bee8b13d090\", \"substitutions\": { \":name\": \"John Doe\",\":resetLink\": \"www.dummuyLink.io\"}, \"categories\": [\"category1\",\"category2\" ]}";
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(Users.UserB).id,
                getUser(Users.UserB).password,
                true,
                tr);
        tr.Run();

        _wrapper.getClient().getPlayerStateService().updateContactEmail(
                emailAddress,
                tr);
        tr.Run();

        _wrapper.getClient().getAuthenticationService().resetUniversalIdPasswordAdvanced(
                getUser(Users.UserB).id,
                serviceParams,
                tr);
        tr.Run();
    }

    @Test
    public void testResetUniversalIdPasswordWithExpiry() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(Users.UserB).id,
                getUser(Users.UserB).password,
                true,
                tr);
        tr.Run();

        _wrapper.getClient().getPlayerStateService().updateContactEmail(emailAddress, tr);
        tr.Run();

        _wrapper.getClient().getAuthenticationService().resetUniversalIdPasswordWithExpiry(
                getUser(Users.UserB).id,
                1,
                tr);
        tr.Run();
    }

    @Test
    public void testResetUniversalIdPasswordAdvancedWithExpiry() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        String serviceParams = "{\"templateId\": \"8f14c77d-61f4-4966-ab6d-0bee8b13d090\", \"substitutions\": { \":name\": \"John Doe\",\":resetLink\": \"www.dummuyLink.io\"}, \"categories\": [\"category1\",\"category2\" ]}";
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(Users.UserB).id,
                getUser(Users.UserB).password,
                true,
                tr);
        tr.Run();

        _wrapper.getClient().getPlayerStateService().updateContactEmail(
                emailAddress,
                tr);
        tr.Run();

        _wrapper.getClient().getAuthenticationService().resetUniversalIdPasswordAdvancedWithExpiry(
                getUser(Users.UserB).id,
                serviceParams,
                1,
                tr);
        tr.Run();
    }

    @Test
    public void testBadSig() throws Exception {
        // our problem is that users who refresh their app secret via the portal, the
        // client would fail to read the response, and would retry infinitely.
        // This threatens our servers, because huge numbers of errors related to bad
        // signature show up, and infinitely retry to get out of this error.
        // Instead of updating the signature via the portal, we will mimic a bad
        // signature from the client.
        Map<String, String> originalAppSecretMap = new HashMap<String, String>();
        originalAppSecretMap.put(m_appId, m_secret);
        originalAppSecretMap.put(m_childAppId, m_childSecret);
        int numRepeatBadSigFailures = 0;

        // mess up app
        Map<String, String> updatedAppSecretMap = new HashMap<String, String>();
        ;

        for (Map.Entry<String, String> entry : originalAppSecretMap.entrySet()) {
            updatedAppSecretMap.put(entry.getKey(), entry.getValue() + "123");
        }
        ///////////////////// Phase 1
        // first auth
        TestResult tr1 = new TestResult(_wrapper);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserB).id,
                getUser(Users.UserB).password, true, tr1);
        if (tr1.Run()) {
            // Check the packet coming in and compare it to the last recevied packet. if
            // they're both -1, we may be in a repeating scenario.
            if (mostRecentPacket == -1000000 && secondMostRecentPacket == -1000000) {
                mostRecentPacket = _wrapper.getClient().getRestClient().getLastReceivedPacketId();
            } else {
                secondMostRecentPacket = mostRecentPacket;
                mostRecentPacket = _wrapper.getClient().getRestClient().getLastReceivedPacketId();
            }

            // Is there the sign of a repeat?
            if (mostRecentPacket == -1 && secondMostRecentPacket == -1) {
                numRepeatBadSigFailures++;
            }
            // we shouldnt expect more than 2 times that most recent and second most recent
            // are both bad sig errors for this test, else its repeating itself.
            if (numRepeatBadSigFailures > 2)
                throw new Exception("Repeating Bad sig errors");
        }

        // check state
        _wrapper.getClient().getPlayerStateService().readUserState(tr1);

        ////////////////////////// Phase 2
        TestResult tr3 = new TestResult(_wrapper);
        _wrapper.getClient().initializeWithApps(m_serverUrl, m_appId, updatedAppSecretMap, m_appVersion);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserB).id,
                getUser(Users.UserB).password, true, tr3);
        if (tr3.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.BAD_SIGNATURE)) {
            // Check the packet coming in and compare it to the last recevied packet. if
            // they're both -1, we may be in a repeating scenario.
            if (mostRecentPacket == -1000000 && secondMostRecentPacket == -1000000) {
                mostRecentPacket = _wrapper.getClient().getRestClient().getLastReceivedPacketId();
            } else {
                secondMostRecentPacket = mostRecentPacket;
                mostRecentPacket = _wrapper.getClient().getRestClient().getLastReceivedPacketId();
            }

            // Is there the sign of a repeat?
            if (mostRecentPacket == -1 && secondMostRecentPacket == -1) {
                numRepeatBadSigFailures++;
            }
            // we shouldnt expect more than 2 times that most recent and second most recent
            // are both bad sig errors for this test, else its repeating itself.
            if (numRepeatBadSigFailures > 2)
                throw new Exception("Repeating Bad sig errors");
        }

        // check state
        _wrapper.getClient().getPlayerStateService().readUserState(tr3);

        // wait a while
        Thread.sleep(5 * 1000);

        ///////////////////// Phase 3
        TestResult tr5 = new TestResult(_wrapper);
        _wrapper.getClient().initializeWithApps(m_serverUrl, m_appId, originalAppSecretMap, m_appVersion);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserB).id,
                getUser(Users.UserB).password, true, tr5);
        if (tr5.Run()) {
            // Check the packet coming in and compare it to the last recevied packet. if
            // they're both -1, we may be in a repeating scenario.
            if (mostRecentPacket == -1000000 && secondMostRecentPacket == -1000000) {
                mostRecentPacket = _wrapper.getClient().getRestClient().getLastReceivedPacketId();
            } else {
                secondMostRecentPacket = mostRecentPacket;
                mostRecentPacket = _wrapper.getClient().getRestClient().getLastReceivedPacketId();
            }

            // Is there the sign of a repeat?
            if (mostRecentPacket == -1 && secondMostRecentPacket == -1) {
                numRepeatBadSigFailures++;
            }
            // we shouldnt expect more than 2 times that most recent and second most recent
            // are both bad sig errors for this test, else its repeating itself.
            if (numRepeatBadSigFailures > 2)
                throw new Exception("Repeating Bad sig errors");
        }

        // check state
        _wrapper.getClient().getPlayerStateService().readUserState(tr5);
    }

    @Test
    public void testReInit() throws Exception {
        Map<String, String> originalAppSecretMap = new HashMap<String, String>();
        originalAppSecretMap.put(m_appId, m_secret);
        originalAppSecretMap.put(m_childAppId, m_childSecret);

        int initCounter = 1;

        // case 1 Multiple init on client
        _wrapper.getClient().initializeWithApps(m_serverUrl, m_appId, originalAppSecretMap, m_appVersion);
        Assert.assertTrue(initCounter == 1);
        initCounter++;

        _wrapper.getClient().initializeWithApps(m_serverUrl, m_appId, originalAppSecretMap, m_appVersion);
        Assert.assertTrue(initCounter == 2);
        initCounter++;

        _wrapper.getClient().initializeWithApps(m_serverUrl, m_appId, originalAppSecretMap, m_appVersion);
        Assert.assertTrue(initCounter == 3);

        // case 2

        // Auth
        TestResult tr1 = new TestResult(_wrapper);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserB).id,
                getUser(Users.UserB).password, true, tr1);
        tr1.Run();

        // Call
        TestResult tr2 = new TestResult(_wrapper);
        _wrapper.getTimeService().readServerTime(
                tr2);
        tr2.Run();

        // reinit
        _wrapper.getClient().initializeWithApps(m_serverUrl, m_appId, originalAppSecretMap, m_appVersion);

        // //call without auth - expecting it to fail because we need to reauth after
        // init
        TestResult tr3 = new TestResult(_wrapper);
        _wrapper.getTimeService().readServerTime(
                tr3);
        tr3.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.NO_SESSION);

    }

    @Test
    public void testAuthenticateUltra() throws Exception {
        if (!getServerUrl().contains("api-internal.braincloudservers.com") &&
                !getServerUrl().contains("internala.braincloudservers.com") &&
                !getServerUrl().contains("api.internalg.braincloudservers.com")/*
                                                                                * &&
                                                                                * !getServerUrl().contains(
                                                                                * "api.ultracloud.ultra.io")
                                                                                */) {
            return;
        }

        TestResult tr = new TestResult(_wrapper);

        // Auth universal
        _wrapper.getClient().getAuthenticationService().authenticateEmailPassword(
                getUser(Users.UserA).email,
                getUser(Users.UserA).password,
                true, tr);
        tr.Run();

        // Run a cloud script to grab the ultra's JWT token
        _wrapper.getClient().getScriptService().runScript("getUltraToken", "{}", tr);
        tr.Run();
        String id_token = tr.m_response.getJSONObject("data").getJSONObject("response").getJSONObject("data")
                .getJSONObject("json").getString("id_token");

        // Logout
        _wrapper.getClient().getPlayerStateService().logout(tr);
        tr.Run();

        // Auth ultra
        _wrapper.getClient().getAuthenticationService().authenticateUltra("braincloud1", id_token, true, tr);
        tr.Run();
    }

    @Test
    public void testSmartSwitchAuthenticateEmailFromAnonAuth() throws Exception {
        // get anon auth
        TestResult tr = new TestResult(_wrapper);
        String anonId = _client.getAuthenticationService().generateAnonymousId();
        _client.getAuthenticationService().authenticateAnonymous(anonId, true, tr);

        tr.Run();

        TestResult tr2 = new TestResult(_wrapper);
        _wrapper.smartSwitchAuthenticateEmail(anonId, "12345", true, tr2);
        tr2.Run();
    }

    @Test
    public void testSmartSwitchAuthenticateUniversalFromAnon() throws Exception {
        // get anon auth
        TestResult tr = new TestResult(_wrapper);
        String anonId = _client.getAuthenticationService().generateAnonymousId();
        _client.getAuthenticationService().authenticateAnonymous(anonId, true, tr);

        tr.Run();

        TestResult tr2 = new TestResult(_wrapper);
        _wrapper.smartSwitchAuthenticateUniversal(anonId, "12345", true, tr2);
        tr2.Run();
    }

    @Test
    public void testSmartSwitchAuthenticateEmailFromUniversal() throws Exception {
        String emailAddress = getUser(Users.UserA).email;
        // get anon auth
        TestResult tr = new TestResult(_wrapper);

        _client.getAuthenticationService().authenticateUniversal(emailAddress, "12345", true, tr);

        tr.Run();

        TestResult tr2 = new TestResult(_wrapper);
        _wrapper.smartSwitchAuthenticateEmail(emailAddress, "12345", true, tr2);
        tr2.Run();
    }
}
