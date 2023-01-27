package com.bitheads.braincloud.comms;

import org.json.JSONObject;

/**
 * Created by prestonjennings on 15-09-01.
 */
public class ServerResponse {

    boolean _isError;

    ServerCall _serverCall;

    int _statusCode;
    int _reasonCode;

    // on success
    JSONObject _data;

    // on failure
    String _statusMessage;
}
