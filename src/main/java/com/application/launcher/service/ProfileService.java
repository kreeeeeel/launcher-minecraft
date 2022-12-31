package com.application.launcher.service;

import com.application.launcher.design.draw.AlertDraw;
import com.application.launcher.design.draw.ServersDraw;
import com.application.launcher.design.image.PhotoImage;
import com.application.launcher.design.label.ProfileLabel;
import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.api.ProfileApi;
import com.application.launcher.rest.response.ProfileResponse;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileService {

    private final Label login;
    private final Label balance;

    private final ImageView settings;
    private final ImageView ruble;

    private final AnchorPane anchorPane;

    private final Circle circle;
    private final Pane pane;
    private final AlertDraw alertDraw;
    private final LauncherService launcherService;

    public ProfileService(Label login, Label balance, ImageView settings, ImageView ruble, AnchorPane anchorPane, Circle circle, Pane pane, AlertDraw alertDraw, LauncherService launcherService) {
        this.login = login;
        this.balance = balance;
        this.settings = settings;
        this.ruble = ruble;
        this.anchorPane = anchorPane;
        this.circle = circle;
        this.pane = pane;
        this.alertDraw = alertDraw;
        this.launcherService = launcherService;
    }

    public void init() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Platform.runLater(() -> pane.setVisible(true));
            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();

            ProfileApi profileApi = RetrofitUtils.getRetrofit().create(ProfileApi.class);
            profileApi.getProfile(token).enqueue(new ProfileExecutor());
        });
    }

    class ProfileExecutor implements Callback<ProfileResponse> {

        @Override
        public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
            if (!response.isSuccessful()) {
                alertDraw.init("Ошибка", "Произошла ошибка, при получении данных о пользователе..");
                return;
            }

            ProfileResponse profileResponse = response.body();
            if (profileResponse == null) {
                alertDraw.init("Ошибка авторизации!", "Произошла ошибка, нет полученных данных");
                return;
            }

            Platform.runLater(() -> {

                PhotoImage photoImage = new PhotoImage(circle);
                photoImage.setCircle(profileResponse);

                ProfileLabel profileLabel = new ProfileLabel(login, balance, settings, ruble);
                profileLabel.setLogin(profileResponse);
                profileLabel.setBalance(profileResponse);

                ServersDraw serversDraw = new ServersDraw(anchorPane, launcherService);
                serversDraw.setServers(profileResponse.getServers());

                pane.setVisible(false);
            });
        }

        @Override
        public void onFailure(Call call, Throwable throwable) {
            alertDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
        }
    }
}
