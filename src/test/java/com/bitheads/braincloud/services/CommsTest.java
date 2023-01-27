package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by prestonjennings on 15-10-05.
 */
public class CommsTest extends TestFixtureNoAuth
{
    /*
    @Test
    public void testDevBadAuthNoRetry() throws Exception
    {

        // this should always succeed but helps devs verify that only one auth packet gets sent

        TestResult tr = new TestResult(_wrapper);
        _wrapper.initialize("123", "123", "1.0.0", "http://localhost:5432");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal("abc", "123", true, tr);
        tr.RunExpectFail(-1, -1);

        _wrapper.resetCommunication();
    }

    @Test
    public void testDevBad503() throws Exception
    {
        // this test assumes you're running a server that returns 503
        TestResult tr = new TestResult(_wrapper);
        _wrapper.initialize("123", "123", "1.0.0", "http://localhost:5432");
        // don't authenticate as we want retries to happen
        _wrapper.getPlayerStateService().getAttributes(tr);
        tr.RunExpectFail(StatusCodes.CLIENT_NETWORK_ERROR, ReasonCodes.CLIENT_NETWORK_ERROR_TIMEOUT);

        _wrapper.resetCommunication();
    }
    */

    @Override
    public boolean shouldAuthenticate() {
        return false;
    }

    @Test
    public void testGlobalErrorHandler() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getClient().registerGlobalErrorCallback(tr);

        int globalErrorCount = 0;

