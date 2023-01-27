package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class GamificationServiceTest extends TestFixtureBase
{
    private final String _achievementId01 = "testAchievement01";
    private final String _achievementId02 = "testAchievement02";
    private final String _milestoneCategory = "Experience";
    private final String _milestoneId = "2";
    private final String _questsCategory = "Experience";
    
    @Test
    public void testReadAllGamification() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readAllGamification(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadMilestones() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readMilestones(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadAchievements() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readAchievements(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadXpLevels() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readXpLevels(
                tr);

        tr.Run();
    }

    @Test
    public void testReadAchievedAchievements() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readAchievedAchievements(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadCompletedMilestones() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readCompletedMilestones(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadInProgressMilestones() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readInProgressMilestones(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadMilestonesByCategory() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readMilestonesByCategory(
                _milestoneCategory,
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testAwardAchievements() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().awardAchievements(
                new String[]{_achievementId01, _achievementId02},
                tr);

        tr.Run();
    }

    @Test
    public void testCheckForAchievementsToAward() throws Exception
    {

    }

    @Test
    public void testReadQuests() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuests(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadQuestsCompleted() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuestsCompleted(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadQuestsInProgress() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuestsInProgress(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadQuestsNotStarted() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuestsNotStarted(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadQuestsWithStatus() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuestsWithStatus(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadQuestsWithBasicPercentage() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuestsWithBasicPercentage(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadQuestsWithComplexPercentage() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuestsWithComplexPercentage(
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testReadQuestsByCategory() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getGamificationService().readQuestsByCategory(
                _questsCategory,
                true,
                tr);

        tr.Run();
    }
}