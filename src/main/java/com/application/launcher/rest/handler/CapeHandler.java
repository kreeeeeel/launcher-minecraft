package com.application.launcher.rest.handler;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.application.launcher.controller.MainController.alertMainDraw;
import static com.application.launcher.controller.MainController.textureDraw;
import static com.application.launcher.controller.RegisterController.alertRegDraw;

public class CapeHandler implements Callback<ResponseBody> {
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (!response.isSuccessful()) {
            alertMainDraw.init("Произошла ошибка", "Произошла ошибка, попробуйте позже..");
            return;
        }

        alertMainDraw.init("Успешно!", "Ваш плащ был удалён!");
        textureDraw.removeCap();
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
        alertRegDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
    }
}
