package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.BrainCloudWrapper;
import com.bitheads.braincloud.client.IGlobalErrorCallback;
import com.bitheads.braincloud.client.INetworkErrorCallback;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.StatusCodes;

import junit.framework.Assert;

import org.json.JSONObject;

public class TestResult implements IServerCallback, IGlobalErrorCallback, INetworkErrorCallback
{
    public boolean m_done;
    public boolean m_result;
    public int m_apiCountExpected;

    // if success
    public JSONObject m_response;

    // if error
    public int m_statusCode;
    public int m_reasonCode;
    public String m_statusMessage;
    public int m_globalErrorCount;
    public int m_networkErrorCount;
    public int m_maxWait = 30 * 1000;

    BrainCloudWrapper _wrapper;

    public TestResult(BrainCloudWrapper wrapper)
    {
        _wrapper = wrapper;
    }

    public void Reset()
    {
        m_done = false;
        m_result = false;
        m_apiCountExpected = 0;
        m_response = null;
        m_statusCode = 0;
        m_reasonCode = 0;
        m_statusMessage = null;
        m_globalErrorCount = 0;
        m_networkErrorCount = 0;
    }

    public boolean Run()
    {
        return Run(false);
    }

    public boolean Run(boolean noAssert)
    {
        Reset();
        Spin();

        if(!noAssert) {
            Assert.assertTrue(m_result);
        }

        return m_result;
    }

    public boolean RunExpectCount(int in_apiCountExpected)
    {
        Reset();
        m_apiCountExpected = in_apiCountExpected;
        Spin();

        Assert.assertTrue(m_result);

        return m_result;
    }

    public boolean RunExpectFail(int in_expectedStatusCode, int in_expectedReasonCode)
    {
        Reset();
        Spin();

        Assert.assertFalse(m_result);
        if (in_expectedStatusCode != -1)
        {
            Assert.assertEquals(in_expectedStatusCode, m_statusCode);
        }
        if (in_expectedReasonCode != -1)
        {
            Assert.assertEquals(in_expectedReasonCode, m_reasonCode);
        }

        return !m_result;
    }

    public void serverCallback(ServiceName serviceName, ServiceOperation serviceOperation, JSONObject jsonData)
    {
        m_response = jsonData;
        m_result = true;
        --m_apiCountExpected;
        if (m_apiCountExpected <= 0)
        {
            m_done = true;
        }
    }

    public void serverError(ServiceName serviceName, ServiceOperation serviceOperation, int statusCode, int reasonCode, String statusMessage)
    {
        m_statusCode = statusCode;
        m_reasonCode = reasonCode;
        m_statusMessage = statusMessage;
        m_result = false;
        --m_apiCountExpected;
        if (m_apiCountExpected <= 0)
        {
            m_done = true;
        }
    }

    public void globalError(ServiceName serviceName, ServiceOperation serviceOperation, int statusCode, int reasonCode, String statusMessage)
    {
        ++m_globalErrorCount;
        m_statusCode = statusCode;
        m_reasonCode = reasonCode;
        m_statusMessage = statusMessage;
        m_result = false;
        --m_apiCountExpected;
        if (m_apiCountExpected <= 0)
        {
            m_done = true;
        }
    }

    public void networkError()
    {
        ++m_networkErrorCount;
        m_statusCode = StatusCodes.CLIENT_NETWORK_ERROR;
        m_reasonCode = ReasonCodes.CLIENT_NETWORK_ERROR_TIMEOUT;
        m_statusMessage = "Network error";
        m_result = false;
        --m_apiCountExpected;
        if (m_apiCountExpected <= 0)
        {
            m_done = true;
        }
    }

    public void setMaxWait(int maxWaitSecs)
    {
        m_maxWait = maxWaitSecs * 1000;
    }

    public boolean IsDone()
    {
        return m_done;
    }

    private void Spin()
    {
        long maxWait = m_maxWait;
        while(!m_done && maxWait > 0)
        {
            TestFixtureBase._client.runCallbacks();
            _wrapper.runCallbacks();
            try
            {
                Thread.sleep (100);
            } catch(InterruptedException ie)
            {}
            maxWait -= 100;
        }
    }
}
