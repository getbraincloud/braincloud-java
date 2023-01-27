package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prestonjennings on 15-12-14.
 */
public class ProfanityService {

    private enum Parameter {
        text,
        languages,
        flagEmail,
        flagPhone,
        flagUrls,
        replaceSymbol
    }

    private BrainCloudClient _client;

    public ProfanityService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Checks supplied text for profanity.
     *
     * Service Name - Profanity
     * Service Operation - ProfanityCheck
     *
     * @param text The text to check
     * @param languages Optional comma delimited list of two character language codes
     * @param flagEmail Optional processing of email addresses
     * @param flagPhone Optional processing of phone numbers
     * @param flagUrls Optional processing of urls
     * @param callback The method to be invoked when the server response is received
     *
     * Significant error codes:
     *
     * 40421 - WebPurify not configured
     * 40422 - General exception occurred
     * 40423 - WebPurify returned an error (Http status != 200)
     * 40424 - WebPurify not enabled
     */
    public void profanityCheck(
        String text,
        String languages,
        boolean flagEmail,
        boolean flagPhone,
        boolean flagUrls,
        IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.text.name(), text);
            if (languages != null) {
                data.put(Parameter.languages.name(), languages);
            }
            data.put(Parameter.flagEmail.name(), flagEmail);
            data.put(Parameter.flagPhone.name(), flagPhone);
            data.put(Parameter.flagUrls.name(), flagUrls);

            ServerCall sc = new ServerCall(ServiceName.profanity, ServiceOperation.PROFANITY_CHECK, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Replaces the characters of profanity text with a passed character(s).
     *
     * Service Name - Profanity
     * Service Operation - ProfanityReplaceText
     *
     * @param text The text to check
     * @param replaceSymbol The text to replace individual characters of profanity text with
     * @param languages Optional comma delimited list of two character language codes
     * @param flagEmail Optional processing of email addresses
     * @param flagPhone Optional processing of phone numbers
     * @param flagUrls Optional processing of urls
     * @param callback The method to be invoked when the server response is received
     *
     * Significant error codes:
     *
     * 40421 - WebPurify not configured
     * 40422 - General exception occurred
     * 40423 - WebPurify returned an error (Http status != 200)
     * 40424 - WebPurify not enabled
     */
    public void profanityReplaceText(
        String text,
        String replaceSymbol,
        String languages,
        boolean flagEmail,
        boolean flagPhone,
        boolean flagUrls,
        IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.text.name(), text);
            data.put(Parameter.replaceSymbol.name(), replaceSymbol);
            if (languages != null) {
                data.put(Parameter.languages.name(), languages);
            }
            data.put(Parameter.flagEmail.name(), flagEmail);
            data.put(Parameter.flagPhone.name(), flagPhone);
            data.put(Parameter.flagUrls.name(), flagUrls);

            ServerCall sc = new ServerCall(ServiceName.profanity, ServiceOperation.PROFANITY_REPLACE_TEXT, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Checks supplied text for profanity and returns a list of bad wors.
     *
     * Service Name - Profanity
     * Service Operation - ProfanityIdentifyBadWords
     *
     * @param text The text to check
     * @param languages Optional comma delimited list of two character language codes
     * @param flagEmail Optional processing of email addresses
     * @param flagPhone Optional processing of phone numbers
     * @param flagUrls Optional processing of urls
     * @param callback The method to be invoked when the server response is received
     *
     * Significant error codes:
     *
     * 40421 - WebPurify not configured
     * 40422 - General exception occurred
     * 40423 - WebPurify returned an error (Http status != 200)
     * 40424 - WebPurify not enabled
     */
    void profanityIdentifyBadWords(
        String text,
        String languages,
        boolean flagEmail,
        boolean flagPhone,
        boolean flagUrls,
        IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.text.name(), text);
            if (languages != null) {
                data.put(Parameter.languages.name(), languages);
            }
            data.put(Parameter.flagEmail.name(), flagEmail);
            data.put(Parameter.flagPhone.name(), flagPhone);
            data.put(Parameter.flagUrls.name(), flagUrls);

            ServerCall sc = new ServerCall(ServiceName.profanity, ServiceOperation.PROFANITY_IDENTIFY_BAD_WORDS, data, callback);
            _client.sendRequest(sc);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
