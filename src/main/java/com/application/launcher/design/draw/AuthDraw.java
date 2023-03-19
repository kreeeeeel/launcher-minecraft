package com.application.launcher.design.draw;

import com.application.launcher.service.AuthService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class AuthDraw {

    private final TextField login;
    private final PasswordField password;
    private final Button button;

    private final Label title;
    private final Pane pane;
    private final Pane fast;

    public AuthDraw(TextField login, PasswordField password, Button button, Label title, Pane pane, Pane fast) {
        this.login = login;
        this.password = password;
        this.button = button;
        this.title = title;
        this.pane = pane;
        this.fast = fast;
    }

    public void actionFields() {
        login.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB){
                login.setFocusTraversable(false);
                password.setFocusTraversable(true);
            }

            if (event.getCode() == KeyCode.ENTER){
                authService();
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB){
                password.setFocusTraversable(false);
                login.setFocusTraversable(true);
            }

            if (event.getCode() == KeyCode.ENTER){
                authService();
            }
        });
    }

    public void setOnMouseEntered() {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #0d6f34"));
    }

    public void setOnMouseExited() {
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #099e47"));
    }

    public void setOnMouseClicked() {
        button.setOnMouseClicked(event -> authService());
    }

    public void authService() {
        String username = login.getText().trim();
        String pass = password.getText().trim();

        AuthService authService = new AuthService(username, pass, title, pane, fast);
        authService.init();
    }

}
