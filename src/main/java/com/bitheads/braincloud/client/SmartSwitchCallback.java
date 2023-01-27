package com.bitheads.braincloud.client;

import com.bitheads.braincloud.client.IServerCallback;
import org.json.JSONObject;


/**
* Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
* In event the current session was previously an anonymous account, the smart switch will delete that profile.
* Use this function to keep a clean designflow from anonymous to signed profiles
 */
public class SmartSwitchCallback implements IServerCallback 
{
    protected BrainCloudWrapper _wrapper;
    protected IServerCallback _callback;

    public SmartSwitchCallback(BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
    {
        _wrapper = in_wrapper;
        _callback = in_callback;
    }

    public void clearIds() 
    {
        _wrapper.resetStoredAnonymousId();
        _wrapper.resetStoredProfileId();
        _wrapper.getClient().getAuthenticationService().clearSavedProfileId();
    }

    public void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject jsonData) 
    {
        _callback.serverCallback(serviceName, serviceOperation, jsonData);
    }

    public void serverError(ServiceName in_serviceName, ServiceOperation in_serviceOperation, int in_statusCode, int in_reasonCode, String jsonString) 
    {
        _callback.serverError(in_serviceName, in_serviceOperation, in_statusCode, in_reasonCode, jsonString);
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user with a custom Email and Password.  Note that the client app
    * is responsible for collecting (and storing) the e-mail and potentially password
    * (for convenience) in the client data.  For the greatest security,
    * force the user to re-enter their * password at each login.
    * (Or at least give them that option).
    *
    * Note that the password sent from the client to the server is protected via SSL.
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_email  The e-mail address of the user
    * @param in_password  The password of the user
    * @param forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    */
    public class SmartSwitchEmail extends SmartSwitchCallback 
    {
        private String _email;
        private String _password;
        private boolean _forceCreate;

        public SmartSwitchEmail(BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchEmail(String in_email, String in_password, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);
            _email = in_email;
            _password = in_password;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString) 
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateEmailPassword(_email, _password, _forceCreate, _callback);
        }
    }

    /**
     * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
     * In event the current session was previously an anonymous account, the smart switch will delete that profile.
     * Use this function to keep a clean designflow from anonymous to signed profiles
     *
     * Authenticate the user via cloud code (which in turn validates the supplied credentials against an external system).
     * This allows the developer to extend brainCloud authentication to support other backend authentication systems.
     *
     * Service Name - Authenticate
     * Server Operation - Authenticate
     *
     * @param in_userid The user id
     * @param in_token The user token (password etc)
     * @param in_externalAuthName The name of the cloud script to call for external authentication
     * @param in_force Should a new profile be created for this user if the account does not exist?
     *
     * @returns   performs the in_success callback on success, in_failure callback on failure
     */
    public class SmartSwitchExternal extends SmartSwitchCallback
    {
        private String _userId;
        private String _token;
        private String _externalAuthName;
        private boolean _forceCreate;

