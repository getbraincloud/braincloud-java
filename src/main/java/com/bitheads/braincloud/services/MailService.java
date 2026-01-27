// Copyright 2026 bitHeads, Inc. All Rights Reserved.
package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class MailService {

    public enum Parameter {
        profileId,
        emailAddress,
        emailAddresses,
        subject,
        body,
        serviceParams
    }

    private BrainCloudClient _client;

    public MailService(BrainCloudClient client) {
        _client = client;
    }

    /**
		 * Sends a simple text email to the specified player
		 *
		 * Service Name - mail
		 * Service Operation - SEND_BASIC_EMAIL
		 *
		 * @param in_profileId The user to send the email to
		 * @param in_subject The email subject
		 * @param in_body The email body
		 * @param in_callback The method to be invoked when the server response is received
		 */
    public void sendBasicEmail(String profileId, String subject, String body, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);
            data.put(Parameter.subject.name(), subject);
            data.put(Parameter.body.name(), body);

            ServerCall sc = new ServerCall(ServiceName.mail, ServiceOperation.SEND_BASIC_EMAIL, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Sends an advanced email to the specified player
		 *
		 * Service Name - mail
		 * Service Operation - SEND_ADVANCED_EMAIL
		 *
		 * @param in_profileId The user to send the email to
		 * @param in_jsonServiceParams Parameters to send to the email service. See the documentation for
		 *	a full list. http://getbraincloud.com/apidocs/apiref/#capi-mail
		 * @param in_callback The method to be invoked when the server response is received
		 */
    public void sendAdvancedEmail(String profileId, String jsonServiceParams, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.profileId.name(), profileId);

            JSONObject jsonData = new JSONObject(jsonServiceParams);
            data.put(Parameter.serviceParams.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.mail, ServiceOperation.SEND_ADVANCED_EMAIL, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Sends an advanced email to the specified email address
		 *
		 * Service Name - mail
		 * Service Operation - SEND_ADVANCED_EMAIL_BY_ADDRESS
		 *
		 * @param in_emailAddress The address to send the email to
		 * @param in_jsonServiceParams Parameters to send to the email service. See the documentation for
		 *	a full list. http://getbraincloud.com/apidocs/apiref/#capi-mail
		 * @param in_callback The method to be invoked when the server response is received
		 */
    public void sendAdvancedEmailByAddress(String emailAddress, String jsonServiceParams, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.emailAddress.name(), emailAddress);

            JSONObject jsonData = new JSONObject(jsonServiceParams);
            data.put(Parameter.serviceParams.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.mail, ServiceOperation.SEND_ADVANCED_EMAIL_BY_ADDRESS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
		 * Sends an advanced email to the specified email addresses.
		 *
		 * Service Name - Mail
		 * Service Operation - SEND_ADVANCED_EMAIL_BY_ADDRESSES
		 *
		 * @param in_emailAddress The list of addresses to send the email to
		 * @param in_serviceParams Set of parameters dependant on the mail service configured
		 * @param in_callback The method to be invoked when the server response is received
		 */
    public void sendAdvancedEmailByAddresses(String[] emailAddresses, String serviceParams, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONObject jsonServiceParams = new JSONObject(serviceParams);

            data.put(Parameter.emailAddresses.name(), emailAddresses);
            data.put(Parameter.serviceParams.name(), jsonServiceParams);

            ServerCall sc = new ServerCall(ServiceName.mail, ServiceOperation.SEND_ADVANCED_EMAIL_BY_ADDRESSES, data,
                    callback);

            _client.sendRequest(sc);

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
