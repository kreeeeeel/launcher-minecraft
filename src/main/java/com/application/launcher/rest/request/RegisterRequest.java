package com.application.launcher.rest.request;

public class RegisterRequest {
    public final String name;
    public final String username;
    public final String email;
    public final String password;

    public RegisterRequest(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
