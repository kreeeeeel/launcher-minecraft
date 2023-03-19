package com.application.launcher.design.draw;

import com.application.launcher.controller.AuthController;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static com.application.launcher.controller.MainController.alertMainDraw;

public class ExitAccountDraw {
    private final Button button;
    private final Button exitBtn;
    private final Button cancelBtn;
    private final Pane pane;


    public ExitAccountDraw(Button button, Button exitBtn, Button cancelBtn, Pane pane) {
        this.button = button;
        this.exitBtn = exitBtn;
        this.cancelBtn = cancelBtn;
        this.pane = pane;
    }

    public void setOnMouseEntered() {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #500404; -fx-background-radius: 5px"));
        exitBtn.setOnMouseEntered(event -> exitBtn.setStyle("-fx-background-color: #540202; -fx-background-radius: 5px"));
        cancelBtn.setOnMouseEntered(event -> cancelBtn.setStyle("-fx-background-color: #044d64; -fx-background-radius: 5px"));
    }

    public void setOnMouseExited() {
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #8c0606; -fx-background-radius: 5px"));
        exitBtn.setOnMouseExited(event -> exitBtn.setStyle("-fx-background-color: #8c0606; -fx-background-radius: 5px"));
        cancelBtn.setOnMouseExited(event -> cancelBtn.setStyle("-fx-background-color: #067597; -fx-background-radius: 5px"));
    }

    public void setOnMouseClicked() {
        button.setOnMouseClicked(event -> {
            pane.setVisible(true);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
        });

        exitBtn.setOnMouseClicked(event -> {
            try {
                Stage stage = (Stage) exitBtn.getScene().getWindow();
                AuthController authController = new AuthController();
                authController.start(stage);
            } catch (IOException e) {
                alertMainDraw.init("Произошла ошибка!", "Попробуйте позже..");
            }
        });

        cancelBtn.setOnMouseClicked(event -> pane.setVisible(false));
    }
}
