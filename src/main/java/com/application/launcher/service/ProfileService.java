package com.application.launcher.service;

import com.application.launcher.design.draw.PlayersDraw;
import com.application.launcher.design.draw.ServersDraw;
import com.application.launcher.design.label.ProfileLabel;
import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.api.ProfileApi;
import com.application.launcher.rest.response.ProfileResponse;
import com.application.launcher.rest.response.TextureType;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.constant.Constant.API;
import static com.application.launcher.controller.MainController.alertMainDraw;
import static com.application.launcher.controller.MainController.textureDraw;

public class ProfileService {

    private final Label login;
    private final Label balance;

    private final ImageView settings;
    private final ImageView ruble;

    private final AnchorPane anchorPane;

    private final Pane pane;

    private final PlayersDraw playersDraw;

    public ProfileService(Label login, Label balance, ImageView settings, ImageView ruble, AnchorPane anchorPane, Pane pane, PlayersDraw playersDraw) {
        this.login = login;
        this.balance = balance;
        this.settings = settings;
        this.ruble = ruble;
        this.anchorPane = anchorPane;
        this.pane = pane;
        this.playersDraw = playersDraw;
    }

    public void init() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            pane.setVisible(true);
            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();

            ProfileApi profileApi = RetrofitUtils.getRetrofit().create(ProfileApi.class);
            profileApi.getProfile(token).enqueue(new ProfileExecutor());
        });
    }

    class ProfileExecutor implements Callback<ProfileResponse> {

        @Override
        public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
            if (!response.isSuccessful()) {
                alertMainDraw.init("Ошибка", "Произошла ошибка, при получении данных о пользователе..");
                return;
            }

            ProfileResponse profileResponse = response.body();
            if (profileResponse == null) {
                alertMainDraw.init("Ошибка авторизации!", "Произошла ошибка, нет полученных данных");
                return;
            }

            Platform.runLater(() -> {
                textureDraw.setUsername(profileResponse.getLogin());

                ProfileLabel profileLabel = new ProfileLabel(login, balance, settings, ruble);
                profileLabel.setLogin(profileResponse);
                profileLabel.setBalance(profileResponse);

                ServersDraw serversDraw = new ServersDraw(playersDraw, anchorPane);
                serversDraw.setServers(profileResponse.getServers());

                Map<TextureType, String> textures = profileResponse.getTextures();
                for(Map.Entry<TextureType, String> entry : textures.entrySet()){
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new URL(API + "/image/" + entry.getValue()));
                        switch (entry.getKey()){
                            case SKIN:
                                textureDraw.setTextureSkin(bufferedImage);
                                break;
                            case CAPE:
                                textureDraw.setTextureCape(bufferedImage);
                                break;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                pane.setVisible(false);
            });
        }

        @Override
        public void onFailure(Call call, Throwable throwable) {
            alertMainDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
        }
    }
}
