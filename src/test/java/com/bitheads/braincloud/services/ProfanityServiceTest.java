package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prestonjennings on 15-12-14.
 */
public class ProfanityServiceTest extends TestFixtureBase
{

    @Test
    public void testProfanityCheck() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getProfanityService().profanityCheck("shitbird fly away", "en", true, true, true, tr);
        tr.Run();
    }

    @Test
    public void testProfanityReplaceText() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getProfanityService().profanityReplaceText("shitbird fly away", "*", "en", false, false, false, tr);
        tr.Run();
    }

    @Test
    public void testProfanityIdentifyBadWords() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getProfanityService().profanityIdentifyBadWords("shitbird fly away", "en,fr", true, false, false, tr);
        tr.Run();
    }
}