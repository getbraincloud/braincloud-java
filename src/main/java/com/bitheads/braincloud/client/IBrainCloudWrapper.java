package com.bitheads.braincloud.client;

import org.json.JSONObject;

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

public interface IBrainCloudWrapper {

	//get the release platform 
	Platform getReleasePlatform();

	void setReleasePlatform(Platform releasePlatform);

	/**
	 * Returns a singleton instance of the BrainCloudClient, if this is the BrainCloudWrapper Singleton.
	 * Otherwise, return an instance of the BrainCloudClient, if this is an instance of the BrainCloudWrapper.
	 *
	 * @return A singleton instance of the BrainCloudClient.
	 */
	BrainCloudClient getClient();

	/**
	 * Method initializes the BrainCloudClient.
	 *
	 * @param appId      The app id
	 * @param secretKey  The secret key for your app
	 * @param appVersion The app version
	 */
	void initialize(String appId, String secretKey, String appVersion);

	/**
	 * Method initializes the BrainCloudClient.
	 *
	 * @param appId      The app id
	 * @param secretKey  The secret key for your app
	 * @param appVersion The app version
	 * @param serverUrl  The url to the brainCloud server
	 */
	void initialize(String appId, String secretKey, String appVersion, String serverUrl);

	/**
	 * Returns the stored profile id
	 *
	 * @return The stored profile id
	 */
	String getStoredProfileId();

	/**
	 * Sets the stored profile id
	 *
	 * @param profileId The profile id to set
	 */
	void setStoredProfileId(String profileId);

	/**
	 * Resets the profile id to empty string
	 */
	void resetStoredProfileId();

	/**
	 * Sets the stored anonymous id
	 *
	 * @param anonymousId The anonymous id to set
	 */
	void setStoredAnonymousId(String anonymousId);

	/**
	 * Resets the anonymous id to empty string
	 */
	void resetStoredAnonymousId();

	/**
	 * For non-anonymous authentication methods, a profile id will be passed in
	 * when this value is set to false. This will generate an error on the server
	 * if the profile id passed in does not match the profile associated with the
	 * authentication credentials. By default, this value is true.
	 *
	 * @param alwaysAllow Controls whether the profile id is passed in with
	 *                    non-anonymous authentications.
	 */
	void setAlwaysAllowProfileSwitch(boolean alwaysAllow);

	/**
	 * Returns the value for always allow profile switch
	 *
	 * @return Whether to always allow profile switches
	 */
	boolean getAlwaysAllowProfileSwitch();

	/**
	 * Authenticate a user anonymously with brainCloud - used for apps that
	 * don't want to bother the user to login, or for users who are sensitive to
	 * their privacy
	 *
	 * @param callback The callback handler
	 */
	void authenticateAnonymous(IServerCallback callback);

	/**
	 * Authenticate the user using a handoffId and an authentication token.
	 *
	 * @param handoffId   braincloud handoffId generated frim cloud script
	 * @param securityToken The authentication token
	 * @param callback   The callback handler
	 */
	void authenticateHandoff(String handoffId, String securityToken, IServerCallback callback);

	/**
	 * Authenticate the user using a handoffId and an authentication token.
	 *
	 * @param handoffCode generate in cloud code
	 * @param callback   The callback handler
	 */
	void authenticateSettopHandoff(String handoffCode, IServerCallback callback);

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
	void authenticateEmailPassword(String email, String password, boolean forceCreate, IServerCallback callback);

	/**
	 * Authenticate the user via cloud code (which in turn validates the supplied credentials against an external system).
	 * This allows the developer to extend brainCloud authentication to support other backend authentication systems.
	 * <p>
	 * Service Name - Authenticate
	 * Server Operation - Authenticate
	 *
	 * @param userId           	The user id
	 * @param token            	The user token (password etc)
	 * @param externalAuthName 	The name of the cloud script to call for external authentication
	 * @param forceCreate      	Should a new profile be created for this user if the account
	 *                         	does not exist?
	 * @param callback			The callback handler 
	 */
	void authenticateExternal(String userId, String token, String externalAuthName, boolean forceCreate,
			IServerCallback callback);

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
	void authenticateFacebook(String fbUserId, String fbAuthToken, boolean forceCreate, IServerCallback callback);

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
	void authenticateFacebookLimited(String fbLimitedUserId, String fbAuthToken, boolean forceCreate,
			IServerCallback callback);

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
	void authenticateGoogle(String googleUserId, String googleAuthToken, boolean forceCreate, IServerCallback callback);

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
	void authenticateGoogleOpenId(String googleOpenId, String googleAuthToken, boolean forceCreate,
			IServerCallback callback);

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
	void authenticateApple(String appleUserId, String token, boolean forceCreate, IServerCallback callback);

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
	void authenticateSteam(String steamUserId, String steamSessionTicket, boolean forceCreate,
			IServerCallback callback);

	/**
	 * Authenticate the user for Ultra.
	 *
	 * @param ultraUsername      it's what the user uses to log into the Ultra endpoint initially
	 * @param ultraIdToken       The "id_token" taken from Ultra's JWT.
	 * @param forceCreate        Should a new profile be created for this user if the account
	 *                           does not exist?
	 * @param callback           The callback handler
	 */
	void authenticateUltra(String ultraUsername, String ultraIdToken, boolean forceCreate, IServerCallback callback);

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
	void authenticateTwitter(String userId, String token, String secret, boolean forceCreate, IServerCallback callback);

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
	void authenticateUniversal(String userId, String userPassword, boolean forceCreate, IServerCallback callback);

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
	void authenticateAdvanced(AuthenticationType authenticationType, AuthenticationIds ids, boolean forceCreate,
			String extraJson, IServerCallback callback);

