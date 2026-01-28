// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenceService {
	private enum Parameter {
		platform,
		includeOffline,
		groupId,
		profileIds,
		bidirectional,
		visible,
		activity
	}

	private BrainCloudClient _client;

	public PresenceService(BrainCloudClient client) {
		_client = client;
	}

	/**
	 * Force an RTT presence update to all listeners of the caller.
	 *
	 * Service Name - presence
	 * Service Operation - FORCE_PUSH
	 *
	 * @param callback The callback invoked when the server response is received.
	 */
	public void forcePush(IServerCallback callback) {
		ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.FORCE_PUSH, null, callback);
		_client.sendRequest(sc);
	}

	/**
	 * Retrieves the presence data for friends on the specified platform.
	 *
	 * Service Name - presence
	 * Service Operation - GET_PRESENCE_OF_FRIENDS
	 *
	 * @param platform       One of "all", "brainCloud", or "facebook".
	 * @param includeOffline If true, includes offline profiles.
	 * @param callback       Callback invoked when the server response is received.
	 */
	public void getPresenceOfFriends(String platform, boolean includeOffline, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.platform.name(), platform);
			data.put(Parameter.includeOffline.name(), includeOffline);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.GET_PRESENCE_OF_FRIENDS, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	 * Retrieves the presence data for members of a given group.
	 *
	 * Service Name - presence
	 * Service Operation - GET_PRESENCE_OF_GROUP
	 *
	 * @param groupId        Group ID to query.
	 * @param includeOffline If true, includes offline profiles.
	 * @param callback       Callback invoked when the server response is received.
	 */
	public void getPresenceOfGroup(String groupId, boolean includeOffline, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.groupId.name(), groupId);
			data.put(Parameter.includeOffline.name(), includeOffline);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.GET_PRESENCE_OF_GROUP, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	 * Retrieves the presence data for the specified users.
	 *
	 * Service Name - presence
	 * Service Operation - GET_PRESENCE_OF_USERS
	 *
	 * @param profileIds     Vector of profile IDs to query.
	 * @param includeOffline If true, includes offline profiles.
	 * @param callback       Callback invoked when the server response is received.
	 */
	public void getPresenceOfUsers(ArrayList<String> profileIds, boolean includeOffline, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
			data.put(Parameter.includeOffline.name(), includeOffline);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.GET_PRESENCE_OF_USERS, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	 * Registers the caller for RTT presence updates from friends on a given
	 * platform.
	 *
	 * Service Name - presence
	 * Service Operation - REGISTER_LISTENERS_FOR_FRIENDS
	 *
	 * @param platform      One of "all", "brainCloud", or "facebook".
	 * @param bidirectional If true, also registers targeted users for updates from
	 *                      the caller.
	 * @param callback      Callback invoked when the server response is received.
	 */
	public void registerListenersForFriends(String platform, boolean bidirectional, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.platform.name(), platform);
			data.put(Parameter.bidirectional.name(), bidirectional);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.REGISTER_LISTENERS_FOR_FRIENDS, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	 * Registers the caller for RTT presence updates from members of a given group.
	 *
	 * Service Name - presence
	 * Service Operation - REGISTER_LISTENERS_FOR_GROUP
	 *
	 * @param groupId       Group ID to listen to. Caller must be a member.
	 * @param bidirectional If true, also registers targeted users for updates from
	 *                      the caller.
	 * @param callback      Callback invoked when the server response is received.
	 */
	public void registerListenersForGroup(String groupId, boolean bidirectional, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.groupId.name(), groupId);
			data.put(Parameter.bidirectional.name(), bidirectional);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.REGISTER_LISTENERS_FOR_GROUP, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	 * Registers the caller for RTT presence updates from specific profiles.
	 *
	 * Service Name - presence
	 * Service Operation - REGISTER_LISTENERS_FOR_PROFILES
	 *
	 * @param profileIds    Vector of profile IDs to listen to.
	 * @param bidirectional If true, also registers targeted users for updates from
	 *                      the caller.
	 * @param callback      Callback invoked when the server response is received.
	 */
	public void registerListenersForProfiles(ArrayList<String> profileIds, boolean bidirectional,
			IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
			data.put(Parameter.bidirectional.name(), bidirectional);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.REGISTER_LISTENERS_FOR_PROFILES, data,
					callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	 * Updates the visibility field of the caller's presence data.
	 *
	 * Service Name - presence
	 * Service Operation - SET_VISIBILITY
	 *
	 * @param visible  True to make the caller visible, false to hide.
	 * @param callback Callback invoked when the server response is received.
	 */
	public void setVisibility(boolean visible, IServerCallback callback) {
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.visible.name(), visible);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.SET_VISIBILITY, data, callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	 * Stops the caller from receiving RTT presence updates.
	 * Does not affect broadcasting of the caller's own presence updates.
	 *
	 * Service Name - presence
	 * Service Operation - STOP_LISTENING
	 *
	 * @param callback Callback invoked when the server response is received.
	 */
	public void stopListening(IServerCallback callback) {
		ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.STOP_LISTENING, null, callback);
		_client.sendRequest(sc);
	}

	/**
	 * Updates the activity field of the caller's presence data.
	 *
	 * Service Name - presence
	 * Service Operation - UPDATE_ACTIVITY
	 *
	 * @param jsonActivity JSON string representing activity information.
	 * @param callback     Callback invoked when the server response is received.
	 */
	public void updateActivity(String activity, IServerCallback callback) {
		try {

			JSONObject data = new JSONObject();
			JSONObject jsonDataObj = new JSONObject(activity);
			data.put(Parameter.activity.name(), jsonDataObj);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.UPDATE_ACTIVITY, data, callback);
			_client.sendRequest(sc);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}
}