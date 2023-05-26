package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudWrapper;
import com.bitheads.braincloud.client.IFileUploadCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GroupFileServiceTest extends TestFixtureNoAuth {

    private static String _tempFilename = "testfile-java.txt";
    private static String _groupId = "a7ff751c-3251-407a-b2fd-2bd1e9bca64a";
    private static JSONObject acl = new JSONObject();
    private static boolean uploadSuccess;
    private static String fileId = "";

    private String movedFilename = "moved-testfile-java.txt";
    private String copiedFilename = "copied-testfile-java.txt";
    private String updatedFilename = "updated-testfile-java.txt";

    /**
     * Creates and uploads a temporary file to be used for each of the Group File
     * tests.
     * Also acts as a test for the moveUserToGroupFile method.
     * If the file fails to upload or get moved to the group, the other tests should
     * also fail.
     */
    @BeforeClass
    public static void uploadTestFile() {
        JSONObject data;
        JSONObject fileDetails;

        /* From setup() in TestFixtureBase.java */
        _wrapper = new BrainCloudWrapper();
        _client = _wrapper.getClient();
        TestResult tr = new TestResult(_wrapper);

        m_secretMap = new HashMap<String, String>();
        m_secretMap.put(m_appId, m_secret);
        m_secretMap.put(m_childAppId, m_childSecret);

        _client.initializeWithApps(m_serverUrl, m_appId, m_secretMap, m_appVersion);

        /* Authenticate */
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr);
        tr.Run();

        _client.registerFileUploadCallback(new IFileUploadCallback() {

            @Override
            public void fileUploadCompleted(String fileUploadId, String jsonResponse) {
                System.out.println("Temporary file uploaded successfully");
                uploadSuccess = true;
            }

            @Override
            public void fileUploadFailed(String fileUploadId, int statusCode, int reasonCode, String jsonResponse) {
                System.out.println("Temporary file failed to upload");
            }
        });

        /* Create and upload a temporary file */
        // Create the file
        System.out.println("Creating file...");
        File file = new File(_tempFilename);
        try {
            if (file.createNewFile()) {
                System.out.println("File created.");
            } else {
                System.out.println("File already exists...");
            }
            file.deleteOnExit();
        } catch (IOException e) {
            System.out.println("Error creating/locating '" + _tempFilename + "'");
            throw new RuntimeException(e);
        }

        // Upload the file
        System.out.println("Uploading file...");
        _wrapper.getFileService().uploadFile(
                "TestFolder",
                _tempFilename,
                true,
                true,
                _tempFilename,
                tr);
        tr.Run();

        // Wait for upload to complete
        if (tr.m_result) {
            try {
                String id = getUploadId(tr.m_response);

                waitForReturn(new String[] { id }, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        assertTrue(uploadSuccess);

        // Create acl json object
        try {
            acl.put("other", 0);
            acl.put("member", 2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        /* moveUserToGroupFile Test */
        System.out.println("Moving file...");
        _wrapper.getGroupFileService().moveUserToGroupFile(
                "TestFolder/",
                _tempFilename,
                _groupId,
                "",
                _tempFilename,
                acl,
                true,
                tr);
        tr.Run();

        /* Save group file ID for tests */
        try {
            data = tr.m_response.getJSONObject("data");
            fileDetails = data.getJSONObject("fileDetails");
            fileId = fileDetails.getString("fileId");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void groupFileTearDown() {
        _client.deregisterFileUploadCallback();
    }

    /**
     * Authenticate a user that has been added to a group.
     */
    @Before
    public void groupFileAuthenticate() {
        TestResult tr = new TestResult(_wrapper);

        /* Authenticate */
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(
                "java-tester",
                "java-tester",
                true,
                tr);
        tr.Run();
    }

    @Test
    public void testCheckFilenameExists() {
        System.out.println("testCheckFilenameExists");

        TestResult tr = new TestResult(_wrapper);
        JSONObject data;
        boolean exists;

        _wrapper.getGroupFileService().checkFilenameExists(_groupId, "", _tempFilename, tr);
        tr.Run();

        // Confirm that the file exists
        try {
            data = tr.m_response.getJSONObject("data");
            exists = data.getBoolean("exists");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        assertTrue(exists);
    }

    @Test
    public void testCheckFullpathFilenameExists() {
        System.out.println("testCheckFullpathFilenameExists");

        TestResult tr = new TestResult(_wrapper);
        JSONObject data;
        boolean exists;

        _wrapper.getGroupFileService().checkFullpathFilenameExists(_groupId, _tempFilename, tr);
        tr.Run();

        // Confirm that the file exists
        try {
            data = tr.m_response.getJSONObject("data");
            exists = data.getBoolean("exists");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        assertTrue(exists);
    }

    @Test
    public void testGetFileInfo() {
        System.out.println("testGetFileInfo");

        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupFileService().getFileInfo(_groupId, fileId, tr);
        tr.Run();
    }

    @Test
    public void testGetFileInfoSimple() {
        System.out.println("testGetFileInfoSimple");
        
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupFileService().getFileInfoSimple(_groupId, "", _tempFilename, tr);
        tr.Run();
    }

    @Test
    public void testGetCDNUrl() {
        System.out.println("testGetCDNUrl");

        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupFileService().getCDNUrl(_groupId, fileId, tr);
        tr.Run();
    }

    @Test
    public void testGetFileList() {
        System.out.println("testGetFileList");

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getGroupFileService().getFileList(_groupId, "", true, tr);
        tr.Run();
    }

    @Test
    public void testMoveFile(){
        System.out.println("testMoveFile");
        
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGroupFileService().moveFile(
                _groupId,
                fileId,
                -1,
                "",
                0,
                movedFilename,
                true,
                tr
        );
        tr.Run();

        // Revert back
        System.out.println("Moving back...");
        _wrapper.getGroupFileService().moveFile(
                _groupId,
                fileId,
                -1,
                "",
                0,
                _tempFilename,
                true,
                tr
        );
        tr.Run();
    }

    @Test
    public void testCopyFile(){
        TestResult tr = new TestResult(_wrapper);
        JSONObject data;
        JSONObject fileDetails;
        String copiedFileId;

        System.out.println("testCopyFile...");

        _wrapper.getGroupFileService().copyFile(
                _groupId,
                fileId,
                -1,
                "",
                0,
                copiedFilename,
                true,
                tr
        );
        tr.Run();

        // Save data from copied file
        try {
            data = tr.m_response.getJSONObject("data");
            fileDetails = data.getJSONObject("fileDetails");
            copiedFileId = fileDetails.getString("fileId");

            // Delete new file
            System.out.println("Deleting new file...");
            _wrapper.getGroupFileService().deleteFile(
                    _groupId,
                    copiedFileId,
                    -1,
                    copiedFilename,
                    tr
            );
            tr.Run();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateFileInfo(){
        System.out.println("testUpdateFileInfo");
        
        TestResult tr = new TestResult(_wrapper);
        JSONObject acl = new JSONObject();

        // Create acl json object
        try {
            acl.put("other", 0);
            acl.put("member", 2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Updating file info...");
        _wrapper.getGroupFileService().updateFileInfo(
                _groupId,
                fileId,
                -1,
                updatedFilename,
                acl,
                tr
        );
        tr.Run();

        // Revert back
        System.out.println("Reverting back...");
        _wrapper.getGroupFileService().updateFileInfo(
                _groupId,
                fileId,
                -1,
                _tempFilename,
                acl,
                tr
        );
        tr.Run();
    }

    /* Taken from FileServiceTest. */
    private static void waitForReturn(String[] uploadIds, Boolean cancelUpload) throws Exception {
        FileService service = _wrapper.getFileService();
        int count = 0;
        Boolean sw = true;

        System.out.println("Waiting for file to upload...");

        while (!uploadSuccess && count < 1000 * 30) {
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

    private static String getUploadId(JSONObject response) throws Exception {
        return response.getJSONObject("data").getJSONObject("fileDetails").getString("uploadId");
    }

}
