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
     * Returns information on a file using fileId.
     *
     * Service Name - globalFile
     * Service Operation - ACCEPT_GROUP_INVITATION
     *
     * @param fileId the file's ID.
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
     * Returns information on a file using path and name.
     *
     * Service Name - globalFile
     * Service Operation - GET_FILE_INFO_SIMPLE
     *
     * @param folderPath the folder path the file is stored in.
     * @param filename the name of the file beign sought
     * @param callback The method to be invoked when the server response is received
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
     * Return CDN url for file for clients that cannot handle redirect.
     *
     * Service Name - globalFile
     * Service Operation - GET_GLOBAL_CDN_URL
     *
     * @param fileId the file's ID.
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
     * Return CDN url for file for clients that cannot handle redirect.
     *
     * Service Name - globalFile
     * Service Operation - GET_GLOBAL_FILE_LIST
     *
     * @param folderPath the folder path the file is stored in.
     * @param recurse does it recurse?
     * @param callback The method to be invoked when the server response is received
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
