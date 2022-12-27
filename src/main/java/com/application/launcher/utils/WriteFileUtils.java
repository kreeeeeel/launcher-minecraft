package com.application.launcher.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WriteFileUtils {
    private File file;

    public WriteFileUtils(){}

    public WriteFileUtils(File file){
        this.file = file;
    }

    public boolean write(String input){
        return write(this.file, input);
    }

    public boolean write(File file, String input){
        try {
            if(!file.isFile() && !file.createNewFile()){
                return false;
            }

            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bytes);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
