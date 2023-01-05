package com.application.launcher.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ProfileResponse {

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("balance")
    @Expose
    private BigDecimal balance;

    @SerializedName("servers")
    @Expose
    private ServerResponse[] servers;

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public ServerResponse[] getServers() {
        return servers;
    }
}
