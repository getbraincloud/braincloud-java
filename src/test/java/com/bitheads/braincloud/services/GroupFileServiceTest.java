package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.IFileUploadCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GroupFileServiceTest extends TestFixtureNoAuth implements IFileUploadCallback {

    /* Information grabbed from internal servers -> Unit Test Master */
    //id for testingGroupFile.dat
    private String groupFileId = "d2dd646a-f1af-4a96-90a7-a0310246f5a2";
    private String groupID = "a7ff751c-3251-407a-b2fd-2bd1e9bca64a";

    //Making version a negative value to tell the server to use the latest version
    private int version = -1;
    private int _returnCount;
    private int _failCount;
    private String filename = "testingGroupFile.dat";
    String newFileName = "testCopiedFile.dat";
    private String tempFilename = "deleteThisFileAfter.dat";
    private String updatedName = "UpdatedGroupFile.dat";

    @Test
    public void testCheckFilenameExists(){
        TestResult tr = new TestResult(_wrapper);
        JSONObject data;
        boolean exists;

        System.out.println("testCheckFilenameExists...");

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr
        );
        tr.Run();

        System.out.println("Checking if file exists...");
        _wrapper.getGroupFileService().checkFilenameExists(groupID, "", filename, tr);
        tr.Run();

        //Confirm that file exists
        try {
            data = tr.m_response.getJSONObject("data");
            exists = data.getBoolean("exists");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Assert.assertTrue(exists);
    }

    @Test
    public void testCheckFullpathFilenameExists(){
        TestResult tr = new TestResult(_wrapper);
        JSONObject data;
        boolean exists;

        System.out.println("testCheckFullpathFilenameExists...");

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr
        );
        tr.Run();

        System.out.println("Checking if file exists...");
        _wrapper.getGroupFileService().checkFullpathFilenameExists(groupID, filename, tr);
        tr.Run();

        //Confirm that file exists
        try {
            data = tr.m_response.getJSONObject("data");
            exists = data.getBoolean("exists");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Assert.assertTrue(exists);
    }

    @Test
    public void testGetFileInfo(){
        TestResult tr = new TestResult(_wrapper);

        System.out.println("testGetFileInfo...");

        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr
        );
        tr.Run();

        System.out.println("Getting file info...");
        _wrapper.getGroupFileService().getFileInfo(groupID, groupFileId, tr);
        tr.Run();

        //testGetCDNUrl
        System.out.println("Getting CDN URL...");
        _wrapper.getGroupFileService().getCDNUrl(groupID, groupFileId, tr);
        tr.Run();
    }

    @Test
    public void testGetFileInfoSimple(){
        TestResult tr = new TestResult(_wrapper);

        System.out.println("testGetFileInfoSimple...");

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr
        );
        tr.Run();

        System.out.println("Getting file info...");
        _wrapper.getGroupFileService().getFileInfoSimple(groupID, "", filename, tr);
        tr.Run();
    }

    @Test
    public void testGetFileList(){
        TestResult tr = new TestResult(_wrapper);

        System.out.println("testGetFileList...");

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr
        );
        tr.Run();

        System.out.println("Getting file list...");
        boolean recurse = true;
        _wrapper.getGroupFileService().getFileList(groupID, "", recurse, tr);
        tr.Run();
    }

    @Test
    public void testMoveFile(){
        TestResult tr = new TestResult(_wrapper);

        System.out.println("testMoveFile...");

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr
        );
        tr.Run();

        System.out.println("Moving file...");
        _wrapper.getGroupFileService().moveFile(
                groupID,
                groupFileId,
                version,
                "",
                0,
                newFileName,
                true,
                tr
        );
        tr.Run();

        //Revert back
        System.out.println("Moving back...");
        _wrapper.getGroupFileService().moveFile(
                groupID,
                groupFileId,
                version,
                "",
                0,
                filename,
                true,
                tr
        );
        tr.Run();
    }

    @Test
    public void testMoveUserToGroupFile(){
        TestResult tr = new TestResult(_wrapper);
        JSONObject acl = new JSONObject();
        JSONObject data;
        JSONObject fileDetails;
        String newFileId;

        System.out.println("testMoveUserToGroupFile...");

        _wrapper.getClient().registerFileUploadCallback(this);

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal("java-tester", "java-tester", true, tr);
        tr.Run();

        /* Upload a new file */
        //Create the file
        System.out.println("Creating file...");
        File file = new File(tempFilename);
        try {
            if(file.createNewFile()){
                System.out.println("File created.");
            }
            else{
                System.out.println("File already exists...");
            }
        } catch (IOException e) {
            System.out.println("Error creating/locating '" + tempFilename + "'");
            throw new RuntimeException(e);
        }

        //Create acl json object
        try {
            acl.put("other", 0);
            acl.put("member", 2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Upload the file
        System.out.println("Uploading file...");
        boolean _shareable = true;
        boolean _replaceIfExists = true;
        _wrapper.getFileService().uploadFile(
                "TestFolder",
                tempFilename,
                _shareable,
                _replaceIfExists,
                tempFilename,
                tr
        );
        tr.Run();

        //Wait for upload to complete
        if (tr.m_result) {
            try {
                String id = getUploadId(tr.m_response);

                waitForReturn(new String[]{id}, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Assert.assertEquals(0, _failCount);

        //Move file
        System.out.println("Moving file...");
        _wrapper.getGroupFileService().moveUserToGroupFile(
                "TestFolder/",
                tempFilename,
                groupID,
                "",
                tempFilename,
                acl,
                true,
                tr
        );
        tr.Run();

        try{
            data = tr.m_response.getJSONObject("data");
            fileDetails = data.getJSONObject("fileDetails");
            newFileId = fileDetails.getString("fileId");
        } catch (JSONException e){
            throw new RuntimeException(e);
        }

        //Delete new file
        System.out.println("Deleting file...");
        _wrapper.getGroupFileService().deleteFile(
                groupID,
                newFileId,
                version,
                tempFilename,
                tr
        );
        tr.Run();

        _wrapper.getClient().deregisterFileUploadCallback();
    }

    @Test
    public void testCopyFile(){
        TestResult tr = new TestResult(_wrapper);
        JSONObject data;
        JSONObject fileDetails;
        String fileId;

        System.out.println("testCopyFile...");

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal("java-tester", "java-tester", true, tr);
        tr.Run();

        System.out.println("Copying file...");
        _wrapper.getGroupFileService().copyFile(
                groupID,
                groupFileId,
                version,
                "",
                0,
                newFileName,
                true,
                tr
        );
        tr.Run();

        try {
            data = tr.m_response.getJSONObject("data");
            fileDetails = data.getJSONObject("fileDetails");
            fileId = fileDetails.getString("fileId");

            //Delete new file
            System.out.println("Deleting new file...");
            _wrapper.getGroupFileService().deleteFile(
                    groupID,
                    fileId,
                    version,
                    newFileName,
                    tr
            );
            tr.Run();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateFileInfo(){
        TestResult tr = new TestResult(_wrapper);
        JSONObject acl = new JSONObject();

        System.out.println("testUpdateFileInfo...");

        System.out.println("Authenticating...");
        _wrapper.getClient().getAuthenticationService().authenticateUniversal("java-tester", "java-tester", true, tr);
        tr.Run();

        try {
            acl.put("other", 0);
            acl.put("member", 2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Updating file info...");
        _wrapper.getGroupFileService().updateFileInfo(
                groupID,
                groupFileId,
                version,
                updatedName,
                acl,
                tr
        );
        tr.Run();

        //Revert back
        System.out.println("Reverting back...");
        _wrapper.getGroupFileService().updateFileInfo(
                groupID,
                groupFileId,
                version,
                filename,
                acl,
                tr
        );
        tr.Run();
    }

    /* Taken from FileServiceTest. */
    private String createFile(int fileSizeMb) throws Exception {
        File tempFile = File.createTempFile("test", ".dat");
        tempFile.deleteOnExit();

        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");

        int fileSize = fileSizeMb * 1024 * 1024;
        raf.setLength(fileSize);
        raf.close();

        return tempFile.getCanonicalPath();
    }

    private void waitForReturn(String[] uploadIds, Boolean cancelUpload) throws Exception {
        FileService service = _wrapper.getFileService();
        int count = 0;
        Boolean sw = true;

        System.out.println("Waiting for file to upload...");

        while (_returnCount < uploadIds.length && count < 1000 * 30) {
            _wrapper.runCallbacks();
            for (String id : uploadIds) {
                double progress = service.getUploadProgress(id);

                if (progress > -1 && sw) {
                    String logStr = "File " + id + " Progress: " +
                            progress + " | " +
                            service.getUploadBytesTransferred(id) + "/" +
                            service.getUploadTotalBytesToTransfer(id);
                    System.out.println(logStr);
                }

                if (cancelUpload && progress > 0.05)
                    service.cancelUpload(id);
            }

            sw = !sw;
            count += 100;
            Thread.sleep(100);
        }
    }

    private String getUploadId(JSONObject response) throws Exception {
        return response.getJSONObject("data").getJSONObject("fileDetails").getString("uploadId");
    }

    @Override
    public void fileUploadCompleted(String fileUploadId, String jsonResponse) {
        _returnCount++;
    }

    @Override
    public void fileUploadFailed(String fileUploadId, int statusCode, int reasonCode, String jsonResponse) {
        _returnCount++;
        _failCount++;
    }
}
