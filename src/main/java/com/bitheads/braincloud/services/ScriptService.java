package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ScriptService {

    public enum Parameter {
        scriptName,
        scriptData,
        startDateUTC,
        minutesFromNow,
        parentLevel,
        jobId,
        peer
    }

    private BrainCloudClient _client;

    public ScriptService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Executes a script on the server.
     *
     * Service Name - Script
     * Service Operation - Run
     *
     * @param scriptName The name of the script to be run
     * @param jsonScriptData Data to be sent to the script in json format
     * See The API documentation site for more details on cloud code
     */
    public void runScript(String scriptName, String jsonScriptData, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scriptName.name(), scriptName);

            if (StringUtil.IsOptionalParameterValid(jsonScriptData)) {
                JSONObject jsonData = new JSONObject(jsonScriptData);
                data.put(Parameter.scriptData.name(), jsonData);
            }

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.RUN, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * @deprecated Use scheduleRunScriptMillisUTC instead - Removal September 1, 2021
     */
    public void scheduleRunScriptUTC(String scriptName, String jsonScriptData, Date startTimeUTC, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scriptName.name(), scriptName);

            if (StringUtil.IsOptionalParameterValid(jsonScriptData)) {
                JSONObject jsonData = new JSONObject(jsonScriptData);
                data.put(Parameter.scriptData.name(), jsonData);
            }

            data.put(Parameter.startDateUTC.name(), startTimeUTC.getTime());

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.SCHEDULE_CLOUD_SCRIPT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Allows cloud script executions to be scheduled
     *
     * Service Name - Script
     * Service Operation - ScheduleCloudScript
     *
     * @param scriptName The name of the script to be run
     * @param jsonScriptData JSON bundle to pass to script
     * @param startTimeUTC The start date as a Date object
     * See The API documentation site for more details on cloud code
     */
    public void scheduleRunScriptMillisUTC(String scriptName, String jsonScriptData, long startTimeUTC, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scriptName.name(), scriptName);

            if (StringUtil.IsOptionalParameterValid(jsonScriptData)) {
                JSONObject jsonData = new JSONObject(jsonScriptData);
                data.put(Parameter.scriptData.name(), jsonData);
            }

            data.put(Parameter.startDateUTC.name(), startTimeUTC);

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.SCHEDULE_CLOUD_SCRIPT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Allows cloud script executions to be scheduled
     *
     * Service Name - Script
     * Service Operation - ScheduleCloudScript
     *
     * @param scriptName The name of the script to be run
     * @param jsonScriptData JSON bundle to pass to script
     * @param minutesFromNow Number of minutes from now to run script
     * See The API documentation site for more details on cloud code
     */
    public void scheduleRunScriptMinutes(String scriptName, String jsonScriptData, int minutesFromNow, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scriptName.name(), scriptName);

            if (StringUtil.IsOptionalParameterValid(jsonScriptData)) {
                JSONObject jsonData = new JSONObject(jsonScriptData);
                data.put(Parameter.scriptData.name(), jsonData);
            }

            data.put(Parameter.minutesFromNow.name(), minutesFromNow);

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.SCHEDULE_CLOUD_SCRIPT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Run a cloud script in a parent app
     *
     * Service Name - Script
     * Service Operation - RUN_PARENT_SCRIPT
     *
     * @param scriptName The name of the script to be run
     * @param scriptData Data to be sent to the script in json format
     * @param parentLevel The level name of the parent to run the script from
     * @param callback The method to be invoked when the server response is received
     * See The API documentation site for more details on cloud code
     */
    public void runParentScript(String scriptName,
                                String scriptData,
                                String parentLevel,
                                IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scriptName.name(), scriptName);
            data.put(Parameter.parentLevel.name(), parentLevel);

            if (StringUtil.IsOptionalParameterValid(scriptData)) {
                JSONObject jsonData = new JSONObject(scriptData);
                data.put(Parameter.scriptData.name(), jsonData);
            }

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.RUN_PARENT_SCRIPT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

        /**
     * Allows cloud script executions to be scheduled
     *
     * Service Name - Script
     * Service Operation - ScheduleCloudScript
     *
     * @param startTimeUTC The start date as a Date object
     * See The API documentation site for more details on cloud code
     */
    public void getScheduledCloudScripts(Date startTimeUTC, IServerCallback callback) {

        try {
            JSONObject data = new JSONObject();


            data.put(Parameter.startDateUTC.name(), startTimeUTC.getTime());

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.GET_SCHEDULED_CLOUD_SCRIPTS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Allows cloud script executions to be scheduled
     *
     * Service Name - Script
     * Service Operation - ScheduleCloudScript
     *
     * See The API documentation site for more details on cloud code
     */
    public void getRunningOrQueuedCloudScripts(IServerCallback callback) {
            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.GET_RUNNING_OR_QUEUED_CLOUD_SCRIPTS, null, callback);
            _client.sendRequest(sc);
    }

    /**
     * Cancels a scheduled cloud code script
     *
     * Service Name - Script
     * Service Operation - CANCEL_SCHEDULED_SCRIPT
     *
     * @param jobId The scheduled script job to cancel
     * @param callback The method to be invoked when the server response is received
     */
    public void cancelScheduledScript(String jobId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.jobId.name(), jobId);

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.CANCEL_SCHEDULED_SCRIPT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Runs a script from the context of a peer
     *
     * Service Name - Script
     * Service Operation - RUN_PEER_SCRIPT
     *
     * @param scriptName The name of the script to be run
     * @param jsonScriptData Data to be sent to the script in json format
     * @param peer Peer the script belongs to
     * @param callback The method to be invoked when the server response is received
     */
    public void runPeerScript(String scriptName, String jsonScriptData, String peer, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scriptName.name(), scriptName);
            data.put(Parameter.peer.name(), peer);

            if (StringUtil.IsOptionalParameterValid(jsonScriptData)) {
                JSONObject jsonData = new JSONObject(jsonScriptData);
                data.put(Parameter.scriptData.name(), jsonData);
            }

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.RUN_PEER_SCRIPT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Runs a script asynchronously from the context of a peer
     * This method does not wait for the script to complete before returning
     *
     * Service Name - Script
     * Service Operation - RUN_PEER_SCRIPT_ASYNC
     *
     * @param scriptName The name of the script to be run
     * @param jsonScriptData Data to be sent to the script in json format
     * @param peer Peer the script belongs to
     * @param callback The method to be invoked when the server response is received
     */
    public void runPeerScriptAsync(String scriptName, String jsonScriptData, String peer, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.scriptName.name(), scriptName);
            data.put(Parameter.peer.name(), peer);

            if (StringUtil.IsOptionalParameterValid(jsonScriptData)) {
                JSONObject jsonData = new JSONObject(jsonScriptData);
                data.put(Parameter.scriptData.name(), jsonData);
            }

            ServerCall sc = new ServerCall(ServiceName.script, ServiceOperation.RUN_PEER_SCRIPT_ASYNC, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
