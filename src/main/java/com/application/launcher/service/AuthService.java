package com.application.launcher.service;

import com.application.launcher.entity.ConfigEntity;
import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.handler.AuthHandler;
import com.application.launcher.rest.request.AuthRequest;
import com.application.launcher.utils.ConfigUtils;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import retrofit2.Retrofit;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.constant.Constant.*;
import static com.application.launcher.controller.AuthController.alertAuthDraw;

public class AuthService {
    private final String username;
    private final String password;

    private final Label title;
    private final Pane pane;
    private final Pane fast;

    public AuthService(String username, String password, Label title, Pane pane, Pane fast) {
        this.username = username;
        this.password = password;

        this.title = title;
        this.pane = pane;
        this.fast = fast;
    }

    public void init() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            Platform.runLater(() -> fast.setVisible(false));
            if (username.length() < LOGIN_MIN || username.length() > LOGIN_MAX) {
                alertAuthDraw.init("Корректность данных", "Неверно указан формат логина");
                return;
            }

            if (password.length() < PASSWORD_MIN || password.length() > PASSWORD_MAX) {
                alertAuthDraw.init("Корректность данных", "Неверно указан формат пароля.");
                return;
            }

            Platform.runLater(() -> {
                title.setText(username);
                pane.setVisible(true);
            });

            ConfigUtils configUtils = new ConfigUtils();
            configUtils.init();
            ConfigEntity configEntity = configUtils.getConfigEntity();

            for (int i = 0; i < configEntity.getAccounts().size(); i++) {
                if (configEntity.getAccounts().get(i).getUsername().equals(username)){
                    configEntity.getAccounts().get(i).setPassword(password);
                    configEntity.getAccounts().get(i).setLast(new Date().getTime());
                    configUtils.setConfigEntity(configEntity);
                    configUtils.write();
                }
            }

            AuthRequest authRequest = new AuthRequest(username, password);

            Retrofit retrofit = RetrofitUtils.getRetrofit();
            AuthApi authApi = retrofit.create(AuthApi.class);
            authApi.auth(authRequest).enqueue(new AuthHandler(username, password));
        });
    }
}
