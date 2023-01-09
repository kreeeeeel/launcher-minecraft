package com.application.launcher.service;

import com.application.launcher.design.draw.AlertDraw;
import com.application.launcher.design.draw.ProcessDraw;
import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.api.LauncherApi;
import com.application.launcher.rest.response.ClientResponse;
import com.application.launcher.rest.response.FileResponse;
import com.application.launcher.utils.LaunchUtils;
import com.application.launcher.utils.MD5Utils;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LauncherService {

    private String launcher;

    private final Pane pane;
    private final Label fileUpdate;
    private final Label titleUpdate;
    private final ProgressBar progressBar;
    private final RadioButton boxLaunchFullScreen;
    private final RadioButton boxLaunchAuto;

    private final AlertDraw alertDraw;
    private final ProcessDraw processDraw;

    private final AtomicInteger sendRequest = new AtomicInteger(0);
    private final AtomicInteger successRequest = new AtomicInteger(0);

    public LauncherService(Pane pane, Label fileUpdate, Label titleUpdate, ProgressBar progressBar, RadioButton boxLaunchFullScreen, RadioButton boxLaunchAuto, AlertDraw alertDraw, ProcessDraw processDraw) {
        this.pane = pane;
        this.fileUpdate = fileUpdate;
        this.titleUpdate = titleUpdate;
        this.progressBar = progressBar;
        this.boxLaunchFullScreen = boxLaunchFullScreen;
        this.boxLaunchAuto = boxLaunchAuto;
        this.alertDraw = alertDraw;
        this.processDraw = processDraw;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    // Step one
    public void init() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            pane.setVisible(true);
            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();

            LauncherApi launcherApi = RetrofitUtils.getRetrofit().create(LauncherApi.class);
            launcherApi.getLauncher(token, launcher).enqueue(new LauncherExecutor(launcher));
        });

    }

    class LauncherExecutor implements Callback<ClientResponse> {

        private final String launcher;

        LauncherExecutor(String launcher) {
            this.launcher = launcher;
        }

        @Override
        public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {

            if (!response.isSuccessful()) {
                alertDraw.init("Ошибка авторизации!", "Время сеанса истекло...");
                return;
            }

            ClientResponse clientResponse = response.body();
            updateFiles(launcher, clientResponse);

        }

        @Override
        public void onFailure(Call<ClientResponse> call, Throwable throwable) {
            alertDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
        }
    }

    // Step two
    public void updateFiles(String name, ClientResponse clientResponse) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            sendRequest.set(0);
            successRequest.set(0);

            Platform.runLater(() -> {
                titleUpdate.setText("Обновление файлов..");
                fileUpdate.setText("Подготовка к обновлению..");
            });

            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();
            LauncherApi launcherApi = RetrofitUtils.getRetrofit().create(LauncherApi.class);
            MD5Utils md5Utils = new MD5Utils();

            File folder = new File("launcher");
            if (folder.mkdir()) {
                Platform.runLater(() -> fileUpdate.setText("Создание папки с клиентами."));
            }

            File client = new File("launcher/client");
            if (client.mkdir()) {
                Platform.runLater(() -> fileUpdate.setText("Создание папки с клиентами."));
            }

            File launcher = new File("launcher/client/" + name);
            if (launcher.mkdir()) {
                Platform.runLater(() -> fileUpdate.setText("Создание папки для сборки."));
            }

            for (String fileName : clientResponse.getFolders()) {

                File file = new File(fileName);

                if (!file.isDirectory() && !file.mkdir()) {
                    return;
                }

                Platform.runLater(() -> {
                    fileUpdate.setText(fileName);
                    progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                });
            }

            for (FileResponse fileResponse : clientResponse.getFiles()) {

                File file = new File(fileResponse.getPath());
                String hash = null;
                try {
                    hash = md5Utils.getMD5File(file.getAbsolutePath());
                } catch (IOException | NoSuchAlgorithmException e) {
                    System.out.println("Not md5 hash in file: " + fileResponse.getPath());
                }

                Platform.runLater(() -> fileUpdate.setText(fileResponse.getPath()));
                if (!file.exists() || hash == null || !hash.equals(fileResponse.getMd5()) || file.length() != fileResponse.getSize()) {
                    sendRequest.incrementAndGet();
                    try {
                        launcherApi.download(token, URLEncoder.encode(fileResponse.getPath().replace("\\", "/"), StandardCharsets.UTF_8.toString())).enqueue(new UpdateFilesExecutor(fileResponse, name));
                    } catch (UnsupportedEncodingException e) {
                        alertDraw.init("Ошибка загрузки", "Не удалось получить файл..");
                    }
                }
            }

            if (sendRequest.get() == 0) {
                launchMinecraft(name);
            }
        });
    }

    class UpdateFilesExecutor implements Callback<ResponseBody> {

        private final FileResponse fileResponse;
        private final String name;

        UpdateFilesExecutor(FileResponse fileResponse, String name) {
            this.fileResponse = fileResponse;
            this.name = name;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            int cur = successRequest.incrementAndGet();

            Platform.runLater(() -> {
                fileUpdate.setText(fileResponse.getPath());
                progressBar.setProgress((double) cur / sendRequest.get());
            });

            if (!response.isSuccessful()) {
                //alertShow("Ошибка авторизации!", "Неверно указан логин или пароль!");
                System.out.println(response.code());
                return;
            }

            try {
                ResponseBody responseBody = response.body();
                InputStream inputStream = null;
                if (responseBody != null) {
                    inputStream = new BufferedInputStream(responseBody.byteStream());
                }
                FileOutputStream fileOutputStream = new FileOutputStream(fileResponse.getPath());

                byte[] buffer = new byte[65536];
                int line;
                if (inputStream != null) {
                    while ((line = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        fileOutputStream.write(buffer, 0, line);
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                fileOutputStream.close();

            } catch (IOException ex){
                System.out.println(ex.getMessage());
            }

            if (sendRequest.get() == cur) {
                launchMinecraft(name);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    public void launchMinecraft(String client) {
        String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();

        LauncherApi launcherApi = RetrofitUtils.getRetrofit().create(LauncherApi.class);
        launcherApi.getLauncherSettings(token, client, boxLaunchFullScreen.isSelected(), boxLaunchAuto.isSelected()).enqueue(new LaunchExecutor(client));
    }

    class LaunchExecutor implements Callback<ArrayList<String>> {

        private final String client;

        LaunchExecutor(String client) {
            this.client = client;
        }

        @Override
        public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

            if (!response.isSuccessful()) {
                alertDraw.init("Ошибка запуска!", "Не удалось получить данные..");
                return;
            }

            ArrayList<String> params = response.body();
            if (params == null) {
                alertDraw.init("Ошибка запуска!", "Тело запроса оказалось пустым..");
                return;
            }

            Platform.runLater(() -> {
                titleUpdate.setText("Запуск..");
                fileUpdate.setText("Инициализация запуска..");
                progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            });

            pane.setVisible(false);
            LaunchUtils launchUtils = new LaunchUtils(client, processDraw, alertDraw);
            launchUtils.setupOptions();
            launchUtils.start(params);
        }

        @Override
        public void onFailure(Call<ArrayList<String>> call, Throwable t) {
            alertDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
        }

    }
}
