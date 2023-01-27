package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationIds;
import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationService {

    private BrainCloudClient _client;

    private class PreviousAuthParams
    {
        public String externalId = "";
        public String authenticationToken = "";
        public AuthenticationType authenticationType = AuthenticationType.Unknown;
        public String externalAuthName = "";
        public boolean forceCreate = true;
        public String extraJson;
    };

    private PreviousAuthParams _previousAuthParams = new PreviousAuthParams();

    public AuthenticationService(BrainCloudClient client) {
        _client = client;
    }

    private enum Parameter {
        externalId,
        emailAddress,
        authenticationToken,
        authenticationType,
        tokenTtlInMinutes,
        appId,
        gameId,
        forceCreate,
        releasePlatform,
        clientLibVersion,
        externalAuthName,
        extraJson,
        profileId,
        anonymousId,
        gameVersion,
        countryCode,
        serviceParams,
        languageCode,
        timeZoneOffset,
        universalId,
        handoffCode,
        serverAuthCode,
        googleUserId,
        googleUserAccountEmail,
        IdToken
    }

    private String _anonymousId;
    private String _profileId;

    public String getAnonymousId() {
        return _anonymousId;
    }

    public void setAnonymousId(String anonymousId) {
        _anonymousId = anonymousId;
    }

    public String getProfileId() {
        return _profileId;
    }

    public void setProfileId(String profileId) {
        _profileId = profileId;
    }

    public void retryPreviousAuthenticate(IServerCallback callback)
    {
        authenticate(_previousAuthParams.externalId,
                     _previousAuthParams.authenticationToken,
                     _previousAuthParams.authenticationType,
                     _previousAuthParams.externalAuthName,
                     _previousAuthParams.forceCreate,
                     _previousAuthParams.extraJson,
                     callback);
    }

    /**
     * Initialize - initializes the identity service with a saved
     * anonymous installation ID and most recently used profile ID
     *
     * @param anonymousId The anonymous installation id that was generated for this device
     * @param profileId   The id of the profile id that was most recently used by the app (on this device)
     */
    public void initialize(String profileId, String anonymousId) {
        _anonymousId = anonymousId;
        _profileId = profileId;
    }

    /**
     * Used to clear the saved profile id - to use in cases when the user is
     * attempting to switch to a different game profile.
     */
    public void clearSavedProfileId() {
        _profileId = "";
    }

    /**
     * Used to create the anonymous installation id for the brainCloud profile.
     * @return A unique Anonymous ID
     */
    public String generateAnonymousId() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * Authenticate a user anonymously with brainCloud - used for apps that
     * don't want to bother the user to login, or for users who are sensitive to
     * their privacy
     *
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback    The callback handler
     */
    public void authenticateAnonymous(boolean forceCreate, IServerCallback callback) {
        authenticate(_anonymousId, "", AuthenticationType.Anonymous, null, forceCreate, null, callback);
    }

    /**
     * Overloaded for users not using wrapper, they will need to create their own anonId.
     * Authenticate a user anonymously with brainCloud - used for apps that
     * don't want to bother the user to login, or for users who are sensitive to
     * their privacy.
     *
     * @param anonymousId 
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback    The callback handler
     */
    public void authenticateAnonymous(String anonymousId, boolean forceCreate, IServerCallback callback) {
        _anonymousId = anonymousId;
        authenticateAnonymous(forceCreate, callback);
    }

    /**
     * Authenticate the user with a custom Email and Password. Note that the
     * client app is responsible for collecting (and storing) the e-mail and
     * potentially password (for convenience) in the client data. For the
     * greatest security, force the user to re-enter their * password at each
     * login. (Or at least give them that option).
     * <p/>
     * Note that the password sent from the client to the server is protected
     * via SSL.
     *
     * @param email       The e-mail address of the user
     * @param password    The password of the user
     * @param forceCreate Should a new profile be created for this user if the account
     *                    does not exist?
     * @param callback    The callback handler
     */
    public void authenticateEmailPassword(String email, String password, boolean forceCreate, IServerCallback callback) {
        authenticate(email, password, AuthenticationType.Email, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user via cloud code (which in turn validates the supplied credentials against an external system).
     * This allows the developer to extend brainCloud authentication to support other backend authentication systems.
     * <p/>
     * Service Name - Authenticate
     * Server Operation - Authenticate
     *
     * @param userId           The user id
     * @param token            The user token (password etc)
     * @param externalAuthName The name of the cloud script to call for external authentication
     * @param forceCreate      Should a new profile be created for this user if the account
     *                         does not exist?
     */
    public void authenticateExternal(
            String userId,
            String token,
            String externalAuthName,
            boolean forceCreate,
            IServerCallback callback) {
        authenticate(userId, token, AuthenticationType.External, externalAuthName, forceCreate, null, callback);
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
    public void authenticateFacebook(String fbUserId, String fbAuthToken, boolean forceCreate, IServerCallback callback) {
        authenticate(fbUserId, fbAuthToken, AuthenticationType.Facebook, null, forceCreate, null, callback);
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
    public void authenticateFacebookLimited(String fbLimitedUserId, String fbAuthToken, boolean forceCreate, IServerCallback callback) {
        authenticate(fbLimitedUserId, fbAuthToken, AuthenticationType.FacebookLimited, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user with brainCloud using their Oculus Credentials
     *
     * @param oculusUserId    The oculus id of the user
     * @param oculusNonce Validation token from oculus gotten through the oculus sdk
     * @param forceCreate Should a new profile be created for this user if the account
     *                    does not exist?
     * @param callback    The callback handler
     */
    public void authenticateOculus(String oculusUserId, String oculusNonce, boolean forceCreate, IServerCallback callback) {
        authenticate(oculusUserId, oculusNonce, AuthenticationType.Oculus, null, forceCreate, null, callback);
    }

    /**
     *Authenticate the user using an apple id
     *
     * @param appleUserId  This can be the user id OR the email of the user for the account
     * @param identityToken The token confirming the user's identity
     * @param forceCreate     Should a new profile be created for this user if the account
     *                        does not exist?
     * @param callback        The callback handler
     */
    public void authenticateApple(String appleUserId, String identityToken, boolean forceCreate, IServerCallback callback) {
        authenticate(appleUserId, identityToken, AuthenticationType.Apple, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user using a google userid(email address) and google
     * authentication token.
     *
     * @param googleUserId    String representation of google+ userId. Gotten with calls like RequestUserId
     * @param serverAuthCode The server authentication token derived via the google apis. Gotten with calls like RequestServerAuthCode
     * @param forceCreate     Should a new profile be created for this user if the account
     *                        does not exist?
     * @param callback        The callback handler
     */
    public void authenticateGoogle(String googleUserId, String serverAuthCode, boolean forceCreate, IServerCallback callback) {
        authenticate(googleUserId, serverAuthCode, AuthenticationType.Google, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user using a google userid(email address) and google
     * openid token.
     *
     * @param googleUserAccountEmail The email associated with the google user
     * @param IdToken The id token of the google account. Can get with calls like requestIdToken
     * @param forceCreate     Should a new profile be created for this user if the account
     *                        does not exist?
     * @param callback        The callback handler
     */
    public void authenticateGoogleOpenId(String googleUserAccountEmail, String IdToken, boolean forceCreate, IServerCallback callback) {
        authenticate(googleUserAccountEmail, IdToken, AuthenticationType.GoogleOpenId, null, forceCreate, null, callback);
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
    public void authenticateSteam(String steamUserId, String steamSessionTicket, boolean forceCreate, IServerCallback callback) {
        authenticate(steamUserId, steamSessionTicket, AuthenticationType.Steam, null, forceCreate, null, callback);
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
    public void authenticateUltra(String ultraUsername, String ultraIdToken, boolean forceCreate, IServerCallback callback) {
        authenticate(ultraUsername, ultraIdToken, AuthenticationType.Ultra, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user using a Twitter userid, authentication token, and secret from Twitter.
     * <p/>
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param userId      String representation of Twitter userid
     * @param token       The authentication token derived via the Twitter apis.
     * @param secret      The secret given when attempting to link with Twitter
     * @param forceCreate Should a new profile be created for this user if the account does not exist?
     * @param callback    The callback handler
     */
    public void authenticateTwitter(String userId,
                                    String token,
                                    String secret,
                                    boolean forceCreate,
                                    IServerCallback callback) {
        String tokenSecretCombo = token + ":" + secret;
        authenticate(userId, tokenSecretCombo, AuthenticationType.Twitter, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user using a userid and password (without any validation
     * on the userid). Similar to AuthenticateEmailPassword - except that that
     * method has additional features to allow for e-mail validation, password
     * resets, etc.
     *
     * @param userId       The id of the user
     * @param userPassword The password of the user
     * @param forceCreate  Should a new profile be created for this user if the account
     *                     does not exist?
     * @param callback     The callback handler
     */
    public void authenticateUniversal(String userId, String userPassword, boolean forceCreate, IServerCallback callback) {
        authenticate(userId, userPassword, AuthenticationType.Universal, null, forceCreate, null, callback);
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
    public void authenticateAdvanced(AuthenticationType authenticationType, AuthenticationIds ids, boolean forceCreate, String extraJson, IServerCallback callback) {
        authenticate(ids.externalId, ids.authenticationToken, AuthenticationType.Universal, ids.authenticationSubType, forceCreate, extraJson, callback);
    }

    /**
     * Authenticate the user using a Parse user ID authentication token.
     *
     * @param userId    String representation of Parse user ID
     * @param authenticationToken The authentication token derived via the google apis.
     * @param forceCreate     Should a new profile be created for this user if the account
     *                        does not exist?
     * @param callback        The callback handler
     */
    public void authenticateParse(String userId, String authenticationToken, boolean forceCreate, IServerCallback callback) {
        authenticate(userId, authenticationToken, AuthenticationType.Parse, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user using a handoffId and an authentication token.
     *
     * @param handoffId   braincloud handoffId generated frim cloud script
     * @param securityToken The authentication token
     * @param callback   The callback handler
     */
    public void authenticateHandoff(String handoffId, String securityToken, IServerCallback callback) {
        authenticate(handoffId, securityToken, AuthenticationType.Handoff, null, false, null, callback);
    }

    /**
     * Authenticate the user using a handoffId and an authentication token.
     *
     * @param handoffCode generate in cloud code
     * @param callback   The callback handler
     */
    public void authenticateSettopHandoff(String handoffCode, IServerCallback callback) {
        authenticate(handoffCode, "", AuthenticationType.SettopHandoff, null, false, null, callback);
    }

    /**
     * Reset Email password - Sends a password reset email to the specified
     * address
     *
     * @param email    The email address to send the reset email to.
     * @param callback The callback handler
     *
     * Note the follow error reason codes:
     * SECURITY_ERROR (40209) - If the email address cannot be found.
     */
    public void resetEmailPassword(String email, IServerCallback callback) {
        try {
            JSONObject message = new JSONObject();
            message.put(Parameter.externalId.name(), email);
            message.put(Parameter.gameId.name(), _client.getAppId());

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_EMAIL_PASSWORD, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Reset Email password with service parameters - sends a password reset email to the
     * specified address
     *
     * @param email the email address to send the reset email to
     * @param serviceParams parameters to send to the email service. see documentation for full
     *                      list. http://getbraincloud.com/apidocs/apiref/#capi-mail
     * @param callback The callback handler
     *
     * Note the follow error reason codes:
     * SECURITY_ERROR (40209) - If the email address cannot be found.
     */
    public void resetEmailPasswordAdvanced(String email, String serviceParams, IServerCallback callback) {
        try {
            String appId = _client.getAppId();

            JSONObject message = new JSONObject();
            message.put(Parameter.gameId.name(), appId);
            message.put(Parameter.emailAddress.name(), email);
            message.put(Parameter.serviceParams.name(), new JSONObject(serviceParams));

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_EMAIL_PASSWORD_ADVANCED, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Reset Email password with expiry - Sends a password reset email to the specified
     * address
     *
     * @param email    The email address to send the reset email to.
     * @param tokenTtlInMinutes,   expiry token in mins
     * @param callback The callback handler
     *
     * Note the follow error reason codes:
     * SECURITY_ERROR (40209) - If the email address cannot be found.
     */
    public void resetEmailPasswordWithExpiry(String email, int tokenTtlInMinutes, IServerCallback callback) {
        try {
            JSONObject message = new JSONObject();
            message.put(Parameter.externalId.name(), email);
            message.put(Parameter.tokenTtlInMinutes.name(), tokenTtlInMinutes);
            message.put(Parameter.gameId.name(), _client.getAppId());

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_EMAIL_PASSWORD_WITH_EXPIRY, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Reset Email password with service parameters and expiry token - sends a password reset email to the
     * specified address
     *
     * @param email the email address to send the reset email to
     * @param serviceParams parameters to send to the email service. see documentation for full
     *                      list. http://getbraincloud.com/apidocs/apiref/#capi-mail
     * @param tokenTtlInMinutes,   expiry token in mins
     * @param callback The callback handler
     *
     * Note the follow error reason codes:
     * SECURITY_ERROR (40209) - If the email address cannot be found.
     */
    public void resetEmailPasswordAdvancedWithExpiry(String email, String serviceParams, int tokenTtlInMinutes, IServerCallback callback) {
        try {
            String appId = _client.getAppId();

            JSONObject message = new JSONObject();
            message.put(Parameter.gameId.name(), appId);
            message.put(Parameter.emailAddress.name(), email);
            message.put(Parameter.serviceParams.name(), new JSONObject(serviceParams));
            message.put(Parameter.tokenTtlInMinutes.name(), tokenTtlInMinutes);

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_EMAIL_PASSWORD_ADVANCED_WITH_EXPIRY, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Reset password of universalId
     *
     * @param universalId    The users universalId
     * @param callback The callback handler
     */
    public void resetUniversalIdPassword(String universalId, IServerCallback callback) {
        try {
            JSONObject message = new JSONObject();
            message.put(Parameter.universalId.name(), universalId);
            message.put(Parameter.gameId.name(), _client.getAppId());

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_UNIVERSAL_ID_PASSWORD, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Reset universal Ids password of universalId with template options
     *
     * @param universalId the email address to send the reset email to
     * @param serviceParams parameters to send to the service. see documentation for full
     *                      list. http://getbraincloud.com/apidocs/apiref/#capi-mail
     * @param callback The callback handler
     *
     */
    public void resetUniversalIdPasswordAdvanced(String universalId, String serviceParams, IServerCallback callback) {
        try {
            String appId = _client.getAppId();

            JSONObject message = new JSONObject();
            message.put(Parameter.gameId.name(), appId);
            message.put(Parameter.universalId.name(), universalId);
            message.put(Parameter.serviceParams.name(), new JSONObject(serviceParams));

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_UNIVERSAL_ID_PASSWORD_ADVANCED, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Reset password of universalId with expiry token
     *
     * @param universalId    The users universalId
     * @param tokenTtlInMinutes,   expiry token in mins
     * @param callback The callback handler
     */
    public void resetUniversalIdPasswordWithExpiry(String universalId, int tokenTtlInMinutes, IServerCallback callback) {
        try {
            JSONObject message = new JSONObject();
            message.put(Parameter.universalId.name(), universalId);
            message.put(Parameter.gameId.name(), _client.getAppId());
            message.put(Parameter.tokenTtlInMinutes.name(), tokenTtlInMinutes);

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_UNIVERSAL_ID_PASSWORD_WITH_EXPIRY, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Reset universal Ids password of universalId with template options with expiry token
     *
     * @param universalId the email address to send the reset email to
     * @param serviceParams parameters to send to the service. see documentation for full
     *                      list. http://getbraincloud.com/apidocs/apiref/#capi-mail
     * @param tokenTtlInMinutes,   expiry token in mins
     * @param callback The callback handler
     *
     */
    public void resetUniversalIdPasswordAdvancedWithExpiry(String universalId, String serviceParams, int tokenTtlInMinutes, IServerCallback callback) {
        try {
            String appId = _client.getAppId();

            JSONObject message = new JSONObject();
            message.put(Parameter.gameId.name(), appId);
            message.put(Parameter.universalId.name(), universalId);
            message.put(Parameter.serviceParams.name(), new JSONObject(serviceParams));
            message.put(Parameter.tokenTtlInMinutes.name(), tokenTtlInMinutes);

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.RESET_UNIVERSAL_ID_PASSWORD_ADVANCED_WITH_EXPIRY, message,
                    callback);
            _client.sendRequest(serverCall);
        } catch (JSONException ignored) {
        }
    }

    private void authenticate(
            String externalId,
            String authenticationToken,
            AuthenticationType authenticationType,
            String externalAuthName,
            boolean forceCreate,
            String extraJson,
            IServerCallback callback) {
        try {
            _previousAuthParams.externalId = externalId == null ? "" : externalId;
            _previousAuthParams.authenticationToken = authenticationToken == null ? "" : authenticationToken;
            _previousAuthParams.authenticationType = authenticationType;
            _previousAuthParams.externalAuthName = externalAuthName == null ? "" : externalAuthName;
            _previousAuthParams.forceCreate = forceCreate;
            _previousAuthParams.extraJson = extraJson;

            JSONObject message = new JSONObject();
            message.put(Parameter.externalId.name(), externalId);
            message.put(Parameter.authenticationToken.name(), authenticationToken);
            message.put(Parameter.authenticationType.name(), authenticationType.toString());
            message.put(Parameter.forceCreate.name(), forceCreate);

            message.put(Parameter.profileId.name(), _profileId);
            message.put(Parameter.anonymousId.name(), _anonymousId);
            message.put(Parameter.gameId.name(), _client.getAppId());
            message.put(Parameter.releasePlatform.name(), _client.getReleasePlatform());
            message.put(Parameter.gameVersion.name(), _client.getAppVersion());
            message.put(Parameter.clientLibVersion.name(), _client.getBrainCloudVersion());

            if (StringUtil.IsOptionalParameterValid(externalAuthName)) {
                message.put(Parameter.externalAuthName.name(), externalAuthName);
            }
            message.put(Parameter.countryCode.name(), _client.getCountryCode());
            message.put(Parameter.languageCode.name(), _client.getLanguageCode());
            message.put(Parameter.timeZoneOffset.name(), _client.getTimeZoneOffset());
            message.put("clientLib", "java");

            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                message.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }

            ServerCall serverCall = new ServerCall(
                    ServiceName.authenticationV2,
                    ServiceOperation.AUTHENTICATE, message, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
