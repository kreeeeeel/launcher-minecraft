package com.application.launcher.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsResponse {

    @SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("natives")
    @Expose
    private String natives;

    @SerializedName("libraries")
    @Expose
    private String libraries;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("index")
    @Expose
    private String index;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("tweak")
    @Expose
    private String tweak;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("launchwrapper")
    @Expose
    private String launchwrapper;

    @SerializedName("ip")
    @Expose
    private String ip;

    @SerializedName("port")
    @Expose
    private String port;

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
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
