package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class S3HandlingService {

    public enum Parameter {
        category,
        fileDetails,
        fileId
    }

    private BrainCloudClient _client;

    public S3HandlingService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Sends an array of file details and returns
     * the details of any of those files that have changed
     *
     * Service Name - S3Handling
     * Service Operation - GetUpdatedFiles
     *
     * @param category Category of files on server to compare against
     * @param fileDetailsJson An array of file details
     * @param callback The callback object
     */
    public void getUpdatedFiles(
            String category,
            String fileDetailsJson,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            if (StringUtil.IsOptionalParameterValid(category)) {
                data.put(Parameter.category.name(), category);
            }
            if (StringUtil.IsOptionalParameterValid(fileDetailsJson)) {
                JSONArray jsonFiles = new JSONArray(fileDetailsJson);
                data.put(Parameter.fileDetails.name(), jsonFiles);
            }

            ServerCall serverCall = new ServerCall(ServiceName.s3Handling,
                    ServiceOperation.GET_UPDATED_FILES, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retreives the detailds of custom files stored on the server
     *
     * Service Name - S3Handling
     * Server Operation - GetFileList
     *
     * @param category Category of files to retrieve
     * @param callback The callback object
     */
    public void getFileList(String category, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            if (StringUtil.IsOptionalParameterValid(category)) {
                data.put(Parameter.category.name(), category);
            }

            ServerCall serverCall = new ServerCall(ServiceName.s3Handling,
                    ServiceOperation.GET_FILE_LIST, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the CDN url for a file
     *
     * @param fileId ID of file
     * @param callback The method to be invoked when the server response is received
     */
    public void getCDNUrl(String fileId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.fileId.name(), fileId);

            ServerCall sc = new ServerCall(ServiceName.s3Handling, ServiceOperation.GET_CDN_URL, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
