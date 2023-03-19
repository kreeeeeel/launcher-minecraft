package com.application.launcher;

import com.application.launcher.controller.AuthController;
import com.application.launcher.utils.RetrofitUtils;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class Runner extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws URISyntaxException, IOException {
        primaryStage = stage;
        RetrofitUtils.generate();

        stage.setTitle("EnchantedCraft");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(Objects.requireNonNull(Runner.class.getResource("images/logo.png")).toURI().toString()));

        AuthController authController = new AuthController();
        authController.start(stage);
    }

    public static void main(String[] args) {

        try {
            long pidToKill = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-pid") && i < args.length - 1) {
                    pidToKill = Long.parseLong(args[i + 1]);
                    break;
                }
            }

            if (pidToKill != 0) {
                Process process = Runtime.getRuntime().exec(System.getProperty("os.name").startsWith("Windows") ? "taskkill /F /PID " + pidToKill : "kill " + pidToKill);
                process.waitFor();
            }
        } catch (IOException | InterruptedException ex){
            ex.printStackTrace();
        } finally {
            launch();
        }
    }

}
