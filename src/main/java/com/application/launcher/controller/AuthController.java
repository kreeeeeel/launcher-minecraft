package com.application.launcher.controller;

import com.application.launcher.Runner;
import com.application.launcher.constant.Constant;
import com.application.launcher.design.draw.AlertDraw;
import com.application.launcher.design.draw.AuthDraw;
import com.application.launcher.design.draw.BrowseDraw;
import com.application.launcher.design.draw.FastAuthDraw;
import com.application.launcher.design.image.CollapseImage;
import com.application.launcher.design.image.ExitImage;
import com.application.launcher.design.label.RecoveryLabel;
import com.application.launcher.design.label.RegisterLabel;
import com.application.launcher.handler.RedirectHandler;
import com.application.launcher.service.AuthService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthController extends Application {

    @FXML private Button authBtn;
    @FXML private Button yesUrlBtn;
    @FXML private Button noUrlBtn;
    @FXML private Button fastAuthBtn;

    @FXML private Pane authPane;
    @FXML private Pane loadPane;
    @FXML private Pane alertPane;
    @FXML private Pane alertPaneMain;
    @FXML private Pane urlPane;
    @FXML private Pane fastAuthPane;

    @FXML private Label authTitle;
    @FXML private Label alertMessage;
    @FXML private Label alertTitle;
    @FXML private Label urlContent;
    @FXML private Label urlLabel;
    @FXML private Label recoveryLabel;
    @FXML private Label registerLabel;

    @FXML private ImageView collapseImg;
    @FXML private ImageView alertCloseImg;
    @FXML private ImageView exitImg;
    @FXML private ImageView fastAuthCloseImg;
    @FXML private ImageView fastAuthImg;

    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private AnchorPane fastAuthAnchor;

    private double stagePosX;
    private double stagePosY;

    public static AlertDraw alertAuthDraw;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setScene(scene);
        stage.show();

        // For Used to move the scene
        scene.setOnMousePressed(event -> {
            stagePosX = stage.getX() - event.getScreenX();
            stagePosY = stage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + stagePosX);
            stage.setY(event.getScreenY() + stagePosY);
        });
    }

    @FXML
    public void initialize() {

        List<Pane> list = new ArrayList<>();
        list.add(authPane);
        list.add(fastAuthPane);

        alertAuthDraw = new AlertDraw(list, alertPane, alertPaneMain, alertCloseImg, alertTitle, alertMessage);
        BrowseDraw recoveryDraw = new BrowseDraw(
                Constant.WEB + Constant.RECOVERY,
                "Вы действительно хотите перейти по ссылке для восстановления пароля?",
                urlPane, urlLabel, urlContent, yesUrlBtn, noUrlBtn
        );

        ExitImage exitImage = new ExitImage(exitImg);
        exitImage.setOnMouseEntered();
        exitImage.setOnMouseExited();
        exitImage.setOnMouseClicked();

        CollapseImage collapseImage = new CollapseImage(collapseImg);
        collapseImage.setOnMouseEntered();
        collapseImage.setOnMouseExited();
        collapseImage.setOnMouseClicked();

        RegisterLabel register = new RegisterLabel(registerLabel, loadPane);
        register.setOnMouseEntered();
        register.setOnMouseExited();
        register.setOnMouseClicked();

        RecoveryLabel recovery = new RecoveryLabel(recoveryLabel, recoveryDraw);
        recovery.setOnMouseEntered();
        recovery.setOnMouseExited();
        recovery.setOnMouseClicked();

        AuthDraw authDraw = new AuthDraw(login, password, authBtn, authTitle, authPane, fastAuthPane);
        authDraw.setOnMouseEntered();
        authDraw.setOnMouseExited();
        authDraw.setOnMouseClicked();
        authDraw.actionFields();

        FastAuthDraw fastAuthDraw = new FastAuthDraw(fastAuthImg, fastAuthCloseImg, fastAuthPane, fastAuthBtn, fastAuthAnchor, authTitle, authPane);
        fastAuthDraw.setOnMouseEntered();
        fastAuthDraw.setOnMouseExited();
        fastAuthDraw.setOnMouseClicked();

        if (RedirectHandler.register) {
            login.setText(RedirectHandler.username);
            password.setText(RedirectHandler.password);

            AuthService authService = new AuthService(RedirectHandler.username, RedirectHandler.password, authTitle, authPane, fastAuthPane);
            authService.init();

            RedirectHandler.username = null;
            RedirectHandler.password = null;
            RedirectHandler.register = false;
        }
    }
}
