package com.bitheads.braincloud.services;

import org.junit.Test;

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