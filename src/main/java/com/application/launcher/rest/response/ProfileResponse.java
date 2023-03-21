package com.application.launcher.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Map;

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

    @SerializedName("textures")
    @Expose
    private Map<TextureType, String> textures;

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

    public Map<TextureType, String> getTextures() {
        return textures;
    }
}
