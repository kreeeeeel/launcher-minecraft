package com.application.launcher.service;

import com.application.launcher.controller.MainController;
import com.application.launcher.design.draw.AlertDraw;
import com.application.launcher.entity.AccountEntity;
import com.application.launcher.entity.ConfigEntity;
import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.request.AuthRequest;
import com.application.launcher.rest.response.TokenResponse;
import com.application.launcher.utils.ConfigUtils;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.constant.Constant.*;

public class AuthService {
    private final String username;
    private final String password;

    private final AlertDraw alertDraw;

    private final Label title;
    private final Pane pane;
    private final Pane fast;

    public AuthService(String username, String password, AlertDraw alertDraw, Label title, Pane pane, Pane fast) {
        this.username = username;
        this.password = password;

        this.alertDraw = alertDraw;

        this.title = title;
        this.pane = pane;
        this.fast = fast;
    }

    public void init() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            Platform.runLater(() -> fast.setVisible(false));
            if (username.length() < LOGIN_MIN || username.length() > LOGIN_MAX) {
                alertDraw.init("Корректность данных", "Неверно указан формат логина");
                return;
            }

            if (password.length() < PASSWORD_MIN || password.length() > PASSWORD_MAX) {
                alertDraw.init("Корректность данных", "Неверно указан формат пароля.");
                return;
            }

            Platform.runLater(() -> {
                title.setText(username);
                pane.setVisible(true);
            });

            AuthRequest authRequest = new AuthRequest(username, password);

            Retrofit retrofit = RetrofitUtils.getRetrofit();
            AuthApi authApi = retrofit.create(AuthApi.class);
            authApi.auth(authRequest).enqueue(new AuthExecutor(username, password));
        });
    }

    class AuthExecutor implements Callback<TokenResponse> {

        private final String username;
        private final String password;

        AuthExecutor(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

            if(!response.isSuccessful()){
                alertDraw.init("Ошибка авторизации!", "Неверно указан логин или пароль!");
                return;
            }

            TokenResponse tokenResponse = response.body();
            if(tokenResponse == null){
                alertDraw.init("Ошибка авторизации!", "Не удалось получить данные..");
                return;
            }

            ConfigUtils configUtils = new ConfigUtils();
            configUtils.init();

            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setUsername(username);
            accountEntity.setPassword(password);

            ConfigEntity configEntity = configUtils.getConfigEntity();
            if(configEntity == null){
                configEntity = new ConfigEntity();
            }
            configEntity.addAccount(accountEntity);
            configUtils.setConfigEntity(configEntity);
            configUtils.write();

            TokenHandler.setAccessToken(tokenResponse.getAccessToken());
            TokenHandler.setTokenType(tokenResponse.getTokenType());
            Platform.runLater(() -> {
                try {
                    Stage stage = (Stage) pane.getScene().getWindow();

                    MainController mainController = new MainController();
                    mainController.start(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                    alertDraw.init("Произошла ошибка!", "Произошла ошибка в загрузке сцены!");
                }
            });
        }

        @Override
        public void onFailure(Call<TokenResponse> call, Throwable t) {
            alertDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
        }
    }
}
