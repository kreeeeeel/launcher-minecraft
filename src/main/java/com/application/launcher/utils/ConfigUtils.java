package com.application.launcher.utils;

import com.application.launcher.entity.ConfigEntity;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class ConfigUtils {

    public static ConfigEntity loadEntity() {
        try {
            File file = new File("config.json");
            if (!file.isFile() && !file.createNewFile()){
                return null;
            }

            return new Gson().fromJson(FileUtils.getString(file), ConfigEntity.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ConfigEntity();
    }

    public static void write(ConfigEntity configEntity){
        try {
            File file = new File("config.json");
            if (!file.isFile() && file.createNewFile()){
                return;
            }

            FileUtils.write(file, new Gson().toJson(configEntity));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
