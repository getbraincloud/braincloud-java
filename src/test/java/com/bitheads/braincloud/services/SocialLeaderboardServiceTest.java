package com.bitheads.braincloud.services;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.junit.Test;

import com.bitheads.braincloud.client.ReasonCodes;
import com.bitheads.braincloud.client.StatusCodes;

public class SocialLeaderboardServiceTest extends TestFixtureBase {
        private final String _globalLeaderboardId = "testLeaderboard";
        private final String _socialLeaderboardId = "testSocialLeaderboard";
        private final String _dynamicLeaderboardId = "testDynamicLeaderboard";
        private final String _groupLeaderboardId = "groupLeaderboardConfig";

        @Test
        public void testGetSocialLeaderboard() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getSocialLeaderboard(
                                _socialLeaderboardId,
                                true,
                                tr);

                tr.Run();
        }

        @Test
        public void testGetSocialLeaderboardIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getSocialLeaderboardIfExists(_socialLeaderboardId, false, tr);
                tr.Run();

                _wrapper.getLeaderboardService().getSocialLeaderboardIfExists(
                                "nonExistentLeaderboard",
                                true,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetSocialLeaderboardByVersion() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getSocialLeaderboardByVersion(
                                _socialLeaderboardId,
                                true,
                                0,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetSocialLeaderboardByVersionIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getSocialLeaderboardByVersionIfExists(
                                _socialLeaderboardId,
                                true,
                                0,
                                tr);
                tr.Run();

                _wrapper.getLeaderboardService().getSocialLeaderboardByVersionIfExists(
                                "nonExistentLeaderboard",
                                true,
                                0,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetMultiSocialLeaderboard() throws Exception {
                postScoreToDynamicLeaderboard();
                postScoreToNonDynamicLeaderboard();

                TestResult tr = new TestResult(_wrapper);

                String[] lbIds = new String[] {
                                _globalLeaderboardId,
                                _dynamicLeaderboardId + "_" + SocialLeaderboardService.SocialLeaderboardType.LAST_VALUE
                };
                _wrapper.getLeaderboardService().getMultiSocialLeaderboard(
                                lbIds,
                                10,
                                true,
                                tr);

                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardPageHigh() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardPage(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                0,
                                10,
                                tr);

                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardPageLow() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardPage(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.LOW_TO_HIGH,
                                0,
                                10,
                                tr);

                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardPageFail() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardPage(
                                "thisDoesNotExistLeaderboard",
                                SocialLeaderboardService.SortOrder.LOW_TO_HIGH,
                                0,
                                10,
                                tr);

                tr.RunExpectFail(StatusCodes.INTERNAL_SERVER_ERROR, 40499);
        }

        @Test
        public void testGetGlobalLeaderboardPageIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardPageIfExists(_globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW, 0, 10, tr);
                tr.Run();

                _wrapper.getLeaderboardService().getGlobalLeaderboardPageIfExists("nonExistentLeaderboard",
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW, 0, 10, tr);
                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardPageByVersion() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardPageByVersion(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                0,
                                10,
                                1,
                                tr);

                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardPageByVersionIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardPageByVersionIfExists(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                0,
                                10,
                                1,
                                tr);
                tr.Run();

                _wrapper.getLeaderboardService().getGlobalLeaderboardPageByVersionIfExists(
                                "nonExistentLeaderboard",
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                0,
                                10,
                                1,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardView() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardView(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardViewIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardViewIfExists(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                tr);
                tr.Run();

                _wrapper.getLeaderboardService().getGlobalLeaderboardViewIfExists(
                                "nonExistentLeaderboard",
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardViewByVersion() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardViewByVersion(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                1,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardViewByVersionIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardViewByVersionIfExists(
                                _globalLeaderboardId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                1,
                                tr);
                tr.Run();

                _wrapper.getLeaderboardService().getGlobalLeaderboardViewByVersionIfExists(
                                "nonExistentLeaderboard",
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                1,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGlobalLeaderboardVersions() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardVersions(
                                _globalLeaderboardId,
                                tr);

                tr.Run();
        }

        @Test
        public void testPostScoreToLeaderboard() throws Exception {
                postScoreToNonDynamicLeaderboard();
        }

        @Test
        public void testPostScoreToDynamicLeaderboard() throws Exception {
                postScoreToDynamicLeaderboard();
        }

        @Test
        public void testPostScoreToDynamicLeaderboardUsingConfig() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                String scoreData = "{\"nickname\": \"Tarnished\"}";
                JSONObject configJsonObject = new JSONObject();
                configJsonObject.put("leaderboardType", "HIGH_VALUE");
                configJsonObject.put("rotationType", "DAYS");
                configJsonObject.put("numDaysToRotate", 4);
                long date = TimeUtil.UTCDateTimeToUTCMillis(addDays(new Date(), 3));
                configJsonObject.put("resetAt", date);
                configJsonObject.put("retainedCount", 2);

                String configJson = configJsonObject.toString();

                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboardUsingConfig(_dynamicLeaderboardId, 777, scoreData, configJson, tr);

                tr.Run();
        }

        @Test
        public void testPostScoreToDynamicLeaderboardUTC() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                long date = TimeUtil.UTCDateTimeToUTCMillis(addDays(new Date(), 3));
                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboardUTC(
                                _dynamicLeaderboardId,
                                100,
                                Helpers.createJsonPair("testDataKey", 400),
                                SocialLeaderboardService.SocialLeaderboardType.LAST_VALUE.toString(),
                                SocialLeaderboardService.RotationType.NEVER.toString(),
                                date,
                                5,
                                tr);

                tr.Run();
        }

        @Test
        @SuppressWarnings("deprecation")
        public void testPostScoreToDynamicLeaderboardDays() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboardDays(
                                _dynamicLeaderboardId,
                                100,
                                Helpers.createJsonPair("testDataKey", 400),
                                SocialLeaderboardService.SocialLeaderboardType.LOW_VALUE.toString(),
                                null,
                                5,
                                3,
                                tr);

                tr.Run();
        }

        @Test
        public void testPostScoreToDynamicLeaderboardDaysUTC() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboardDaysUTC(
                                _dynamicLeaderboardId,
                                100,
                                Helpers.createJsonPair("testDataKey", 400),
                                SocialLeaderboardService.SocialLeaderboardType.LOW_VALUE.toString(),
                                100,
                                5,
                                3,
                                tr);

                tr.Run();
        }

        @Test
        @SuppressWarnings("deprecation")
        public void testPostScoreToDynamicLeaderboardLowValue() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboard(
                                _dynamicLeaderboardId,
                                100,
                                Helpers.createJsonPair("testDataKey", 400),
                                SocialLeaderboardService.SocialLeaderboardType.LOW_VALUE.toString(),
                                SocialLeaderboardService.RotationType.NEVER.toString(),
                                null,
                                5,
                                tr);

                tr.Run();
        }

        @Test
        @SuppressWarnings("deprecation")
        public void testPostScoreToDynamicLeaderboardCumulative() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboard(
                                _dynamicLeaderboardId,
                                100,
                                Helpers.createJsonPair("testDataKey", 400),
                                SocialLeaderboardService.SocialLeaderboardType.CUMULATIVE.toString(),
                                SocialLeaderboardService.RotationType.WEEKLY.toString(),
                                addDays(new Date(), 3),
                                5,
                                tr);

                tr.Run();
        }

        @Test
        @SuppressWarnings("deprecation")
        public void testPostScoreToDynamicLeaderboardLastValue() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboard(
                                _dynamicLeaderboardId,
                                100,
                                Helpers.createJsonPair("testDataKey", 400),
                                SocialLeaderboardService.SocialLeaderboardType.LAST_VALUE.toString(),
                                SocialLeaderboardService.RotationType.DAILY.toString(),
                                addDays(new Date(), 2),
                                3,
                                tr);

                tr.Run();
        }

        @Test
        public void testPostScoreToDynamicLeaderboardNullRotationTime() throws Exception {
                postScoreToDynamicLeaderboard();
        }

        @Test
        public void testGetGroupSocialLeaderboard() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");

                _wrapper.getLeaderboardService().getGroupSocialLeaderboard(
                                _socialLeaderboardId,
                                groupId,
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGroupSocialLeaderboardByVersion() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGroupSocialLeaderboardByVersion(
                                _socialLeaderboardId,
                                "_invalid_",
                                0,
                                tr);
                tr.RunExpectFail(400, ReasonCodes.MISSING_RECORD);
        }

        @Test
        public void testGetPlayersSocialLeaderboard() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getPlayersSocialLeaderboard(
                                _socialLeaderboardId,
                                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                                tr);
                tr.Run();
        }

        @Test
        public void testGetPlayersSocialLeaderboardIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getPlayersSocialLeaderboardIfExists(
                                _socialLeaderboardId,
                                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                                tr);
                tr.Run();

                _wrapper.getLeaderboardService().getPlayersSocialLeaderboardIfExists(
                                "nonExistentLeaderboard",
                                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                                tr);
                tr.Run();
        }

        @Test
        public void testGetPlayersSocialLeaderboardByVersion() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getPlayersSocialLeaderboardByVersion(
                                _socialLeaderboardId,
                                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                                0,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetPlayersSocialLeaderboardByVersionIfExists() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getPlayersSocialLeaderboardByVersionIfExists(
                                _socialLeaderboardId,
                                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                                0,
                                tr);
                tr.Run();

                _wrapper.getLeaderboardService().getPlayersSocialLeaderboardByVersionIfExists(
                                "nonExistentLeaderboard",
                                new String[] { getUser(Users.UserA).profileId, getUser(Users.UserB).profileId },
                                0,
                                tr);
                tr.Run();
        }

        @Test
        public void testListAllLeaderboards() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().listAllLeaderboards(tr);

                tr.Run();
        }

        @Test
        public void getGlobalLeaderboardEntryCount() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardEntryCount(_globalLeaderboardId, tr);

                tr.Run();
        }

        @Test
        public void getGlobalLeaderboardEntryCountByVersion() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().getGlobalLeaderboardEntryCountByVersion(_globalLeaderboardId, 1, tr);

                tr.Run();
        }

        @Test
        public void testGetPlayerScore() throws Exception {
                postScoreToNonDynamicLeaderboard();

                TestResult tr = new TestResult(_wrapper);
                _wrapper.getLeaderboardService().getPlayerScore(
                                _globalLeaderboardId,
                                -1,
                                tr);

                tr.Run();
        }

        @Test
        public void testRemovePlayerScore() throws Exception {
                postScoreToNonDynamicLeaderboard();

                TestResult tr = new TestResult(_wrapper);
                _wrapper.getLeaderboardService().removePlayerScore(
                                _globalLeaderboardId,
                                -1,
                                tr);

                tr.Run();
        }

        @Test
        public void testGetPlayerScores() throws Exception {
                postScoreToNonDynamicLeaderboard();

                TestResult tr = new TestResult(_wrapper);
                _wrapper.getLeaderboardService().getPlayerScores(
                                _globalLeaderboardId,
                                -1,
                                2,
                                tr);

                tr.Run();
        }

        @Test
        public void testGetPlayerScoresFromLeaderboards() throws Exception {
                postScoreToDynamicLeaderboard();
                postScoreToNonDynamicLeaderboard();

                TestResult tr = new TestResult(_wrapper);

                String[] lbIds = new String[] {
                                _globalLeaderboardId,
                                _dynamicLeaderboardId + "_" + SocialLeaderboardService.SocialLeaderboardType.LAST_VALUE
                };
                _wrapper.getLeaderboardService().getPlayerScoresFromLeaderboards(
                                lbIds,
                                tr);

                tr.Run();
        }

        @Test
        public void testPostScoreToGroupLeaderboard() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");

                _wrapper.getLeaderboardService().postScoreToGroupLeaderboard(
                                _groupLeaderboardId,
                                groupId,
                                0,
                                Helpers.createJsonPair("test", "stuff"),
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        @SuppressWarnings("deprecation")
        public void testPostScoreToDynamicGroupLeaderboard() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                Date date = new Date();
                date.setTime(date.getTime() + 120 * 1000);

                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");
                _wrapper.getLeaderboardService().postScoreToDynamicGroupLeaderboard(
                                _groupLeaderboardId,
                                groupId,
                                0,
                                Helpers.createJsonPair("test", "stuff"),
                                "HIGH_VALUE",
                                "WEEKLY",
                                date,
                                2,
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        public void testPostScoreToDynamicGroupLeaderboardUTC() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");
                _wrapper.getLeaderboardService().postScoreToDynamicGroupLeaderboardUTC(
                                _groupLeaderboardId,
                                groupId,
                                0,
                                Helpers.createJsonPair("test", "stuff"),
                                "HIGH_VALUE",
                                "WEEKLY",
                                157082811,
                                2,
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        public void testPostScoreToDynamicGroupLeaderboardDaysUTC() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                long date = TimeUtil.UTCDateTimeToUTCMillis(new Date());
                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");
                _wrapper.getLeaderboardService().postScoreToDynamicGroupLeaderboardDaysUTC(
                                _groupLeaderboardId,
                                groupId,
                                0,
                                Helpers.createJsonPair("test", "stuff"),
                                "HIGH_VALUE",
                                date,
                                2,
                                5,
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        public void testPostScoreToDynamicGroupLeaderboardUsingConfig() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                String leaderboardId = _groupLeaderboardId;
                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");
                long score = 99;
                JSONObject scoreDataObject = new JSONObject();
                scoreDataObject.put("nickname", "Tarnished");
                String scoreData = scoreDataObject.toString();
                JSONObject configJsonObject = new JSONObject();
                configJsonObject.put("leaderboardType", "HIGH_VALUE");
                configJsonObject.put("rotationType", "DAYS");
                configJsonObject.put("numDaysToRotate", 4);
                long date = TimeUtil.UTCDateTimeToUTCMillis(addDays(new Date(), 3));
                configJsonObject.put("resetAt", date);
                configJsonObject.put("retainedCount", 2);
                String configJson = configJsonObject.toString();

                System.out.println("\n\nActual API Test...");

                _wrapper.getLeaderboardService().postScoreToDynamicGroupLeaderboardUsingConfig(leaderboardId, groupId, score,
                                scoreData, configJson, tr);
                tr.Run();

                System.out.println("\n\nDeleting Group...");

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        public void testRemoveGroupScore() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");

                _wrapper.getLeaderboardService().postScoreToGroupLeaderboard(
                                _groupLeaderboardId,
                                groupId,
                                0,
                                Helpers.createJsonPair("test", "stuff"),
                                tr);
                tr.Run();

                _wrapper.getLeaderboardService().removeGroupScore(
                                _groupLeaderboardId,
                                groupId,
                                1,
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGroupLeaderboardView() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");

                _wrapper.getLeaderboardService().getGroupLeaderboardView(
                                _groupLeaderboardId,
                                groupId,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @Test
        public void testGetGroupLeaderboardViewByVersion() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getGroupService().createGroup(
                                "testGroup",
                                "test",
                                false,
                                new GroupACL(GroupACL.Access.ReadWrite, GroupACL.Access.ReadWrite),
                                Helpers.createJsonPair("testInc", 123),
                                Helpers.createJsonPair("test", "test"),
                                Helpers.createJsonPair("test", "test"),
                                tr);

                tr.Run();

                JSONObject data = tr.m_response.getJSONObject("data");
                String groupId = data.getString("groupId");

                _wrapper.getLeaderboardService().getGroupLeaderboardViewByVersion(
                                _groupLeaderboardId,
                                groupId,
                                1,
                                SocialLeaderboardService.SortOrder.HIGH_TO_LOW,
                                5,
                                5,
                                tr);
                tr.Run();

                _wrapper.getGroupService().deleteGroup(
                                groupId,
                                -1,
                                tr);
                tr.Run();
        }

        @SuppressWarnings("deprecation")
        public void postScoreToDynamicLeaderboard() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().postScoreToDynamicLeaderboard(
                                _dynamicLeaderboardId + "_" + (int) (Math.random() * 10000000),
                                100,
                                Helpers.createJsonPair("testDataKey", 400),
                                SocialLeaderboardService.SocialLeaderboardType.LAST_VALUE.toString(),
                                SocialLeaderboardService.RotationType.NEVER.toString(),
                                addDays(new Date(), 3),
                                5,
                                tr);

                tr.Run();
        }

        public void postScoreToNonDynamicLeaderboard() throws Exception {
                TestResult tr = new TestResult(_wrapper);

                _wrapper.getLeaderboardService().postScoreToLeaderboard(
                                _globalLeaderboardId,
                                1000,
                                Helpers.createJsonPair("testDataKey", 400),
                                tr);

                tr.Run();
        }

        public static Date addDays(Date date, int days) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, days); // minus number would decrement the days
                return cal.getTime();
        }
}