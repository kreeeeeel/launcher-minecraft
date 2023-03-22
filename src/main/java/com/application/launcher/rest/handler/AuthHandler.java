package com.application.launcher.rest.handler;

import com.application.launcher.controller.MainController;
import com.application.launcher.entity.AccountEntity;
import com.application.launcher.entity.ConfigEntity;
import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.response.TokenResponse;
import com.application.launcher.utils.ConfigUtils;
import javafx.application.Platform;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

import static com.application.launcher.Runner.primaryStage;
import static com.application.launcher.controller.AuthController.alertAuthDraw;

public class AuthHandler implements Callback<TokenResponse> {

    private final String username;
    private final String password;
    private final boolean isSave;

    public AuthHandler(String username, String password, boolean isSave) {
        this.username = username;
        this.password = password;
        this.isSave = isSave;
    }

    @Override
    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

        if(!response.isSuccessful()){
            alertAuthDraw.init("Ошибка авторизации!", "Неверно указан логин или пароль!");
            return;
        }

        TokenResponse tokenResponse = response.body();
        if(tokenResponse == null){
            alertAuthDraw.init("Ошибка авторизации!", "Не удалось получить данные..");
            return;
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(username);
        accountEntity.setPassword(password);

        ConfigEntity configEntity = ConfigUtils.loadEntity();
        if(configEntity == null){
            configEntity = new ConfigEntity();
        }
        configEntity.setSaveAccount(isSave);
        configEntity.setAccountEntity(accountEntity);
        ConfigUtils.write(configEntity);

        TokenHandler.setAccessToken(tokenResponse.getAccessToken());
        TokenHandler.setTokenType(tokenResponse.getTokenType());
        Platform.runLater(() -> {
            try {
                Stage stage = (Stage) primaryStage.getScene().getWindow();

                MainController mainController = new MainController();
                mainController.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
                alertAuthDraw.init("Произошла ошибка!", "Произошла ошибка в загрузке сцены!");
            }
        });
    }

    @Override
    public void onFailure(Call<TokenResponse> call, Throwable t) {
        alertAuthDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
    }
}
