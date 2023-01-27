package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bradleyh on 5/6/2016.
 */
public class ChatService {

    private enum Parameter {
        channelId,
        maxReturn,
        msgId,
        version,
        channelType,
        channelSubId,
        content,
        recordInHistory,
        plain,
        text,
        rich
    }

    private BrainCloudClient _client;

    public ChatService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Registers a listener for incoming events from <channelId>.
     * Also returns a list of <maxReturn> recent messages from history.
     *
     * Service Name - Chat
     * Service Operation - ChannelConnect
     *
     * @param channelId The id of the chat channel to return history from.
     * @param maxReturn Maximum number of messages to return.
     * @param callback The method to be invoked when the server response is received
     */
    public void channelConnect(String channelId, int maxReturn, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.CHANNEL_CONNECT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Unregisters a listener for incoming events from <channelId>.
     *
     * Service Name - Chat
     * Service Operation - channelDisconnect
     *
     * @param channelId The id of the chat channel to unsubscribed from.
     * @param callback The method to be invoked when the server response is received
     */
    public void channelDisconnect(String channelId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.CHANNEL_DISCONNECT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Delete a chat message. <version> must match the latest or pass -1 to bypass version check.
     *
     * Service Name - Chat
     * Service Operation - deleteChatMessage
     *
     * @param channelId The id of the chat channel that contains the message to delete.
     * @param msgId The message id to delete.
     * @param version Version of the message to delete. Must match latest or pass -1 to bypass version check.
     * @param callback The method to be invoked when the server response is received
     */
    public void deleteChatMessage(String channelId, String msgId, int version, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);
            data.put(Parameter.msgId.name(), msgId);
            data.put(Parameter.version.name(), version);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.DELETE_CHAT_MESSAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets the channelId for the given <channelType> and <channelSubId>. Channel type must be one of "gl" or "gr".
     *
     * Service Name - Chat
     * Service Operation - getChannelId
     *
     * @param channelType Channel type must be one of "gl" or "gr". For (global) or (group) respectively.
     * @param channelSubId The sub id of the channel.
     * @param callback The method to be invoked when the server response is received
     */
    public void getChannelId(String channelType, String channelSubId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelType.name(), channelType);
            data.put(Parameter.channelSubId.name(), channelSubId);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.GET_CHANNEL_ID, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets description info and activity stats for channel <channelId>.
     * Note that numMsgs and listeners only returned for non-global groups.
     * Only callable for channels the user is a member of.
     *
     * Service Name - Chat
     * Service Operation - getChannelInfo
     *
     * @param channelId Id of the channel to receive the info from.
     * @param callback The method to be invoked when the server response is received.
     */
    public void getChannelInfo(String channelId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.GET_CHANNEL_INFO, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets a populated chat object (normally for editing).
     *
     * Service Name - Chat
     * Service Operation - getChatMessage
     *
     * @param channelId Id of the channel to receive the message from.
     * @param msgId Id of the message to read.
     * @param callback The method to be invoked when the server response is received.
     */
    public void getChatMessage(String channelId, String msgId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);
            data.put(Parameter.msgId.name(), msgId);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.GET_CHAT_MESSAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Get a list of <maxReturn> messages from history of channel <channelId>.
     *
     * Service Name - Chat
     * Service Operation - GET_RECENT_CHAT_MESSAGES
     *
     * @param channelId Id of the channel to receive the info from.
     * @param maxReturn Maximum message count to return.
     * @param callback The method to be invoked when the server response is received.
     */
    public void getRecentChatMessages(String channelId, int maxReturn, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);
            data.put(Parameter.maxReturn.name(), maxReturn);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.GET_RECENT_CHAT_MESSAGES, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Gets a list of the channels of type <channelType> that the user has access to.
     * Channel type must be one of "gl", "gr" or "all".
     *
     * Service Name - Chat
     * Service Operation - getSubscribedChannels
     *
     * @param channelType Type of channels to get back. "gl" for global, "gr" for group or "all" for both.
     * @param callback The method to be invoked when the server response is received.
     */
    public void getSubscribedChannels(String channelType, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelType.name(), channelType);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.GET_SUBSCRIBED_CHANNELS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Send a potentially rich chat message.
     * <content> must contain at least a "plain" field for plain-text messaging.
     *
     * Service Name - Chat
     * Service Operation - postChatMessage
     *
     * @param channelId Channel id to post message to.
     * @param plain the text message.
     * @param rich custom data.
     * @param recordInHistory true if the message persist in history
     * @param callback The method to be invoked when the server response is received.
     */
    public void postChatMessage(String channelId, String text, String rich, Boolean recordInHistory, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);

            JSONObject content = new JSONObject();
            content.put(Parameter.text.name(), text);
            content.put(Parameter.rich.name(), new JSONObject(rich != null ? rich : "{}"));
            data.put(Parameter.content.name(), content);

            data.put(Parameter.recordInHistory.name(), recordInHistory);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.POST_CHAT_MESSAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Send a plaiin text message
     *
     * Service Name - Chat
     * Service Operation - postChatMessageSimple
     *
     * @param channelId Channel id to post message to.
     * @param text the text message.
     * @param recordInHistory true if the message persist in history
     * @param callback The method to be invoked when the server response is received.
     */
    public void postChatMessageSimple(String channelId, String text, Boolean recordInHistory, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);
            data.put(Parameter.text.name(), text);
            data.put(Parameter.recordInHistory.name(), recordInHistory);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.POST_CHAT_MESSAGE_SIMPLE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Update a chat message.
     * <content> must contain at least a "plain" field for plain-text messaging.
     * <version> must match the latest or pass -1 to bypass version check.
     *
     * Service Name - Chat
     * Service Operation - updateChatMessage
     *
     * @param channelId Channel id where the message to update is.
     * @param msgId Message id to update.
     * @param version Version of the message to update. Must match latest or pass -1 to bypass version check.
     * @param plain the text message.
     * @param rich custom data.
     * @param callback The method to be invoked when the server response is received.
     */
    public void updateChatMessage(String channelId, String msgId, int version, String text, String rich, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.channelId.name(), channelId);
            data.put(Parameter.msgId.name(), msgId);
            data.put(Parameter.version.name(), version);

            JSONObject content = new JSONObject();
            content.put(Parameter.text.name(), text);
            content.put(Parameter.rich.name(), new JSONObject(rich != null ? rich : "{}"));
            data.put(Parameter.content.name(), content);

            ServerCall sc = new ServerCall(ServiceName.chat,
                    ServiceOperation.UPDATE_CHAT_MESSAGE, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
