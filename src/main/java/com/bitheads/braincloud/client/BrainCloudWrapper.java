package com.bitheads.braincloud.client;

import java.util.Map;
import java.util.prefs.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchAdvanced;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchApple;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchEmail;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchExternal;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchFacebook;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchGoogle;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchGoogleOpenId;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchOculus;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchSteam;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchTwitter;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchUltra;
import com.bitheads.braincloud.client.SmartSwitchCallback.SmartSwitchUniversal;
import com.bitheads.braincloud.services.AppStoreService;
import com.bitheads.braincloud.services.AsyncMatchService;
import com.bitheads.braincloud.services.AuthenticationService;
import com.bitheads.braincloud.services.ChatService;
import com.bitheads.braincloud.services.CustomEntityService;
import com.bitheads.braincloud.services.DataStreamService;
import com.bitheads.braincloud.services.EntityService;
import com.bitheads.braincloud.services.EventService;
import com.bitheads.braincloud.services.FileService;
import com.bitheads.braincloud.services.FriendService;
import com.bitheads.braincloud.services.GamificationService;
import com.bitheads.braincloud.services.GlobalAppService;
import com.bitheads.braincloud.services.GlobalEntityService;
import com.bitheads.braincloud.services.GlobalFileService;
import com.bitheads.braincloud.services.GlobalStatisticsService;
import com.bitheads.braincloud.services.GroupFileService;
import com.bitheads.braincloud.services.GroupService;
import com.bitheads.braincloud.services.IdentityService;
import com.bitheads.braincloud.services.ItemCatalogService;
import com.bitheads.braincloud.services.LobbyService;
import com.bitheads.braincloud.services.MailService;
import com.bitheads.braincloud.services.MatchMakingService;
import com.bitheads.braincloud.services.MessagingService;
import com.bitheads.braincloud.services.OneWayMatchService;
import com.bitheads.braincloud.services.PlaybackStreamService;
import com.bitheads.braincloud.services.PlayerStateService;
import com.bitheads.braincloud.services.PlayerStatisticsEventService;
import com.bitheads.braincloud.services.PlayerStatisticsService;
import com.bitheads.braincloud.services.PresenceService;
import com.bitheads.braincloud.services.ProfanityService;
import com.bitheads.braincloud.services.PushNotificationService;
import com.bitheads.braincloud.services.RTTService;
import com.bitheads.braincloud.services.RedemptionCodeService;
import com.bitheads.braincloud.services.RelayService;
import com.bitheads.braincloud.services.S3HandlingService;
import com.bitheads.braincloud.services.ScriptService;
import com.bitheads.braincloud.services.SocialLeaderboardService;
import com.bitheads.braincloud.services.TimeService;
import com.bitheads.braincloud.services.TournamentService;
import com.bitheads.braincloud.services.UserItemsService;
import com.bitheads.braincloud.services.VirtualCurrencyService;

/**
 * The BrainCloudWrapper provides some convenience functionality to developers when they are
 * getting started with the authentication system.
 * <p>
 * By using the wrapper authentication methods, the anonymous and profile ids will be automatically
 * persisted upon successful authentication. When authenticating, any stored anonymous/profile ids will
 * be sent to the server. This strategy is useful when using anonymous authentication.
 */
public class BrainCloudWrapper implements IServerCallback, IBrainCloudWrapper {

    private static final String AUTHENTICATION_ANONYMOUS = "anonymous";
    private static final String _SHARED_PREFERENCES = "bcprefs";
    private static final String _DEFAULT_URL = "https://api.braincloudservers.com/dispatcherv2";

    private Preferences _prefs = Preferences.userNodeForPackage(com.bitheads.braincloud.client.BrainCloudWrapper.class);

    private boolean _alwaysAllowProfileSwitch = true;
    private IServerCallback _authenticateCallback = null;

    private BrainCloudClient _client = null;
    private String _wrapperName = "";

    //get the release platform 
    @Override
	public Platform getReleasePlatform() {
        return _client.getReleasePlatform();
    }
    @Override
	public void setReleasePlatform(Platform releasePlatform) {
        getClient().setReleasePlatform(releasePlatform);
    }

    /**
     * Returns a singleton instance of the BrainCloudClient, if this is the BrainCloudWrapper Singleton.
     * Otherwise, return an instance of the BrainCloudClient, if this is an instance of the BrainCloudWrapper.
     *
     * @return A singleton instance of the BrainCloudClient.
     */
    @Override
	public BrainCloudClient getClient() {
        return _client;
    }

