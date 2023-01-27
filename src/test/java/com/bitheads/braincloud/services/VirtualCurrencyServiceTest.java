package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.junit.Test;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by David St-Louis on 18-09-17.
 */
public class VirtualCurrencyServiceTest extends TestFixtureBase
{
    @Test
    public void testGetCurrency() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getVirtualCurrencyService().getCurrency("_invalid_id_", tr);
        tr.Run();
    }

    @Test
    public void testGetParentCurrency() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getVirtualCurrencyService().getParentCurrency("_invalid_id_", "_invalid_level_", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_PLAYER_PARENT);
    }

    @Test
    public void testGetPeerCurrency() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getVirtualCurrencyService().getPeerCurrency("_invalid_id_", "_invalid_peer_code_", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.PROFILE_PEER_NOT_FOUND);
    }

    @Test
    public void testAwardCurrency() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getVirtualCurrencyService().awardCurrency("credits", 100, tr);
        tr.Run();
    }

    @Test
    public void testConsumeCurrency() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getVirtualCurrencyService().consumeCurrency("credits", 100, tr);
        tr.Run();
    }

    @Test
    public void testResetCurrency() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getVirtualCurrencyService().resetCurrency(tr);
        tr.Run();
    }
}
