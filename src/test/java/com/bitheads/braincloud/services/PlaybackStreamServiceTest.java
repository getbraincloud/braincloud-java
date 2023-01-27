package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.json.JSONArray;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class PlaybackStreamServiceTest extends TestFixtureBase
{

    @Test
    public void testStartStream() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        String streamId = "";

        _wrapper.getPlaybackStreamService().startStream(
                getUser(Users.UserB).profileId,
                true,
                tr);

        if (tr.Run())
        {
            streamId = tr.m_response.getJSONObject("data").getString("playbackStreamId");
        }

        endStream(streamId);
    }

    @Test
    public void testReadStream() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String streamId = startStream();

        _wrapper.getPlaybackStreamService().readStream(
                streamId,
                tr);

        tr.Run();
        endStream(streamId);
    }

    @Test
    public void testEndStream() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String streamId = startStream();

        _wrapper.getPlaybackStreamService().endStream(
                streamId,
                tr);

        tr.Run();
    }

    @Test
    public void testDeleteStream() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String streamId = startStream();

        _wrapper.getPlaybackStreamService().deleteStream(
                streamId,
                tr);

        tr.Run();
    }

    @Test
    public void testAddEvent() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String streamId = startStream();

        _wrapper.getPlaybackStreamService().addEvent(
                streamId,
                Helpers.createJsonPair("data", 1),
                Helpers.createJsonPair("total", 5),
                tr);

        tr.Run();
        endStream(streamId);
    }

    @Test
    public void testGetRecentStreamsForTargetPlayer() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        String streamId = startStream();
        int maxStream = 10;

        _wrapper.getPlaybackStreamService().getRecentStreamsForTargetPlayer(
                getUser(Users.UserB).profileId,
                maxStream,
                tr);

        tr.Run();
        endStream(streamId);
    }
    
    ///// helpers

    private String startStream() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        String streamId = "";

        _wrapper.getPlaybackStreamService().startStream(
                getUser(Users.UserB).profileId,
                true,
                tr);

        if (tr.Run())
        {
            streamId = tr.m_response.getJSONObject("data").getString("playbackStreamId");
        }

        return streamId;
    }

    private void endStream(String streamId)
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlaybackStreamService().endStream(
                streamId,
                tr);

        tr.Run();
    }
}