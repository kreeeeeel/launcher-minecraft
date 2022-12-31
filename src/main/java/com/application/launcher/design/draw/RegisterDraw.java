package com.application.launcher.design.draw;

import com.application.launcher.service.RegisterService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class RegisterDraw {
    private final TextField email;
    private final TextField login;
    private final TextField name;
    private final PasswordField password;
    private final PasswordField passwordConfirm;
    private final Button button;

    private final Label title;
    private final Pane pane;

    private final AlertDraw alertDraw;

    public RegisterDraw(TextField email, TextField login, TextField name, PasswordField password, PasswordField passwordConfirm, Button button, Label title, Pane pane, AlertDraw alertDraw) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.button = button;
        this.title = title;
        this.pane = pane;
        this.alertDraw = alertDraw;
    }

    public void setOnMouseEntered() {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #0d6f34"));
    }

    public void setOnMouseExited() {
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #099e47"));
    }

    public void setOnMouseClicked() {
        button.setOnMouseClicked(event -> registerService());
    }

    public void actionFields() {
        email.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB){
                email.setFocusTraversable(false);
                login.setFocusTraversable(true);
            }
            if (event.getCode() == KeyCode.ENTER){
                registerService();
            }
        });

        login.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB){
                login.setFocusTraversable(false);
                name.setFocusTraversable(true);
            }
            if (event.getCode() == KeyCode.ENTER){
                registerService();
            }
        });

        name.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB){
                name.setFocusTraversable(false);
                password.setFocusTraversable(true);
            }
            if (event.getCode() == KeyCode.ENTER){
                registerService();
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB){
                password.setFocusTraversable(false);
                passwordConfirm.setFocusTraversable(true);
            }
            if (event.getCode() == KeyCode.ENTER){
                registerService();
            }
        });

        passwordConfirm.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB){
                passwordConfirm.setFocusTraversable(false);
                email.setFocusTraversable(true);
            }
            if (event.getCode() == KeyCode.ENTER){
                registerService();
            }
        });
    }

    public void registerService() {
        RegisterService registerService = new RegisterService(email, login, name, password, passwordConfirm, title, pane, alertDraw);
        registerService.init();
    }
}
