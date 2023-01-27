package com.bitheads.braincloud.services;

public class Helpers
{
    /// <summary>
    /// Creates a properly formatted key/value json pair
    /// </summary>
    /// <param name="key"> Key </param>
    /// <param name="value"> Value </param>
    /// <returns> Formatted Json pair </returns>
    public static String createJsonPair(String key, String value)
    {
        return "{ \"" + key + "\" : \"" + value + "\"}";
    }

    public static String createJsonPair(String key, long value)
    {
        return "{ \"" + key + "\" : " + value + "}";
    }
}
