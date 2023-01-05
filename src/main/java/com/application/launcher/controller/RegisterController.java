package com.application.launcher.controller;

import com.application.launcher.Runner;
import com.application.launcher.design.draw.AlertDraw;
import com.application.launcher.design.draw.RegisterDraw;
import com.application.launcher.design.image.CollapseImage;
import com.application.launcher.design.image.ExitImage;
import com.application.launcher.design.label.AuthorizationLabel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterController extends Application {

    @FXML private ImageView collapseImg;
    @FXML private ImageView exitImg;
    @FXML private ImageView alertCloseImg;

    @FXML private Label alertMessage;
    @FXML private Label alertTitle;
    @FXML private Label authLabel;
    @FXML private Label registerTitle;

    @FXML private Pane alertPane;
    @FXML private Pane alertPaneMain;
    @FXML private Pane registerPane;
    @FXML private Pane loadPane;

    @FXML private TextField email;
    @FXML private TextField login;
    @FXML private TextField name;

    @FXML private PasswordField password;
    @FXML private PasswordField passwordConfirm;

    @FXML private Button register;

    private double stagePosX;
    private double stagePosY;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/register.fxml"));
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
        list.add(registerPane);

        AlertDraw alertDraw = new AlertDraw(list, alertPane, alertPaneMain, alertCloseImg, alertTitle, alertMessage);

        ExitImage exitImage = new ExitImage(exitImg);
        exitImage.setOnMouseEntered();
        exitImage.setOnMouseExited();
        exitImage.setOnMouseClicked();

        CollapseImage collapseImage = new CollapseImage(collapseImg);
        collapseImage.setOnMouseEntered();
        collapseImage.setOnMouseExited();
        collapseImage.setOnMouseClicked();

        AuthorizationLabel authorizationLabel = new AuthorizationLabel(authLabel, loadPane, alertDraw);
        authorizationLabel.setOnMouseEntered();
        authorizationLabel.setOnMouseExited();
        authorizationLabel.setOnMouseClicked();

        RegisterDraw registerDraw = new RegisterDraw(email, login, name, password, passwordConfirm, register, registerTitle, registerPane, alertDraw);
        registerDraw.setOnMouseEntered();
        registerDraw.setOnMouseExited();
        registerDraw.setOnMouseClicked();
        registerDraw.actionFields();
    }
}