    public BrainCloudWrapper() {
        _client = new BrainCloudClient();
    }

    /**
     * Instantiate a copy of the brainCloud wrapper. Don't use getInstance if creating your own copy.
     *
     * @param wrapperName value used to differentiate saved wrapper data
     */
    public BrainCloudWrapper(String wrapperName) {
        _wrapperName = wrapperName;
        _client = new BrainCloudClient();
        _prefs = Preferences.userRoot().node(getSaveName());
    }

    private void detectPlatform()
    {
        //detect os being used
        setReleasePlatform(Platform.detectGenericPlatform(System.getProperty("os.name").toLowerCase()));
    }

    private class InitializeParams
    {
        public String appId = "";
        public String secretKey = "";
        public String appVersion = "";
        public String serverUrl = "";
        public Map<String, String> secretMap = null;
    };
    private InitializeParams m_initializeParams = new InitializeParams();

    /**
     * Method initializes the BrainCloudClient.
     *
     * @param appId      The app id
     * @param secretKey  The secret key for your app
     * @param appVersion The app version
     */
    @Override
	public void initialize(String appId, String secretKey, String appVersion) {
        
        m_initializeParams.appId = appId;
        m_initializeParams.secretKey = secretKey;
        m_initializeParams.appVersion = appVersion;
        m_initializeParams.serverUrl = _DEFAULT_URL;
        m_initializeParams.secretMap = null;

        if(_client == null)
        {
            _client = new BrainCloudClient();
        }
        //need to do detection in the wrapper because java doesn't recognize defines or precompiler statements... 
        //Both java_desktop and java_android have lib specific ways of detecting platforms and they are not cross compatible.  
        detectPlatform();

        getClient().initialize(_DEFAULT_URL, appId, secretKey, appVersion);
    }

    /**
     * Method initializes the BrainCloudClient.
     *
     * @param appId      The app id
     * @param secretKey  The secret key for your app
     * @param appVersion The app version
     * @param serverUrl  The url to the brainCloud server
     */
    @Override
	public void initialize(String appId, String secretKey, String appVersion, String serverUrl) {

        m_initializeParams.appId = appId;
        m_initializeParams.secretKey = secretKey;
        m_initializeParams.appVersion = appVersion;
        m_initializeParams.serverUrl = serverUrl;
        m_initializeParams.secretMap = null;

        if(_client == null)
        {
            _client = new BrainCloudClient();
        }
        //need to do detection in the wrapper because java doesn't recognize defines or precompiler statements... 
        //Both java_desktop and java_android have lib specific ways of detecting platforms and they are not cross compatible.  
        detectPlatform();

        getClient().initialize(serverUrl, appId, secretKey, appVersion);
    }

    /**
     * Method initializes the BrainCloudClient. Note - this is here for testing purposes so a tester can toggle initializations being used. 
     *
     * @param appId      The app id
     * @param secretKey  The secret key for your app
     * @param appVersion The app version
     * @param serverUrl  The url to the brainCloud server
     * @param compantName 
     * @param appName
     */
	@SuppressWarnings("unused")
    private void initializeWithApps(String url, String defaultAppId, Map<String, String> secretMap, String version, String companyName, String appName)
    {
        m_initializeParams.appId = defaultAppId;
        m_initializeParams.secretKey = "";
        m_initializeParams.appVersion = version;
        m_initializeParams.serverUrl = url;
        m_initializeParams.secretMap = secretMap;

        if(_client == null)
        {
            _client = new BrainCloudClient();
        }
        //need to do detection in the wrapper because java doesn't recognize defines or precompiler statements... 
        //Both java_desktop and java_android have lib specific ways of detecting platforms and they are not cross compatible.  
        detectPlatform();

        getClient().initializeWithApps(url, defaultAppId, secretMap, version);
    } 

    /**
     * Method initializes the BrainCloudClient.
     *
     * @param appId      The app id
     * @param secretKey  The secret key for your app
     * @param appVersion The app version
     * @param serverUrl  The url to the brainCloud server
     */
	@SuppressWarnings("unused")
    private void initializeWithApps(String url, String defaultAppId, Map<String, String> secretMap, String version)
    {
        m_initializeParams.appId = defaultAppId;
        m_initializeParams.secretKey = "";
        m_initializeParams.appVersion = version;
        m_initializeParams.serverUrl = url;
        m_initializeParams.secretMap = secretMap;

        if(_client == null)
        {
            _client = new BrainCloudClient();
        }
        //need to do detection in the wrapper because java doesn't recognize defines or precompiler statements... 
        //Both java_desktop and java_android have lib specific ways of detecting platforms and they are not cross compatible.  
        detectPlatform();

        getClient().initializeWithApps(url, defaultAppId, secretMap, version);
    } 

