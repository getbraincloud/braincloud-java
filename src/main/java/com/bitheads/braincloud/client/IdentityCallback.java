package com.bitheads.braincloud.client;

import com.bitheads.braincloud.client.IServerCallback;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IdentityCallback implements IServerCallback 
{
    private BrainCloudWrapper _wrapper;
    private IServerCallback _callback;

    public IdentityCallback(BrainCloudWrapper in_wrapper, IServerCallback in_callback) 
    {
        _wrapper = in_wrapper;
        _callback = in_callback;
    }

    public void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject jsonData) 
    {
        List<String> keyList = new ArrayList<String>();
        for (Iterator<String> it = jsonData.keys(); it.hasNext(); )
        {
            String key = it.next();
            keyList.add(key);
        }

        String[] listOfidentities = keyList.toArray(new String[keyList.size()]);

        if (listOfidentities.length > 0)
        {
            _wrapper.getClient().getPlayerStateService().logout(null);
        }
        else
        {
            _wrapper.getClient().getPlayerStateService().deleteUser(null);
        }

        _wrapper.getClient().insertEndOfMessageBundleMarker();
        _callback.serverCallback(serviceName, serviceOperation, jsonData);
    }

    public void serverError(ServiceName in_serviceName, ServiceOperation in_serviceOperation, int in_statusCode, int in_reasonCode, String jsonString) 
    {
        _callback.serverError(in_serviceName, in_serviceOperation, in_statusCode, in_reasonCode, jsonString);
    }
}
