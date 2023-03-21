package com.application.launcher.rest.handler;

import com.application.launcher.rest.response.TextureResponse;
import com.application.launcher.rest.response.TextureType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static com.application.launcher.constant.Constant.API;
import static com.application.launcher.controller.MainController.alertMainDraw;
import static com.application.launcher.controller.MainController.textureDraw;
import static com.application.launcher.controller.RegisterController.alertRegDraw;

public class TexturesHandler implements Callback<TextureResponse> {

    @Override
    public void onResponse(Call<TextureResponse> call, Response<TextureResponse> response) {

        if (!response.isSuccessful()) {
            alertMainDraw.init("Ошибка загрузки!", "Произошла ошибка при загрузке файла!");
            return;
        }

        TextureResponse textureResponse = response.body();
        if (textureResponse == null){
            alertMainDraw.init("Ошибка загрузки!", "Ответ от сервера оказался пустым..");
            return;
        }

        if (textureResponse.getTexture() == TextureType.SKIN){
            try {
                BufferedImage bufferedImage = ImageIO.read(new URL(API + "/image/" + textureResponse.getHash()));
                textureDraw.setTextureSkin(bufferedImage);
                alertMainDraw.init("Успешно!", "Ваш скин был успешно обновлён!");
            } catch (IOException e) {
                alertMainDraw.init("Ошибка загрузки!", "Не удалось обновить скин..");
            }
        } else if (textureResponse.getTexture() == TextureType.CAPE){
            try {
                BufferedImage bufferedImage = ImageIO.read(new URL(API + "/image/" + textureResponse.getHash()));
                textureDraw.setTextureCape(bufferedImage);
                alertMainDraw.init("Успешно!", "Ваш плащ был успешно установлен!");
            } catch (IOException e) {
                alertMainDraw.init("Ошибка загрузки!", "Не удалось обновить плащ..");
            }
        }

    }

    @Override
    public void onFailure(Call<TextureResponse> call, Throwable throwable) {
        alertRegDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
    }

}
