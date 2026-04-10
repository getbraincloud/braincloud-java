package com.bitheads.braincloud.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Runs first (alphabetically before all other *Test classes) to verify that all
 * required portal configurations exist. A failure here means the environment is
 * missing portal setup — fix those before investigating other test failures.
 */
public class AAAPortalPreflightChecksTest extends TestFixtureBase {

    @Test
    public void portalPreflightCheck() throws Exception {
        List<String> missing = new ArrayList<>();

        // -----------------------------------------------------------------------
        // Leaderboards
        // -----------------------------------------------------------------------
        for (String lbId : new String[]{ "testLeaderboard", "testSocialLeaderboard", "testTournamentLeaderboard", "groupLeaderboardConfig" }) {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getLeaderboardService().getGlobalLeaderboardEntryCount(lbId, tr);
            if (!tr.Run(true)) missing.add("leaderboard: " + lbId);
        }

        // -----------------------------------------------------------------------
        // Item catalog
        // -----------------------------------------------------------------------
        for (String itemId : new String[]{ "sword001", "equipmentBundle" }) {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getItemCatalogService().getCatalogItemDefinition(itemId, tr);
            if (!tr.Run(true)) missing.add("catalog item: " + itemId);
        }

        // -----------------------------------------------------------------------
        // Global properties
        // -----------------------------------------------------------------------
        {
            TestResult tr = new TestResult(_wrapper);
            ArrayList<String> propertyNames = new ArrayList<>(Arrays.asList("prop1", "prop2", "prop3"));
            _wrapper.getGlobalAppService().readSelectedProperties(propertyNames, tr);
            if (tr.Run(true)) {
                JSONObject response = tr.m_response.optJSONObject("data");
                for (String name : new String[]{ "prop1", "prop2", "prop3" }) {
                    if (response == null || !response.has(name))
                        missing.add("global property: " + name);
                }
            } else {
                missing.add("global properties: prop1, prop2, prop3");
            }
        }

        // -----------------------------------------------------------------------
        // Achievements
        // -----------------------------------------------------------------------
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getGamificationService().readAchievements(false, tr);
            if (tr.Run(true)) {
                JSONArray achs = tr.m_response.getJSONObject("data").optJSONArray("achievements");
                boolean found01 = false, found02 = false;
                if (achs != null) {
                    for (int i = 0; i < achs.length(); i++) {
                        String id = achs.getJSONObject(i).optString("id", "");
                        if ("testAchievement01".equals(id)) found01 = true;
                        if ("testAchievement02".equals(id)) found02 = true;
                    }
                }
                if (!found01) missing.add("achievement: testAchievement01");
                if (!found02) missing.add("achievement: testAchievement02");
            } else {
                missing.add("achievement: testAchievement01");
                missing.add("achievement: testAchievement02");
            }
        }

        // -----------------------------------------------------------------------
        // Milestone and quest category: Experience
        // -----------------------------------------------------------------------
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getGamificationService().readMilestonesByCategory("Experience", false, tr);
            if (tr.Run(true)) {
                JSONArray milestones = tr.m_response.getJSONObject("data").optJSONArray("milestones");
                if (milestones == null || milestones.length() == 0)
                    missing.add("milestone category: Experience (no milestones defined)");
            } else {
                missing.add("milestone category: Experience");
            }
        }
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getGamificationService().readQuestsByCategory("Experience", false, tr);
            if (tr.Run(true)) {
                JSONArray quests = tr.m_response.getJSONObject("data").optJSONArray("quests");
                if (quests == null || quests.length() == 0)
                    missing.add("quest category: Experience (no quests defined)");
            } else {
                missing.add("quest category: Experience");
            }
        }

        // -----------------------------------------------------------------------
        // Virtual currency type: credits
        // -----------------------------------------------------------------------
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getVirtualCurrencyService().getCurrency(null, tr);
            if (tr.Run(true)) {
                JSONObject currency = tr.m_response.getJSONObject("data").optJSONObject("currencyMap");
                if (currency == null || !currency.has("credits"))
                    missing.add("virtual currency type: credits");
            } else {
                missing.add("virtual currency type: credits");
            }
        }

        // -----------------------------------------------------------------------
        // Custom entity type: athletes
        // -----------------------------------------------------------------------
        {
            TestResult tr = new TestResult(_wrapper);
            _wrapper.getCustomEntityService().getEntityPage(
                    "athletes",
                    "{\"pagination\":{\"rowsPerPage\":1,\"pageNumber\":1},\"searchCriteria\":{}}",
                    tr);
            if (!tr.Run(true)) missing.add("custom entity type: athletes");
        }

        // -----------------------------------------------------------------------
        // Lobby type: MATCH_UNRANKED
        // -----------------------------------------------------------------------
        {
            TestResult tr = new TestResult(_wrapper);
            String[] lobbyTypes = new String[]{"MATCH_UNRANKED"};
            _wrapper.getLobbyService().getRegionsForLobbies(lobbyTypes, tr);
            if (!tr.Run(true)) missing.add("lobby type: MATCH_UNRANKED");
        }

        // -----------------------------------------------------------------------
        // Report
        // -----------------------------------------------------------------------
        if (!missing.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nPORTAL PREFLIGHT CHECK FAILED - the following items are not configured on the portal:\n");
            for (String item : missing)
                sb.append("  - ").append(item).append("\n");
            sb.append("\nSet these up in the portal before running the full test suite.\n");
            Assert.fail(sb.toString());
        }
    }
}
