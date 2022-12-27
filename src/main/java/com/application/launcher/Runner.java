package com.application.launcher;

import com.application.launcher.controller.AuthController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;

public class Runner extends Application {

    @Override
    public void start(Stage stage) throws URISyntaxException, IOException {
        stage.setTitle("Title");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(Runner.class.getResource("images/logo.png").toURI().toString()));

        AuthController authController = new AuthController();
        authController.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}
