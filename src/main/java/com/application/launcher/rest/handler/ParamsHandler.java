package com.application.launcher.rest.handler;

import com.application.launcher.utils.LaunchUtils;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

import static com.application.launcher.controller.MainController.*;

public class ParamsHandler implements Callback<ArrayList<String>> {

    private final String client;

    public ParamsHandler(String client) {
        this.client = client;
    }

    @Override
    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

        if (!response.isSuccessful()) {
            alertMainDraw.init("Ошибка запуска!", "Не удалось получить данные..");
            return;
        }

        ArrayList<String> params = response.body();
        if (params == null) {
            alertMainDraw.init("Ошибка запуска!", "Тело запроса оказалось пустым..");
            return;
        }

        Platform.runLater(() -> {
            titleUpdateS.setText("Запуск..");
            fileUpdateS.setText("Инициализация запуска..");
            progressUpdateS.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        });

        Platform.runLater(() -> paneUpdateS.setVisible(true));
        LaunchUtils.start(client, params);
    }

    @Override
    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
        alertMainDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
    }

}