package com.application.launcher.utils;

import com.application.launcher.rest.response.SettingsResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LaunchUtils {

    public void start(String client, SettingsResponse settingsResponse, boolean fullscreen) {
        try {

            String home = System.getProperty("user.dir");
            ArrayList<String> params = new ArrayList<>();

            params.add("\"" + home + "\\jdk\\jre\\bin\\javaw.exe\"");
            params.add("-Xmx4096M");
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
            params.add(TokenUtils.getAccessToken());
            params.add("--userType");
            params.add(settingsResponse.getUser());
            params.add("--tweakClass");
            params.add(settingsResponse.getTweak());
            params.add("--versionType");
            params.add(settingsResponse.getType());
            if (fullscreen){
                params.add("--fullscreen");
            }

            ProcessBuilder processBuilder = new ProcessBuilder(params);
            processBuilder.directory(new File(home + "\\launcher\\client\\" + client + "\\"));

            System.out.println(String.join(" ",processBuilder.command().toArray(new String[0])));

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
            System.exit(1);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

}