        _wrapper.getTimeService().readServerTime(tr);
        tr.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.NO_SESSION);
        globalErrorCount += tr.m_globalErrorCount;

        _wrapper.getTimeService().readServerTime(tr);
        tr.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.NO_SESSION);
        globalErrorCount += tr.m_globalErrorCount;

        _wrapper.getClient().deregisterGlobalErrorCallback();
        Assert.assertEquals(2, globalErrorCount);

        _wrapper.getClient().resetCommunication();
    }

    @Test
    public void testNoSession() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTimeService().readServerTime(tr);
        tr.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.NO_SESSION);

        System.out.println(tr.m_statusMessage);

        _wrapper.getClient().resetCommunication();
    }

    @Test
    public void testSessionTimeout() throws Exception
    {
        // this test assumes you're running a server that returns 503
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        long prevSessionTimeout = _wrapper.getClient().getHeartbeatInterval();
        _wrapper.getClient().setHeartbeatInterval(prevSessionTimeout * 4);

        System.out.println("Waiting for session to timeout...");

        Thread.sleep(61 * 1000);

        _wrapper.getTimeService().readServerTime(tr);
        //tr.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.USER_SESSION_EXPIRED);
        tr.Run();

        _wrapper.getTimeService().readServerTime(tr);
        //tr.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.USER_SESSION_EXPIRED);
        tr.Run();

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        _wrapper.getPlayerStateService().logout(tr);
        tr.Run();

        _wrapper.getTimeService().readServerTime(tr);
        tr.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.NO_SESSION);

        _wrapper.getClient().resetCommunication();
        _wrapper.getClient().setHeartbeatInterval(prevSessionTimeout);
    }

    @Test
    public void testHeartBeat() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        System.out.println("Waiting for session to timeout (It shouldn't)...");

        _wrapper.getTimeService().readServerTime(tr);
        tr.Run();

        Thread.sleep(62 * 1000);

        _wrapper.getTimeService().readServerTime(tr);
        tr.Run();

        _wrapper.getTimeService().readServerTime(tr);
        tr.Run();

        _wrapper.getPlayerStateService().logout(tr);
        tr.Run();

        _wrapper.getTimeService().readServerTime(tr);
        tr.RunExpectFail(StatusCodes.FORBIDDEN, ReasonCodes.NO_SESSION);

        _wrapper.getClient().resetCommunication();
    }

    @Test
    public void testErrorCallback() throws Exception
    {
        _wrapper.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);
        _wrapper.getClient().enableLogging(true);

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getEntityService().createEntity("type", "{}", "", tr);
        tr.RunExpectFail(-1, -1);
        Assert.assertTrue(tr.m_statusMessage.startsWith("{"));

        tr.Reset();
        _wrapper.getClient().setOldStyleStatusMessageErrorCallback(true);
        _wrapper.getEntityService().createEntity("type", "{}", "", tr);
        tr.RunExpectFail(-1, -1);
        Assert.assertFalse(tr.m_statusMessage.startsWith("{"));

        _wrapper.initialize(m_appId, m_secret, m_appVersion, "https://localhost:5432");

        tr.Reset();
        _wrapper.getClient().setOldStyleStatusMessageErrorCallback(false);
        _wrapper.getEntityService().createEntity("type", "{}", "", tr);
        tr.RunExpectFail(-1, -1);
        Assert.assertTrue(tr.m_statusMessage.startsWith("{"));

        tr.Reset();
        _wrapper.getClient().setOldStyleStatusMessageErrorCallback(true);
        _wrapper.getEntityService().createEntity("type", "{}", "", tr);
        tr.RunExpectFail(-1, -1);
        Assert.assertFalse(tr.m_statusMessage.startsWith("{"));

        _wrapper.getClient().setOldStyleStatusMessageErrorCallback(false);
    }

    @Test
    public void testMessageCache() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        BrainCloudClient bcc = _wrapper.getClient();

        bcc.initialize(m_appId, m_secret, m_appVersion, m_serverUrl + "failunittest");
        bcc.enableLogging(true);
        bcc.registerNetworkErrorCallback(tr);
        bcc.registerGlobalErrorCallback(tr);
        bcc.enableNetworkErrorMessageCaching(true);

        ArrayList<Integer> packetTimeouts = new ArrayList<Integer>();
        packetTimeouts.add(1);
        packetTimeouts.add(1);
        packetTimeouts.add(1);
        bcc.setPacketTimeouts(packetTimeouts);

        tr.setMaxWait(30);

        int networkErrorCount = 0;
        int globalErrorCount = 0;

        System.out.println("Authenticate Universal");
        bcc.getAuthenticationService().authenticateUniversal("abc", "abc", true, tr);
        tr.RunExpectFail(StatusCodes.CLIENT_NETWORK_ERROR, ReasonCodes.CLIENT_NETWORK_ERROR_TIMEOUT);
        networkErrorCount += tr.m_networkErrorCount;

        System.out.println("retryCachedMessages");
        bcc.retryCachedMessages();
        tr.RunExpectFail(StatusCodes.CLIENT_NETWORK_ERROR, ReasonCodes.CLIENT_NETWORK_ERROR_TIMEOUT);
        networkErrorCount += tr.m_networkErrorCount;

        System.out.println("flushCachedMessages");
        bcc.flushCachedMessages(true);
        bcc.runCallbacks();
        globalErrorCount += tr.m_globalErrorCount;

        bcc.enableNetworkErrorMessageCaching(false);
        bcc.deregisterNetworkErrorCallback();
        bcc.deregisterGlobalErrorCallback();
        bcc.setPacketTimeoutsToDefault();
        bcc.resetCommunication();

        Assert.assertEquals(2, networkErrorCount);
        Assert.assertEquals(1, globalErrorCount);
    }

    @Test(timeout=600000)
    public void testMessageBundleMarker() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        tr.setMaxWait(600);
        BrainCloudClient bcc = _wrapper.getClient();

        //bcc.initialize(m_appId, m_secret, m_appVersion, m_serverUrl);
        //bcc.enableLogging(true);
        _wrapper.getClient().getAuthenticationService().authenticateUniversal("abc", "abc", true, tr);
        _wrapper.getClient().insertEndOfMessageBundleMarker();
        _wrapper.getPlayerStatisticsService().readAllUserStats(tr);
        _wrapper.getClient().insertEndOfMessageBundleMarker();
        _wrapper.getPlayerStatisticsService().readAllUserStats(tr);
        _wrapper.getPlayerStatisticsService().readAllUserStats(tr);

        // messages launch right away so only need to call run twice

        tr.Run();
        tr.Run();
        tr.Run();
    }

    @Test(timeout=10000000)
    public void testAuthFirst() throws Exception
    {
        TestUser user = getUser(Users.UserA);
        TestResult tr = new TestResult(_wrapper);
        BrainCloudClient bcc = _wrapper.getClient();

        bcc.getPlayerStatisticsService().readAllUserStats(tr);
        bcc.insertEndOfMessageBundleMarker();

        bcc.getPlayerStatisticsService().readAllUserStats(tr);
        bcc.getAuthenticationService().authenticateUniversal(user.id, user.password, true, tr);

        tr.RunExpectFail(403, ReasonCodes.NO_SESSION);
        tr.Run();
        tr.Run();


        tr.setMaxWait(30);
    }

    @Test
    public void testKillSwitch() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        tr.Run();

        for (int i  = 0; i < 3; i++)
        {
            _wrapper.getIdentityService().refreshIdentity(
                    "fail", "fail", AuthenticationType.Universal, tr);
            tr.Run(true);
        }

        _wrapper.getTimeService().readServerTime(tr);
        tr.Run();

        for (int i  = 0; i < 12; i++)
        {
            _wrapper.getIdentityService().refreshIdentity(
                    "fail", "fail", AuthenticationType.Universal, tr);
            tr.Run(true);
        }

        _wrapper.getTimeService().readServerTime(tr);
        tr.RunExpectFail(900, ReasonCodes.CLIENT_DISABLED);
    }
}