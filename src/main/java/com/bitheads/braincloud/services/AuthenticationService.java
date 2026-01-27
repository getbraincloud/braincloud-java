// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitheads.braincloud.client.AuthenticationIds;
import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

public class AuthenticationService {

    private BrainCloudClient _client;

    private class PreviousAuthParams {
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
        IdToken,
        compressResponses
    }

    private String _anonymousId;
    private String _profileId;
    private boolean _compressResponses = true;

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

    public boolean getCompressResponses() {
        return _compressResponses;
    }

    public void setCompressResponses(boolean compressResponses) {
        _compressResponses = compressResponses;
    }

    public void retryPreviousAuthenticate(IServerCallback callback) {
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
     * anonymous installation id and most recently used profile id
     *
     * @param anonymousId The anonymous installation id that was generated for this
     *                    device
     * @param profileId   The id of the profile id that was most recently used by
     *                    the app (on this device)
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
     * 
     * @returns A unique Anonymous ID
     */
    public String generateAnonymousId() {
        return java.util.UUID.randomUUID().toString();
    }

    /*
     * Get server version.
     */
    public void getServerVersion(IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.appId.name(), _client.getAppId());

            ServerCall sc = new ServerCall(ServiceName.authenticationV2, ServiceOperation.GET_SERVER_VERSION, data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Authenticate a user anonymously with brainCloud - used for apps that don't
     * want to bother
     * the user to login, or for users who are sensitive to their privacy
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback    The method to be invoked when the server response is
     *                    received
     *
     */
    public void authenticateAnonymous(boolean forceCreate, IServerCallback callback) {
        authenticate(_anonymousId, "", AuthenticationType.Anonymous, null, forceCreate, null, callback);
    }

    /**
     * Overloaded for users not using wrapper, they will need to create their own
     * anonId.
     * Authenticate a user anonymously with brainCloud - used for apps that
     * don't want to bother the user to login, or for users who are sensitive to
     * their privacy.
     *
     * @param anonymousId The anonymous id of the user
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback    The callback handler
     */
    public void authenticateAnonymous(String anonymousId, boolean forceCreate, IServerCallback callback) {
        _anonymousId = anonymousId;
        authenticateAnonymous(forceCreate, callback);
    }

    /**
     * Authenticate the user with a custom Email and Password. Note that the client
     * app
     * is responsible for collecting (and storing) the e-mail and potentially
     * password
     * (for convenience) in the client data. For the greatest security,
     * force the user to re-enter their * password at each login.
     * (Or at least give them that option).
     *
     * Note that the password sent from the client to the server is protected via
     * SSL.
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param email       The e-mail address of the user
     * @param password    The password of the user
     * @param forceCreate Should a new profile be created for this user if the
     *                    account does not exist?
     * @param callback    The method to be invoked when the server response is
     *                    received
     *
     */
    public void authenticateEmailPassword(String email, String password, boolean forceCreate,
            IServerCallback callback) {
        authenticate(email, password, AuthenticationType.Email, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user with brainCloud using their Facebook Credentials
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param fbUserId    The facebook id of the user
     * @param fbAuthToken The validated token from the Facebook SDK
     *                    (that will be further validated when sent to the bC
     *                    service)
     * @param forceCreate Should a new profile be created for this user if the
     *                    account does not exist?
     * @param callback    The method to be invoked when the server response is
     *                    received
     *
     */
    public void authenticateFacebook(String fbUserId, String fbAuthToken, boolean forceCreate,
            IServerCallback callback) {
        authenticate(fbUserId, fbAuthToken, AuthenticationType.Facebook, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user with brainCloud using their Oculus Credentials
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param oculusUserId The oculus id of the user
     * @param oculusNonce  Oculus token from the Oculus SDK
     * @param forceCreate  Should a new profile be created for this user if the
     *                     account does not exist?
     * @param callback     The method to be invoked when the server response is
     *                     received
     *
     */
    public void authenticateOculus(String oculusUserId, String oculusNonce, boolean forceCreate,
            IServerCallback callback) {
        authenticate(oculusUserId, oculusNonce, AuthenticationType.Oculus, null, forceCreate, null, callback);
    }

    /*
     * Authenticate the user using a google userid(email address) and google
     * authentication token.
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param in_appleUserId String of the apple accounts user Id OR email
     * 
     * @param in_identityToken The authentication token confirming users identity
     * 
     * @param in_forceCreate Should a new profile be created for this user if the
     * account does not exist?
     * 
     * @param in_callback The method to be invoked when the server response is
     * received
     */
    public void authenticateApple(String appleUserId, String identityToken, boolean forceCreate,
            IServerCallback callback) {
        authenticate(appleUserId, identityToken, AuthenticationType.Apple, null, forceCreate, null, callback);
    }

    /*
     * Authenticate the user using a google userid(email address) and google
     * authentication token.
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param in_googleUserId String representation of google+ userid (email)
     * 
     * @param in_serverAuthCode The authentication token derived via the google
     * apis.
     * 
     * @param in_forceCreate Should a new profile be created for this user if the
     * account does not exist?
     * 
     * @param in_callback The method to be invoked when the server response is
     * received
     */
    public void authenticateGoogle(String googleUserId, String serverAuthCode, boolean forceCreate,
            IServerCallback callback) {
        authenticate(googleUserId, serverAuthCode, AuthenticationType.Google, null, forceCreate, null, callback);
    }

    /*
     * Authenticate the user using a google userid(email address) and google
     * authentication token.
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param in_googleUserAccountEmail String representation of google+ userid
     * (email)
     * 
     * @param in_IdToken The authentication token derived via the google apis.
     * 
     * @param in_forceCreate Should a new profile be created for this user if the
     * account does not exist?
     * 
     * @param in_callback The method to be invoked when the server response is
     * received
     */
    public void authenticateGoogleOpenId(String googleUserAccountEmail, String IdToken, boolean forceCreate,
            IServerCallback callback) {
        authenticate(googleUserAccountEmail, IdToken, AuthenticationType.GoogleOpenId, null, forceCreate, null,
                callback);
    }

    /*
     * Authenticate the user using a steam userid and session ticket (without any
     * validation on the userid).
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param in_userId String representation of 64 bit steam id
     * 
     * @param in_sessionticket The session ticket of the user (hex encoded)
     * 
     * @param in_forceCreate Should a new profile be created for this user if the
     * account does not exist?
     * 
     * @param in_callback The method to be invoked when the server response is
     * received
     */
    public void authenticateSteam(String steamUserId, String steamSessionTicket, boolean forceCreate,
            IServerCallback callback) {
        authenticate(steamUserId, steamSessionTicket, AuthenticationType.Steam, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user for Ultra.
     *
     * Service Name - Authenticate
     * Server Operation - Authenticate
     *
     * @param ultraUsername it's what the user uses to log into the Ultra endpoint
     *                      initially
     * @param ultraIdToken  The "id_token" taken from Ultra's JWT.
     * @param forceCreate   Should a new profile be created for this user if the
     *                      account does not exist?
     * @param callback      The method to be invoked when the server response is
     *                      received
     */
    public void authenticateUltra(String ultraUsername, String ultraIdToken, boolean forceCreate,
            IServerCallback callback) {
        authenticate(ultraUsername, ultraIdToken, AuthenticationType.Ultra, null, forceCreate, null, callback);
    }

    /**
     * Authenticate the user using a userid and password (without any validation on
     * the userid).
     * Similar to AuthenticateEmailPassword - except that that method has additional
     * features to
     * allow for e-mail validation, password resets, etc.
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param email       The e-mail address of the user
     * @param password    The password of the user
     * @param forceCreate Should a new profile be created for this user if the
     *                    account does not exist?
     * @param callback    The method to be invoked when the server response is
     *                    received
     */
    public void authenticateUniversal(String userId, String userPassword, boolean forceCreate,
            IServerCallback callback) {
        authenticate(userId, userPassword, AuthenticationType.Universal, null, forceCreate, null, callback);
    }

    /*
     * A generic Authenticate method that translates to the same as calling a
     * specific one, except it takes an extraJson
     * that will be passed along to pre- or post- hooks.
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param authenticationType Universal, Email, Facebook, etc
     * 
     * @param ids Auth IDs object
     * 
     * @param forceCreate Should a new profile be created for this user if the
     * account does not exist?
     * 
     * @param extraJson Additional to piggyback along with the call, to be picked up
     * by pre- or post- hooks. Leave empty string for no extraJson.
     * 
     * @param callback The method to be invoked when the server response is received
     */
    public void authenticateAdvanced(AuthenticationType authenticationType, AuthenticationIds ids, boolean forceCreate,
            String extraJson, IServerCallback callback) {
        authenticate(ids.externalId, ids.authenticationToken, AuthenticationType.Universal, ids.authenticationSubType,
                forceCreate, extraJson, callback);
    }

    /*
     * Authenticate the user using a Pase userid and authentication token
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param in_userId String representation of Parse userid
     * 
     * @param in_token The authentication token
     * 
     * @param in_forceCreate Should a new profile be created for this user if the
     * account does not exist?
     * 
     * @param in_callback The method to be invoked when the server response is
     * received
     */
    public void authenticateParse(String userId, String authenticationToken, boolean forceCreate,
            IServerCallback callback) {
        authenticate(userId, authenticationToken, AuthenticationType.Parse, null, forceCreate, null, callback);
    }

    /*
     * Authenticate the user using a handoffId and authentication token
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param in_handoffId braincloud handoff id generated from cloud script
     * 
     * @param in_securityToken The authentication token
     * 
     * @param in_callback The method to be invoked when the server response is
     * received
     */
    public void authenticateHandoff(String handoffId, String securityToken, IServerCallback callback) {
        authenticate(handoffId, securityToken, AuthenticationType.Handoff, null, false, null, callback);
    }

    /*
     * Authenticate the user using a handoffCode
     *
     * Service Name - Authenticate
     * Service Operation - Authenticate
     *
     * @param in_handoffCode the code we generate in cloudcode
     * 
     * @param in_callback The method to be invoked when the server response is
     * received
     */
    public void authenticateSettopHandoff(String handoffCode, IServerCallback callback) {
        authenticate(handoffCode, "", AuthenticationType.SettopHandoff, null, false, null, callback);
    }

    /**
     * Reset Email password - Sends a password reset email to the specified address
     *
     * Service Name - Authenticate
     * Operation - ResetEmailPassword
     *
     * @param externalId The email address to send the reset email to.
     * @param callback   The method to be invoked when the server response is
     *                   received
     *
     *                   Note the follow error reason codes:
     *
     *                   SECURITY_ERROR (40209) - If the email address cannot be
     *                   found.
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
     * Reset Email password with service parameters - Sends a password reset email
     * to
     * the specified address
     *
     * Service Name - Authenticate
     * Operation - ResetEmailPasswordAdvanced
     *
     * @param appId         the applicationId
     * @param emailAddress  The email address to send the reset email to.
     * @param serviceParams - parameters to send to the email service. See
     *                      documentation for
     *                      full list.
     *                      http://getbraincloud.com/apidocs/apiref/#capi-mail
     * @param callback      The method to be invoked when the server response is
     *                      received
     *
     *                      Note the follow error reason codes:
     *
     *                      SECURITY_ERROR (40209) - If the email address cannot be
     *                      found.
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
     * Reset Email password - Sends a password reset email to the specified address
     *
     * Service Name - Authenticate
     * Operation - ResetEmailPassword
     *
     * @param externalId The email address to send the reset email to.
     * @param callback   The method to be invoked when the server response is
     *                   received
     *
     *                   Note the follow error reason codes:
     *
     *                   SECURITY_ERROR (40209) - If the email address cannot be
     *                   found.
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
     * Reset Email password with service parameters - Sends a password reset email
     * to
     * the specified address
     *
     * Service Name - Authenticate
     * Operation - ResetEmailPasswordAdvanced
     *
     * @param appId         the applicationId
     * @param emailAddress  The email address to send the reset email to.
     * @param serviceParams - parameters to send to the email service. See
     *                      documentation for
     *                      full list.
     *                      http://getbraincloud.com/apidocs/apiref/#capi-mail
     * @param callback      The method to be invoked when the server response is
     *                      received
     *
     *                      Note the follow error reason codes:
     *
     *                      SECURITY_ERROR (40209) - If the email address cannot be
     *                      found.
     */
    public void resetEmailPasswordAdvancedWithExpiry(String email, String serviceParams, int tokenTtlInMinutes,
            IServerCallback callback) {
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
     * Resets Universal ID password
     *
     * Service Name - Authenticate
     * Operation - ResetUniversalIdPassword
     *
     * @param appId       the applicationId
     * @param universalId the universal Id in question
     * @param callback    The method to be invoked when the server response is
     *                    received
     *
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
     * Advanced Universal ID password reset using templates
     *
     * Service Name - Authenticate
     * Operation - ResetUniversalIdPassword
     *
     * @param appId         the applicationId
     * @param universalId   the universal Id in question
     * @param serviceParams - parameters to send to the email service.
     * @param callback      The method to be invoked when the server response is
     *                      received
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
     * Resets Universal ID password
     *
     * Service Name - Authenticate
     * Operation - ResetUniversalIdPassword
     *
     * @param appId       the applicationId
     * @param universalId the universal Id in question
     * @param callback    The method to be invoked when the server response is
     *                    received
     *
     */
    public void resetUniversalIdPasswordWithExpiry(String universalId, int tokenTtlInMinutes,
            IServerCallback callback) {
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
     * Advanced Universal ID password reset using templates
     *
     * Service Name - Authenticate
     * Operation - ResetUniversalIdPassword
     *
     * @param appId         the applicationId
     * @param universalId   the universal Id in question
     * @param serviceParams - parameters to send to the email service.
     * @param callback      The method to be invoked when the server response is
     *                      received
     *
     */
    public void resetUniversalIdPasswordAdvancedWithExpiry(String universalId, String serviceParams,
            int tokenTtlInMinutes, IServerCallback callback) {
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
            message.put(Parameter.compressResponses.name(), _compressResponses);
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
