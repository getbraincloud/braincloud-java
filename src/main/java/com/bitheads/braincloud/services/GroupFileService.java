// Copyright 2026 bitHeads, Inc. All Rights Reserved.
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
     * Check if filename exists for provided path and name
     *
     * Service Name GroupFile
     * Service Operation  CheckFilenameExists
     *
     * @param groupId ID of the group.
     * @param folderPath The path of the file
     * @param filename The filename of the file
     * @param in_callback Block to call on return of  server response
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
     * Check if filename exists for provided full path name
     *
     * Service Name GroupFile
     * Service Operation CheckFullpathFilenameExists
     *
     * @param groupId ID of the group.
     * @param fullPathFilename The full path of the file
     * @param in_callback Block to call on return of  server response
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
     *  Copy a file.
     *
     * Service Name GroupFile
     * Service Operation CopyFile
     *
     * @param groupId the groupId
     * @param fileId the fileId
     * @param version the version
     * @param newTreeId thenewTreeId
     * @param treeVersion the treeVersion
     * @param newFilename the newFilename
     * @param in_callback Block to call on return of  server response
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
     *
     * Service Name GroupFile
     * Service Operation DeleteFile
     *
     * @param groupId the groupId
     * @param fileId the fileId
     * @param version the version
     * @param newFilename the newFilename
     * @param in_callback Block to call on return of  server response
     
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
     * Return CDN url for file for clients that cannot handle redirect.
     *
     * Service Name GroupFile
     * Service Operation GetCdnUrl
     *
     * @param groupId the groupId
     * @param fileId the fileId
     * @param in_callback Block to call on return of  server response
     
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
     *
     * Service Name GroupFile
     * Service Operation GetFileInfo
     *
     * @param groupId the groupId
     * @param fileId the fileId
     * @param in_callback Block to call on return of  server response
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
     *
     * Service Name GroupFile
     * Service Operation GetFileInfoSimple
     *
     * @param groupId the groupId
     * @param folderPath the folderPath
     * @param fileName the fileName
     * @param in_callback Block to call on return of  server response
     
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
     *
     * Service Name GroupFile
     * Service Operation GetFileList
     *
     * @param groupId the groupId
     * @param folderPath the folderPath
     * @param recurse true to recurse
     * @param in_callback Block to call on return of  server response
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
     *  Move a file.
     *
     * Service Name GroupFile
     * Service Operation MoveFile
     *
     * @param groupId the groupId
     * @param fileId the fileId
     * @param version the version
     * @param newTreeId the newTreeId
     * @param newFilename the newFilename
     * @param in_callback Block to call on return of  server response
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
     *
     * Service Name GroupFile
     * Service Operation MoveUserToGroupFile
     *
     * @param userCloudPath the userCloudPath
     * @param userCloudFilename the userCloudFilename
     * @param groupId the groupId
     * @param groupTreeId the groupTreeId
     * @param groupFilename the groupFilename
     * @param groupFileAcl the groupFileAcl
     * @param overwriteIfPresent the overwriteIfPresent
     * @param in_callback Block to call on return of  server response
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
     * updates information on a file given fileId.
     *
     * Service Name GroupFile
     * Service Operation UpdateFileInfo
     *
     * @param groupId the groupId
     * @param fileId the fileId
     * @param version the version
     * @param newFilename the newFilename
     * @param newAcl the newAcl
     * @param in_callback Block to call on return of  server response
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
