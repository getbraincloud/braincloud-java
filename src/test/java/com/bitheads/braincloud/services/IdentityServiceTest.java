package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Created by prestonjennings on 15-09-02.
 */
public class IdentityServiceTest extends TestFixtureBase {

    @Test
    public void testAttachEmailIdentity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getIdentityService().attachEmailIdentity(
                "id_" + getUser(Users.UserA).email,
                getUser(Users.UserA).password,
                tr);

        tr.Run();
    }

    @Test
    public void testMergeEmailIdentity() throws Exception {

    }

    @Test
    public void testDetachEmailIdentity() throws Exception {

    }

    @Test
    public void testSwitchToChildProfile() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().switchToChildProfile(null, m_childAppId, true, tr);
        tr.Run();
    }

    @Test
    public void testSwitchToSingletonChildProfile() throws Exception {
        //will need to come back to this so not to use false positive, we are defaulting to userA  
        //which is getting multiple children added to it at some point in the tests and this call expects failure 
        //if a profileId has multiple children attached to it. 
        //"Processing exception (message): Multiple child candidates, must supply profile id."
        TestResult tr2 = new TestResult(_wrapper);
        _wrapper.getIdentityService().switchToSingletonChildProfile(m_childAppId, true, tr2);
        tr2.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_PLAYER_ID);
    }

    @Test
    public void testSwitchToParentProfile() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().switchToChildProfile(null, m_childAppId, true, tr);
        tr.Run();

        tr.Reset();
        _wrapper.getIdentityService().switchToParentProfile(m_parentLevelName, tr);
        tr.Run();
    }

    @Test
    public void testAttachParentWithIdentity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        goToChildProfile();

        _wrapper.getIdentityService().detachParent(tr);

        TestUser testUser = getUser(Users.UserA);
        _wrapper.getIdentityService().attachParentWithIdentity(
                testUser.id, testUser.password, AuthenticationType.Universal, null, true, tr);
        tr.Run();
    }

    @Test
    public void testGetChildProfiles() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().getChildProfiles(true, tr);
        tr.Run();
    }

    @Test
    public void testGetIdentities() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().getIdentities(tr);
        tr.Run();
    }

    @Test
    public void testGetExpiredIdentities() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().getExpiredIdentities(tr);
        tr.Run();
    }

    @Test
    public void testRefreshIdentity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().refreshIdentity(
                getUser(Users.UserA).id,
                getUser(Users.UserA).password,
                AuthenticationType.Universal,
                tr);
        tr.RunExpectFail(400, 40464);
    }

    @Test
    public void testChangeEmailIdentity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().changeEmailIdentity(
                getUser(Users.UserA).email,
                getUser(Users.UserA).password,
                getUser(Users.UserA).email,
                true,
                tr);
        tr.RunExpectFail(400, 40584);
    }

    @Test
    public void testAttachPeerProfile() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        TestUser testUser = getUser(Users.UserA);
        _wrapper.getIdentityService().attachPeerProfile(
                m_peerName, testUser.id + "_peer", testUser.password, AuthenticationType.Universal, null, true, tr);

        if (tr.Run()) detachPeer();
    }

    @Test
    public void testDetachPeer() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        if (attachPeer(Users.UserA)) {
            _wrapper.getIdentityService().detachPeer(m_peerName, tr);
            tr.Run();
        }
    }

    @Test
    public void testGetPeerProfiles() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getIdentityService().getIdentities(tr);
        tr.Run();
    }

    @Test
    public void testChildSwitch() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getIdentityService().switchToChildProfile(null, "20005", true, tr);
        tr.Run();
    }

    @Test
    public void testParentSwitch() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getIdentityService().switchToChildProfile(null, "20005", true, tr);
        tr.Run();

        _wrapper.getIdentityService().switchToParentProfile("Master", tr);
        tr.Run();
    }

    @Test
    public void testAttachNonUniversalIdLogin() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getIdentityService().attachNonLoginUniversalId("braincloudtest@gmail.com", tr);
        tr.RunExpectFail(202, ReasonCodes.DUPLICATE_IDENTITY_TYPE);
    }

    @Test
    public void testUpdateUniversalIdLogin() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getIdentityService().updateUniversalIdLogin("braincloudtest@gmail.com", tr);
        tr.RunExpectFail(400, ReasonCodes.NEW_CREDENTIAL_IN_USE);
    }

    
    @Test
    public void testAttachDetachBlockchain() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getIdentityService().attachBlockchainIdentity("config", "publicKEEEEEEEEY", tr);
        tr.Run();

        TestResult tr2 = new TestResult(_wrapper);

        _wrapper.getIdentityService().detachBlockchainIdentity("config", tr2);
        tr2.Run();
    }
}