        public SmartSwitchExternal(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchExternal(String in_userId, String in_token, String in_externalAuthName, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _userId = in_userId;
            _token = in_token;
            _externalAuthName = in_externalAuthName;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString) 
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateExternal(_userId, _token, _externalAuthName, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user with brainCloud using their Facebook Credentials
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_fbUserId The facebook id of the user
    * @param in_fbAuthToken The validated token from the Facebook SDK
    *   (that will be further validated when sent to the bC service)
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    */
    public class SmartSwitchFacebook extends SmartSwitchCallback
    {
        private String _fbUserId;
        private String _fbAuthToken;
        private boolean _forceCreate;

        public SmartSwitchFacebook(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchFacebook(String in_fbUserId, String in_fbAuthToken, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _fbUserId = in_fbUserId;
            _fbAuthToken = in_fbAuthToken;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString)
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateFacebook(_fbUserId, _fbAuthToken, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user with brainCloud using their Oculus Credentials
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_oculusUserId The oculus id of the user
    * @param in_oculusNonce oculus token from the Oculus SDK
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    */
    public class SmartSwitchOculus extends SmartSwitchCallback
    {
        private String _oculusUserId;
        private String _oculusNonce;
        private boolean _forceCreate;

        public SmartSwitchOculus(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchOculus(String in_oculusUserId, String in_oculusNonce, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _oculusUserId = in_oculusUserId;
            _oculusNonce = in_oculusNonce;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString)
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateOculus(_oculusUserId, _oculusNonce, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user using a google userid(email address) and google authentication token.
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_googleUserId  String representation of google+ userid (email)
    * @param in_serverAuthCode  The authentication token derived via the google apis.
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    * @returns   performs the in_success callback on success, in_failure callback on failure
    *
    */
    public class SmartSwitchGoogle extends SmartSwitchCallback
    {
        private String _googleUserId;
        private String _serverAuthCode;
        private boolean _forceCreate;

        public SmartSwitchGoogle(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchGoogle(String in_googleUserId, String in_serverAuthCode, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _googleUserId = in_googleUserId;
            _serverAuthCode = in_serverAuthCode;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString)
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateGoogle(_googleUserId, _serverAuthCode, _forceCreate, _callback);
        }
    }
    
    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user using a google userid(email address) and google authentication token.
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_googleUserAccountEmail  String representation of google+ userid (email)
    * @param in_IdToken  The authentication token derived via the google apis.
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    * @returns   performs the in_success callback on success, in_failure callback on failure
    *
    */
    public class SmartSwitchGoogleOpenId extends SmartSwitchCallback
    {
        private String _googleUserAccountEmail;
        private String _IdToken;
        private boolean _forceCreate;

        public SmartSwitchGoogleOpenId(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchGoogleOpenId(String in_googleUserAccountEmail, String in_IdToken, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _googleUserAccountEmail = in_googleUserAccountEmail;
            _IdToken = in_IdToken;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString)
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateGoogle(_googleUserAccountEmail, _IdToken, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user using a apple userid(email address) and apple authentication token.
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_appleUserId  String representation of apple+ userid (email)
    * @param in_token  The authentication token derived via the apple apis.
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    * @returns   performs the in_success callback on success, in_failure callback on failure
    *
    */
    public class SmartSwitchApple extends SmartSwitchCallback
    {
        private String _appleUserId;
        private String _token;
        private boolean _forceCreate;

        public SmartSwitchApple(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchApple(String in_appleUserId, String in_token, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _appleUserId = in_appleUserId;
            _token = in_token;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString)
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateApple(_appleUserId, _token, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user using a steam userid and session ticket (without any validation on the userid).
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_steamUserId  String representation of 64 bit steam id
    * @param in_sessionticket  The session ticket of the user (hex encoded)
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    * @returns   performs the in_success callback on success, in_failure callback on failure
    *
    */
    public class SmartSwitchSteam extends SmartSwitchCallback
    {
        private String _steamUserId;
        private String _sessionTicket;
        private boolean _forceCreate;

        public SmartSwitchSteam(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchSteam(String in_steamUserId, String in_sessionTicket, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _steamUserId = in_steamUserId;
            _sessionTicket = in_sessionTicket;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString)
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateSteam(_steamUserId, _sessionTicket, _forceCreate, _callback);
        }
    }
    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user using a Twitter userid, authentication token, and secret from Twitter.
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_userid  String representation of Twitter userid
    * @param in_token  The authentication token derived via the Twitter apis.
    * @param in_secret  The secret given when attempting to link with Twitter
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    * @returns   performs the in_success callback on success, in_failure callback on failure
    *
    */
    public class SmartSwitchTwitter extends SmartSwitchCallback
    {
        private String _userId;
        private String _token;
        private String _secret;
        private boolean _forceCreate;

        public SmartSwitchTwitter(BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchTwitter(String in_userId, String in_token, String in_secret, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback)
        {
            super(in_wrapper, in_callback);
            _userId = in_userId;
            _token = in_token;
            _secret = in_secret;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString) 
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateTwitter(_userId, _token, _secret, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * Authenticate the user using a userid and password (without any validation on the userid).
    * Similar to AuthenticateEmailPassword - except that that method has additional features to
    * allow for e-mail validation, password resets, etc.
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_userId  The e-mail address of the user
    * @param in_password  The password of the user
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    *
    */
    public class SmartSwitchUniversal extends SmartSwitchCallback 
    {
        private String _userId;
        private String _password;
        private boolean _forceCreate;

        public SmartSwitchUniversal(BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchUniversal(String in_userId, String in_password, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);
            _userId = in_userId;
            _password = in_password;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString) 
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateUniversal(_userId, _password, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    * 
    * Authenticate the user for Ultra.
    *
    * Service Name - Authenticate
    * Server Operation - Authenticate
    *
    * @param _ultraUserId it's what the user uses to log into the Ultra endpoint initially
    * @param in_ultraIdToken The "id_token" taken from Ultra's JWT.
    * @param _forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_callback The method to be invoked when the server response is received
    */
    public class SmartSwitchUltra extends SmartSwitchCallback 
    {
        private String _ultraUserId;
        private String _ultraIdToken;
        private boolean _forceCreate;

        public SmartSwitchUltra(BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);
        }

        public SmartSwitchUltra(String in_ultraUserId, String in_ultraIdToken, boolean in_forceCreate, BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);
            _ultraUserId = in_ultraUserId;
            _ultraIdToken = in_ultraIdToken;
            _forceCreate = in_forceCreate;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString) 
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateUltra(_ultraUserId, _ultraIdToken, _forceCreate, _callback);
        }
    }

    /*
    * Smart Switch Authenticate will logout of the current profile, and switch to the new authentication type.
    * In event the current session was previously an anonymous account, the smart switch will delete that profile.
    * Use this function to keep a clean designflow from anonymous to signed profiles
    *
    * A generic Authenticate method that translates to the same as calling a specific one, except it takes an extraJson
    * that will be passed along to pre- or post- hooks.
    *
    * Service Name - Authenticate
    * Service Operation - Authenticate
    *
    * @param in_authenticationType Universal, Email, Facebook, etc
    * @param in_ids Auth IDs structure
    * @param in_forceCreate Should a new profile be created for this user if the account does not exist?
    * @param in_extraJson Additional to piggyback along with the call, to be picked up by pre- or post- hooks. Leave empty string for no extraJson.
    * @param in_callback The method to be invoked when the server response is received
    */
    public class SmartSwitchAdvanced extends SmartSwitchCallback 
    {
        private AuthenticationType _authenticationType;
        private AuthenticationIds _ids;
        private boolean _forceCreate;
        private String _extraJson;

        public SmartSwitchAdvanced(BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);

        }

        public SmartSwitchAdvanced(AuthenticationType in_authenticationType, AuthenticationIds in_ids, boolean in_forceCreate, String in_extraJson, BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
        {
            super(in_wrapper, in_callback);
            _authenticationType = in_authenticationType;
            _ids = in_ids;
            _forceCreate = in_forceCreate;
            _extraJson = in_extraJson;
        }

        public void serverCallback(ServiceName in_serviceName, ServiceOperation serviceOperation, String jsonString) 
        {
            clearIds();
            _wrapper.getClient().getAuthenticationService().authenticateAdvanced(_authenticationType, _ids, _forceCreate, _extraJson, _callback);
        }
    }
}
