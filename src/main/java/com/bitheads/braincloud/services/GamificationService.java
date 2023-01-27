package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GamificationService {

    private enum Parameter {
        includeMetaData,
        category,
        achievements,
        data,
        milestones
    }

    private BrainCloudClient _client;

    public GamificationService(BrainCloudClient client) {
        _client = client;
    }

    public IAchievementsDelegate m_achievementsDelegate;

    /**
     * Sets the achievement awarded delegate which is called anytime
     * an achievement is awarded
     */
    public void setAchievementAwardedDelegate(IAchievementsDelegate delegate) {
        m_achievementsDelegate = delegate;
    }

    /**
     * Method retrieves all gamification data for the player.
     *
     * Service Name - Gamification
     * Service Operation - Read
     */
    public void readAllGamification(
            boolean includeMetaData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Method retrieves all milestones defined for the game.
     *
     * Service Name - Gamification
     * Service Operation - ReadMilestones
     */
    public void readMilestones(
            boolean includeMetaData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_MILESTONES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Read all of the achievements defined for the game.
     *
     * Service Name - Gamification
     * Service Operation - ReadAchievements
     */
    public void readAchievements(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_ACHIEVEMENTS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Method returns all defined xp levels and any rewards associated
     * with those xp levels.
     *
     * Service Name - Gamification
     * Service Operation - ReadXpLevels
     *
     * @param callback Callback.
     */
    public void readXpLevels(
            IServerCallback callback) {

        ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_XP_LEVELS, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Method retrives the list of achieved achievements.
     *
     * Service Name - Gamification
     * Service Operation - ReadAchievedAchievements
     *
     * @param callback Callback.
     */
    public void readAchievedAchievements(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_ACHIEVED_ACHIEVEMENTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }


    /**
     * Method retrieves the list of completed milestones.
     *
     * Service Name - Gamification
     * Service Operation - ReadCompleteMilestones
     *
     * @param callback Callback.
     */
    public void readCompletedMilestones(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_COMPLETED_MILESTONES, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }

    /**
     * Method retrieves the list of in progress milestones
     *
     * Service Name - Gamification
     * Service Operation - ReadInProgressMilestones
     *
     * @param callback Callback.
     */
    public void readInProgressMilestones(
            boolean includeMetaData,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_IN_PROGRESS_MILESTONES, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }

    /**
     * Method retrieves milestones of the given category.
     *
     * Service Name - Gamification
     * Service Operation - ReadMilestonesByCategory
     *
     * @param category The milestone category
     * @param callback Callback.
     */
    public void readMilestonesByCategory(
            String category,
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.category.name(), category);
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_MILESTONES_BY_CATEGORY, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Method will award the achievements specified. On success, this will
     * call AwardThirdPartyAchievement to hook into the client-side Achievement
     * service (ie GameCentre, Facebook etc).
     *
     * Service Name - Gamification
     * Service Operation - AwardAchievements
     *
     * @param achievementIds Array of achievement ids to award
     * @param callback Callback.
     */
    public void awardAchievements(String[] achievementIds, IServerCallback callback) {
        try {
            JSONArray achievements = new JSONArray();
            for (String achId : achievementIds) {
                achievements.put(achId);
            }

            JSONObject data = new JSONObject();
            data.put(Parameter.achievements.name(), achievements);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.AWARD_ACHIEVEMENTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     */
    private void achievementAwardedSuccessCallback(String data) {
        //To be implemented for Android
    }

    // goes through JSON response to award achievements via third party (ie game centre, facebook etc).
    // notifies achievement delegate
    public void checkForAchievementsToAward(ServiceName serviceName, ServiceOperation serviceOperation, String data) {
        try {
            JSONObject incomingData = new JSONObject(data);

            if (!incomingData.isNull(Parameter.data.name())) {

                JSONArray josnData = incomingData.optJSONArray(Parameter.data.name());
                if (data != null) {
                    // TODO
                }

                if (m_achievementsDelegate != null) {
                    m_achievementsDelegate.serverCallback(serviceName, serviceOperation, data.toString());
                }
            }

        } catch (JSONException je) {
        }
    }

    private void awardThirdPartyAchievements(String achievements) {
        //TODO Platform specific
    }

    /**
     * Method retrieves all of the quests defined for the game.
     *
     * Service Name - Gamification
     * Service Operation - ReadQuests
     *
     * @param callback Callback.
     *
     *
     *  {
     *   "status": 200,
     *   "data": {
     *     "quests": []
     *   }
     * }
     */
    public void readQuests(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_QUESTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }


    /**
     *
     * Service Name - Gamification
     * Service Operation - ReadCompletedQuests
     *
     * @param callback Callback.
     */
    public void readQuestsCompleted(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_COMPLETED_QUESTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }

    /**
     *
     * Service Name - Gamification
     * Service Operation - ReadInProgressQuests
     *
     * @param callback Callback.
     */
    public void readQuestsInProgress(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_IN_PROGRESS_QUESTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }

    /**
     *
     * Service Name - Gamification
     * Service Operation - ReadNotStartedQuests
     *
     * @param callback Callback.
     */
    public void readQuestsNotStarted(
            boolean includeMetaData,
            IServerCallback callback) {
        try {


            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_NOT_STARTED_QUESTS, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }

    /**
     *
     * Service Name - Gamification
     * Service Operation - ReadQuestsWithStatus
     *
     * @param callback Callback.
     */
    public void readQuestsWithStatus(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_QUESTS_WITH_STATUS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     *
     * Service Name - Gamification
     * Service Operation - ReadQuestsWithBasicPercentage
     *
     * @param callback Callback.
     */
    public void readQuestsWithBasicPercentage(
            boolean includeMetaData,
            IServerCallback callback) {
        try {


            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_QUESTS_WITH_BASIC_PERCENTAGE, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }

    /**
     *
     * Service Name - Gamification
     * Service Operation - ReadQuestsWithComplexPercentage
     *
     * @param callback Callback
     */
    public void readQuestsWithComplexPercentage(
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_QUESTS_WITH_COMPLEX_PERCENTAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Method
     *
     * Service Name - Gamification
     * Service Operation - ReadQuestsByCategory
     *
     *
     * @param category The quest category
     * @param callback Callback.
     */
    public void readQuestsByCategory(
            String category,
            boolean includeMetaData,
            IServerCallback callback) {
        try {

            JSONObject data = new JSONObject();
            data.put(Parameter.category.name(), category);
            data.put(Parameter.includeMetaData.name(), includeMetaData);

            ServerCall sc = new ServerCall(ServiceName.gamification, ServiceOperation.READ_QUESTS_BY_CATEGORY, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException je) {
        }
    }
}
