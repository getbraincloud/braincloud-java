package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to handle data stream api calls.
 */
public class DataStreamService {
    private BrainCloudClient _client;

    public DataStreamService(BrainCloudClient client) {
        _client = client;
    }

    private enum Parameter {
        eventName,
        eventProperties,
        crashType,
        errorMsg,
        crashJson,
        crashLog,
        userName,
        userEmail,
        userNotes,
        userSubmitted
    }

    /**
     * Creates custom data stream page event
     *
     * @param eventName Name of event
     * @param jsonEventProperties Properties of event
     */
    public void customPageEvent(String eventName, String jsonEventProperties, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.eventName.name(), eventName);

            if (StringUtil.IsOptionalParameterValid(jsonEventProperties)) {
                JSONObject jsonEventPropertiesObj = new JSONObject(jsonEventProperties);
                data.put(Parameter.eventProperties.name(), jsonEventPropertiesObj);
            }

            ServerCall serverCall = new ServerCall(ServiceName.dataStream,
                    ServiceOperation.CUSTOM_PAGE_EVENT, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates custom data stream screen event
     *
     * @param eventName Name of event
     * @param jsonEventProperties Properties of event
     */
    public void customScreenEvent(String eventName, String jsonEventProperties, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.eventName.name(), eventName);

            if (StringUtil.IsOptionalParameterValid(jsonEventProperties)) {
                JSONObject jsonEventPropertiesObj = new JSONObject(jsonEventProperties);
                data.put(Parameter.eventProperties.name(), jsonEventPropertiesObj);
            }

            ServerCall serverCall = new ServerCall(ServiceName.dataStream,
                    ServiceOperation.CUSTOM_SCREEN_EVENT, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates custom data stream track event
     *
     * @param eventName Name of event
     * @param jsonEventProperties Properties of event
     */
    public void customTrackEvent(String eventName, String jsonEventProperties, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.eventName.name(), eventName);

            if (StringUtil.IsOptionalParameterValid(jsonEventProperties)) {
                JSONObject jsonEventPropertiesObj = new JSONObject(jsonEventProperties);
                data.put(Parameter.eventProperties.name(), jsonEventPropertiesObj);
            }

            ServerCall serverCall = new ServerCall(ServiceName.dataStream,
                    ServiceOperation.CUSTOM_TRACK_EVENT, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send crash report
     *
     * @param crashType
     * @param errorMsg
     * @param crashJson
     * @param crashLog
     * @param userName
     * @param userEmail
     * @param userNotes
     * @param userSubmitted
     */
    public void submitCrashReport(String crashType, String errorMsg, String crashJson, String crashLog, String userName, String userEmail, String userNotes, Boolean userSubmitted, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.crashType.name(), crashType);
            data.put(Parameter.errorMsg.name(), errorMsg);
            JSONObject crashJsonData = new JSONObject(crashJson);
            data.put(Parameter.crashJson.name(), crashJsonData);
            data.put(Parameter.crashLog.name(), crashLog);
            data.put(Parameter.userName.name(), userName);
            data.put(Parameter.userEmail.name(), userEmail);
            data.put(Parameter.userNotes.name(), userNotes);
            data.put(Parameter.userSubmitted.name(), userSubmitted);

            ServerCall serverCall = new ServerCall(ServiceName.dataStream,
                    ServiceOperation.SEND_CRASH_REPORT, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
