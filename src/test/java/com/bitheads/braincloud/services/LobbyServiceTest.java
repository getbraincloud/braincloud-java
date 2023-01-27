package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

import org.junit.Test;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by David St-Louis on 18-07-17.
 */
public class LobbyServiceTest extends TestFixtureBase
{
    static private String _lobbyId = "";

    @Test
    public void testGetLobbyInstances() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().getLobbyInstances("MATCH_UNRANKED", "{\"rating\":{\"min\":1,\"max\":1000}}", tr);
        tr.Run();
    }

    @Test
    public void testGetLobbyInstancesWithPingData() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        String[] lobbyTypes = new String[1];
        lobbyTypes[0] = "MATCH_UNRANKED";
        _wrapper.getLobbyService().getRegionsForLobbies(lobbyTypes, tr);
        tr.Run();

        _wrapper.getLobbyService().pingRegions(tr);
        tr.Run();

        _wrapper.getLobbyService().getLobbyInstancesWithPingData("MATCH_UNRANKED", "{\"rating\":{\"min\":1,\"max\":1000},\"ping\":{\"max\":100}}", tr);
        tr.Run();
    }

    @Test
    public void testCreateLobby() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().createLobby("MATCH_UNRANKED", 0, null, true, "{}", "all", "{}", tr);
        tr.Run();
    }

    @Test
    public void testFindLobby() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().findLobby("MATCH_UNRANKED", 0, 1, "{\"strategy\":\"ranged-absolute\",\"alignment\":\"center\",\"ranges\":[1000]}", "{}", null, true, "{}", "all", tr);
        tr.Run();
    }

    @Test
    public void testFindOrCreateLobby() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().findOrCreateLobby("MATCH_UNRANKED", 0, 1, "{\"strategy\":\"ranged-absolute\",\"alignment\":\"center\",\"ranges\":[1000]}", "{}", null, "{}", true, "{}", "all", tr);
        tr.Run();
    }

    @Test
    public void testGetLobbyData() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().getLobbyData("wrongLobbyId", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }

    @Test
    public void testLeaveLobby() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().leaveLobby("wrongLobbyId", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }

    @Test
    public void testJoinLobby() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().joinLobby("wrongLobbyId", true, "{}", "red", null, tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }

    @Test
    public void testRemoveMember() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().removeMember("wrongLobbyId", "wrongConId", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }

    @Test
    public void testSendSignal() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().sendSignal("wrongLobbyId", "{\"msg\":\"test\"}", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }

    @Test
    public void testSwitchTeam() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().switchTeam("wrongLobbyId", "all", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }

    @Test
    public void testUpdateReady() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().updateReady("wrongLobbyId", true, "{}", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }

    @Test
    public void testUpdateSettings() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getLobbyService().updateSettings("wrongLobbyId", "{\"test\":\"me\"}", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }
  
    @Test
    public void testCancelFindRequest() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        //_wrapper.getLobbyService().cancelFindRequest("MATCH_UNRANKED", _wrapper.client.getRttConnectionId(), tr);
        //tr.Run();

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //need to come back to this test. When I send a bad cxId, it actually sends the parameter cxId to the server. But when I send a proper 
        //cxId, it only sends the lobbyType and no cxId parameter, so it always says that the cxId parameter is missing. 
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        _wrapper.getLobbyService().cancelFindRequest("MATCH_UNRANKED", "badCxId", tr);
        //40653 is cxId must belong to the caller. 
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, 40653);;
    }

    @Test
    public void testPings() throws Exception {

        String[] lobbyTypes = new String[1];
        lobbyTypes[0] = "MATCH_UNRANKED";
        String[] badLobbyTypes = new String[1];
        badLobbyTypes[0] = "InvalidLobbyId";
        
        TestResult tr = new TestResult(_wrapper);

        // Didn't getRegionsForLobbies, should fail
        _wrapper.getLobbyService().pingRegions(tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_REQUIRED_PARAMETER);

        // Wrong lobby types, should fail
        _wrapper.getLobbyService().getRegionsForLobbies(badLobbyTypes, tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_TYPE_NOT_FOUND);

        // Proper get regions
        _wrapper.getLobbyService().getRegionsForLobbies(lobbyTypes, tr);
        tr.Run();

        // Call a function <>WithPingData without having ping servers, should fail
        _wrapper.getLobbyService().createLobbyWithPingData("MATCH_UNRANKED", 0, null, true, "{}", "all", "{}", tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.MISSING_REQUIRED_PARAMETER);

        // Ping
        _wrapper.getLobbyService().pingRegions(tr);
        tr.Run();

        // Do it again and make sure pings are not all bellow 10 which would happen in case of http caching
        _wrapper.getLobbyService().pingRegions(tr);
        tr.Run();
        JSONObject pingData = _wrapper.getLobbyService().getPingData();
        if (pingData == null) {
            throw new Exception("Expected valid pingData");
        }
        int avg = 0;
        for (int i = 0; i < pingData.names().length(); ++i) {
            String regionName = pingData.names().getString(i);
            int ping = pingData.getInt(regionName);
            avg += ping;
        }
        avg /= pingData.names().length();
        if (avg <= 10) {
            throw new Exception("Pings are all too small, it's impossible we ping east to west. Cached HTTP calls?");
        }

        // Call all the <>WithPingData functions and make sure they go through braincloud
        _wrapper.getLobbyService().findOrCreateLobbyWithPingData("MATCH_UNRANKED", 0, 1, "{\"strategy\":\"ranged-absolute\",\"alignment\":\"center\",\"ranges\":[1000]}", "{}", null, "{}", true, "{}", "all", tr);
        tr.Run();

        _wrapper.getLobbyService().findLobbyWithPingData("MATCH_UNRANKED", 0, 1, "{\"strategy\":\"ranged-absolute\",\"alignment\":\"center\",\"ranges\":[1000]}", "{}", null, true, "{}", "all", tr);
        tr.Run();

        _wrapper.getLobbyService().createLobbyWithPingData("MATCH_UNRANKED", 0, null, true, "{}", "all", "{}", tr);
        tr.Run();

        _wrapper.getLobbyService().joinLobbyWithPingData("wrongLobbyId", true, "{}", "red", null, tr);
        tr.RunExpectFail(StatusCodes.BAD_REQUEST, ReasonCodes.LOBBY_NOT_FOUND);
    }
}
