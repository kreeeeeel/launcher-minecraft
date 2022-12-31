package com.application.launcher.utils;

import com.application.launcher.entity.ConfigEntity;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class ConfigUtils {

    private ConfigEntity configEntity;

    public ConfigEntity getConfigEntity(){
        return configEntity;
    }

    public void setConfigEntity(ConfigEntity configEntity){
        this.configEntity = configEntity;
    }

    public void init() {
        try {
            File file = new File("config.json");
            if (!file.isFile() && file.createNewFile()){
                return;
            }

            FileUtils fileUtils = new FileUtils(file);
            configEntity = new Gson().fromJson(fileUtils.get(), ConfigEntity.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(){
        try {
            File file = new File("config.json");
            if (!file.isFile() && file.createNewFile()){
                return;
            }

            FileUtils fileUtils = new FileUtils(file);
            fileUtils.write(new Gson().toJson(configEntity));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
