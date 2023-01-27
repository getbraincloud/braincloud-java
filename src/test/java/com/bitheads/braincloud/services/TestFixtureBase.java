package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.BrainCloudWrapper;

import org.junit.After;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map;

public class TestFixtureBase {
    static protected String m_serverUrl = "";
    static protected String m_appId = "";
    static protected String m_secret = "";
    static protected String m_appVersion = "";
    static protected String m_parentLevelName = "";
    static protected String m_childAppId = "";
    static protected String m_childSecret = "";
    static protected String m_peerName = "";
    static protected String m_redirectAppId = "";

    static protected Map<String, String> m_secretMap;
    public static BrainCloudWrapper _wrapper;
    public static BrainCloudClient _client;

    static String getServerUrl()
    {
        return m_serverUrl;
    }

    @Before
    public void setUp() throws Exception {

        LoadIds();

        _wrapper = new BrainCloudWrapper();
        _client = _wrapper.getClient();

        m_secretMap = new HashMap<String, String>();
        m_secretMap.put(m_appId, m_secret);
        m_secretMap.put(m_childAppId, m_childSecret);

        _client.initializeWithApps(m_serverUrl, m_appId, m_secretMap, m_appVersion);
        _client.enableLogging(true);

        if (shouldAuthenticate()) {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);

            if (!tr.Run()) {
                // what do we do on error?
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        _wrapper.getClient().resetCommunication();
        _wrapper.getClient().deregisterEventCallback();
        _wrapper.getClient().deregisterRewardCallback();
        _client.resetCommunication();
        _client.deregisterEventCallback();
        _client.deregisterRewardCallback();
    }

    /// <summary>
    /// Overridable method which if set to true, will cause unit test "SetUp" to
    /// attempt an authentication before calling the test method.
    /// </summary>
    /// <returns><c>true</c>, if authenticate was shoulded, <c>false</c> otherwise.</returns>
    public boolean shouldAuthenticate() {
        return true;
    }

    /// <summary>
    /// Routine loads up brainCloud configuration info from "tests/ids.txt" (hopefully)
    /// in a platform agnostic way.
    /// </summary>
    private void LoadIds() {
        if (m_serverUrl.length() > 0) return;

        File idsFile = new File("ids.txt");
        try {
            System.out.println("Looking for ids.txt file in " + idsFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (idsFile.exists()) System.out.println("Found ids.txt file");

        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(idsFile));
            String text;
            while ((text = reader.readLine()) != null) {
                lines.add(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String line : lines) {
            String[] split = line.split("=");
            switch (split[0]) {
                case "serverUrl":
                    m_serverUrl = split[1];
                    break;
                case "appId":
                    m_appId = split[1];
                    break;
                case "secret":
                    m_secret = split[1];
                    break;
                case "version":
                    m_appVersion = split[1];
                    break;
                case "childAppId":
                    m_childAppId = split[1];
                    break;
                case "childSecret":
                    m_childSecret = split[1];
                    break;
                case "parentLevelName":
                    m_parentLevelName = split[1];
                    break;
                case "peerName":
                    m_peerName = split[1];
                    break;
                case "redirectAppId":
                    m_redirectAppId = split[1];
                    break;
            }
        }
    }

    public enum Users {
        UserA,
        UserB,
        UserC,
        UserD;

        public static Users byOrdinal(int ord) {
            for (Users m : Users.values()) {
                if (m.ordinal() == ord) {
                    return m;
                }
            }
            return null;
        }
    }

    private static TestUser[] _testUsers;
    private static boolean _init = false;

    /// <summary>
    /// Returns the specified user's data
    /// </summary>
    /// <param name="user"> User's data to return </param>
    /// <returns> Object contining the user's Id, Password, and profileId </returns>
    protected TestUser getUser(Users user) {
        if (!_init) {
            //Log.i(getClass().getName(), "Initializing New Random Users");
            _wrapper.getClient().enableLogging(false);
            _testUsers = new TestUser[TestFixtureBase.Users.values().length];
            Random rand = new Random();

            for (int i = 0, ilen = _testUsers.length; i < ilen; ++i) {
                if(i < 2)
                {
                    _testUsers[i] = new TestUser(_wrapper, Users.byOrdinal(i).toString() + "-", rand.nextInt(), false);
                }
                //a crummy solution to this scritp's logic. Sets it up so all the users other than A and B authenticate with email. It is necessary to allow test users to
                // have both authentications because Jenkins tends to miss vital information on some tests based on the test user's authentication.
                if(i <= 2)
                {
                    _testUsers[i] = new TestUser(_wrapper, Users.byOrdinal(i).toString() + "-", rand.nextInt(), true);
                }
                //Log.i(getClass().getName(), ".");
            }
            //Log.i(getClass().getName(), "\n");
            _wrapper.getClient().enableLogging(true);
            _init = true;
        }

        return _testUsers[user.ordinal()];
    }

    public boolean goToChildProfile() {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().switchToChildProfile(null, m_childAppId, true, tr);
        return tr.Run();
    }

    public boolean goToParentProfile() {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().switchToParentProfile(m_parentLevelName, tr);
        return tr.Run();
    }

    public boolean attachPeer(Users user) {
        TestUser testUser = getUser(user);
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().attachPeerProfile(
                m_peerName, testUser.id + "_peer", testUser.password, AuthenticationType.Universal,null,  true, tr);
        return tr.Run();
    }

    public boolean detachPeer() {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getIdentityService().detachPeer(m_peerName, tr);
        return tr.Run();
    }

    public void Logout() {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStateService().logout(
                tr);
        tr.Run();
        _wrapper.getClient().resetCommunication();
        _wrapper.getClient().getAuthenticationService().clearSavedProfileId();
    }
}