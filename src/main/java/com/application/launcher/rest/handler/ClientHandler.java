package com.application.launcher.rest.handler;

import com.application.launcher.rest.response.ClientResponse;
import com.application.launcher.service.LauncherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.application.launcher.controller.MainController.alertMainDraw;

public class ClientHandler implements Callback<ClientResponse> {

    private final String launcher;

    public ClientHandler(String launcher) {
        this.launcher = launcher;
    }

    @Override
    public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {

        if (!response.isSuccessful()) {
            alertMainDraw.init("Ошибка авторизации!", "Время сеанса истекло...");
            return;
        }

        ClientResponse clientResponse = response.body();
        new LauncherService(launcher).fileWork(clientResponse);
    }

    @Override
    public void onFailure(Call<ClientResponse> call, Throwable throwable) {
        alertMainDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
    }
}
