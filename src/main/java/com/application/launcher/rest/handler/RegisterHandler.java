package com.application.launcher.rest.handler;

import com.application.launcher.controller.AuthController;
import com.application.launcher.handler.RedirectHandler;
import javafx.application.Platform;
import javafx.stage.Stage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

import static com.application.launcher.Runner.primaryStage;
import static com.application.launcher.controller.RegisterController.alertRegDraw;

public class RegisterHandler implements Callback<ResponseBody> {

    private final String username;
    private final String pass;

    public RegisterHandler(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            if(!response.isSuccessful()) {
                if (response.errorBody() != null) {
                    switch (response.errorBody().string()) {
                        case "Username is already taken!": {
                            alertRegDraw.init("Имя пользователя", "Даннное имя пользователя занято!");
                            break;
                        }
                        case "Email Address already in use!": {
                            alertRegDraw.init("E-mail адрес", "Даннный e-mail адрес занято!");
                            break;
                        }
                        default: {
                            alertRegDraw.init("Ошибка регистрации", "Произошла ошибка, попробуйте позже..");
                            break;
                        }
                    }
                }

                return;
            }

            Platform.runLater(() -> {
                try {
                    RedirectHandler.username = username;
                    RedirectHandler.password = pass;
                    RedirectHandler.register = true;

                    Stage stage = (Stage) primaryStage.getScene().getWindow();
                    AuthController authController = new AuthController();
                    authController.start(stage);

                } catch (IOException e) {
                    e.printStackTrace();
                    alertRegDraw.init("Произошла ошибка!", "Произошла ошибка в загрузке сцены!");
                }
            });

        } catch (NullPointerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        alertRegDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
    }

}
