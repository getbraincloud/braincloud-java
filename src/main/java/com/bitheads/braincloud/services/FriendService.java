package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendService {

    private enum Parameter {
        externalId,
        externalIds,
        mode,
        authenticationType,
        profileId,
        searchText,
        maxResults,
        includeSummaryData,
        friendId,
        entityId,
        entityType,
        friendPlatform,
        profileIds,
        externalAuthType
    }

    private BrainCloudClient _client;

    public FriendService(BrainCloudClient client) {
        _client = client;
    }

    public enum FriendPlatform {
        All,
        brainCloud,
        Facebook
    }

    /**
     * Retrieves profile information for the specified user.
     *
     * Service Name - friend
     * Service Operation - GET_PROFILE_INFO_FOR_CREDENTIAL
     *
     * @param externalId The users's external ID
     * @param authenticationType The authentication type of the user ID
     */
    public void getProfileInfoForCredential(String externalId, AuthenticationType authenticationType, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.authenticationType.name(), authenticationType.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.GET_PROFILE_INFO_FOR_CREDENTIAL, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Retrieves profile information for the specified external auth user.
     *
     * Service Name - friend
     * Service Operation - GET_PROFILE_INFO_FOR_EXTERNAL_AUTH_ID
     *
     * @param externalId External ID of the user to find
     * @param externalAuthType The external authentication type used for this users's external ID
     */
    public void getProfileInfoForExternalAuthId(String externalId, String externalAuthType, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.externalId.name(), externalId);
            data.put(Parameter.externalAuthType.name(), externalAuthType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.GET_PROFILE_INFO_FOR_EXTERNAL_AUTH_ID, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Retrieves the external ID for the specified user profile ID on the specified social platform.
     *
     * Service Name - Friend
     * Service Operation - GET_EXTERNAL_ID_FOR_PROFILE_ID
     *
     * @param profileId Profile ID.
     * @param authenticationType The authentication type e.g. Facebook
     */
    public void getExternalIdForProfileId(String profileId, String authenticationType, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.authenticationType.name(), authenticationType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.GET_EXTERNAL_ID_FOR_PROFILE_ID, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Finds a list of users matching the search text by performing an exact match search
     *
     * Service Name - friend
     * Service Operation - FIND_USERS_BY_EXACT_NAME
     *
     * @param searchText The string to search for.
     * @param maxResults  Maximum number of results to return.
     * @param callback Method to be invoked when the server response is received.
     */
    public void findUsersByExactName(String searchText, int maxResults, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.searchText.name(), searchText);
            data.put(Parameter.maxResults.name(), maxResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.FIND_USERS_BY_EXACT_NAME, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Finds a list of users matching the search text by performing a substring
     * search of all user names.
     *
     * Service Name - friend
     * Service Operation - FIND_USERS_BY_SUBSTR_NAME
     *
     * @param searchText The substring to search for. Minimum length of 3 characters.
     * @param maxResults  Maximum number of results to return. If there are more the message
     * @param callback Method to be invoked when the server response is received.
     */
    public void findUsersBySubstrName(String searchText, int maxResults, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.searchText.name(), searchText);
            data.put(Parameter.maxResults.name(), maxResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.FIND_USERS_BY_SUBSTR_NAME, data, callback);
        _client.sendRequest(sc);
    }

    /** Retrieves profile information for the partial matches of the specified text. 
     * 
     * @param searchText Universal Id text on which to search
     * @param maxResults Maximum number of results to return
     */
    public void findUserByExactUniversalId(String searchText, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.searchText.name(), searchText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.FIND_USER_BY_EXACT_UNIVERSAL_ID, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Returns a particular entity of a particular friend.
     *
     * Service Name - Friend
     * Service Operation - ReadFriendEntity
     *
     * @param friendId Profile Id of friend who owns entity.
     * @param entityId Id of entity to retrieve.
     * @param callback The callback handler
     */
    public void readFriendEntity(String friendId, String entityId, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.friendId.name(), friendId);
            data.put(Parameter.entityId.name(), entityId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.READ_FRIEND_ENTITY, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Returns entities of all friends based on type
     *
     * Service Name - Friend
     * Service Operation - ReadFriendsEntities
     *
     * @param entityType Types of entities to retrieve.
     * @param callback The callback handler
     */
    public void readFriendsEntities(String entityType, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            if (StringUtil.IsOptionalParameterValid(entityType)) {
                data.put(Parameter.entityType.name(), entityType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.READ_FRIENDS_ENTITIES, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Read a friend's user state.
     * If you are not friend with this user, you will get an error
     * with NOT_FRIENDS reason code.
     *
     * Service Name - PlayerState
     * Service Operation - ReadFriendsPlayerState
     *
     * @param friendId Target friend
     * @param callback The callback handler
     */
    public void readFriendUserState(String friendId, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.friendId.name(), friendId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.READ_FRIEND_PLAYER_STATE, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Retrieves a list of player and friend platform information for all friends of the current player.
     *
     * Service Name - Friend
     * Service Operation - LIST_FRIENDS
     *
     * @param friendPlatform Friend platform to query.
     * @param includeSummaryData  True if including summary data; false otherwise.
     * @param callback Method to be invoked when the server response is received.
     */
    public void listFriends(FriendPlatform friendPlatform, Boolean includeSummaryData, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.friendPlatform.name(), friendPlatform.name());
            data.put(Parameter.includeSummaryData.name(), includeSummaryData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.LIST_FRIENDS, data, callback);
        _client.sendRequest(sc);
    }

    
    /**
     *Retrieves the social information associated with the logged in user. Includes summary data if includeSummaryData is true.
     *
     * Service Name - Friend
     * Service Operation - GET_MY_SOCIAL_INFO
     *
     * @param friendPlatform Friend platform to query.
     * @param includeSummaryData  True if including summary data; false otherwise.
     * @param callback Method to be invoked when the server response is received.
     */
    public void getMySocialInfo(FriendPlatform friendPlatform, Boolean includeSummaryData, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.friendPlatform.name(), friendPlatform.name());
            data.put(Parameter.includeSummaryData.name(), includeSummaryData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.GET_MY_SOCIAL_INFO, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Links the current user and the specified users as brainCloud friends.
     *
     * Service Name - Friend
     * Service Operation - ADD_FRIENDS
     *
     * @param profileIds Collection of profile IDs.
     * @param callback Method to be invoked when the server response is received.
     */
    public void addFriends(String[] profileIds, IServerCallback callback) {
        JSONArray profiles = new JSONArray();
        for (String achId : profileIds) {
            profiles.put(achId);
        }

        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.profileIds.name(), profiles);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.ADD_FRIENDS, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Links the profiles for the specified externalIds for the given friend platform as internal friends.
     *
     * Service Name - Friend
     * Service Operation - ADD_FRIENDS_FROM_PLATFORM
     *
     * @param friendPlatform Platform to add from (i.e: "Facebook").
     * @param mode ADD or SYNC.
     * @param externalIds Collection of external IDs from the friend platform.
     * @param callback Method to be invoked when the server response is received.
     */
    public void addFriendsFromPlatform(FriendPlatform friendPlatform, String mode, String[] externalIds, IServerCallback callback) {
        JSONArray externals = new JSONArray();
        for (String extId : externalIds) {
            externals.put(extId);
        }

        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.friendPlatform.name(), friendPlatform.name());
            data.put(Parameter.mode.name(), mode);
            data.put(Parameter.externalIds.name(), externals);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.ADD_FRIENDS_FROM_PLATFORM, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Unlinks the current user and the specified users as brainCloud friends.
     *
     * Service Name - Friend
     * Service Operation - REMOVE_FRIENDS
     *
     * @param profileIds Collection of profile IDs.
     * @param callback Method to be invoked when the server response is received.
     */
    public void removeFriends(String[] profileIds, IServerCallback callback) {
        JSONArray profiles = new JSONArray();
        for (String achId : profileIds) {
            profiles.put(achId);
        }

        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.profileIds.name(), profiles);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.REMOVE_FRIENDS, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Returns state of a particular user.
     *
     * @param profileId Profile Id of user to retrieve user state for.
     * @param callback Method to be invoked when the server response is received.
     */
    public void getSummaryDataForProfileId(String profileId, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.profileId.name(), profileId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.GET_SUMMARY_DATA_FOR_PROFILE_ID, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Get users online status
     *
     * Service Name - Friend
     * Service Operation - GET_USERS_ONLINE_STATUS
     *
     * @param profileIds Collection of profile IDs.
     * @param callback Method to be invoked when the server response is received.
     */
    public void getUsersOnlineStatus(String[] profileIds, IServerCallback callback) {
        JSONArray profiles = new JSONArray();
        for (String achId : profileIds) {
            profiles.put(achId);
        }

        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.profileIds.name(), profiles);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.GET_USERS_ONLINE_STATUS, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Retrieves profile information for users whose names starts with search text. 
     * Optional parameter maxResults allows you to search an amount of names. 
     *
     * Service Name - Friend
     * Service Operation - FIND_USERS_BY_NAME_STARTING_WITH
     *
     * @param searchText Collection of profile IDs.
     * @param maxResults how many names you want to return 
     * @param callback Method to be invoked when the server response is received.
     */
    public void findUsersByNameStartingWith(String searchText, int maxResults, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.searchText.name(), searchText);
            data.put(Parameter.entityId.name(), maxResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.FIND_USERS_BY_NAME_STARTING_WITH, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Retrieves profile information for users whose universal Id starts with search text. 
     * Optional parameter maxResults allows you to search an amount of names. 
     *
     * Service Name - Friend
     * Service Operation - FIND_USERS_BY_UNIVERSAL_ID_STARTING_WITH
     *
     * @param searchText Collection of profile IDs.
     * @param maxResults how many names you want to return 
     * @param callback Method to be invoked when the server response is received.
     */
    public void findUsersByUniversalIdStartingWith(String searchText, int maxResults, IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.searchText.name(), searchText);
            data.put(Parameter.entityId.name(), maxResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerCall sc = new ServerCall(ServiceName.friend, ServiceOperation.FIND_USERS_BY_UNIVERSAL_ID_STARTING_WITH, data, callback);
        _client.sendRequest(sc);
    }
}
