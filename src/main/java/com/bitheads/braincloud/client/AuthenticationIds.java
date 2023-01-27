package com.bitheads.braincloud.client;

public class AuthenticationIds
{
    public String externalId = "";
    public String authenticationToken = "";
    public String authenticationSubType = ""; // Empty string for most auth types

    public AuthenticationIds(String in_externalId, String in_authenticationToken)
    {
        externalId = in_externalId;
        authenticationToken = in_authenticationToken;
        authenticationSubType = "";
    }

    public AuthenticationIds(String in_externalId, String in_authenticationToken, String in_authenticationSubType)
    {
        externalId = in_externalId;
        authenticationToken = in_authenticationToken;
        authenticationSubType = in_authenticationSubType;
    }
};
