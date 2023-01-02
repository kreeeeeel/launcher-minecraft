package com.application.launcher.rest.response;

public class SettingsResponse {
    private String uuid;
    private String username;
    private String javaagent;
    private String natives;
    private String libraries;
    private String version;
    private String index;
    private String user;
    private String tweak;
    private String type;
    private String launchwrapper;
    private String ip;
    private String port;

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getJavaagent() {
        return javaagent;
    }

    public String getNatives() {
        return natives;
    }

    public String getLibraries() {
        return libraries;
    }

    public String getVersion() {
        return version;
    }

    public String getIndex() {
        return index;
    }

    public String getUser() {
        return user;
    }

    public String getTweak() {
        return tweak;
    }

    public String getType() {
        return type;
    }

    public String getLaunchwrapper() {
        return launchwrapper;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}
