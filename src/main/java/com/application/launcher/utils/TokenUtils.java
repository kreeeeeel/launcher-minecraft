package com.application.launcher.utils;

public class TokenUtils {
    private static String accessToken;
    private static String tokenType;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        TokenUtils.accessToken = accessToken;
    }

    public static String getTokenType() {
        return tokenType;
    }

    public static void setTokenType(String tokenType) {
        TokenUtils.tokenType = tokenType;
    }
}
