package com.application.launcher.utils;

public class TokenHandler {
    private static String accessToken;
    private static String tokenType;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        TokenHandler.accessToken = accessToken;
    }

    public static String getTokenType() {
        return tokenType;
    }

    public static void setTokenType(String tokenType) {
        TokenHandler.tokenType = tokenType;
    }
}
