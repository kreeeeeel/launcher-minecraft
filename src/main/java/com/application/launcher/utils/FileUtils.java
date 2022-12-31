package com.application.launcher.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtils {

    private File file;

    public FileUtils(File file) {
        this.file = file;
    }

    public FileUtils() {}

    public String getResource(String name) {
        return "file:///" + new File("images/" + name + ".png").getAbsolutePath();
    }

    public boolean write(String input){
        try {
            if(!file.isFile() && !file.createNewFile()){
                return false;
            }

            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            bos.write(bytes);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String get() {

        if(!file.isFile()){
            return null;
        }

        try (FileInputStream in = new FileInputStream(file.getPath())) {

            byte[] buffer = new byte[in.available()];
            in.read(buffer, 0, buffer.length);

            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }

    }

    public void deleteFiles(String path) {
        File file = new File(path);
        if(file.isFile()) file.delete();
        if(file.isDirectory()){
            File[] array = file.listFiles();
            if (array != null) {
                for(File temp : array){
                    deleteFiles(temp.getPath());
                }
            }
            file.delete();
        }
    }
}