    protected void initializeIdentity(boolean isAnonymousAuth) {

        // check if we already have saved IDs
        String profileId = getStoredProfileId();
        String anonymousId = getStoredAnonymousId();

        // create an anonymous ID if necessary
        if ((!anonymousId.isEmpty() && profileId.isEmpty()) || anonymousId.isEmpty()) {
            anonymousId = getClient().getAuthenticationService().generateAnonymousId();
            profileId = "";
            setStoredAnonymousId(anonymousId);
            setStoredProfileId(profileId);
        }

        String profileIdToAuthenticateWith = profileId;
        if (!isAnonymousAuth && _alwaysAllowProfileSwitch) {
            profileIdToAuthenticateWith = "";
        }
        setStoredAuthenticationType(isAnonymousAuth ? AUTHENTICATION_ANONYMOUS : "");

        // send our IDs to brainCloud
        getClient().initializeIdentity(profileIdToAuthenticateWith, anonymousId);
    }

    /**
     * Combines the wrapperName and the _SHARED_PREFERENCES to create a unique save name
     *
     * ie. userone_bcprefs
     *
     * @return
     */
    private String getSaveName() {
        String prefix = _wrapperName.isEmpty() ? "" : "_" + _wrapperName;
        return prefix + _SHARED_PREFERENCES;
    }

    /**
     * Returns the stored profile id
     *
     * @return The stored profile id
     */
    @Override
	public String getStoredProfileId() {
        return _prefs.get("profileId", "");
    }

    /**
     * Sets the stored profile id
     *
     * @param profileId The profile id to set
     */
    @Override
	public void setStoredProfileId(String profileId) {
    	_prefs.put("profileId", profileId);
    }


    /**
     * Resets the profile id to empty string
     */
    @Override
	public void resetStoredProfileId() {
        setStoredProfileId("");
    }

    /**
     * Returns the stored anonymous id
     *
     * @return The stored anonymous id
     */
    String getStoredAnonymousId() {
    	return _prefs.get("anonymousId", "");
    }

    /**
     * Sets the stored anonymous id
     *
     * @param anonymousId The anonymous id to set
     */
    @Override
	public void setStoredAnonymousId(String anonymousId) {
    	_prefs.put("anonymousId", anonymousId);
    }

    /**
     * Resets the anonymous id to empty string
     */
    @Override
	public void resetStoredAnonymousId() {
        setStoredAnonymousId("");
    }

    /**
     * For non-anonymous authentication methods, a profile id will be passed in
     * when this value is set to false. This will generate an error on the server
     * if the profile id passed in does not match the profile associated with the
     * authentication credentials. By default, this value is true.
     *
     * @param alwaysAllow Controls whether the profile id is passed in with
     *                    non-anonymous authentications.
     */
    @Override
	public void setAlwaysAllowProfileSwitch(boolean alwaysAllow) {
        _alwaysAllowProfileSwitch = alwaysAllow;
    }

    /**
     * Returns the value for always allow profile switch
     *
     * @return Whether to always allow profile switches
     */
    @Override
	public boolean getAlwaysAllowProfileSwitch() {
        return _alwaysAllowProfileSwitch;
    }

    // these methods are not really used
    protected String getStoredAuthenticationType() {
    	return _prefs.get("authenticationType", "");
    }

    protected void setStoredAuthenticationType(String authenticationType) {
    	_prefs.put("authenticationType", authenticationType);
    }

    protected void resetStoredAuthenticationType() {
        setStoredAuthenticationType("");
    }

    /**
     * Authenticate a user anonymously with brainCloud - used for apps that
     * don't want to bother the user to login, or for users who are sensitive to
     * their privacy
     *
     * @param callback The callback handler
     */
    @Override
	public void authenticateAnonymous(IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(true);

        getClient().getAuthenticationService().authenticateAnonymous(true, this);
    }

    /**
     * Authenticate the user using a handoffId and an authentication token.
     *
     * @param handoffId   braincloud handoffId generated frim cloud script
     * @param securityToken The authentication token
     * @param callback   The callback handler
     */
    @Override
	public void authenticateHandoff(String handoffId, String securityToken, IServerCallback callback) {
    	_authenticateCallback = callback;
    	
    	getClient().getAuthenticationService().authenticateHandoff(handoffId, securityToken, this);
    }

