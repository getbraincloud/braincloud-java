package com.bitheads.braincloud.comms;

import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by bradleyh on 3/29/2016.
 */
public class FileUploader implements Runnable {

    public enum FileUploaderStatus {
        None,
        Pending,
        Uploading,
        CompleteFailed,
        CompleteSuccess
    }

    public FileUploaderStatus getStatus() {
        synchronized (_lock) {
            return _status;
        }
    }

    public String getUploadId() {
        return _uploadId;
    }

    public double getProgress() {
        synchronized (_lock) {
            return _progress;
        }
    }

    public long getBytesTransferred() {
        synchronized (_lock) {
            return _bytesTransferred;
        }
    }

    public long getTotalBytesToTransfer() {
        synchronized (_lock) {
            return _totalBytesToTransfer;
        }
    }

    public int getStatusCode() {
        synchronized (_lock) {
            return _statusCode;
        }
    }

    public int getReasonCode() {
        synchronized (_lock) {
            return _reasonCode;
        }
    }

    public String getResponse() {
        synchronized (_lock) {
            return _response;
        }
    }

    private String _serverUrl;
    private String _uploadId;
    private String _localPath;
    private String _sessionId;
    private long _timeoutThreshold = 50;
    private int _timeout = 120;
    private String _fileName;

    private FileUploaderStatus _status;
    private double _progress;
    private long _bytesTransferred;
    private long _totalBytesToTransfer;
    private int _statusCode;
    private int _reasonCode;
    private String _response;

    private final Object _lock = new Object();

    private boolean _isCanceled;

    //timeout
    private long _timeBelowThreshold;
    private long _prevTime;

    public FileUploader(String uploadId, String localPath, String serverUrl, String sessionId, int timeout, int timeoutThreshold) {
        _serverUrl = serverUrl;
        _uploadId = uploadId;
        _localPath = localPath;
        _sessionId = sessionId;
        _timeout = timeout;
        _timeoutThreshold = timeoutThreshold;

        File file = new File(localPath);
        if (!file.exists()) {
            throwError(ReasonCodes.CLIENT_UPLOAD_FILE_UNKNOWN, "File at" + localPath + " does not exist");
            return;
        }

        _fileName = file.getName();
        _status = FileUploaderStatus.Uploading;

        //set timeout start
        _prevTime = System.currentTimeMillis();

        Thread _thread = new Thread(this);
        _thread.start();
    }

    public void setBytesTransferred(long bytes) {
        synchronized (_lock) {
            _bytesTransferred = bytes;
            _progress = (double) _bytesTransferred / _totalBytesToTransfer;
        }

        long elapsedTime = System.currentTimeMillis() - _prevTime;
        double rate = bytes / (elapsedTime / 1000.0);

        if (rate > _timeoutThreshold)
            _timeBelowThreshold = 0;
        else
            _timeBelowThreshold += elapsedTime;

        _prevTime = System.currentTimeMillis();

        if (_timeBelowThreshold > _timeout * 1000) {
            _isCanceled = true;
            throwError(ReasonCodes.CLIENT_UPLOAD_FILE_TIMED_OUT,
                    "Upload of " + _fileName + " timed out.");
        }
    }

    @Override
    public void run() {
        File file = new File(_localPath);

        HttpURLConnection connection = null;
        String fileName = file.getName();
        try {
            connection = (HttpURLConnection) new URL(_serverUrl).openConnection();
            connection.setConnectTimeout(_timeout * 1000);
            connection.setReadTimeout(_timeout * 1000);

            connection.setRequestMethod("POST");
            String boundary = "---------------------------boundary";
            String tail = "\r\n--" + boundary + "--\r\n";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            String metadataPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                    + "" + "\r\n";

            String sessionPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"sessionId\"\r\n\r\n"
                    + _sessionId + "\r\n";

            String uploadIdPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"uploadId\"\r\n\r\n"
                    + _uploadId + "\r\n";

            String fileSizePart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"fileSize\"\r\n\r\n"
                    + file.length() + "\r\n";

            String fileHeader1 = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"uploadFile\"; filename=\""
                    + fileName + "\"\r\n"
                    + "Content-Type: application/octet-stream\r\n"
                    + "Content-Transfer-Encoding: binary\r\n";

            long fileLength = file.length() + tail.length();
            String fileHeader2 = "Content-length: " + fileLength + "\r\n";
            String fileHeader = sessionPart + uploadIdPart + fileSizePart + fileHeader1 + fileHeader2 + "\r\n";
            String stringData = metadataPart + fileHeader;

            _totalBytesToTransfer = stringData.length() + fileLength;
            connection.setRequestProperty("Content-length", "" + _totalBytesToTransfer);
            connection.setFixedLengthStreamingMode((int) _totalBytesToTransfer);
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            try {
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file));

                while ((bytesRead = bufInput.read(buf)) != -1 && !_isCanceled) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    out.flush();
                    progress += bytesRead;
                    // update progress
                    setBytesTransferred(progress);
                }

                // Write closing boundary and close stream
                if (!_isCanceled) {
                    out.writeBytes(tail);
                    out.flush();
                }
            } finally {
                out.close();
            }

            if (!_isCanceled) {
                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                synchronized (_lock) {
                    _statusCode = connection.getResponseCode();

                    if (_statusCode == HttpURLConnection.HTTP_OK) {
                        _response = builder.toString();
                        _status = FileUploaderStatus.CompleteSuccess;
                    } else {
                        throwError(ReasonCodes.CLIENT_UPLOAD_FILE_UNKNOWN,
                                connection.getResponseMessage());
                    }
                }
            }


        } catch (SocketTimeoutException e) {
            synchronized (_lock) {
                throwError(ReasonCodes.CLIENT_UPLOAD_FILE_TIMED_OUT,
                        "Upload of " + _fileName + " timed out.");
            }
        } catch (IOException e) {
            if (!_isCanceled)
                e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public void cancel() {
        if (_isCanceled) return;

        _isCanceled = true;
        throwError(ReasonCodes.CLIENT_UPLOAD_FILE_CANCELLED,
                "Upload of " + _fileName + " cancelled by user");
    }

    private void throwError(int reasonCode, String message) {
        _status = FileUploaderStatus.CompleteFailed;
        _statusCode = StatusCodes.CLIENT_NETWORK_ERROR;
        _reasonCode = reasonCode;
        _response = createErrorString(_statusCode, _reasonCode, message);
    }

    private String createErrorString(int statusCode, int reasonCode, String message) {
        JSONObject error = new JSONObject();
        try {
            error.put("status", statusCode);
            error.put("reason_code", reasonCode);
            error.put("status_message", message);
            error.put("severity", "ERROR");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error.toString();
    }
}
