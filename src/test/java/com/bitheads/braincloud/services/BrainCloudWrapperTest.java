package com.bitheads.braincloud.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

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
}