    /**
     * Authenticate the user using a handoffId and an authentication token.
     *
     * @param handoffCode generate in cloud code
     * @param callback   The callback handler
     */
    @Override
	public void authenticateSettopHandoff(String handoffCode, IServerCallback callback) {
    	_authenticateCallback = callback;
    	
    	getClient().getAuthenticationService().authenticateSettopHandoff(handoffCode, this);
    }

    /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param email       The e-mail address of the user
     * @param password    The password of the user
     * @param forceCreate Should a new profile be created for this user if the account
     *                    does not exist?
     * @param callback    The callback handler
     */
    @Override
	public void authenticateEmailPassword(String email,
                                          String password,
                                          boolean forceCreate,
                                          IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateEmailPassword(email, password, forceCreate, this);
    }

    /**
     * Authenticate the user via cloud code (which in turn validates the supplied credentials against an external system).
     * This allows the developer to extend brainCloud authentication to support other backend authentication systems.
     * <p>
     * Service Name - Authenticate
     * Server Operation - Authenticate
     *
     * @param userId           The user id
     * @param token            The user token (password etc)
     * @param externalAuthName The name of the cloud script to call for external authentication
     * @param forceCreate      Should a new profile be created for this user if the account
     *                         does not exist?
     */
    @Override
	public void authenticateExternal(String userId,
                                     String token,
                                     String externalAuthName,
                                     boolean forceCreate,
                                     IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateExternal(userId, token, externalAuthName, forceCreate, this);
    }

    /**
     * Authenticate the user with brainCloud using their Facebook Credentials
     *
     * @param fbUserId    The facebook id of the user
     * @param fbAuthToken The validated token from the Facebook SDK (that will be
     *                    further validated when sent to the bC service)
     * @param forceCreate Should a new profile be created for this user if the account
     *                    does not exist?
     * @param callback    The callback handler
     */
    @Override
	public void authenticateFacebook(String fbUserId,
                                     String fbAuthToken,
                                     boolean forceCreate,
                                     IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateFacebook(fbUserId, fbAuthToken, forceCreate, this);
    }

    /**
     * Authenticate the user with brainCloud using their FacebookLimited Credentials
     *
     * @param fbLimitedUserId    The facebookLimited id of the user
     * @param fbAuthToken The validated token from the Facebook SDK (that will be
     *                    further validated when sent to the bC service)
     * @param forceCreate Should a new profile be created for this user if the account
     *                    does not exist?
     * @param callback    The callback handler
     */
    @Override
	public void authenticateFacebookLimited(String fbLimitedUserId,
                                     String fbAuthToken,
                                     boolean forceCreate,
                                     IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateFacebookLimited(fbLimitedUserId, fbAuthToken, forceCreate, this);
    }

    /**
     * Authenticate the user using a google userid(email address) and google
     * authentication token.
     *
     * @param googleUserId    String representation of google+ userid (email)
     * @param googleAuthToken The authentication token derived via the google apis.
     * @param forceCreate     Should a new profile be created for this user if the account
     *                        does not exist?
     * @param callback        The callback handler
     */
    @Override
	public void authenticateGoogle(String googleUserId,
                                   String googleAuthToken,
                                   boolean forceCreate,
                                   IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateGoogle(googleUserId, googleAuthToken, forceCreate, this);
    }

    /**
     * Authenticate the user using a google userid(email address) and google
     * authentication token.
     *
     * @param googleOpenId    String representation of google+ userid (email)
     * @param googleAuthToken The authentication token derived via the google apis.
     * @param forceCreate     Should a new profile be created for this user if the account
     *                        does not exist?
     * @param callback        The callback handler
     */
    @Override
	public void authenticateGoogleOpenId(String googleOpenId,
                                   String googleAuthToken,
                                   boolean forceCreate,
                                   IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateGoogleOpenId(googleOpenId, googleAuthToken, forceCreate, this);
    }

    /**
     * Authenticate the user using an apple userid(email address) and apple
     * authentication token.
     *
     * @param appleUserId    String representation of apple userid (email)
     * @param token The authentication token derived via the apple apis.
     * @param forceCreate     Should a new profile be created for this user if the account
     *                        does not exist?
     * @param callback        The callback handler
     */
    @Override
	public void authenticateApple(String appleUserId,
                                   String token,
                                   boolean forceCreate,
                                   IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateApple(appleUserId, token, forceCreate, this);
    }

