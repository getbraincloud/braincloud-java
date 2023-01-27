package com.bitheads.braincloud.services;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class PlayerStatisticsServiceTest extends TestFixtureBase
{

    @Test
    public void testReadAllPlayerStats() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStatisticsService().readAllUserStats(
                tr);

        tr.Run();
    }

    @Test
    public void testReadPlayerStatsSubset() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        String[] stats = {"currency", "highestScore"};

        _wrapper.getPlayerStatisticsService().readUserStatsSubset(
                stats,
                tr);

        tr.Run();
    }

    @Test
    public void testReadPlayerStatisticsByCategory() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getPlayerStatisticsService().readUserStatsForCategory("Test", tr);
        tr.Run();
    }

    @Test
    public void testResetAllPlayerStats() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStatisticsService().resetAllUserStats(
                tr);

        tr.Run();
    }

    @Test
    public void testIncrementPlayerStats() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        JSONObject stats = new JSONObject();
        stats.put("highestScore", "RESET");

        _wrapper.getPlayerStatisticsService().incrementUserStats(
                stats.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testIncrementExperiencePoints() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStatisticsService().incrementExperiencePoints(
                10,
                tr);

        tr.Run();
    }

    @Test
    public void testGetNextExperienceLevel() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStatisticsService().getNextExperienceLevel(
                tr);

        tr.Run();
    }

    @Test
    public void testSetExperiencePoints() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getPlayerStatisticsService().setExperiencePoints(
                100,
                tr);

        tr.Run();
    }

    @Test
    public void testProcessStatistics() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        JSONObject stats = new JSONObject();
        stats.put("TestStat", "RESET");

        _wrapper.getPlayerStatisticsService().processStatistics(
                stats.toString(),
                tr);

        tr.Run();
    }
}