package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupFileService {
    private enum Parameter {
        groupId,
        folderPath,
        filename,
        fullPathFilename,
        fileId,
        version,
        newTreeId,
        treeVersion,
        newFilename,
        overwriteIfPresent,
        recurse,
        userCloudPath,
        userCloudFilename,
        groupTreeId,
        groupFilename,
        groupFileAcl,
        newAcl
    }
    private BrainCloudClient _client;

    public GroupFileService(BrainCloudClient client){
        _client = client;
    }

    /**
     * Check if filename exists for provided path and name.
     * @param groupId ID of the group
     * @param folderPath File located cloud path/folder
     * @param filename File cloud name
     * @param callback The method to be invoked when the server response is received
     */
    public void checkFilenameExists(
            String groupId,
            String folderPath,
            String filename,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.folderPath.name(), folderPath);
            data.put(Parameter.filename.name(), filename);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.CHECK_FILENAME_EXISTS,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if filename exists for provided path and name.
     * @param groupId ID of the group
     * @param fullPathFilename File cloud name in full path
     * @param callback The method to be invoked when the server response is received
     */
    public void checkFullpathFilenameExists(
            String groupId,
            String fullPathFilename,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.fullPathFilename.name(), fullPathFilename);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.CHECK_FULLPATH_FILENAME_EXISTS,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy a file.
     * @param groupId ID of the group
     * @param fileId ID of the file
     * @param version Target version of the file
     * @param newTreeId ID of the destination folder
     * @param treeVersion Target version of the folder tree
     * @param newFilename Optional new file name
     * @param overwriteIfPresent Whether to allow overwrite of an existing file if present
     * @param callback The method to be invoked when the server response is received
     */
    public void copyFile(
            String groupId,
            String fileId,
            int version,
            String newTreeId,
            int treeVersion,
            String newFilename,
            boolean overwriteIfPresent,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.fileId.name(), fileId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.newTreeId.name(), newTreeId);
            data.put(Parameter.treeVersion.name(), treeVersion);
            data.put(Parameter.newFilename.name(), newFilename);
            data.put(Parameter.overwriteIfPresent.name(), overwriteIfPresent);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.COPY_FILE,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a file.
     * @param groupId ID of the group
     * @param fileId ID of the file
     * @param version Target version of the file
     * @param filename File name for verification purposes
     * @param callback The method to be invoked when the server response is received
     */
    public void deleteFile(
            String groupId,
            String fileId,
            int version,
            String filename,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.fileId.name(), fileId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.filename.name(), filename);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.DELETE_FILE,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the CDN url for file for clients that cannot handle redirect
     * @param groupId ID of the group
     * @param fileId ID of the file
     * @param callback The method to be invoked when the server response is received
     */
    public void getCDNUrl(String groupId, String fileId, IServerCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.fileId.name(), fileId);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.GET_CDN_URL,
                    data,
                    callback);
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns information on a file using fileId.
     * @param groupId ID of the group
     * @param fileId ID of the file
     * @param callback The method to be invoked when the server response is received
     */
    public void getFileInfo(String groupId, String fileId, IServerCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.fileId.name(), fileId);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.GET_FILE_INFO,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns information on a file using path and name.
     * @param groupId ID of the group
     * @param folderPath Folder path
     * @param filename File name
     * @param callback The method to be invoked when the server response is received
     */
    public void getFileInfoSimple(
            String groupId,
            String folderPath,
            String filename,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.folderPath.name(), folderPath);
            data.put(Parameter.filename.name(), filename);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.GET_FILE_INFO_SIMPLE,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of files.
     * @param groupId ID of group
     * @param folderPath Folder path
     * @param recurse Whether to recurse beyond the starting folder
     * @param callback The method to be invoked when the server response is received
     */
    public void getFileList(
            String groupId,
            String folderPath,
            boolean recurse,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.folderPath.name(), folderPath);
            data.put(Parameter.recurse.name(), recurse);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.GET_FILE_LIST,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Move a file.
     * @param groupId ID of the group
     * @param fileId ID of the file
     * @param version Target version of the file. As an option, you can use -1 for the latest version of the file
     * @param newTreeId ID of the destination folder
     * @param treeVersion Target version of the folder tree
     * @param newFilename Optional new file name
     * @param overwriteIfPresent Whether to allow overwrite of an existing file if present
     * @param callback The method to be invoked when the server response is received
     */
    public void moveFile(
            String groupId,
            String fileId,
            int version,
            String newTreeId,
            int treeVersion,
            String newFilename,
            boolean overwriteIfPresent,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.fileId.name(), fileId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.newTreeId.name(), newTreeId);
            data.put(Parameter.treeVersion.name(), treeVersion);
            data.put(Parameter.newFilename.name(), newFilename);
            data.put(Parameter.overwriteIfPresent.name(), overwriteIfPresent);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.MOVE_FILE,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Move a file from user space to group space.
     * @param userCloudPath User file folder
     * @param userCloudFilename User file name
     * @param groupId ID of the group
     * @param groupTreeId ID of the destination folder
     * @param groupFileName Group file name
     * @param groupFileAcl Acl of the new group file
     * @param overwriteIfPresent Whether to allow overwrite of an existing file if present
     * @param callback The method to be invoked when the server response is received
     */
    public void moveUserToGroupFile(
            String userCloudPath,
            String userCloudFilename,
            String groupId,
            String groupTreeId,
            String groupFileName,
            JSONObject groupFileAcl,
            boolean overwriteIfPresent,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.userCloudPath.name(), userCloudPath);
            data.put(Parameter.userCloudFilename.name(), userCloudFilename);
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.groupTreeId.name(), groupTreeId);
            data.put(Parameter.groupFilename.name(), groupFileName);
            data.put(Parameter.groupFileAcl.name(), groupFileAcl);
            data.put(Parameter.overwriteIfPresent.name(), overwriteIfPresent);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.MOVE_USER_TO_GROUP_FILE,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rename or edit permissions of an uploaded file. Does not change the contents of the file.
     * @param groupId ID of the group
     * @param fileId ID of the file
     * @param version Target version of the file
     * @param newFilename Optional new file name
     * @param newAcl Optional new acl
     * @param callback The method to be invoked when the server response is received
     */
    public void updateFileInfo(
            String groupId,
            String fileId,
            int version,
            String newFilename,
            JSONObject newACL,
            IServerCallback callback
    ){
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.fileId.name(), fileId);
            data.put(Parameter.version.name(), version);
            data.put(Parameter.newFilename.name(), newFilename);
            data.put(Parameter.newAcl.name(), newACL);

            ServerCall sc = new ServerCall(
                    ServiceName.groupFile,
                    ServiceOperation.UPDATE_FILE_INFO,
                    data,
                    callback
            );
            _client.sendRequest(sc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
