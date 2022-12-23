package com.application.launcher.rest.response;

import java.util.List;

public class ClientResponse {
    private List<FileResponse> files;
    private List<String> folders;
    private int countFiles;
    private int countFolders;

    public List<FileResponse> getFiles() {
        return files;
    }

    public void setFiles(List<FileResponse> files) {
        this.files = files;
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    public int getCountFiles() {
        return countFiles;
    }

    public void setCountFiles(int countFiles) {
        this.countFiles = countFiles;
    }

    public int getCountFolders() {
        return countFolders;
    }

    public void setCountFolders(int countFolders) {
        this.countFolders = countFolders;
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
