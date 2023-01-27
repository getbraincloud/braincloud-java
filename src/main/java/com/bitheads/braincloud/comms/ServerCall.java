package com.bitheads.braincloud.comms;

import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerCall {

    private ServiceName _serviceName;

    private ServiceOperation _serviceOperation;
    private JSONObject _data;
    private IServerCallback _callback;
    boolean _isEndOfBundleMarker;

    public ServerCall(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject data, IServerCallback callback) {
        _serviceName = serviceName;
        _serviceOperation = serviceOperation;
        _data = data;
        _callback = callback;
        _isEndOfBundleMarker = false;
    }

    public JSONObject getPayload() throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("service", _serviceName.name());
        payload.put("operation", _serviceOperation.name());
        if(_data != null) payload.put("data", _data);

        return payload;
    }

    public ServiceName getServiceName() {
        return _serviceName;
    }

    public void setServiceName(ServiceName _serviceName) {
        this._serviceName = _serviceName;
    }

    public ServiceOperation getServiceOperation() {
        return _serviceOperation;
    }

    public void setServiceOperation(ServiceOperation _serviceOperation) {
        this._serviceOperation = _serviceOperation;
    }

    public IServerCallback getCallback() {
        return _callback;
    }

    public void setCallback(IServerCallback _callback) {
        this._callback = _callback;
    }

    public boolean isEndOfBundleMarker() {
        return _isEndOfBundleMarker;
    }

    public void setEndOfBundleMarker(boolean value) {
        _isEndOfBundleMarker = value;
    }
}