    /**
     * Authenticate the user using a steam userid and session ticket (without
     * any validation on the userid).
     *
     * @param steamUserId        String representation of 64 bit steam id
     * @param steamSessionTicket The session ticket of the user (hex encoded)
     * @param forceCreate        Should a new profile be created for this user if the account
     *                           does not exist?
     * @param callback           The callback handler
     */
    @Override
	public void authenticateSteam(String steamUserId,
                                  String steamSessionTicket,
                                  boolean forceCreate,
                                  IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateSteam(steamUserId, steamSessionTicket, forceCreate, this);
    }

    /**
     * Authenticate the user for Ultra.
     *
     * @param ultraUsername      it's what the user uses to log into the Ultra endpoint initially
     * @param ultraIdToken       The "id_token" taken from Ultra's JWT.
     * @param forceCreate        Should a new profile be created for this user if the account
     *                           does not exist?
     * @param callback           The callback handler
     */
    @Override
	public void authenticateUltra(String ultraUsername,
                                  String ultraIdToken,
                                  boolean forceCreate,
                                  IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateUltra(ultraUsername, ultraIdToken, forceCreate, this);
    }
    
    /**
     * Authenticate the user using a Twitter userid, authentication token, and secret from Twitter.
     * <p>
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param userId      String representation of Twitter userid
     * @param token       The authentication token derived via the Twitter apis.
     * @param secret      The secret given when attempting to link with Twitter
     * @param forceCreate Should a new profile be created for this user if the account does not exist?
     * @param callback    The callback handler
     */
    @Override
	public void authenticateTwitter(String userId,
                                    String token,
                                    String secret,
                                    boolean forceCreate,
                                    IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateTwitter(userId, token, secret, forceCreate, this);
    }


    /**
     * Authenticate the user using a userid and password (without any validation
     * on the userid). Similar to AuthenticateEmailPassword - except that that
     * method has additional features to allow for e-mail validation, password
     * resets, etc.
     *
     * @param userId       The e-mail address of the user
     * @param userPassword The password of the user
     * @param forceCreate  Should a new profile be created for this user if the account
     *                     does not exist?
     * @param callback     The callback handler
     */
    @Override
	public void authenticateUniversal(String userId,
                                      String userPassword,
                                      boolean forceCreate,
                                      IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateUniversal(userId, userPassword, forceCreate, this);
    }

    /*
     * A generic Authenticate method that translates to the same as calling a specific one, except it takes an extraJson
     * that will be passed along to pre- or post- hooks.
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param authenticationType Universal, Email, Facebook, etc
     * @param ids Auth IDs object
     * @param forceCreate Should a new profile be created for this user if the account does not exist?
     * @param extraJson Additional to piggyback along with the call, to be picked up by pre- or post- hooks. Leave empty string for no extraJson.
     * @param callback The method to be invoked when the server response is received
     */
    @Override
	public void authenticateAdvanced(AuthenticationType authenticationType, AuthenticationIds ids, boolean forceCreate, String extraJson, IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(false);

        getClient().getAuthenticationService().authenticateAdvanced(authenticationType, ids, forceCreate, extraJson, this);
    }

    /**
     * Re-authenticates the user with brainCloud
     *
     * @param callback The callback handler
     */
    @Override
	public void reconnect(IServerCallback callback) {
        _authenticateCallback = callback;

        initializeIdentity(true);

        getClient().getAuthenticationService().authenticateAnonymous(false, this);
    }

        /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param email       The e-mail address of the user
     * @param callback    The callback handler
     */
    @Override
	public void resetEmailPassword(String email,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetEmailPassword(email, this);
    }

            /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param email         The e-mail address of the user
     * @param serviceParams Set of parameters dependant on the mail service configured.
     * @param callback      The callback handler
     */
    @Override
	public void resetEmailPasswordAdvanced(String email, String serviceParams,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetEmailPasswordAdvanced(email, serviceParams, this);
    }

            /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param email             The e-mail address of the user
     * @param tokenTtlInMinutes The token expiry time
     * @param callback          The callback handler
     */
    @Override
	public void resetEmailPasswordWithExpiry(String email, int tokenTtlInMinutes,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetEmailPasswordWithExpiry(email, tokenTtlInMinutes, this);
    }

