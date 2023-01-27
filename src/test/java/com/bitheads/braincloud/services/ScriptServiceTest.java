package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class ScriptServiceTest extends TestFixtureBase {
    private final String _scriptName = "testScript";
    private final String _peerScriptName = "TestPeerScriptPublic";

    @Test
    public void testRunScript() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getScriptService().runScript(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                tr);

        tr.Run();
    }

    @Test
    public void testScheduleRunScriptUTC() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        Date date = new Date();
        date.setTime(date.getTime() + 120 * 1000);
        _wrapper.getScriptService().scheduleRunScriptUTC(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                date,
                tr);

        tr.Run();
    }

    @Test
    public void testScheduleRunScriptMillisUTC() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        Date date = new Date();
        date.setTime(date.getTime() + 120 * 1000);
        _wrapper.getScriptService().scheduleRunScriptMillisUTC(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                TimeUtil.UTCDateTimeToUTCMillis(date),
                tr);

        tr.Run();
    }

    @Test
    public void testScheduleRunScriptUTC_DATE_CONVERSIONS() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        Date date = new Date();
        long testDateAsLong = TimeUtil.UTCDateTimeToUTCMillis(date);
        System.out.println("UTC MILLIS : " + testDateAsLong);
        Date testDate = TimeUtil.UTCMillisToUTCDateTime(testDateAsLong);
        System.out.println("UTC Date : " + testDate);
        System.out.println("UTC DateTime : " + TimeUtil.LocalTimeToUTCTime(date));
        System.out.println("Local DateTime : " + TimeUtil.UTCTimeToLocalTime(date));
        
        date.setTime(date.getTime() + 120 * 1000);
        _wrapper.getScriptService().scheduleRunScriptUTC(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                TimeUtil.LocalTimeToUTCTime(date),
                tr);
        tr.Run();
    }

    @Test
    public void testScheduleRunScriptMinutes() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getScriptService().scheduleRunScriptMinutes(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                60,
                tr);

        tr.Run();
    }

    @Test
    public void testCancelScheduledScript() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getScriptService().scheduleRunScriptMinutes(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                60,
                tr);

        tr.Run();

        String jobId = tr.m_response.getJSONObject("data").getString("jobId");

        _wrapper.getScriptService().cancelScheduledScript(
                jobId, tr);

        tr.Run();
    }

    @Test
    public void testRunParentScript() throws Exception {
        goToChildProfile();

        TestResult tr = new TestResult(_wrapper);
        _wrapper.getScriptService().runParentScript(
                _scriptName,
                Helpers.createJsonPair("testParm1", 1),
                m_parentLevelName,
                tr);
        tr.Run();
    }

    @Test
    public void runPeerScript() throws Exception {
        if(attachPeer(Users.UserA)) {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getScriptService().runPeerScript(
                    _peerScriptName,
                    Helpers.createJsonPair("testParm1", 1),
                    m_peerName,
                    tr);
            tr.Run();

            detachPeer();
        }
    }

    @Test
    public void runPeerScriptAsync() throws Exception {
        if(attachPeer(Users.UserA)) {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getScriptService().runPeerScriptAsync(
                    _peerScriptName,
                    Helpers.createJsonPair("testParm1", 1),
                    m_peerName,
                    tr);
            tr.Run();

            detachPeer();
        }
    }
}