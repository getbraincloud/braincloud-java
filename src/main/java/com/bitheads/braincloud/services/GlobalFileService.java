// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class GlobalFileService {

    private BrainCloudClient _client;

    public GlobalFileService(BrainCloudClient client) {
        _client = client;
    }

    private enum Parameter {
        fileId,
        folderPath,
        filename,
        recurse
    }

    /**
     * Returns the complete info for the specified file given it’s fileId
     *
     * Service Name - globalFileV3
     * Service Operation - GET_FILE_INFO
     *
     * @param fileId   The fileId of the global file
     * @param callback The method to be invoked when the server response is received
     */
    public void getFileInfo(String fileId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.fileId.name(), fileId);

            ServerCall sc = new ServerCall(ServiceName.globalFileV3,
                    ServiceOperation.GET_FILE_INFO, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns the complete info for the specified file, without having to look up
     * the fileId first.
     *
     * Service Name - globalFileV3
     * Service Operation - GET_FILE_INFO_SIMPLE
     *
     * @param folderPath The folder path of the file
     * @param filename   The name of the file
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void getFileInfoSimple(String folderPath, String filename, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.folderPath.name(), folderPath);
            data.put(Parameter.filename.name(), filename);

            ServerCall sc = new ServerCall(ServiceName.globalFileV3,
                    ServiceOperation.GET_FILE_INFO_SIMPLE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns the CDN of the specified file.
     *
     * Service Name - globalFileV3
     * Service Operation - GET_GLOBAL_CDN_URL
     *
     * @param fileId   The fileId of the global file
     * @param callback The method to be invoked when the server response is received
     */
    public void getGlobalCDNUrl(String fileId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.fileId.name(), fileId);

            ServerCall sc = new ServerCall(ServiceName.globalFileV3,
                    ServiceOperation.GET_GLOBAL_CDN_URL, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns files at the current path.
     *
     * Service Name - globalFileV3
     * Service Operation - GET_GLOBAL_FILE_LIST
     *
     * @param folderPath The folder path to list files from
     * @param recurse    Whether to recurse into subfolders
     * @param callback   The method to be invoked when the server response is
     *                   received
     */
    public void getGlobalFileList(String folderPath, boolean recurse, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.folderPath.name(), folderPath);
            data.put(Parameter.recurse.name(), recurse);

            ServerCall sc = new ServerCall(ServiceName.globalFileV3,
                    ServiceOperation.GET_GLOBAL_FILE_LIST, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

}