            /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param email             The e-mail address of the user
     * @param serviceParams     Set of parameters dependant on the mail service configured.
     * @param tokenTtlInMinutes Token expiry time
     * @param callback          The callback handler
     */
    @Override
	public void resetEmailPasswordAdvancedWithExpiry(String email, String serviceParams, Integer tokenTtlInMinutes,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetEmailPasswordAdvancedWithExpiry(email, serviceParams, tokenTtlInMinutes, this);
    }

            /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param universalId The e-mail address of the user
     * @param callback    The callback handler
     */
    @Override
	public void resetUniversalIdPassword(String universalId,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetUniversalIdPassword(universalId, this);
    }

            /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param universalId   The e-mail address of the user
     * @param serviceParams Set of parameters dependant on the mail service configured.
     * @param callback      The callback handler
     */
    @Override
	public void resetUniversalIdPasswordAdvanced(String universalId, String serviceParams,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetUniversalIdPasswordAdvanced(universalId, serviceParams, this);
    }

            /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param universalId       The e-mail address of the user
     * @param tokenTtlInMinutes Token expiry time
     * @param callback          The callback handler
     */
    @Override
	public void resetUniversalIdPasswordWithExpiry(String universalId, int tokenTtlInMinutes,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetUniversalIdPasswordWithExpiry(universalId, tokenTtlInMinutes, this);
    }

            /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param universalId       The e-mail address of the user
     * @param serviceParams     Set of parameters dependant on the mail service configured.
     * @param tokenTtlInMinutes Token expiry time
     * @param callback          The callback handler
     */
    @Override
	public void resetUniversalIdPasswordAdvancedWithExpiry(String universalId, String serviceParams, Integer tokenTtlInMinutes,
                                          IServerCallback callback) {
        getClient().getAuthenticationService().resetUniversalIdPasswordAdvancedWithExpiry(universalId, serviceParams, tokenTtlInMinutes, this);
    }

    @Override
	public void smartSwitchAuthenticateEmail(String email, String password, boolean forceCreate, IServerCallback callback) 
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchEmail emailSwitch = smartSwitch.new SmartSwitchEmail(email, password, forceCreate, this, callback);
        
