package com.application.launcher.service;

import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.handler.RegisterHandler;
import com.application.launcher.rest.request.RegisterRequest;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.constant.Constant.*;
import static com.application.launcher.controller.RegisterController.alertRegDraw;

public class RegisterService {

    private final TextField email;
    private final TextField login;
    private final TextField name;
    private final PasswordField password;
    private final PasswordField passwordConfirm;

    private final Label title;
    private final Pane pane;

    public RegisterService(TextField email, TextField login, TextField name, PasswordField password, PasswordField passwordConfirm, Label title, Pane pane) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.title = title;
        this.pane = pane;
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
                alertRegDraw.init("Неверный формат", "Вы указали неверный формат почты..");
                return;
            }

            if(!username.matches("^[a-zA-Z0-9]+$")){
                alertRegDraw.init("Неверный формат", "Логин может содержать только символы и цифры..");
                return;
            }

            if (username.length() < LOGIN_MIN || username.length() > LOGIN_MAX) {
                alertRegDraw.init("Неверный формат", "Логин должен содержать от " + LOGIN_MIN + " до " + LOGIN_MAX + " символов..");
                return;
            }

            if(fullName.length() < NAME_MIN || fullName.length() > NAME_MAX) {
                alertRegDraw.init("Неверный формат", "Имя должно содержать от " + NAME_MIN + " до " + NAME_MAX + " символов..");
                return;
            }

            if(!pass.matches("(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")){
                alertRegDraw.init("Неверный формат", "Неверно указан формат пароля!");
                return;
            }

            if(!pass.equals(passConfirm)){
                alertRegDraw.init("Нет совпадений", "Ваши указанные пароли не совпадают..");
                return;
            }

            Platform.runLater(() -> {
                title.setText(username);
                pane.setVisible(true);
            });
            RegisterRequest registerRequest = new RegisterRequest(
                    fullName,
                    username,
                    mail,
                    pass
            );

            AuthApi authApi = RetrofitUtils.getRetrofit().create(AuthApi.class);
            authApi.register(registerRequest).enqueue(new RegisterHandler(username, pass));
        });
    }
}
