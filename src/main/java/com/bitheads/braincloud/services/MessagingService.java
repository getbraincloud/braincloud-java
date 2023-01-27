package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by David St-Louis on 2018-07-17
 */
public class MessagingService {

    private enum Parameter {
        msgbox,
        markAsRead,
        msgIds,
        context,
        pageOffset,
        toProfileIds,
        contentJson,
        text,
        subject
    }

    private BrainCloudClient _client;

    public MessagingService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Deletes specified user messages on the server.
     *
     * Service Name - Messaging
     * Service Operation - DELETE_MESSAGES
     *
     * @param msgIds Arrays of message ids to delete.
     * @param callback The method to be invoked when the server response is received
     */
    public void deleteMessages(String msgbox, ArrayList<String> msgIds, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.msgbox.name(), msgbox);
            if (msgIds != null) {
                data.put(Parameter.msgIds.name(), new JSONArray(msgIds));
            }

            ServerCall sc = new ServerCall(ServiceName.messaging,
                    ServiceOperation.DELETE_MESSAGES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieve user's message boxes, including 'inbox', 'sent', etc.
     *
     * Service Name - Messaging
     * Service Operation - GET_MESSAGE_BOXES
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getMessageboxes(IServerCallback callback) {
        JSONObject data = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.messaging,
                ServiceOperation.GET_MESSAGE_BOXES, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Returns count of user's 'total' messages and their 'unread' messages.
     *
     * Service Name - Messaging
     * Service Operation - GET_MESSAGE_COUNTS
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getMessageCounts(IServerCallback callback) {
        JSONObject data = new JSONObject();

        ServerCall sc = new ServerCall(ServiceName.messaging,
                ServiceOperation.GET_MESSAGE_COUNTS, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Retrieves list of specified messages.
     *
     * Service Name - Messaging
     * Service Operation - GET_MESSAGES
     *
     * @param msgbox The messagebox that the messages reside in
     * @param msgIds Arrays of message ids to get.
     * @param markAsRead Whether the messages should be marked as read once retrieved.
     * @param callback The method to be invoked when the server response is received
     */
    public void getMessages(String msgbox, ArrayList<String> msgIds, Boolean markAsRead, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.msgbox.name(), msgbox);
            if (msgIds != null) {
                data.put(Parameter.msgIds.name(), new JSONArray(msgIds));
            }
            data.put(Parameter.markAsRead.name(), markAsRead);

            ServerCall sc = new ServerCall(ServiceName.messaging,
                    ServiceOperation.GET_MESSAGES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Retrieves a page of messages.
     *
     * Service Name - Messaging
     * Service Operation - GET_MESSAGES_PAGE
     *
     * @param context
     * @param callback The method to be invoked when the server response is received
     */
    public void getMessagesPage(String context, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            if (StringUtil.IsOptionalParameterValid(context)) {
                data.put(Parameter.context.name(), new JSONObject(context));
            }

            ServerCall sc = new ServerCall(ServiceName.messaging,
                    ServiceOperation.GET_MESSAGES_PAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets the page of messages from the server based on the encoded context and specified page offset.
     *
     * Service Name - Messaging
     * Service Operation - GET_MESSAGES_PAGE_OFFSET
     *
     * @param context
     * @param pageOffset
     * @param callback The method to be invoked when the server response is received
     */
    public void getMessagesPageOffset(String context, int pageOffset, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.context.name(), context);
            data.put(Parameter.pageOffset.name(), pageOffset);

            ServerCall sc = new ServerCall(ServiceName.messaging,
                    ServiceOperation.GET_MESSAGES_PAGE_OFFSET, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Marks list of user messages as read on the server.
     *
     * Service Name - Messaging
     * Service Operation - SEND_MESSAGE
     *
     * @param toProfileIds
     * @param contentJson
     * @param callback The method to be invoked when the server response is received
     */
    public void sendMessage(ArrayList<String> toProfileIds, String contentJson, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            if (toProfileIds != null) {
                data.put(Parameter.toProfileIds.name(), new JSONArray(toProfileIds));
            }
            data.put(Parameter.contentJson.name(), new JSONObject(contentJson));

            ServerCall sc = new ServerCall(ServiceName.messaging,
                    ServiceOperation.SEND_MESSAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Marks list of user messages as read on the server.
     *
     * Service Name - Messaging
     * Service Operation - SEND_MESSAGE_SIMPLE
     *
     * @param toProfileIds
     * @param messageText
     * @param callback The method to be invoked when the server response is received
     */
    public void sendMessageSimple(ArrayList<String> toProfileIds, String messageText, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            if (toProfileIds != null) {
                data.put(Parameter.toProfileIds.name(), new JSONArray(toProfileIds));
            }
            data.put(Parameter.text.name(), messageText);

            ServerCall sc = new ServerCall(ServiceName.messaging,
                    ServiceOperation.SEND_MESSAGE_SIMPLE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Marks list of user messages as read on the server.
     *
     * Service Name - Messaging
     * Service Operation - MARK_MESSAGES_READ
     *
     * @param msgbox
     * @param msgIds
     * @param callback The method to be invoked when the server response is received
     */
    public void markMessagesRead(String msgbox, ArrayList<String> msgIds, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.msgbox.name(), msgbox);
            if (msgIds != null) {
                data.put(Parameter.msgIds.name(), new JSONArray(msgIds));
            }

            ServerCall sc = new ServerCall(ServiceName.messaging,
                    ServiceOperation.MARK_MESSAGES_READ, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
