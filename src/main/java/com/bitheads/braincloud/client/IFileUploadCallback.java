package com.bitheads.braincloud.client;

/**
 * Created by bradleyh on 3/29/2016.
 */
public interface IFileUploadCallback {
    /**
     * Method called when a file upload has completed.
     *
     * @param fileUploadId The file upload id
     * @param jsonResponse The json response describing the file details similar to this
     * {
     *  "status": 200,
     *  "data": {
     *   "fileList": [
     *    {
     *     "updatedAt": 1452603368201,
     *     "uploadedAt": null,
     *     "fileSize": 85470,
     *     "shareable": true,
     *     "createdAt": 1452603368201,
     *     "profileId": "bf8a1433-62d2-448e-b396-f3dbffff44",
     *     "gameId": "99999",
     *     "path": "test2",
     *     "filename": "testup.dat",
     *     "downloadUrl": "https://api.braincloudservers.com/s3/bc/g/99999/u/bf8a1433-62d2-448e-b396-f3dbffff44/f/test2/testup.dat"
     *     "cloudLocation": "bc/g/99999/u/bf8a1433-62d2-448e-b396-f3dbffff44/f/test2/testup.dat"
     *    }
     *   ]
     *  }
     * }
     */
    void fileUploadCompleted(String fileUploadId, String jsonResponse);

    /**
     * Method called when a file upload has failed.
     *
     * @param fileUploadId The file upload id
     * @param statusCode The http status of the operation (see http_codes.h)
     * @param reasonCode The reason code of the operation (see reason_codes.h)
     * @param jsonResponse The json response describing the failure. This uses the usual brainCloud error
     * format similar to this:
     * {
     *   "status": 403,
     *   "reason_code": 40300,
     *   "status_message": "Message describing failure",
     *   "severity": "ERROR"
     * }
     */
    void fileUploadFailed(String fileUploadId, int statusCode, int reasonCode, String jsonResponse);
}
