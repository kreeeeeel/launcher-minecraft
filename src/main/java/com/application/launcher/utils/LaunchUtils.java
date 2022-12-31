package com.application.launcher.utils;

import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.response.SettingsResponse;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LaunchUtils {

    private final Stage stage;
    private final String client;
    private final SettingsResponse settingsResponse;
    private final boolean fullscreen;
    private final boolean auto;

    public LaunchUtils(Stage stage, String client, SettingsResponse settingsResponse, boolean fullscreen, boolean auto) {
        this.stage = stage;
        this.client = client;
        this.settingsResponse = settingsResponse;
        this.fullscreen = fullscreen;
        this.auto = auto;
    }

    public void start() {
        try {

            ConfigUtils configUtils = new ConfigUtils();
            configUtils.init();

            String home = System.getProperty("user.dir");
            ArrayList<String> params = new ArrayList<>();

            params.add("\"" + home + "\\jre\\bin\\javaw.exe\"");
            params.add("-Xmx" + configUtils.getConfigEntity().getSize() + "M");
            params.add("-XX:+UseConcMarkSweepGC");
            params.add("-XX:+CMSIncrementalMode");
            params.add("-XX:-UseAdaptiveSizePolicy");
            params.add("-Xmn128M");
            params.add("\"-Djava.library.path=" + settingsResponse.getNatives() + "\"");
            params.add("-cp");
            params.add(settingsResponse.getLibraries());
            params.add(settingsResponse.getLaunchwrapper());
            params.add("--username");
            params.add(settingsResponse.getUsername());
            params.add("--version");
            params.add(settingsResponse.getVersion());
            params.add("--gameDir");
            params.add(home + "\\launcher\\client\\" + client + "\\");
            params.add("--assetsDir");
            params.add("assets");
            params.add("--assetIndex");
            params.add(settingsResponse.getIndex());
            params.add("--uuid");
            params.add(settingsResponse.getUuid());
            params.add("--accessToken");
            params.add(TokenHandler.getAccessToken());
            params.add("--userType");
            params.add(settingsResponse.getUser());
            params.add("--tweakClass");
            params.add(settingsResponse.getTweak());
            params.add("--versionType");
            params.add(settingsResponse.getType());

            if (fullscreen) {
                params.add("--fullscreen");
            }

            if (auto) {
                params.add("--server");
                params.add("185.221.153.142");
                params.add("--port");
                params.add("25565");
            }

            ProcessBuilder processBuilder = new ProcessBuilder(params);
            processBuilder.directory(new File(home + "\\launcher\\client\\" + client + "\\"));

            System.out.println(String.join(" ",processBuilder.command().toArray(new String[0])));
            Platform.runLater(stage::close);

            Process process = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader er = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s = "";
            while((s = in.readLine()) != null){
                System.out.println(s);
            }
            while((s = er.readLine()) != null) {
                System.out.println(s);
            }

            int status = process.waitFor();
            System.out.println("Exited with status: " + status);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

}
