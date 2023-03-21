package com.application.launcher.service;

import com.application.launcher.dto.ProcessFile;
import com.application.launcher.entity.ConfigEntity;
import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.api.LauncherApi;
import com.application.launcher.rest.handler.ClientHandler;
import com.application.launcher.rest.handler.ParamsHandler;
import com.application.launcher.rest.response.ClientResponse;
import com.application.launcher.rest.response.FileResponse;
import com.application.launcher.utils.ConfigUtils;
import com.application.launcher.utils.DownloadUtils;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.application.launcher.controller.MainController.*;

public class LauncherService {

    private final String launcher;

    public LauncherService(String launcher) {
        this.launcher = launcher;
    }

    public void init() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Platform.runLater(() -> {
                paneUpdateS.setVisible(true);
                titleUpdateS.setText("Выполняется запрос..");
                fileUpdateS.setText("Пожалуйста, подождите..");
                progressUpdateS.setProgress(-1.0);
            });
            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();

            LauncherApi launcherApi = RetrofitUtils.getRetrofit().create(LauncherApi.class);
            launcherApi.getLauncher(token, launcher).enqueue(new ClientHandler(launcher));
        });

    }

    public void fileWork(ClientResponse clientResponse) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Platform.runLater(() -> {
                titleUpdateS.setText("Проверка файлов..");
                fileUpdateS.setText("Пожалуйста, подождите..");
            });

            LauncherApi launcherApi = RetrofitUtils.getRetrofit().create(LauncherApi.class);
            File folder = new File("launcher");
            if (folder.mkdir()) {
                Platform.runLater(() -> fileUpdateS.setText("Создание папки с клиентами."));
            }

            File client = new File("launcher/client");
            if (client.mkdir()) {
                Platform.runLater(() -> fileUpdateS.setText("Создание папки с клиентами."));
            }

            File build = new File("launcher/client/" + launcher);
            if (build.mkdir()) {
                Platform.runLater(() -> fileUpdateS.setText("Создание папки для сборки."));
            }

            clientResponse.getFolders().stream()
                    .map(File::new)
                    .filter(File::mkdir)
                    .forEach(file -> Platform.runLater(() -> {
                        fileUpdateS.setText(file.getName());
                        progressUpdateS.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                    }));

            List<ProcessFile> processFiles = clientResponse.getFiles().stream()
                    .map(this::createProcessFile)
                    .filter(ProcessFile::filterFilesToRequest)
                    .map(it -> it.setCall(launcherApi.download(TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken(), buildFilePath(it))))
                    .collect(Collectors.toList());

            AtomicInteger counter = new AtomicInteger(0);
            processFiles.parallelStream().forEach(it ->
                    DownloadUtils.onResponse(it.getServerPath(), it.getCall(), (double) counter.incrementAndGet() / processFiles.size())
            );

            launchMinecraft(launcher);
        });
    }

    public void launchMinecraft(String client) {

        LauncherApi launcherApi = RetrofitUtils.getRetrofit().create(LauncherApi.class);

        ConfigUtils configUtils = new ConfigUtils();
        configUtils.init();
        ConfigEntity configEntity = configUtils.getConfigEntity();

        launcherApi.getLauncherSettings(
                TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken(),
                client,
                configEntity.isAutoConnect(),
                configEntity.isFullscreen()
        ).enqueue(
            new ParamsHandler(
                client
            )
        );
    }

    private ProcessFile createProcessFile(FileResponse fileResponse) {
        return new ProcessFile(
                new File(fileResponse.getPath()),
                fileResponse.getMd5(),
                fileResponse.getSize(),
                fileResponse.getPath().replace("\\", "/")
        );
    }

    private String buildFilePath(ProcessFile processFile) {
        try {
            return URLEncoder.encode(processFile.getServerPath(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
