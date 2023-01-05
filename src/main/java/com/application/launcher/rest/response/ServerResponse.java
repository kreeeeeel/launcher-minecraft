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
    private int online;
    private int port;

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
}
