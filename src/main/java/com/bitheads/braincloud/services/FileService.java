package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by bradleyh on 3/29/2016.
 */
public class FileService {

    private enum Parameter {
        cloudPath,
        cloudFilename,
        shareable,
        replaceIfExists,
        localPath,
        fileSize,
        recurse
    }

    private BrainCloudClient _client;

    public FileService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Prepares a user file upload. On success the file will begin uploading
     * to the brainCloud server. To be informed of success/failure of the upload
     * register an IFileUploadCallback with the BrainCloudClient class.
     *
     * @param cloudPath The desired cloud path of the file
     * @param cloudFilename The desired cloud filename of the file
     * @param shareable True if the file is shareable.
     * @param replaceIfExists Whether to replace file if it exists
     * @param localPath The path and filename of the local file
     * @param callback The method to be invoked when the server response is received
     *
     * Significant error codes:
     *
     * 40429 - File maximum file size exceeded
     * 40430 - File exists, replaceIfExists not set
     */
    public boolean uploadFile(String cloudPath,
                              String cloudFilename,
                              boolean shareable,
                              boolean replaceIfExists,
                              String localPath,
                              IServerCallback callback) {
        File file = new File(localPath);
        if (!file.exists()) return false;

        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.cloudPath.name(), cloudPath);
            data.put(Parameter.cloudFilename.name(), cloudFilename);
            data.put(Parameter.shareable.name(), shareable);
            data.put(Parameter.replaceIfExists.name(), replaceIfExists);
            data.put(Parameter.localPath.name(), localPath);
            data.put(Parameter.fileSize.name(), file.length());

            ServerCall sc = new ServerCall(ServiceName.file, ServiceOperation.PREPARE_USER_UPLOAD, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }

        return true;
    }

    /**
     * List all user files
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void listUserFiles(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.file, ServiceOperation.LIST_USER_FILES, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * List user files from the given cloud path
     *
     * @param cloudPath File path
     * @param recurse Whether to recurse into sub-directories
     * @param callback The method to be invoked when the server response is received
     */
    public void listUserFiles(String cloudPath, boolean recurse, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.cloudPath.name(), cloudPath);
            data.put(Parameter.recurse.name(), recurse);

            ServerCall sc = new ServerCall(ServiceName.file, ServiceOperation.LIST_USER_FILES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Deletes a single user file.
     *
     * @param cloudPath File path
     * @param cloudFilename name of file
     * @param callback The method to be invoked when the server response is received
     *
     * Significant error codes:
     *
     * 40431 - Cloud storage service error
     * 40432 - File does not exist
     */
    public void deleteUserFile(String cloudPath, String cloudFilename, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.cloudPath.name(), cloudPath);
            data.put(Parameter.cloudFilename.name(), cloudFilename);

            ServerCall sc = new ServerCall(ServiceName.file, ServiceOperation.DELETE_USER_FILE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Delete multiple user files
     *
     * @param cloudPath File path
     * @param recurse Whether to recurse into sub-directories
     * @param callback The method to be invoked when the server response is received
     */
    public void deleteUserFiles(String cloudPath, boolean recurse, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.cloudPath.name(), cloudPath);
            data.put(Parameter.recurse.name(), recurse);

            ServerCall sc = new ServerCall(ServiceName.file, ServiceOperation.DELETE_USER_FILES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Returns the CDN url for a file object
     *
     * @param cloudPath File path
     * @param cloudFileName Name of file
     * @param callback The method to be invoked when the server response is received
     */
    public void getCDNUrl(String cloudPath, String cloudFileName, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.cloudPath.name(), cloudPath);
            data.put(Parameter.cloudFilename.name(), cloudFileName);

            ServerCall sc = new ServerCall(ServiceName.file, ServiceOperation.GET_CDN_URL, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method cancels an upload. If an IFileUploadCallback has been registered with the BrainCloudClient class,
     * the fileUploadFailed callback method will be called once the upload has been canceled.
     *
     * @param uploadId The id of the upload
     */
    public void cancelUpload(String uploadId) {
        _client.getRestClient().cancelUpload(uploadId);
    }

    /**
     * Returns the progress of the given upload from 0.0 to 1.0
     * or -1 if upload not found.
     *
     * @param uploadId The id of the upload
     * @return A progress from 0.0 to 1.0 or -1 if upload not found.
     */
    public double getUploadProgress(String uploadId) {
        return _client.getRestClient().getUploadProgress(uploadId);
    }

    /**
     * Returns the number of bytes uploaded or -1 if upload not found.
     *
     * @param uploadId The id of the upload
     * @return The number of bytes uploaded or -1 if upload not found.
     */
    public long getUploadBytesTransferred(String uploadId) {
        return _client.getRestClient().getUploadBytesTransferred(uploadId);
    }

    /**
     * Returns the total number of bytes that will be uploaded or -1 if upload not found.
     *
     * @param uploadId The id of the upload
     * @return The total number of bytes that will be uploaded or -1 if upload not found.
     */
    public long getUploadTotalBytesToTransfer(String uploadId) {
        return _client.getRestClient().getUploadTotalBytesToTransfer(uploadId);
    }
}