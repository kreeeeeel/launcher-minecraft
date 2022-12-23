package com.application.launcher.rest.response;

public class ServerResponse {
    private String ip;
    private String title;
    private String icon;
    private String version;
    private String client;
    private String pvp;
    private String size;
    private String start;
    private String wipe;
    private String wipeOther;

    private int online;
    private int players;

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

    public int getOnline() {
        return online;
    }

    public int getPlayers() {
        return players;
    }

    public String getWipeOther() {
        return wipeOther;
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
}
