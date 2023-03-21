package com.application.launcher.utils;

import com.application.launcher.entity.ConfigEntity;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.application.launcher.controller.MainController.alertMainDraw;
import static com.application.launcher.controller.MainController.processDraw;

public class LaunchUtils {

    private final String client;

    public LaunchUtils(String client) {
        this.client = client;
    }

    public void start(ArrayList<String> params) {
        try {
            ConfigUtils configUtils = new ConfigUtils();
            configUtils.init();
            ConfigEntity configEntity = configUtils.getConfigEntity();

            String home = System.getProperty("user.dir");
            ArrayList<String> needParams = new ArrayList<>();

            needParams.add("\"" + home + File.separator + "jre" + File.separator + "bin" + File.separator + "javaw.exe\"");
            if (System.getProperty("os.name").startsWith("Windows")){
                needParams.add("-Dos.name=Windows 10");
                needParams.add("-Dos.version=10.0");
            }
            needParams.add("-Xmx" + configUtils.getConfigEntity().getSize() + "M");
            needParams.addAll(params);
            needParams.add("--gameDir");
            needParams.add(home + File.separator + "launcher" + File.separator + "client" + File.separator + client);
            needParams.add("-Dminecraft.applet.TargetDirectory=" + home + File.separator + "launcher" + File.separator + "client" + File.separator+ client);

            ProcessBuilder processBuilder = new ProcessBuilder(needParams);
            processBuilder.directory(new File(home + File.separator + "launcher" + File.separator +"client" + File.separator + client + File.separator));

            File file = new File(
                    System.getProperty("user.dir") + File.separator + "launcher" + File.separator + "client" + File.separator + client + File.separator + "options.txt"
            );

            if (!file.isFile() && file.createNewFile()) {
                String separator = System.getProperty("line.separator");
                FileUtils fileUtils = new FileUtils(file);
                fileUtils.write("lang:ru_RU" + separator + "guiScale:2" + separator + "resourcePacks:[\"default32x32.zip\"]");
            }

            Process process = processBuilder.start();
            if (configEntity.isLoggerGame()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader er = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                processDraw.init();
                processDraw.setOnMouseClicked(process);

                String s;
                while ((s = in.readLine()) != null) {
                    processDraw.input(s);
                }
                while ((s = er.readLine()) != null) {
                    processDraw.input(s);
                }

                int status = process.waitFor();
                System.out.println("Exited with status: " + status);
                processDraw.close();
                Platform.runLater(() -> alertMainDraw.init("Закрылась", "Игра была закрыта!"));
            } else {
                new Thread(() -> {
                    try {
                        process.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                System.exit(0);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
