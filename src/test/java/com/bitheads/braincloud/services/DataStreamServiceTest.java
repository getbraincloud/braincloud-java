package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-09-01.
 */
public class DataStreamServiceTest extends TestFixtureBase
{
    @Test
    public void testCustomPageEvent() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getDataStreamService().customPageEvent("testPage", "{\"testProperty\":\"1\"}", tr);

        tr.Run();
    }

    @Test
    public void testCustomScreenEvent() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getDataStreamService().customScreenEvent("testScreen", "{\"testProperty\":\"1\"}", tr);

        tr.Run();
    }

    @Test
    public void testCustomTrackEvent() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getDataStreamService().customTrackEvent("testTrack", "{\"testProperty\":\"1\"}", tr);

        tr.Run();
    }

    @Test
    public void testSubmitCrashReport() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getDataStreamService().submitCrashReport("test", "test", "{\"testProperty\":\"1\"}", "test", "test", "test", "test", true, tr);

        tr.Run();
    }
}