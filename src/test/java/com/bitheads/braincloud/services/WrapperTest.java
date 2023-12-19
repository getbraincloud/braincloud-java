package com.bitheads.braincloud.services;

import org.junit.Test;

public class WrapperTest extends TestFixtureNoAuth {
    
    @Test
    public void testLogout(){
        TestResult tr = new TestResult(_wrapper);

        System.out.println("ProfileID pre-auth: " + _wrapper.getStoredProfileId());

        System.out.println("Authenticating . . .");
        _wrapper.authenticateUniversal(
                getUser(Users.UserA).id,
                getUser(Users.UserA).password, 
                true, 
                tr);
        tr.Run();

        System.out.println("ProfileID post-auth: " + _wrapper.getStoredProfileId());

        System.out.println("Logging out . . .");
        _wrapper.logout(false, tr);
        tr.Run();

        System.out.println("ProfileID post-logout: " + _wrapper.getStoredProfileId());
    }

    @Test
    public void testLogoutForgetUser(){
        TestResult tr = new TestResult(_wrapper);

        System.out.println("ProfileID pre-auth: " + _wrapper.getStoredProfileId());

        System.out.println("Authenticating . . .");
        _wrapper.authenticateUniversal(
                getUser(Users.UserA).id,
                getUser(Users.UserA).password, 
                true, 
                tr);
        tr.Run();

        System.out.println("ProfileID post-auth: " + _wrapper.getStoredProfileId());

        System.out.println("Logging out . . .");
        _wrapper.logout(true, tr);
        tr.Run();

        System.out.println("ProfileID post-logout: " + _wrapper.getStoredProfileId());
    }
}
