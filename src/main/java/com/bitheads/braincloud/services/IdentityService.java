package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationIds;
import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class IdentityService {

    private enum Parameter {
        externalId,
        authenticationType,
        confirmAnonymous,
        authenticationToken,
        profileId,
        appId,
        gameId,
        forceSingleton,
        includePlayerSummaryData,
        levelName,
        forceCreate,
        releasePlatform,
        countryCode,
        languageCode,
        timeZoneOffset,
        externalAuthName,
        extraJson,
        peer,
        oldEmailAddress,
        newEmailAddress,
        updateContactEmail,
        blockchainConfig,
        publicKey
    }

    private BrainCloudClient _client;

    public IdentityService(BrainCloudClient client) {
        _client = client;
    }

    /**** FACEBOOK Methods ***/

    /**
     * Attach the user's Facebook credentials to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param facebookId The facebook id of the user
     * @param authenticationToken The validated token from the Facebook SDK
     *   (that will be further validated when sent to the bC service)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Errors to watch for:  SWITCHING_PROFILES - this means that the Facebook identity you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call AuthenticateFacebook().
     */
    public void attachFacebookIdentity(String facebookId, String authenticationToken, IServerCallback callback) {
        attachIdentity(facebookId, authenticationToken, AuthenticationType.Facebook, callback);
    }

    /**
     * Merge the profile associated with the provided Facebook credentials with the
     * current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param facebookId The facebook id of the user
     * @param authenticationToken The validated token from the Facebook SDK
     *   (that will be further validated when sent to the bC service)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     */
    public void mergeFacebookIdentity(String facebookId, String authenticationToken, IServerCallback callback) {
        mergeIdentity(facebookId, authenticationToken, AuthenticationType.Facebook, callback);
    }

    /**
     * Detach the Facebook identity from this profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param facebookId The facebook id of the user
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachFacebookIdentity(String facebookId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(facebookId, AuthenticationType.Facebook, continueAnon, callback);
    }

    /**
     * Attach the user's credentials to the current profile.
     *
     * Service Name - identity
     * Service Operation - Attach
     *
     * @param authenticationType Universal, Email, Facebook, etc
     * @param ids Auth IDs structure
     * @param extraJson Additional to piggyback along with the call, to be picked up by pre- or post- hooks. Leave empty string for no extraJson.
     * @param callback The method to be invoked when the server response is received
     *
     * Errors to watch for:  SWITCHING_PROFILES - this means that the identity you provided
     * already points to a different profile.  You will likely want to offer the user the
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call AuthenticateAdvanced().
     */
    public void attachAdvancedIdentity(AuthenticationType authenticationType, AuthenticationIds ids, String extraJson, IServerCallback callback) {
        
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), ids.externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.authenticationToken.name(), ids.authenticationToken);

            if (StringUtil.IsOptionalParameterValid(ids.authenticationSubType)) {
                data.put(Parameter.externalAuthName.name(), ids.authenticationSubType);
            }

            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.ATTACH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Merge the profile associated with the provided credentials with the
     * current profile.
     *
     * Service Name - identity
     * Service Operation - Merge
     *
     * @param authenticationType Universal, Email, Facebook, etc
     * @param ids Auth IDs structure
     * @param extraJson Additional to piggyback along with the call, to be picked up by pre- or post- hooks. Leave empty string for no extraJson.
     * @param callback The method to be invoked when the server response is received
     *
     */
    public void mergeAdvancedIdentity(AuthenticationType authenticationType, AuthenticationIds ids, String extraJson, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), ids.externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.authenticationToken.name(), ids.authenticationToken);

            if (StringUtil.IsOptionalParameterValid(ids.authenticationSubType)) {
                data.put(Parameter.externalAuthName.name(), ids.authenticationSubType);
            }

            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
    
            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.MERGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Detach the identity from this profile.
     *
     * Service Name - identity
     * Service Operation - Detach
     *
     * @param authenticationType Universal, Email, Facebook, etc
     * @param externalId User ID
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param extraJson Additional to piggyback along with the call, to be picked up by pre- or post- hooks. Leave empty string for no extraJson.
     * @param callback The method to be invoked when the server response is received
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set in_continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachAdvancedIdentity(AuthenticationType authenticationType, String externalId, boolean continueAnon, String extraJson, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.confirmAnonymous.name(), continueAnon);

            if (StringUtil.IsOptionalParameterValid(extraJson)) {
                data.put(Parameter.extraJson.name(), new JSONObject(extraJson));
            }
    
            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.DETACH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Attach the user's FacebookLimited credentials to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param facebookLimitedId The facebookLimited id of the user
     * @param authenticationToken The validated token from the Facebook SDK
     *   (that will be further validated when sent to the bC service)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Errors to watch for:  SWITCHING_PROFILES - this means that the FacebookLimited identity you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call AuthenticateFacebookLimited().
     */
    public void attachFacebookLimitedIdentity(String facebookLimitedId, String authenticationToken, IServerCallback callback) {
        attachIdentity(facebookLimitedId, authenticationToken, AuthenticationType.FacebookLimited, callback);
    }

    /**
     * Merge the profile associated with the provided FacebookLimited credentials with the
     * current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param facebookLimitedId The facebookLimited id of the user
     * @param authenticationToken The validated token from the Facebook SDK
     *   (that will be further validated when sent to the bC service)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     */
    public void mergeFacebookLimitedIdentity(String facebookLimitedId, String authenticationToken, IServerCallback callback) {
        mergeIdentity(facebookLimitedId, authenticationToken, AuthenticationType.FacebookLimited, callback);
    }

    /**
     * Detach the FacebookLimited identity from this profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param facebookLimitedId The facebookLimited id of the user
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachFacebookLimitedIdentity(String facebookLimitedId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(facebookLimitedId, AuthenticationType.FacebookLimited, continueAnon, callback);
    }

        /**** OCULUS Methods ***/

    /**
     * Attach the user's Oculus credentials to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param oculusId The Oculus id of the user
     * @param oculusNonce token from the Oculus SDK
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Errors to watch for:  SWITCHING_PROFILES - this means that the Oculus identity you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call AuthenticateOculus().
     */
    public void attachOculusIdentity(String oculusId, String oculusNonce, IServerCallback callback) {
        attachIdentity(oculusId, oculusNonce, AuthenticationType.Oculus, callback);
    }

    /**
     * Merge the profile associated with the provided Oculus credentials with the
     * current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param oculusId The oculus id of the user
     * @param oculusNonce token from the Oculus SDK
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     */
    public void mergeOculusIdentity(String oculusId, String oculusNonce, IServerCallback callback) {
        mergeIdentity(oculusId, oculusNonce, AuthenticationType.Oculus, callback);
    }

    /**
     * Detach the Oculus identity from this profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param oculusId The oculus id of the user
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachOculusIdentity(String oculusId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(oculusId, AuthenticationType.Oculus, continueAnon, callback);
    }

    /**** GAME CENTER Methods ***/

    /**
     * Attach a Game Center identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param gameCenterId The player's game center id  (use the playerID property from the local GKPlayer object)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Errors to watch for:  SWITCHING_PROFILES - this means that the Facebook identity you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call this method again.
     *
     */
    public void attachGameCenterIdentity(String gameCenterId, IServerCallback callback) {
        attachIdentity(gameCenterId, "", AuthenticationType.GameCenter, callback);
    }

    /**
     * Merge the profile associated with the specified Game Center identity with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param gameCenterId The player's game center id  (use the playerID property from the local GKPlayer object)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     */
    public void mergeGameCenterIdentity(String gameCenterId, IServerCallback callback) {
        mergeIdentity(gameCenterId, "", AuthenticationType.GameCenter, callback);
    }

    /**
     * Detach the Game Center identity from the current profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param gameCenterId The player's game center id  (use the playerID property from the local GKPlayer object)
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that 
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachGameCenterIdentity(String gameCenterId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(gameCenterId, AuthenticationType.GameCenter, continueAnon, callback);
    }

    /*** Google methods ***/

    /**
     * Attach a Google identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param googleId The google id of the player
     * @param authenticationToken  The validated token from the Google SDK
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     *
     * @returns Errors to watch for:  SWITCHING_PROFILES - this means that the Google identity you provided
     * already points to a different profile.  You will likely want to offer the player the
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call this method again.
     *
     */
    public void attachGoogleIdentity(String googleId, String authenticationToken, IServerCallback callback) {
        attachIdentity(googleId, authenticationToken, AuthenticationType.Google, callback);
    }

    /**
     * Merge the profile associated with the specified Google identity with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param googleId The google id of the player
     * @param authenticationToken  The validated token from the Google SDK
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     *
     * @returns
     *
     */
    public void mergeGoogleIdentity(String googleId, String authenticationToken, IServerCallback callback) {
        mergeIdentity(googleId, authenticationToken, AuthenticationType.Google, callback);
    }

    /**
     * Detach the Google identity from the current profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param googleId The google id of the player
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachGoogleIdentity(String googleId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(googleId, AuthenticationType.Google, continueAnon, callback);
    }

        /**
     * Attach a Google identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param googleOpenId The google id of the player
     * @param authenticationToken  The validated token from the Google SDK
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     *
     * @returns Errors to watch for:  SWITCHING_PROFILES - this means that the Google identity you provided
     * already points to a different profile.  You will likely want to offer the player the
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call this method again.
     *
     */
    public void attachGoogleOpenIdIdentity(String googleOpenId, String authenticationToken, IServerCallback callback) {
        attachIdentity(googleOpenId, authenticationToken, AuthenticationType.GoogleOpenId, callback);
    }

    /**
     * Merge the profile associated with the specified Google identity with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param googleOpenId The google id of the player
     * @param authenticationToken  The validated token from the Google SDK
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     *
     * @returns
     *
     */
    public void mergeGoogleOpenIdIdentity(String googleOpenId, String authenticationToken, IServerCallback callback) {
        mergeIdentity(googleOpenId, authenticationToken, AuthenticationType.GoogleOpenId, callback);
    }

    /**
     * Detach the Google identity from the current profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param googleOpenId The google id of the player
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachGoogleOpenIdIdentity(String googleOpenId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(googleOpenId, AuthenticationType.GoogleOpenId, continueAnon, callback);
    }

    /**
     * Attach a Google identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param appleId The google id of the player
     * @param authenticationToken  The validated token from the Google SDK
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     *
     * @returns Errors to watch for:  SWITCHING_PROFILES - this means that the Google identity you provided
     * already points to a different profile.  You will likely want to offer the player the
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call this method again.
     *
     */
    public void attachAppleIdentity(String appleId, String authenticationToken, IServerCallback callback) {
        attachIdentity(appleId, authenticationToken, AuthenticationType.Apple, callback);
    }

    /**
     * Merge the profile associated with the specified Google identity with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param appleId The google id of the player
     * @param authenticationToken  The validated token from the Google SDK
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     *
     * @returns
     *
     */
    public void mergeAppleIdentity(String appleId, String authenticationToken, IServerCallback callback) {
        mergeIdentity(appleId, authenticationToken, AuthenticationType.Apple, callback);
    }

    /**
     * Detach the Google identity from the current profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param appleId The google id of the player
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachAppleIdentity(String appleId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(appleId, AuthenticationType.Apple, continueAnon, callback);
    }


    /**** EMAIL AND PASSWORD Methods ***/

    /**
     * Attach a Email and Password identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param email The player's e-mail address
     * @param password The player's password
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *.
     * Errors to watch for:  SWITCHING_PROFILES - this means that the email address you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and then call AuthenticateEmailPassword().
     */
    public void attachEmailIdentity(String email, String password, IServerCallback callback) {
        attachIdentity(email, password, AuthenticationType.Email, callback);
    }

    /**
     * Merge the profile associated with the provided e=mail with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param email The player's e-mail address
     * @param password The player's password
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     */
    public void mergeEmailIdentity(String email, String password, IServerCallback callback) {
        mergeIdentity(email, password, AuthenticationType.Email, callback);
    }

    /**
     * Detach the e-mail identity from the current profile
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param email The player's e-mail address
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that 
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachEmailIdentity(String email, boolean continueAnon, IServerCallback callback) {
        detachIdentity(email, AuthenticationType.Email, continueAnon, callback);
    }

    /**** UNIVERSAL Identity ***/

    /**
     * Attach a Universal (userid + password) identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param userId The player's user ID
     * @param password The player's password
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *.
     * Errors to watch for:  SWITCHING_PROFILES - this means that the email address you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and then call AuthenticateEmailPassword().
     */
    public void attachUniversalIdentity(String userId, String password, IServerCallback callback) {
        attachIdentity(userId, password, AuthenticationType.Universal, callback);
    }

    /**
     * Merge the profile associated with the provided e=mail with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param userId The player's user ID
     * @param password The player's password
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     */
    public void mergeUniversalIdentity(String userId, String password, IServerCallback callback) {
        mergeIdentity(userId, password, AuthenticationType.Universal, callback);
    }

    /**
     * Detach the universal identity from the current profile
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param userId The player's user ID
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that 
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachUniversalIdentity(String userId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(userId, AuthenticationType.Universal, continueAnon, callback);
    }

    /*** STEAM Identity ***/

    /**
     * Attach a Steam (userid + steamsessionTicket) identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param steamId String representation of 64 bit steam id
     * @param sessionTicket The player's session ticket (hex encoded)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *.
     * Errors to watch for:  SWITCHING_PROFILES - this means that the email address you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and then call AuthenticateSteam().
     */
    public void attachSteamIdentity(String steamId, String sessionTicket, IServerCallback callback) {
        attachIdentity(steamId, sessionTicket, AuthenticationType.Steam, callback);
    }

    /**
     * Merge the profile associated with the provided steam userid with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param steamId String representation of 64 bit steam id
     * @param sessionTicket The player's session ticket (hex encoded)
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     */
    public void mergeSteamIdentity(String steamId, String sessionTicket, IServerCallback callback) {
        mergeIdentity(steamId, sessionTicket, AuthenticationType.Steam, callback);
    }

    /**
     * Detach the steam identity from the current profile
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param steamId String representation of 64 bit steam id
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that 
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachSteamIdentity(String steamId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(steamId, AuthenticationType.Steam, continueAnon, callback);
    }

    /**
     * Attach an Ultra identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param ultraUsername      it's what the user uses to log into the Ultra endpoint initially
     * @param ultraIdToken       The "id_token" taken from Ultra's JWT.
     * @param callback           The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *.
     * Errors to watch for:  SWITCHING_PROFILES - this means that the email address you provided
     * already points to a different profile.  You will likely want to offer the player the 
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and then call AuthenticateUltra().
     */
    public void attachUltraIdentity(String ultraUsername, String ultraIdToken, IServerCallback callback) {
        attachIdentity(ultraUsername, ultraIdToken, AuthenticationType.Ultra, callback);
    }

    /**
     * Merge the profile associated with the provided ultra account with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param ultraUsername      it's what the user uses to log into the Ultra endpoint initially
     * @param ultraIdToken       The "id_token" taken from Ultra's JWT.
     * @param callback           The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     */
    public void mergeUltraIdentity(String ultraUsername, String ultraIdToken, IServerCallback callback) {
        mergeIdentity(ultraUsername, ultraIdToken, AuthenticationType.Ultra, callback);
    }

    /**
     * Detach the ultra identity from the current profile
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param ultraUsername      it's what the user uses to log into the Ultra endpoint initially
     * @param continueAnon       Proceed even if the profile will revert to anonymous?
     * @param callback           The method to be invoked when the server response is received
     *
     * @returns performs the success callback on success, failure callback on failure
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that 
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachUltraIdentity(String ultraUsername, boolean continueAnon, IServerCallback callback) {
        detachIdentity(ultraUsername, AuthenticationType.Ultra, continueAnon, callback);
    }

    /**
     * Attach the user's Twitter credentials to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param twitterId The Twitter id of the user
     * @param authenticationToken The authentication token derrived from the twitter APIs
     * @param secret The secret given when attempting to link with Twitter
     * @param callback The method to be invoked when the server response is received
     *
     * Errors to watch for:  SWITCHING_PROFILES - this means that the Twitter identity you provided
     * already points to a different profile.  You will likely want to offer the player the
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call AuthenticateTwitter().
     */
    public void attachTwitterIdentity(
            String twitterId,
            String authenticationToken,
            String secret,
            IServerCallback callback) {
        String tokenSecretCombo = authenticationToken + ":" + secret;
        attachIdentity(twitterId, tokenSecretCombo, AuthenticationType.Twitter, callback);
    }

    /**
     * Merge the profile associated with the provided Twitter credentials with the
     * current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param twitterId The Twitter id of the user
     * @param authenticationToken The authentication token derrived from the twitter APIs
     * @param secret The secret given when attempting to link with Twitter
     * @param callback The method to be invoked when the server response is received
     *
     */
    public void mergeTwitterIdentity(
            String twitterId,
            String authenticationToken,
            String secret,
            IServerCallback callback) {
        String tokenSecretCombo = authenticationToken + ":" + secret;
        mergeIdentity(twitterId, tokenSecretCombo, AuthenticationType.Twitter, callback);
    }

    /**
     * Detach the Twitter identity from this profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param twitterId The Twitter id of the user
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachTwitterIdentity(String twitterId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(twitterId, AuthenticationType.Twitter, continueAnon, callback);
    }

    /*** Parse methods ***/

    /**
     * Attach a Parse identity to the current profile.
     *
     * Service Name - Identity
     * Service Operation - Attach
     *
     * @param parseId The parse id of the player
     * @param authenticationToken  The validated token from Parse
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     *
     * @returns Errors to watch for:  SWITCHING_PROFILES - this means that the Parse identity you provided
     * already points to a different profile.  You will likely want to offer the player the
     * choice to *SWITCH* to that profile, or *MERGE* the profiles.
     *
     * To switch profiles, call ClearSavedProfileID() and call this method again.
     *
     */
    public void attachParseIdentity(String parseId, String authenticationToken, IServerCallback callback) {
        attachIdentity(parseId, authenticationToken, AuthenticationType.Parse, callback);
    }

    /**
     * Merge the profile associated with the specified Parse identity with the current profile.
     *
     * Service Name - Identity
     * Service Operation - Merge
     *
     * @param parseId The parse id of the player
     * @param authenticationToken  The validated token from Parse
     * (that will be further validated when sent to the bC service)
     * @param callback The callback method
     */
    public void mergeParseIdentity(String parseId, String authenticationToken, IServerCallback callback) {
        mergeIdentity(parseId, authenticationToken, AuthenticationType.Parse, callback);
    }

    /**
     * Detach the Parse identity from the current profile.
     *
     * Service Name - Identity
     * Service Operation - Detach
     *
     * @param parseId The parse id of the player
     * @param continueAnon Proceed even if the profile will revert to anonymous?
     * @param callback The method to be invoked when the server response is received
     *
     * Watch for DOWNGRADING_TO_ANONYMOUS_ERROR - occurs if you set continueAnon to false, and
     * disconnecting this identity would result in the profile being anonymous (which means that
     * the profile wouldn't be retrievable if the user loses their device)
     */
    public void detachParseIdentity(String parseId, boolean continueAnon, IServerCallback callback) {
        detachIdentity(parseId, AuthenticationType.Parse, continueAnon, callback);
    }

    /**
     * Switch to a Child Profile
     *
     * Service Name - Identity
     * Service Operation - SWITCH_TO_CHILD_PROFILE
     *
     * @param childProfileId The profileId of the child profile to switch to
     * If null and forceCreate is true a new profile will be created
     * @param childAppId The appId of the child game to switch to
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback The method to be invoked when the server response is received
     */
    public void switchToChildProfile(String childProfileId, String childAppId, boolean forceCreate, IServerCallback callback) {
        switchToChildProfile(childProfileId, childAppId, forceCreate, false, callback);
    }

    /**
     * Switches to a child profile of an app when only one profile exists
     * If multiple profiles exist this returns an error
     *
     * Service Name - Identity
     * Service Operation - SWITCH_TO_CHILD_PROFILE
     *
     * @param childAppId The id of the child app to switch to
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback The method to be invoked when the server response is received
     */
    public void switchToSingletonChildProfile(String childAppId, boolean forceCreate, IServerCallback callback) {
        switchToChildProfile(null, childAppId, forceCreate, true, callback);
    }

    /**
     * Attaches a univeral id to the current profile with no login capability.
     *
     * Service Name - Identity
     * Service Operation - AttachNonLoginUniversalId
     *
     * @param externalId User id
     * @param callback The method to be invoked when the server response is received
     */
    public void attachNonLoginUniversalId(String externalId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.ATTACH_NONLOGIN_UNIVERSAL, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Updates univeral id of the current profile.
     *
     * Service Name - Identity
     * Service Operation - UpdateUniversalIdLogin
     *
     * @param externalId User id
     * @param callback The method to be invoked when the server response is received
     */
    public void updateUniversalIdLogin(String externalId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.UPDATE_UNIVERSAL_LOGIN, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Switch to a Parent Profile
     *
     * Service Name - Identity
     * Service Operation - SWITCH_TO_PARENT_PROFILE
     *
     * @param parentLevelName The level of the parent to switch to
     * If null and forceCreate is true a new profile will be created
     * @param callback The method to be invoked when the server response is received
     */
    public void switchToParentProfile(String parentLevelName, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.levelName.name(), parentLevelName);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.SWITCH_TO_PARENT_PROFILE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns a list of all child profiles in child Apps
     *
     * Service Name - Identity
     * Service Operation - GET_CHILD_PROFILES
     *
     * @param includeSummaryData Whether to return the summary friend data along with this call
     * @param callback The method to be invoked when the server response is received
     */
    public void getChildProfiles(boolean includeSummaryData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.includePlayerSummaryData.name(), includeSummaryData);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.GET_CHILD_PROFILES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve list of identities
     *
     * Service Name - Identity
     * Service Operation - GET_IDENTITIES
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getIdentities(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.GET_IDENTITIES, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Retrieve list of expired identities
     *
     * Service Name - Identity
     * Service Operation - GET_EXPIRED_IDENTITIES
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getExpiredIdentities(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.GET_EXPIRED_IDENTITIES, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Refreshes an identity for this player
     *
     * Service Name - identity
     * Service Operation - REFRESH_IDENTITY
     *
     * @param externalId User ID
     * @param authenticationToken Password or client side token
     * @param authenticationType Type of authentication
     * @param callback The method to be invoked when the server response is received
     */
    public void refreshIdentity(String externalId, String authenticationToken, AuthenticationType authenticationType, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.authenticationToken.name(), authenticationToken);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.REFRESH_IDENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Allows email identity email address to be changed
     *
     * Service Name - identity
     * Service Operation - CHANGE_EMAIL_IDENTITY
     *
     * @param oldEmailAddress Old email address
     * @param password Password for identity
     * @param newEmailAddress New email address
     * @param updateContactEmail Whether to update contact email in profile
     * @param callback The method to be invoked when the server response is received
     */
    public void changeEmailIdentity(String oldEmailAddress, String password, String newEmailAddress, boolean updateContactEmail, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.oldEmailAddress.name(), oldEmailAddress);
            data.put(Parameter.authenticationToken.name(), password);
            data.put(Parameter.newEmailAddress.name(), newEmailAddress);
            data.put(Parameter.updateContactEmail.name(), updateContactEmail);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.CHANGE_EMAIL_IDENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Attach a new identity to a parent app
     *
     * Service Name - identity
     * Service Operation - ATTACH_PARENT_WITH_IDENTITY
     *
     * @param externalId The users id for the new credentials
     * @param authenticationToken The password/token
     * @param authenticationType Type of identity
     * @param externalAuthName Optional - if attaching an external identity
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback The method to be invoked when the server response is received
     */
    public void attachParentWithIdentity(String externalId, String authenticationToken, AuthenticationType authenticationType,
                                         String externalAuthName, boolean forceCreate, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationToken.name(), authenticationToken);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.forceCreate.name(), forceCreate);
            if (StringUtil.IsOptionalParameterValid(externalAuthName))
                data.put(Parameter.externalAuthName.name(), externalAuthName);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.ATTACH_PARENT_WITH_IDENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Detaches parent from this player's profile
     *
     * Service Name - identity
     * Service Operation - DETACH_PARENT
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void detachParent(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.DETACH_PARENT, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Attaches a peer identity to this player's profile
     *
     * Service Name - identity
     * Service Operation - ATTACH_PEER_PROFILE
     *
     * @param peer Name of the peer to connect to
     * @param externalId The users id for the new credentials
     * @param authenticationToken The password/token
     * @param authenticationType Type of identity
     * @param externalAuthName Optional - if attaching an external identity
     * @param forceCreate Should a new profile be created if it does not exist?
     * @param callback The method to be invoked when the server response is received
     */
    public void attachPeerProfile(String peer, String externalId, String authenticationToken, AuthenticationType authenticationType,
                                  String externalAuthName, boolean forceCreate, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationToken.name(), authenticationToken);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.peer.name(), peer);
            data.put(Parameter.forceCreate.name(), forceCreate);
            if (StringUtil.IsOptionalParameterValid(externalAuthName))
                data.put(Parameter.externalAuthName.name(), externalAuthName);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.ATTACH_PEER_PROFILE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Detaches a peer identity from this player's profile
     *
     * Service Name - identity
     * Service Operation - DETACH_PEER
     *
     * @param peer Name of the peer to connect to
     * @param callback The method to be invoked when the server response is received
     */
    public void detachPeer(String peer, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.peer.name(), peer);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.DETACH_PEER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Attaches the given block chain public key identity to the current profile.
     *
     * Service Name - identity
     * Service Operation - ATTACH_BLOCKCHAIN_IDENTITY
     *
     * @param blockchainConfig
     * @param publicKey
     * @param callback The method to be invoked when the server response is received
     */
    public void attachBlockchainIdentity(String blockchainConfig, String publicKey, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.blockchainConfig.name(), blockchainConfig);
            data.put(Parameter.publicKey.name(), publicKey);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.ATTACH_BLOCKCHAIN_IDENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    /**
     * Detaches the blockchain identity to the current profile.     
     * Service Name - identity
     * Service Operation - DETACH_BLOCKCHAIN_IDENTITY
     *
     * @param blockchainConfig
     * @param publicKey
     * @param callback The method to be invoked when the server response is received
     */
    public void detachBlockchainIdentity(String blockchainConfig, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.blockchainConfig.name(), blockchainConfig);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.DETACH_BLOCKCHAIN_IDENTITY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }


    /**
     * Returns a list of peer profiles attached to this user
     *
     * Service Name - identity
     * Service Operation - GET_PEER_PROFILES
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getPeerProfiles(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.GET_PEER_PROFILES, null, callback);
        _client.sendRequest(sc);
    }

    /*** PRIVATE Methods ***/

    private void switchToChildProfile(String childProfileId,
                                      String childAppId,
                                      boolean forceCreate,
                                      boolean forceSingleton,
                                      IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            if (StringUtil.IsOptionalParameterValid(childProfileId)) {
                data.put(Parameter.profileId.name(), childProfileId);
            }
            data.put(Parameter.gameId.name(), childAppId);
            data.put(Parameter.forceCreate.name(), forceCreate);
            data.put(Parameter.forceSingleton.name(), forceSingleton);

            data.put(Parameter.releasePlatform.name(), _client.getReleasePlatform());
            data.put(Parameter.countryCode.name(), _client.getCountryCode());
            data.put(Parameter.languageCode.name(), _client.getLanguageCode());
            data.put(Parameter.timeZoneOffset.name(), _client.getTimeZoneOffset());

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.SWITCH_TO_CHILD_PROFILE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private void attachIdentity(String externalId, String authenticationToken, AuthenticationType authenticationType, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.authenticationToken.name(), authenticationToken);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.ATTACH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    private void mergeIdentity(String externalId, String authenticationToken, AuthenticationType authenticationType, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.authenticationToken.name(), authenticationToken);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.MERGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }

    private void detachIdentity(String externalId, AuthenticationType authenticationType,
                                boolean continueAnon, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
            data.put(Parameter.confirmAnonymous.name(), continueAnon);

            ServerCall sc = new ServerCall(ServiceName.identity, ServiceOperation.DETACH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException ignored) {
        }
    }
}
