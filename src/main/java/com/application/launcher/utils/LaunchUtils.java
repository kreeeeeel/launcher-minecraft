package com.application.launcher.utils;

import com.application.launcher.design.draw.AlertDraw;
import com.application.launcher.design.draw.ProcessDraw;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LaunchUtils {

    private final String client;
    private final ProcessDraw processDraw;
    private final AlertDraw alertDraw;

    public LaunchUtils(String client, ProcessDraw processDraw, AlertDraw alertDraw) {
        this.client = client;
        this.processDraw = processDraw;
        this.alertDraw = alertDraw;
    }

    public void start(ArrayList<String> params) {
        try {

            ConfigUtils configUtils = new ConfigUtils();
            configUtils.init();

            String home = System.getProperty("user.dir");
            ArrayList<String> needParams = new ArrayList<>();

            needParams.add("\"" + home + "\\jre\\bin\\javaw.exe\"");
            needParams.add("-Xmx" + configUtils.getConfigEntity().getSize() + "M");
            needParams.addAll(params);
            needParams.add("--gameDir");
            needParams.add(home + "\\launcher\\client\\" + client + "\\");

            ProcessBuilder processBuilder = new ProcessBuilder(needParams);
            processBuilder.directory(new File(home + "\\launcher\\client\\" + client + "\\"));

            System.out.println(String.join(" ", processBuilder.command().toArray(new String[0])));

            Process process = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader er = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            processDraw.init();
            processDraw.setOnMouseClicked(process);

            String s;
            while((s = in.readLine()) != null) {
                processDraw.input(s);
                System.out.println(s);
            }
            while((s = er.readLine()) != null) {
                processDraw.input(s);
                System.out.println(s);
            }

            int status = process.waitFor();
            System.out.println("Exited with status: " + status);
            processDraw.close();
            Platform.runLater(() -> alertDraw.init("Закрылась", "Игра была закрыта!"));

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    public void setupOptions() {
        try {

            String home = System.getProperty("user.dir");
            File file = new File(home + "\\launcher\\client\\" + client + "\\options.txt");

            if (!file.isFile() && !file.createNewFile()) {
                return;
            }

            String string = "lang:ru_ru\nguiScale:2";
            FileUtils fileUtils = new FileUtils(file);
            fileUtils.write(string);
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }

    }

}
