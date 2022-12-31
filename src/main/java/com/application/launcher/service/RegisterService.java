package com.application.launcher.service;

import com.application.launcher.controller.AuthController;
import com.application.launcher.controller.RegisterController;
import com.application.launcher.design.draw.AlertDraw;
import com.application.launcher.handler.RedirectHandler;
import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.request.AuthRequest;
import com.application.launcher.rest.request.RegisterRequest;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.utils.Constant.*;
import static com.application.launcher.utils.Constant.PASSWORD_MAX;

public class RegisterService {

    private final TextField email;
    private final TextField login;
    private final TextField name;
    private final PasswordField password;
    private final PasswordField passwordConfirm;

    private final Label title;
    private final Pane pane;

    private final AlertDraw alertDraw;

    public RegisterService(TextField email, TextField login, TextField name, PasswordField password, PasswordField passwordConfirm, Label title, Pane pane, AlertDraw alertDraw) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.title = title;
        this.pane = pane;
        this.alertDraw = alertDraw;
    }

    public void init() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            String mail = email.getText().trim();
            String username = login.getText().trim();
            String fullName = name.getText().trim();

            String pass = password.getText().trim();
            String passConfirm = passwordConfirm.getText().trim();

            if(!mail.matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")) {
                alertDraw.init("Неверный формат", "Вы указали неверный формат почты..");
                return;
            }

            if(!username.matches("^[a-zA-Z0-9]+$")){
                alertDraw.init("Неверный формат", "Логин может содержать только символы и цифры..");
                return;
            }

            if(fullName.length() < NAME_MIN || fullName.length() > NAME_MAX){
                alertDraw.init("Неверный формат", "Имя должно содержать от " + NAME_MIN + " до " + NAME_MAX + " символов..");
                return;
            }

            if(!pass.matches("(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")){
                alertDraw.init("Неверный формат", "Неверно указан формат пароля!");
                return;
            }

            if(!pass.equals(passConfirm)){
                alertDraw.init("Нет совпадений", "Ваши указанные пароли не совпадают..");
                return;
            }

            Platform.runLater(() -> pane.setVisible(true));
            RegisterRequest registerRequest = new RegisterRequest(
                    fullName,
                    username,
                    mail,
                    pass
            );

            AuthApi authApi = RetrofitUtils.getRetrofit().create(AuthApi.class);
            authApi.register(registerRequest).enqueue(new RegisterExecutor(username, pass));
        });
    }

    class RegisterExecutor implements Callback<ResponseBody> {

        private String username;
        private String pass;

        RegisterExecutor(String username, String pass) {
            this.username = username;
            this.pass = pass;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                if(!response.isSuccessful()) {
                    switch (response.errorBody().string()) {
                        case "Username is already taken!": {
                            alertDraw.init("Имя пользователя", "Даннное имя пользователя занято!");
                            break;
                        }
                        case "Email Address already in use!": {
                            alertDraw.init("E-mail адрес", "Даннный e-mail адрес занято!");
                            break;
                        }
                        default: {
                            alertDraw.init("Ошибка регистрации", "Произошла ошибка, попробуйте позже..");
                            break;
                        }
                    }

                    return;
                }

                Platform.runLater(() -> {
                    try {
                        RedirectHandler.username = username;
                        RedirectHandler.password = pass;
                        RedirectHandler.register = true;

                        Stage stage = (Stage) title.getScene().getWindow();
                        AuthController authController = new AuthController();
                        authController.start(stage);

                    } catch (IOException e) {
                        e.printStackTrace();
                        alertDraw.init("Произошла ошибка!", "Произошла ошибка в загрузке сцены!");
                    }
                });

            } catch (NullPointerException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            alertDraw.init("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
        }

    }
}
