package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.ArrayList;

public class GlobalAppService {

	private enum Parameter
	{
		propertyNames,
        categories
	}
    private BrainCloudClient _client;

    public GlobalAppService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Method returns all the global properties of a game.
     *
     * @param callback The callback.
     */
    public void readProperties(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.globalApp, ServiceOperation.READ_PROPERTIES, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Returns a list of properties, identified by the property names provided.
     * If a property from the list isn't found, it just isn't returned (no error).
     *
     * Service Name - GlobalApp
     * Service Operation - READ_SELECTED_PROPERTIES
     * 
     * @param propertyNames Specifies which properties to return
     * @param in_callback The method to be invoked when the server response is received
     */
    public void readSelectedProperties(ArrayList<String> propertyNames, IServerCallback callback) {
		try {
            JSONObject data = new JSONObject();
            data.put(Parameter.propertyNames.name(), new JSONArray(propertyNames));

            ServerCall sc = new ServerCall(ServiceName.globalApp, ServiceOperation.READ_SELECTED_PROPERTIES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns a list of properties, identified by the categories provided.
     * If a category from the list isn't found, it just isn't returned (no error).
     *
     * Service Name - GlobalApp
     * Service Operation - READ_PROPERTIES_IN_CATEGORIES
     * 
     * @param categories Specifies which category to return
     * @param in_callback The method to be invoked when the server response is received
     */
    public void readPropertiesInCategories(ArrayList<String> categories, IServerCallback callback) {
		try {
            JSONObject data = new JSONObject();
            data.put(Parameter.categories.name(), new JSONArray(categories));

            ServerCall sc = new ServerCall(ServiceName.globalApp, ServiceOperation.READ_PROPERTIES_IN_CATEGORIES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
