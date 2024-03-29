package com.application.launcher.service;

import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.handler.AuthHandler;
import com.application.launcher.rest.request.AuthRequest;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import retrofit2.Retrofit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.constant.Constant.*;
import static com.application.launcher.controller.AuthController.alertAuthDraw;

public class AuthService {
    private final String username;
    private final String password;

    private final Label title;
    private final Pane pane;

    private final boolean save;

    public AuthService(String username, String password, Label title, Pane pane, boolean save) {
        this.username = username;
        this.password = password;

        this.title = title;
        this.pane = pane;
        this.save = save;
    }

    public void init() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

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

            AuthRequest authRequest = new AuthRequest(username, password);

            Retrofit retrofit = RetrofitUtils.getRetrofit();
            AuthApi authApi = retrofit.create(AuthApi.class);
            authApi.auth(authRequest).enqueue(new AuthHandler(username, password, save));
        });
    }
}
