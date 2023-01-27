package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;

public interface IAchievementsDelegate extends IServerCallback {

    void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, String jsonData);
}
