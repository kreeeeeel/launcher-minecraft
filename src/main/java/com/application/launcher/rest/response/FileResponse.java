package com.application.launcher.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileResponse {
    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("md5")
    @Expose
    private String md5;

    @SerializedName("size")
    @Expose
    private long size;

    public String getPath() {
        return path;
    }

    public String getMd5() {
        return md5;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString(){
        return path;
    }
}
