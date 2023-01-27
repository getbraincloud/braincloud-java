package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.Platform;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushNotificationService {

    private enum Parameter {
        deviceType,
        deviceToken,
        toPlayerId,
        profileId,
        message,
        notificationTemplateId,
        substitutions,
        groupId,
        alertContent,
        customData,
        profileIds,
        startDateUTC,
        minutesFromNow,
        fcmContent,
        iosContent,
        facebookContent
    }

    private BrainCloudClient _client;

    public PushNotificationService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Deregisters all device tokens currently registered to the player.
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void deregisterAllPushNotificationDeviceTokens(IServerCallback callback) {

        JSONObject data = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.DEREGISTER_ALL, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Deregisters the given device token from the server to disable this device
     * from receiving push notifications.
     *
     * @param platform The device platform being deregistered.
     * @param token The platform-dependant device token needed for push notifications.
     * @param callback The method to be invoked when the server response is received
     */
    public void deregisterPushNotificationDeviceToken(Platform platform, String token, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.deviceType.name(), platform.toString());
            data.put(Parameter.deviceToken.name(), token);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.DEREGISTER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Registers the given device token with the server to enable this device
     * to receive push notifications.
     *
     * @param platform The device platform
     * @param token The platform-dependant device token needed for push notifications.
     * @param callback The method to be invoked when the server response is received
     */
    public void registerPushNotificationToken(Platform platform, String token, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.deviceType.name(), platform.toString());
            data.put(Parameter.deviceToken.name(), token);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.REGISTER, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Sends a simple push notification based on the passed in message.
     * NOTE: It is possible to send a push notification to oneself.
     *
     * @param toProfileId The braincloud profileId of the user to receive the notification
     * @param message Text of the push notification
     * @param callback The method to be invoked when the server response is received
     */
    public void sendSimplePushNotification(String toProfileId, String message, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.toPlayerId.name(), toProfileId);
            data.put(Parameter.message.name(), message);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_SIMPLE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
        }
    }

    /**
     * Sends a notification to a user based on a brainCloud portal configured notification template.
     * NOTE: It is possible to send a push notification to oneself.
     *
     * @param toProfileId The braincloud profileId of the user to receive the notification
     * @param notificationTemplateId Id of the notification template
     * @param callback The method to be invoked when the server response is received
     */
    public void sendRichPushNotification(String toProfileId, int notificationTemplateId, IServerCallback callback) {
        sendRichPushNotificationWithParams(toProfileId, notificationTemplateId, null, callback);
    }

    /**
     * Sends a notification to a user based on a brainCloud portal configured notification template.
     * Includes JSON defining the substitution params to use with the template.
     * See the Portal documentation for more info.
     * NOTE: It is possible to send a push notification to oneself.
     *
     * @param toProfileId The braincloud profileId of the user to receive the notification
     * @param notificationTemplateId Id of the notification template
     * @param substitutionJson JSON defining the substitution params to use with the template
     * @param callback The method to be invoked when the server response is received
     */
    public void sendRichPushNotificationWithParams(String toProfileId, int notificationTemplateId, String substitutionJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.toPlayerId.name(), toProfileId);
            data.put(Parameter.notificationTemplateId.name(), notificationTemplateId);
            if (StringUtil.IsOptionalParameterValid(substitutionJson)) {
                JSONObject subJson = new JSONObject(substitutionJson);
                data.put(Parameter.substitutions.name(), subJson);
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_RICH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends a notification to a "group" of user based on a brainCloud portal configured notification template.
     * Includes JSON defining the substitution params to use with the template.
     * See the Portal documentation for more info.
     *
     * @param groupId Target group
     * @param notificationTemplateId Template to use
     * @param substitutionsJson Map of substitution positions to strings
     * @param callback The method to be invoked when the server response is received
     */
    public void sendTemplatedPushNotificationToGroup(String groupId, int notificationTemplateId, String substitutionsJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.notificationTemplateId.name(), notificationTemplateId);
            if (StringUtil.IsOptionalParameterValid(substitutionsJson)) {
                data.put(Parameter.substitutions.name(), new JSONObject(substitutionsJson));
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_TEMPLATED_TO_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends a notification to a "group" of user consisting of alert content and custom data.
     * See the Portal documentation for more info.
     *
     * @param groupId Target group
     * @param alertContentJson Body and title of alert
     * @param customDataJson Optional custom data
     * @param callback The method to be invoked when the server response is received
     */
    public void sendNormalizedPushNotificationToGroup(String groupId, String alertContentJson, String customDataJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);
            data.put(Parameter.alertContent.name(), new JSONObject(alertContentJson));
            if (StringUtil.IsOptionalParameterValid(customDataJson)) {
                data.put(Parameter.customData.name(), new JSONObject(customDataJson));
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_NORMALIZED_TO_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Schedules raw notifications based on user local time.
     *
     * @param profileId The profileId of the user to receive the notification
     * @param fcmContent Valid Fcm data content
     * @param iosContent Valid ios data content
     * @param facebookContent Facebook template string
     * @param startTimeUTC Start time of sending the push notification - in UTC milliseconds
     * @param callback The method to be invoked when the server response is received
     */
    public void scheduleRawPushNotificationUTC(String profileId, String fcmContent, String iosContent, String facebookContent, long startTimeUTC, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);

            if (StringUtil.IsOptionalParameterValid(fcmContent)) {
                data.put(Parameter.fcmContent.name(), new JSONObject(fcmContent));
            }

            if (StringUtil.IsOptionalParameterValid(iosContent )) {
                data.put(Parameter.iosContent .name(), new JSONObject(iosContent ));
            }

            if (StringUtil.IsOptionalParameterValid(facebookContent )) {
                data.put(Parameter.facebookContent .name(), new JSONObject(facebookContent ));
            }

            data.put(Parameter.startDateUTC.name(), startTimeUTC);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SCHEDULE_RAW_NOTIFICATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Schedules raw notifications based on user local time.
     *
     * @param profileId The profileId of the user to receive the notification
     * @param fcmContent Valid Fcm data content
     * @param iosContent Valid ios data content
     * @param facebookContent Facebook template string
     * @param minutesFromNow Minutes from now to send the push notification
     * @param callback The method to be invoked when the server response is received
     */
    public void scheduleRawPushNotificationMinutes(String profileId, String fcmContent, String iosContent, String facebookContent, int minutesFromNow, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);

            if (StringUtil.IsOptionalParameterValid(fcmContent)) {
                data.put(Parameter.fcmContent.name(), new JSONObject(fcmContent));
            }

            if (StringUtil.IsOptionalParameterValid(iosContent )) {
                data.put(Parameter.iosContent .name(), new JSONObject(iosContent ));
            }

            if (StringUtil.IsOptionalParameterValid(facebookContent )) {
                data.put(Parameter.facebookContent .name(), new JSONObject(facebookContent ));
            }

            data.put(Parameter.minutesFromNow.name(), minutesFromNow);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SCHEDULE_RAW_NOTIFICATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends a raw push notification to a target user.
     *
     * @param toProfileId The profileId of the user to receive the notification
     * @param fcmContent Valid Fcm data content
     * @param iosContent Valid ios data content
     * @param facebookContent Facebook template string
     * @param callback The method to be invoked when the server response is received
     */
    public void sendRawPushNotification(String toProfileId, String fcmContent, String iosContent, String facebookContent, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.toPlayerId.name(), toProfileId);

            if (StringUtil.IsOptionalParameterValid(fcmContent)) {
                data.put(Parameter.fcmContent.name(), new JSONObject(fcmContent));
            }

            if (StringUtil.IsOptionalParameterValid(iosContent )) {
                data.put(Parameter.iosContent .name(), new JSONObject(iosContent ));
            }

            if (StringUtil.IsOptionalParameterValid(facebookContent )) {
                data.put(Parameter.facebookContent .name(), new JSONObject(facebookContent ));
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_RAW, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends a raw push notification to a target list of users.
     *
     * @param profileIds Collection of profile IDs to send the notification to
     * @param fcmContent Valid Fcm data content
     * @param iosContent Valid ios data content
     * @param facebookContent Facebook template string
     * @param callback The method to be invoked when the server response is received
     */
    public void sendRawPushNotificationBatch(String[] profileIds, String fcmContent, String iosContent, String facebookContent, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileIds.name(), new JSONArray(profileIds));

            if (StringUtil.IsOptionalParameterValid(fcmContent)) {
                data.put(Parameter.fcmContent.name(), new JSONObject(fcmContent));
            }

            if (StringUtil.IsOptionalParameterValid(iosContent )) {
                data.put(Parameter.iosContent .name(), new JSONObject(iosContent ));
            }

            if (StringUtil.IsOptionalParameterValid(facebookContent )) {
                data.put(Parameter.facebookContent .name(), new JSONObject(facebookContent ));
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_RAW_BATCH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends a raw push notification to a target group.
     *
     * @param groupId Target group
     * @param fcmContent Valid Fcm data content
     * @param iosContent Valid ios data content
     * @param facebookContent Facebook template string
     * @param callback The method to be invoked when the server response is received
     */
    public void sendRawPushNotificationToGroup(String groupId, String fcmContent, String iosContent, String facebookContent, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.groupId.name(), groupId);

            if (StringUtil.IsOptionalParameterValid(fcmContent)) {
                data.put(Parameter.fcmContent.name(), new JSONObject(fcmContent));
            }

            if (StringUtil.IsOptionalParameterValid(iosContent )) {
                data.put(Parameter.iosContent .name(), new JSONObject(iosContent ));
            }

            if (StringUtil.IsOptionalParameterValid(facebookContent )) {
                data.put(Parameter.facebookContent .name(), new JSONObject(facebookContent ));
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_RAW_TO_GROUP, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Schedules a normalized push notification to a user
     *
     * @param profileId The profileId of the user to receive the notification
     * @param alertContentJson Body and title of alert
     * @param customDataJson Optional custom data
     * @param startTimeUTC Start time of sending the push notification - in UTC miliseconds
     * @param callback The method to be invoked when the server response is received
     */
    public void scheduleNormalizedPushNotificationUTC(String profileId, String alertContentJson, String customDataJson,
                                                      long startTimeUTC, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.alertContent.name(), new JSONObject(alertContentJson));
            if (StringUtil.IsOptionalParameterValid(customDataJson)) {
                data.put(Parameter.customData.name(), new JSONObject(customDataJson));
            }

            data.put(Parameter.startDateUTC.name(), startTimeUTC);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SCHEDULE_NORMALIZED_NOTIFICATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Schedules a normalized push notification to a user
     *
     * @param profileId The profileId of the user to receive the notification
     * @param alertContentJson Body and title of alert
     * @param customDataJson Optional custom data
     * @param minutesFromNow Minutes from now to send the push notification
     * @param callback The method to be invoked when the server response is received
     */
    public void scheduleNormalizedPushNotificationMinutes(String profileId, String alertContentJson, String customDataJson,
                                                          int minutesFromNow, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.alertContent.name(), new JSONObject(alertContentJson));
            if (StringUtil.IsOptionalParameterValid(customDataJson)) {
                data.put(Parameter.customData.name(), new JSONObject(customDataJson));
            }

            data.put(Parameter.minutesFromNow.name(), minutesFromNow);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SCHEDULE_NORMALIZED_NOTIFICATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Schedules a rich push notification to a user
     *
     * @param profileId The profileId of the user to receive the notification
     * @param notificationTemplateId Body and title of alert
     * @param substitutionsJson Map of substitution positions to strings
     * @param startTimeUTC Start time of sending the push notification - in UTC milliseconds
     * @param callback The method to be invoked when the server response is received
     */
    public void scheduleRichPushNotificationUTC(String profileId, int notificationTemplateId, String substitutionsJson,
                                                long startTimeUTC, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.notificationTemplateId.name(), notificationTemplateId);
            if (StringUtil.IsOptionalParameterValid(substitutionsJson)) {
                data.put(Parameter.substitutions.name(), new JSONObject(substitutionsJson));
            }

            data.put(Parameter.startDateUTC.name(), startTimeUTC);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SCHEDULE_RICH_NOTIFICATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Schedules a rich push notification to a user
     *
     * @param profileId The profileId of the user to receive the notification
     * @param notificationTemplateId Body and title of alert
     * @param substitutionsJson Map of substitution positions to strings
     * @param minutesFromNow Minutes from now to send the push notification
     * @param callback The method to be invoked when the server response is received
     */
    public void scheduleRichPushNotificationMinutes(String profileId, int notificationTemplateId, String substitutionsJson,
                                                    int minutesFromNow, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.notificationTemplateId.name(), notificationTemplateId);
            if (StringUtil.IsOptionalParameterValid(substitutionsJson)) {
                data.put(Parameter.substitutions.name(), new JSONObject(substitutionsJson));
            }

            data.put(Parameter.minutesFromNow.name(), minutesFromNow);

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SCHEDULE_RICH_NOTIFICATION, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends a notification to a user consisting of alert content and custom data.
     *
     * @param toProfileId The profileId of the user to receive the notification
     * @param alertContentJson Body and title of alert
     * @param customDataJson Optional custom data
     * @param callback The method to be invoked when the server response is received
     */
    public void sendNormalizedPushNotification(String toProfileId, String alertContentJson, String customDataJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.toPlayerId.name(), toProfileId);
            data.put(Parameter.alertContent.name(), new JSONObject(alertContentJson));
            if (StringUtil.IsOptionalParameterValid(customDataJson)) {
                data.put(Parameter.customData.name(), new JSONObject(customDataJson));
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_NORMALIZED, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Sends a notification to multiple users consisting of alert content and custom data.
     *
     * @param profileIds Collection of profile IDs to send the notification to
     * @param alertContentJson Body and title of alert
     * @param customDataJson Optional custom data
     * @param callback The method to be invoked when the server response is received
     */
    public void sendNormalizedPushNotificationBatch(String[] profileIds, String alertContentJson, String customDataJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileIds.name(), new JSONArray(profileIds));
            data.put(Parameter.alertContent.name(), new JSONObject(alertContentJson));
            if (StringUtil.IsOptionalParameterValid(customDataJson)) {
                data.put(Parameter.customData.name(), new JSONObject(customDataJson));
            }

            ServerCall sc = new ServerCall(ServiceName.pushNotification, ServiceOperation.SEND_NORMALIZED_BATCH, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
