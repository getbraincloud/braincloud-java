package com.bitheads.braincloud.services;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.bitheads.braincloud.client.IFileUploadCallback;

public class GroupFileServiceTest extends TestFixtureBase {

    private boolean uploadSuccess;
    
    @Test
    public void oneBigTest() {
        TestResult tr = new TestResult(_wrapper);
        String tempFilename = "testfile-java.txt";
        String groupId = "a7ff751c-3251-407a-b2fd-2bd1e9bca64a";
        String fileId = "";
        String movedFilename = "moved-testfile-java.txt";
        String copiedFilename = "copied-testfile-java.txt";
        String copiedFileId = "";
        String updatedFilename = "updated-testfile-java.txt";
        JSONObject acl = new JSONObject();

        // Create acl json object
        try {
            acl.put("other", 0);
            acl.put("member", 2);
        } catch (JSONException e) {
            System.out.println("Error creating acl json object");
            e.printStackTrace();
        }

        /* Add user to test group */
        System.out.println("Joining test group...");
    	_wrapper.getGroupService().joinGroup(groupId, tr);
    	tr.Run();
        
        /* Create and upload a temporary file */
        _wrapper.getClient().registerFileUploadCallback(new IFileUploadCallback() {

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

        // Create the file
        System.out.println("Creating file...");
        File file = new File(tempFilename);
        try {
            if (file.createNewFile()) {
                System.out.println("File created.");
            } else {
                System.out.println("File already exists...");
            }
            file.deleteOnExit();
        } catch (IOException e) {
            System.out.println("Error creating/locating '" + tempFilename + "'");
            e.printStackTrace();
        }

        // Upload the file
        System.out.println("Uploading file...");
        _wrapper.getFileService().uploadFile(
                "TestFolder",
                tempFilename,
                true,
                true,
                tempFilename,
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

        /* Test: moveUserToGroupFile */
        System.out.println("Test: moveUserToGroupFile");
        _wrapper.getGroupFileService().moveUserToGroupFile(
                "TestFolder/",
                tempFilename,
                groupId,
                "",
                tempFilename,
                acl,
                true,
                tr);
        tr.Run();

        /* Save group file ID for tests */
        try {
            JSONObject data = tr.m_response.getJSONObject("data");
            JSONObject fileDetails = data.getJSONObject("fileDetails");
            fileId = fileDetails.getString("fileId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Test: checkFilenameExists */
        System.out.println("Test: checkFilenameExists");

        _wrapper.getGroupFileService().checkFilenameExists(groupId, "", tempFilename, tr);
        tr.Run();

        // Confirm that the file exists
        try {
            JSONObject data = tr.m_response.getJSONObject("data");
            boolean exists = data.getBoolean("exists");

            assertTrue(exists);
        } catch (JSONException e) {
            e.printStackTrace();;
        }

        /* Test: checkFullpathFilenameExists */
        System.out.println("Test: checkFullpathFilenameExists");
        _wrapper.getGroupFileService().checkFullpathFilenameExists(groupId, tempFilename, tr);
        tr.Run();

        // Confirm that the file exists
        try {
            JSONObject data = tr.m_response.getJSONObject("data");
            boolean exists = data.getBoolean("exists");

            assertTrue(exists);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Test: getFileInfo */
        System.out.println("Test: getFileInfo");
        _wrapper.getGroupFileService().getFileInfo(groupId, fileId, tr);
        tr.Run();

        /* Test: getFileInfoSimple */
        System.out.println("Test: getFileInfoSimple");
        _wrapper.getGroupFileService().getFileInfoSimple(groupId, "", tempFilename, tr);
        tr.Run();

        /* Test: getCDNUrl */
        System.out.println("Test: getCDNUrl");
        _wrapper.getGroupFileService().getCDNUrl(groupId, fileId, tr);
        tr.Run();

        /* Test: getFileList */
        System.out.println("Test: getFileList");
        _wrapper.getGroupFileService().getFileList(groupId, "", true, tr);
        tr.Run();

        /* Test: moveFile */
        System.out.println("Test: moveFile");
        _wrapper.getGroupFileService().moveFile(
                groupId,
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
                groupId,
                fileId,
                -1,
                "",
                0,
                tempFilename,
                true,
                tr
        );
        tr.Run();

        /* Test: copyFile */
        System.out.println("Test: copyFile");
        _wrapper.getGroupFileService().copyFile(
                groupId,
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
            JSONObject data = tr.m_response.getJSONObject("data");
            JSONObject fileDetails = data.getJSONObject("fileDetails");
            copiedFileId = fileDetails.getString("fileId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Test: deleteFile */
        // Delete file from copyFile test
        System.out.println("Test: deleteFile");
        _wrapper.getGroupFileService().deleteFile(
                groupId,
                copiedFileId,
                -1,
                copiedFilename,
                tr
        );
        tr.Run();

        /* Test: updateFileInfo */
        System.out.println("Test: updateFileInfo");
        _wrapper.getGroupFileService().updateFileInfo(
                groupId,
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
                groupId,
                fileId,
                -1,
                tempFilename,
                acl,
                tr
        );
        tr.Run();

        _wrapper.getClient().deregisterFileUploadCallback();
    }

    /* Taken from FileServiceTest. */
    private void waitForReturn(String[] uploadIds, Boolean cancelUpload) throws Exception {
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

    private String getUploadId(JSONObject response) throws Exception {
        return response.getJSONObject("data").getJSONObject("fileDetails").getString("uploadId");
    }
}
