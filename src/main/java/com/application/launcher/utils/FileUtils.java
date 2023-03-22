package com.application.launcher.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtils {

    public static String getResource(String name) {
        return "file:///" + new File("resources/" + name + ".png").getAbsolutePath();
    }

    public static void write(File file, String input){
        try {
            if(!file.isFile() && !file.createNewFile()){
                return;
            }

            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            bos.write(bytes);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(File file) {

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

    public static void deleteFiles(String path) {
        File file = new File(path);
        if(file.isFile() && !file.delete()) return;
        if(file.isDirectory()){
            File[] array = file.listFiles();
            if (array != null) {
                for(File temp : array){
                    deleteFiles(temp.getPath());
                }
            }

            if(file.delete()) {
                System.out.println("File " + path + " removed!");
            }
        }
    }
}
