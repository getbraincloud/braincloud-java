package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class MatchMakingServiceTest extends TestFixtureBase {

    @Test
    public void testRead() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().read(tr);

        tr.Run();
    }

    @Test
    public void testSetPlayerRating() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().setPlayerRating(
                5,
                tr);

        tr.Run();
    }

    @Test
    public void testResetPlayerRating() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().resetPlayerRating(
                tr);

        tr.Run();
    }

    @Test
    public void testIncrementPlayerRating() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().incrementPlayerRating(
                2,
                tr);

        tr.Run();
    }

    @Test
    public void testDecrementPlayerRating() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().decrementPlayerRating(
                2,
                tr);

        tr.Run();
    }

    @Test
    public void testTurnShieldOn() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().turnShieldOn(
                tr);

        tr.Run();
    }

    @Test
    public void testTurnShieldOnFor() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().turnShieldOnFor(
                1,
                tr);

        tr.Run();
    }

    @Test
    public void testIncrementShieldOnFor() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().incrementShieldOnFor(
                1,
                tr);

        tr.Run();
    }

    @Test
    public void testTurnShieldOff() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().turnShieldOff(
                tr);

        tr.Run();
    }

    @Test
    public void testGetShieldExpiry() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().getShieldExpiry(
                null,
                tr);

        tr.Run();
    }

    @Test
    public void testFindPlayers() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().findPlayers(
                3,
                5,
                tr);

        tr.Run();
    }

    @Test
    public void testFindPlayersUsingFilter() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        JSONObject filters = new JSONObject();
        filters.put("filter1", 10);

        _wrapper.getMatchMakingService().findPlayersUsingFilter(
                3,
                5,
                filters.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testFindPlayersWithAttributes() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        JSONObject attributes = new JSONObject();
        attributes.put("name", "asdf");

        _wrapper.getMatchMakingService().findPlayersWithAttributes(
                3,
                5,
                attributes.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testFindPlayersWithAttributesUsingFilter() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        JSONObject filters = new JSONObject();
        filters.put("filter1", 10);

        JSONObject attributes = new JSONObject();
        attributes.put("name", "asdf");

        _wrapper.getMatchMakingService().findPlayersWithAttributesUsingFilter(
                3,
                5,
                attributes.toString(),
                filters.toString(),
                tr);

        tr.Run();
    }

    @Test
    public void testEnableMatchMaking() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().enableMatchMaking(tr);
        tr.Run();
    }

    @Test
    public void testDisableMatchMaking() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getMatchMakingService().disableMatchMaking(tr);
        tr.Run();

        _wrapper.getMatchMakingService().enableMatchMaking(tr);
        tr.Run();
    }
}