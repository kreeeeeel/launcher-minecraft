package com.application.launcher.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientResponse {
    @SerializedName("files")
    @Expose
    private List<FileResponse> files;

    @SerializedName("folders")
    @Expose
    private List<String> folders;

    public List<FileResponse> getFiles() {
        return files;
    }

    public List<String> getFolders() {
        return folders;
    }

    public FileResponse getFileResponse(String name){
        for (FileResponse fileResponse : files){
            if(fileResponse.getPath().equals(name)){
                return fileResponse;
            }
        }
        return null;
    }

    public boolean isPath(String path){
        for (FileResponse fileResponse : files){
            if(fileResponse.getPath().equals(path)){
                return true;
            }
        }

        for(String name : folders){
            if(name.equals(path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return files.toString();
    }
}
