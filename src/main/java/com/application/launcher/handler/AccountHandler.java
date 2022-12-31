package com.application.launcher.handler;

import javafx.scene.layout.Pane;

public class AccountHandler {
    private static String username;
    private static String password;
    private static Pane pane;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AccountHandler.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AccountHandler.password = password;
    }

    public static Pane getPane() {
        return pane;
    }

    public static void setPane(Pane pane) {
        AccountHandler.pane = pane;
    }
}
