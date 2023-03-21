package com.application.launcher.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextureResponse {

    @SerializedName("hash")
    @Expose
    private String hash;

    @SerializedName("texture")
    @Expose
    private TextureType texture;

    public TextureType getTexture() {
        return texture;
    }

    public String getHash() {
        return hash;
    }
}
