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

public class PresenceService
{
	private enum Parameter
	{
		platform,
		includeOffline,
		groupId,
		profileIds,
		bidirectional,
		visible,
		activity
	}

	private BrainCloudClient _client;
	
	public PresenceService(BrainCloudClient client)
	{
		_client = client; 
	}

 	/**
	* Force an RTT presence update to all listeners of the caller.
	*
	* Service Name - Presence
	* Service Operation - ForcePush
	*
	* @param callback The method to be invoked when the server response is received
	*/
	public void forcePush(IServerCallback callback)
	{
		ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.FORCE_PUSH, null, callback);
		_client.sendRequest(sc);	
	}

	/**
	* Gets the presence data for the given <platform>. Can be one of "all",
	* "brainCloud", or "facebook". Will not include offline profiles
	* unless <includeOffline> is set to true.
	*/
	public void getPresenceOfFriends(String platform, boolean includeOffline, IServerCallback callback)
	{
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.platform.name(), platform);
			data.put(Parameter.includeOffline.name(), includeOffline);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.GET_PRESENCE_OF_FRIENDS, data, callback);
			_client.sendRequest(sc);	
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	* Gets the presence data for the given <groupId>. Will not include
	* offline profiles unless <includeOffline> is set to true.
	*/
	public void getPresenceOfGroup(String groupId, boolean includeOffline, IServerCallback callback)
	{
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.groupId.name(), groupId);
			data.put(Parameter.includeOffline.name(), includeOffline);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.GET_PRESENCE_OF_GROUP, data, callback);
			_client.sendRequest(sc);	
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	* Gets the presence data for the given <profileIds>. Will not include
	* offline profiles unless <includeOffline> is set to true.
	*/
	public void getPresenceOfUsers(ArrayList<String> profileIds, boolean includeOffline, IServerCallback callback)
	{
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
			data.put(Parameter.includeOffline.name(), includeOffline);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.GET_PRESENCE_OF_USERS, data, callback);
			_client.sendRequest(sc);	
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	* Registers the caller for RTT presence updates from friends for the
	* given <platform>. Can be one of "all", "brainCloud", or "facebook".
	* If <bidirectional> is set to true, then also registers the targeted
	* users for presence updates from the caller.
	*/
	public void registerListenersForFriends(String platform, boolean bidirectional, IServerCallback callback)
	{
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.platform.name(), platform);
			data.put(Parameter.bidirectional.name(), bidirectional);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.REGISTER_LISTENERS_FOR_FRIENDS, data, callback);
			_client.sendRequest(sc);	
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	* Registers the caller for RTT presence updates from the members of
	* the given <groupId>. Caller must be a member of said group. If
	* <bidirectional> is set to true, then also registers the targeted
	* users for presence updates from the caller.
	*/
	public void registerListenersForGroup(String groupId, boolean bidirectional, IServerCallback callback)
	{
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.groupId.name(), groupId);
			data.put(Parameter.bidirectional.name(), bidirectional);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.REGISTER_LISTENERS_FOR_GROUP, data, callback);
			_client.sendRequest(sc);	
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	* Registers the caller for RTT presence updates for the given
	* <profileIds>. If <bidirectional> is set to true, then also registers
	* the targeted users for presence updates from the caller.
	*/
	public void registerListenersForProfiles(ArrayList<String> profileIds, boolean bidirectional, IServerCallback callback)
	{
		try {
			JSONObject data = new JSONObject();
			data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
			data.put(Parameter.bidirectional.name(), bidirectional);

			ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.REGISTER_LISTENERS_FOR_PROFILES, data, callback);
			_client.sendRequest(sc);	
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	/**
	* Update the presence data visible field for the caller.
	*/
	public void setVisibility(boolean visible, IServerCallback callback)
	{
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
	* Stops the caller from receiving RTT presence updates. Does not
	* affect the broadcasting of *their* presence updates to other
	* listeners.
	*/
	public void stopListening(IServerCallback callback)
	{
		ServerCall sc = new ServerCall(ServiceName.presence, ServiceOperation.STOP_LISTENING, null, callback);
		_client.sendRequest(sc);
	}

	/**
	* Update the presence data activity field for the caller.
	*/
	public void updateActivity(String activity, IServerCallback callback)
	{
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