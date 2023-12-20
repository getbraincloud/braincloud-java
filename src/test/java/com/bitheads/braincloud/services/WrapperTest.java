package com.bitheads.braincloud.services;

import org.junit.Assert;
import org.junit.Test;

public class WrapperTest extends TestFixtureNoAuth {

    public void testLogOut(boolean forgetUser){        
        TestResult tr = new TestResult(_wrapper);

        System.out.println("Authenticating . . .");
        _wrapper.authenticateUniversal(
                getUser(Users.UserA).id,
                getUser(Users.UserA).password, 
                true, 
                tr);
        tr.Run();

        System.out.println("Logging out . . .");
        _wrapper.logout(forgetUser, tr);
        tr.Run();

        Assert.assertEquals(forgetUser, _wrapper.getStoredProfileId().equals(""));
    }
    
    @Test
    public void testLogoutRememberUser(){
        testLogOut(false);
    }

    @Test
    public void testLogoutForgetUser(){
        testLogOut(true);
    }
}
