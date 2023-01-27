package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IRewardCallback;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class PlayerStatisticsEventServiceTest extends TestFixtureBase implements IRewardCallback
{
    private final String _eventId01 = "testEvent01";
    private final String _eventId02 = "rewardCredits";
    private int m_rewardCallbackHitCount = 0;

    @Test
    public void testTriggerPlayerStatisticsEvent() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStatisticsEventService().triggerStatsEvent(
                _eventId01,
                1,
                tr);

        tr.Run();
    }

    @Test
    public void testTriggerPlayerStatisticsEvents() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        JSONObject event1 = new JSONObject();
        event1.put("eventName", _eventId01);
        event1.put("eventMultiplier", 1);

        JSONObject event2 = new JSONObject();
        event2.put("eventName", _eventId02);
        event2.put("eventMultiplier", 1);

        JSONArray events = new JSONArray();
        events.put(event1);
        events.put(event2);

        _wrapper.getPlayerStatisticsEventService().triggerStatsEvents(
                events.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testRewardHandlerTriggerStatisticsEvents() throws Exception
    {
        m_rewardCallbackHitCount = 0;
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPlayerStateService().resetUser(tr);
        tr.Run();

        JSONObject event1 = new JSONObject();
        event1.put("eventName", "incQuest1Stat");
        event1.put("eventMultiplier", 1);

        JSONObject event2 = new JSONObject();
        event2.put("eventName", "incQuest2Stat");
        event2.put("eventMultiplier", 1);

        JSONArray events = new JSONArray();
        events.put(event1);
        events.put(event2);

        _wrapper.getClient().registerRewardCallback(this);

        _wrapper.getPlayerStatisticsEventService().triggerStatsEvents(
                events.toString(),
                tr);
        tr.Run();

        Assert.assertEquals(m_rewardCallbackHitCount, 1);
    }

    @Test
    public void testRewardHandlerMultipleApiCallsInBundle() throws Exception
    {
        m_rewardCallbackHitCount = 0;
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPlayerStateService().resetUser(tr);
        tr.Run();

        JSONObject event1 = new JSONObject();
        event1.put("eventName", "incQuest1Stat");
        event1.put("eventMultiplier", 1);
        JSONArray events1 = new JSONArray();
        events1.put(event1);

        JSONObject event2 = new JSONObject();
        event2.put("eventName", "incQuest2Stat");
        event2.put("eventMultiplier", 1);
        JSONArray events2 = new JSONArray();
        events2.put(event2);

        _wrapper.getClient().registerRewardCallback(this);

        _wrapper.getPlayerStatisticsEventService().triggerStatsEvents(
                events1.toString(),
                tr);
        _wrapper.getPlayerStatisticsEventService().triggerStatsEvents(
                events2.toString(),
                tr);
        tr.RunExpectCount(2);

        Assert.assertEquals(m_rewardCallbackHitCount, 2);
    }

    public void rewardCallback(JSONObject jsonRewards)
    {
        try
        {
            System.out.println("rewardCallback -- " + jsonRewards.toString(2));
        }
        catch(JSONException je)
        {
            je.printStackTrace();
        }
        ++m_rewardCallbackHitCount;
    }
}