        getIdentitiesCallback(emailSwitch);
    }

    @Override
	public void smartSwitchAuthenticateExternal(String userId, String token, String externalAuthName, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchExternal externalSwitch = smartSwitch.new SmartSwitchExternal(userId, token, externalAuthName, forceCreate, this, callback);

        getIdentitiesCallback(externalSwitch);
    }

    @Override
	public void smartSwitchAuthenticateFacebook(String fbUserId, String fbAuthToken, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchFacebook facebookSwitch = smartSwitch.new SmartSwitchFacebook(fbUserId, fbAuthToken, forceCreate, this, callback);

        getIdentitiesCallback(facebookSwitch);
    }

    @Override
	public void smartSwitchAuthenticateOculus(String oculusUserId, String oculusNonce, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchOculus oculusSwitch = smartSwitch.new SmartSwitchOculus(oculusUserId, oculusNonce, forceCreate, this, callback);

        getIdentitiesCallback(oculusSwitch);
    }

    @Override
	public void smartSwitchAuthenticateGoogle(String googleUserId, String serverAuthCode, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchGoogle googleSwitch = smartSwitch.new SmartSwitchGoogle(googleUserId, serverAuthCode, forceCreate, this, callback);

        getIdentitiesCallback(googleSwitch);
    }

    @Override
	public void smartSwitchAuthenticateGoogleOpenId(String googleUserAccountEmail, String IdToken, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchGoogleOpenId googleSwitch = smartSwitch.new SmartSwitchGoogleOpenId(googleUserAccountEmail, IdToken, forceCreate, this, callback);

        getIdentitiesCallback(googleSwitch);
    }

    @Override
	public void smartSwitchAuthenticateApple(String appleUserId, String token, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchApple appleSwitch = smartSwitch.new SmartSwitchApple(appleUserId, token, forceCreate, this, callback);

        getIdentitiesCallback(appleSwitch);
    }

    @Override
	public void smartSwitchAuthenticateSteam(String steamUserId, String sessionTicket, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchSteam steamSwitch = smartSwitch.new SmartSwitchSteam(steamUserId, sessionTicket, forceCreate, this, callback);

        getIdentitiesCallback(steamSwitch);
    }

    @Override
	public void smartSwitchAuthenticateTwitter(String userId, String token, String secret, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchTwitter twitterSwitch = smartSwitch.new SmartSwitchTwitter(userId, token, secret, forceCreate, this, callback);

        getIdentitiesCallback(twitterSwitch);
    }

    @Override
	public void smartSwitchAuthenticateUniversal(String userId, String password, boolean forceCreate, IServerCallback callback) 
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchUniversal universalSwitch = smartSwitch.new SmartSwitchUniversal(userId, password, forceCreate, this, callback);

        getIdentitiesCallback(universalSwitch);
    }

    @Override
	public void smartSwitchAuthenticateUltra(String ultraUserId, String ultraIdToken, boolean forceCreate, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchUltra ultraSwitch = smartSwitch.new SmartSwitchUltra(ultraUserId, ultraIdToken, forceCreate, this, callback);

        getIdentitiesCallback(ultraSwitch);
    }

    @Override
	public void smartSwitchAuthenticateAdvanced(AuthenticationType authenticationType, AuthenticationIds ids, boolean forceCreate, String extraJson, IServerCallback callback)
    {
        SmartSwitchCallback smartSwitch = new SmartSwitchCallback(this, callback);
        SmartSwitchAdvanced advancedSwitch = smartSwitch.new SmartSwitchAdvanced(authenticationType, ids, forceCreate, extraJson, this, callback);

        getIdentitiesCallback(advancedSwitch);
    }

    @Override
    public void logout(boolean forgetUser, IServerCallback callback) {
        if(forgetUser){
            resetStoredProfileId();
        }

        getClient().getPlayerStateService().logout(callback);
    }

    private void getIdentitiesCallback(IServerCallback success) 
    {

        IdentityCallback identityCallback = new IdentityCallback(this, success);

        if (getClient().isAuthenticated()) 
        {
            getClient().getIdentityService().getIdentities(identityCallback);
        } 
        else 
        {
            success.serverCallback(ServiceName.authenticationV2, ServiceOperation.AUTHENTICATE, null);
        }
    }


    /**
     * Run callbacks, to be called once per frame from your main thread
     */
    @Override
	public void runCallbacks() {
        getClient().runCallbacks();
    }


    /**
     * The serverCallback() method returns server data back to the layer
     * interfacing with the BrainCloud library.
     *
     * @param serviceName      - name of the requested service
     * @param serviceOperation - requested operation
     * @param jsonData         - returned data from the server
     */
    @Override
	public void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject jsonData) {
        if (serviceName.equals(ServiceName.authenticationV2) && serviceOperation.equals(ServiceOperation.AUTHENTICATE)) {
            try {
                String profileId = jsonData.getJSONObject("data").getString("profileId");
                if (!profileId.isEmpty()) {
                    setStoredProfileId(profileId);
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        if (_authenticateCallback != null) {
            _authenticateCallback.serverCallback(serviceName, serviceOperation, jsonData);
        }
    }

    /**
     * Errors are returned back to the layer which is interfacing with the
     * BrainCloud library through the serverError() callback.
     * <p>
     * A server error might indicate a failure of the client to communicate
     * with the server after N retries.
     *
     * @param serviceName      - name of the requested service
     * @param serviceOperation - requested operation
     * @param statusCode       The error status return code (400, 403, 500, etc)
     * @param reasonCode       The brainCloud reason code (see reason codes on apidocs site)
     * @param jsonError        The error json string
     */
    @Override
	public void serverError(ServiceName serviceName, ServiceOperation serviceOperation, int statusCode, int reasonCode, String jsonError)
    {
        if (statusCode == 202 && reasonCode == ReasonCodes.MANUAL_REDIRECT) // This should only happen on auth calls
        {
            // Manual redirection
            JSONObject data = new JSONObject(jsonError);

            m_initializeParams.serverUrl = data.has("redirect_url") ? data.getString("redirect_url") : m_initializeParams.serverUrl;
            String newAppId = data.has("redirect_appid") ? data.getString("redirect_appid") : null;

            // re-initialize the client with our app info
            if (m_initializeParams.secretMap == null)
            {
                if (newAppId != null) m_initializeParams.appId = newAppId;
                getClient().initialize(m_initializeParams.serverUrl, 
                                       m_initializeParams.appId, 
                                       m_initializeParams.secretKey, 
                                       m_initializeParams.appVersion);
            }
            else
            {
                // For initialize with apps, we ignore the app id
                getClient().initializeWithApps(m_initializeParams.serverUrl, 
                                               m_initializeParams.appId, 
                                               m_initializeParams.secretMap, 
                                               m_initializeParams.appVersion);
            }

            initializeIdentity(true);
            getClient().getAuthenticationService().retryPreviousAuthenticate(this);

            return;
        }

        if (_authenticateCallback != null)
        {
            _authenticateCallback.serverError(serviceName, serviceOperation, statusCode, reasonCode, jsonError);
        }
    }

    // brainCloud Services
    @Override
	public AppStoreService getAppStoreService() {
        return _client.getAppStoreService();
    }

    @Override
	public AsyncMatchService getAsyncMatchService() {
        return _client.getAsyncMatchService();
    }

    @Override
	public AuthenticationService getAuthenticationService() {
        return _client.getAuthenticationService();
    }

    @Override
	public ChatService getChatService() {
        return _client.getChatService();
    }

    @Override
	public LobbyService getLobbyService() {
        return _client.getLobbyService();
    }

    @Override
	public DataStreamService getDataStreamService() {
        return _client.getDataStreamService();
    }

    @Override
	public EntityService getEntityService() {
        return _client.getEntityService();
    }

    @Override
	public EventService getEventService() {
        return _client.getEventService();
    }

    @Override
	public FileService getFileService() {
        return _client.getFileService();
    }

    @Override
	public FriendService getFriendService() {
        return _client.getFriendService();
    }

    @Override
	public GamificationService getGamificationService() {
        return _client.getGamificationService();
    }

    @Override
	public GlobalAppService getGlobalAppService() {
        return _client.getGlobalAppService();
    }

    @Override
	public GlobalEntityService getGlobalEntityService() {
        return _client.getGlobalEntityService();
    }

    @Override
	public GlobalStatisticsService getGlobalStatisticsService() {
        return _client.getGlobalStatisticsService();
    }

    @Override
	public GroupService getGroupService() {
        return _client.getGroupService();
    }
    
    @Override
    public GroupFileService getGroupFileService(){
        return _client.getGroupFileService();
    }

    @Override
	public IdentityService getIdentityService() {
        return _client.getIdentityService();
    }

    @Override
	public MailService getMailService() {
        return _client.getMailService();
    }

    @Override
	public MessagingService getMessagingService() {
        return _client.getMessagingService();
    }

    @Override
	public MatchMakingService getMatchMakingService() {
        return _client.getMatchMakingService();
    }

    @Override
	public OneWayMatchService getOneWayMatchService() {
        return _client.getOneWayMatchService();
    }

    @Override
	public PlaybackStreamService getPlaybackStreamService() {
        return _client.getPlaybackStreamService();
    }

    @Override
	public PlayerStateService getPlayerStateService() {
        return _client.getPlayerStateService();
    }

    @Override
	public PlayerStatisticsService getPlayerStatisticsService() {
        return _client.getPlayerStatisticsService();
    }

    @Override
	public PlayerStatisticsEventService getPlayerStatisticsEventService() {
        return _client.getPlayerStatisticsEventService();
    }

    @Override
	public PresenceService getPresenceService()
    {
        return _client.getPresenceService();
    }

    @Override
	public VirtualCurrencyService getVirtualCurrencyService() {
        return _client.getVirtualCurrencyService();
    }

    @Override
	public ProfanityService getProfanityService() {
        return _client.getProfanityService();
    }

    @Override
	public PushNotificationService getPushNotificationService() {
        return _client.getPushNotificationService();
    }

    @Override
	public RedemptionCodeService getRedemptionCodeService() {
        return _client.getRedemptionCodeService();
    }

    @Override
	public RelayService getRelayService() {
        return _client.getRelayService();
    }

    @Override
	public RTTService getRTTService() {
        return _client.getRTTService();
    }

    @Override
	public S3HandlingService getS3HandlingService() {
        return _client.getS3HandlingService();
    }

    @Override
	public ScriptService getScriptService() {
        return _client.getScriptService();
    }

    @Override
	public SocialLeaderboardService getSocialLeaderboardService() {
        return _client.getSocialLeaderboardService();
    }

    @Override
	public SocialLeaderboardService getLeaderboardService() {
        return _client.getSocialLeaderboardService();
    }

    @Override
	public TimeService getTimeService() {
        return _client.getTimeService();
    }

    @Override
	public TournamentService getTournamentService() {
        return _client.getTournamentService();
    }

    @Override
	public GlobalFileService getGlobalFileService() {
        return _client.getGlobalFileService();
    }

    @Override
	public CustomEntityService getCustomEntityService() {
        return _client.getCustomEntityService();
    }

    @Override
	public ItemCatalogService getItemCatalogService() {
        return _client.getItemCatalogService();
    }

    @Override
	public UserItemsService getUserItemsService() {
        return _client.getUserItemsService();
    }
}
