package com.application.launcher.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerResponse {

    @SerializedName("ip")
    @Expose
    private String ip;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("client")
    @Expose
    private String client;

    @SerializedName("pvp")
    @Expose
    private String pvp;

    @SerializedName("size")
    @Expose
    private String size;

    @SerializedName("start")
    @Expose
    private String start;

    @SerializedName("wipe")
    @Expose
    private String wipe;

    @SerializedName("mark")
    @Expose
    private String mark;

    @SerializedName("port")
    @Expose
    private int port;

    @SerializedName("online")
    @Expose
    private int online;

    public String getIp() {
        return ip;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getClient() {
        return client;
    }

    public String getVersion() {
        return version;
    }

    public String getWipe() {
        return wipe;
    }

    public String getStart() {
        return start;
    }

    public String getSize() {
        return size;
    }

    public String getPvp() {
        return pvp;
    }

    public int getPort() {
        return port;
    }

    public String getMark() {
        return mark;
    }

    public int getOnline() { return online; }
}
