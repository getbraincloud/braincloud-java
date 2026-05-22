package com.bitheads.braincloud.services;

import org.junit.Test;

public class CampaignServiceTest extends TestFixtureBase {

    @Test
    public void testGetMyCampaigns() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getCampaignService().getMyCampaigns(
                null,
                tr);

        tr.Run();
    }
    
}