	/**
	 * Re-authenticates the user with brainCloud
	 *
	 * @param callback The callback handler
	 */
	void reconnect(IServerCallback callback);

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
	void resetEmailPassword(String email, IServerCallback callback);

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
	 * @param email       	The e-mail address of the user
	 * @param serviceParams	Parameters to send to the email service. see documentation for full
     *                      list. http://getbraincloud.com/apidocs/apiref/#capi-mail
	 * @param callback    	The callback handler
	 */
	void resetEmailPasswordAdvanced(String email, String serviceParams, IServerCallback callback);

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
	 * @param email       		The e-mail address of the user
	 * @param tokenTtlInMinutes	Token expiry time
	 * @param callback    		The callback handler
	 */
	void resetEmailPasswordWithExpiry(String email, int tokenTtlInMinutes, IServerCallback callback);

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
	 * @param email       		The e-mail address of the user
	 * @param serviceParams		Parameters to send to the email service. see documentation for full
     *                      	list. http://getbraincloud.com/apidocs/apiref/#capi-mail
	 * @param tokenTtlInMinutes	Token expiry time
	 * @param callback    		The callback handler
	 */
	void resetEmailPasswordAdvancedWithExpiry(String email, String serviceParams, Integer tokenTtlInMinutes,
			IServerCallback callback);

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
	void resetUniversalIdPassword(String universalId, IServerCallback callback);

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
	 * @param serviceParams	Parameters to send to the email service. see documentation for full
     *                      list. http://getbraincloud.com/apidocs/apiref/#capi-mail
	 * @param callback    	The callback handler
	 */
	void resetUniversalIdPasswordAdvanced(String universalId, String serviceParams, IServerCallback callback);

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
	 * @param tokenTtlInMinutes	Token expiry time
	 * @param callback    		The callback handler
	 */
	void resetUniversalIdPasswordWithExpiry(String universalId, int tokenTtlInMinutes, IServerCallback callback);

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
	 * @param serviceParams		parameters to send to the email service. see documentation for full
     *                      	list. http://getbraincloud.com/apidocs/apiref/#capi-mail
	 * @param tokenTtlInMinutes	Token expiry time
	 * @param callback    		The callback handler
	 */
	void resetUniversalIdPasswordAdvancedWithExpiry(String universalId, String serviceParams, Integer tokenTtlInMinutes,
			IServerCallback callback);

	void smartSwitchAuthenticateEmail(String email, String password, boolean forceCreate, IServerCallback callback);

	void smartSwitchAuthenticateExternal(String userId, String token, String externalAuthName, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateFacebook(String fbUserId, String fbAuthToken, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateOculus(String oculusUserId, String oculusNonce, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateGoogle(String googleUserId, String serverAuthCode, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateGoogleOpenId(String googleUserAccountEmail, String IdToken, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateApple(String appleUserId, String token, boolean forceCreate, IServerCallback callback);

	void smartSwitchAuthenticateSteam(String steamUserId, String sessionTicket, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateTwitter(String userId, String token, String secret, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateUniversal(String userId, String password, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateUltra(String ultraUserId, String ultraIdToken, boolean forceCreate,
			IServerCallback callback);

	void smartSwitchAuthenticateAdvanced(AuthenticationType authenticationType, AuthenticationIds ids,
			boolean forceCreate, String extraJson, IServerCallback callback);

	/**
	 * Run callbacks, to be called once per frame from your main thread
	 */
	void runCallbacks();

	/**
	 * The serverCallback() method returns server data back to the layer
	 * interfacing with the BrainCloud library.
	 *
	 * @param serviceName      - name of the requested service
	 * @param serviceOperation - requested operation
	 * @param jsonData         - returned data from the server
	 */
	void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject jsonData);

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
	void serverError(ServiceName serviceName, ServiceOperation serviceOperation, int statusCode, int reasonCode,
			String jsonError);

	// brainCloud Services
	AppStoreService getAppStoreService();

	AsyncMatchService getAsyncMatchService();

	AuthenticationService getAuthenticationService();

	ChatService getChatService();

	LobbyService getLobbyService();

	DataStreamService getDataStreamService();

	EntityService getEntityService();

	EventService getEventService();

	FileService getFileService();

	FriendService getFriendService();

	GamificationService getGamificationService();

	GlobalAppService getGlobalAppService();

	GlobalEntityService getGlobalEntityService();

	GlobalStatisticsService getGlobalStatisticsService();

	GroupService getGroupService();

	GroupFileService getGroupFileService();

	IdentityService getIdentityService();

	MailService getMailService();

	MessagingService getMessagingService();

	MatchMakingService getMatchMakingService();

	OneWayMatchService getOneWayMatchService();

	PlaybackStreamService getPlaybackStreamService();

	PlayerStateService getPlayerStateService();

	PlayerStatisticsService getPlayerStatisticsService();

	PlayerStatisticsEventService getPlayerStatisticsEventService();

	PresenceService getPresenceService();

	VirtualCurrencyService getVirtualCurrencyService();

	ProfanityService getProfanityService();

	PushNotificationService getPushNotificationService();

	RedemptionCodeService getRedemptionCodeService();

	RelayService getRelayService();

	RTTService getRTTService();

	S3HandlingService getS3HandlingService();

	ScriptService getScriptService();

	SocialLeaderboardService getSocialLeaderboardService();

	SocialLeaderboardService getLeaderboardService();

	TimeService getTimeService();

	TournamentService getTournamentService();

	GlobalFileService getGlobalFileService();

	CustomEntityService getCustomEntityService();

	ItemCatalogService getItemCatalogService();

	UserItemsService getUserItemsService();

}