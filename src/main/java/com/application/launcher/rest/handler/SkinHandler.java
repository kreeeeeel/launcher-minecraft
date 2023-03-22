package com.application.launcher.rest.handler;

import com.application.launcher.rest.response.TextureResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

import static com.application.launcher.constant.Constant.API;
import static com.application.launcher.controller.MainController.alertMainDraw;
import static com.application.launcher.controller.MainController.textureDraw;
import static com.application.launcher.controller.RegisterController.alertRegDraw;

public class SkinHandler implements Callback<TextureResponse> {
    @Override
    public void onResponse(Call<TextureResponse> call, Response<TextureResponse> response) {
        try {
            if (!response.isSuccessful()) {
                alertMainDraw.init("Произошла ошибка", "Возможно ваш скин является стандартным, если это не так, то попробуйте позже..");
                return;
            }

            TextureResponse textureResponse = response.body();
            if (textureResponse == null) {
                alertMainDraw.init("Ошибка запуска!", "Тело запроса оказалось пустым..");
                return;
            }

            alertMainDraw.init("Успешно!", "Ваш скин был удалён!");
            textureDraw.setTextureSkin(ImageIO.read(new URL(API + "/image/" + textureResponse.getHash())));
        } catch (IOException ex) {
            alertRegDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
        }
    }

    @Override
    public void onFailure(Call<TextureResponse> call, Throwable throwable) {
        alertRegDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
    }
}
