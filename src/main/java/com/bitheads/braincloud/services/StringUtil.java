package com.bitheads.braincloud.services;

public class StringUtil {

    public static boolean IsOptionalParameterValid(String param) {
        return !(param == null || param.length() == 0);
    }
}
