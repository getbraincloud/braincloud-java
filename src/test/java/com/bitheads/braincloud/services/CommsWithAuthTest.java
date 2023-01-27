package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.AuthenticationType;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by David St-Louis on 18-06-14.
 */
public class CommsWithAuthTest extends TestFixtureBase
{
    @Test
    public void testTimeoutRetry30sec() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        tr.setMaxWait(60);

        _wrapper.getScriptService().runScript(
            "TestTimeoutRetry",
            Helpers.createJsonPair("testParm1", 1),
            tr);

        tr.Run();
    }

    @Test
    public void testTimeoutRetry45sec() throws Exception {
        TestResult tr = new TestResult(_wrapper);
            tr.setMaxWait(120);

        _wrapper.getScriptService().runScript(
            "TestTimeoutRetry45",
            Helpers.createJsonPair("testParm1", 1),
            tr);

        tr.Run();
    }
}