package com.bitheads.braincloud.services;
import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.Platform;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by prestonjennings on 2015-10-27.
 */
public class RedemptionServiceTest extends TestFixtureBase
{
    @Test
    public void testRedeemCode() throws Exception
    {
        int code = getValidCode();
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getRedemptionCodeService().redeemCode("" + code, "default", null, tr);
        tr.Run();
    }

    @Test
    public void testGetRedeemedCodes() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getRedemptionCodeService().getRedeemedCodes("default", tr);
        tr.Run();
    }

    public int getValidCode() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);
        JSONObject stats = new JSONObject();
        stats.put("lastCodeUsed", 1);
        _wrapper.getGlobalStatisticsService().incrementGlobalStats(stats.toString(), tr);
        tr.Run();
        return tr.m_response.getJSONObject("data").getJSONObject("statistics").getInt("lastCodeUsed");
    }

}
