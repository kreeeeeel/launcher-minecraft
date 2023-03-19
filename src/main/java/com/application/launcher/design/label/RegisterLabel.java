package com.application.launcher.design.label;

import com.application.launcher.controller.RegisterController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.controller.AuthController.alertAuthDraw;

public class RegisterLabel {
    private final Label label;
    private final Pane load;

    public RegisterLabel(Label label, Pane load) {
        this.label = label;
        this.load = load;

        setOnMouseEntered();
        setOnMouseExited();
        setOnMouseClicked();
    }

    public void setOnMouseEntered(){
        label.setOnMouseEntered(event -> label.setTextFill(Paint.valueOf("#158640")));
    }

    public void setOnMouseExited(){
        label.setOnMouseExited(event -> label.setTextFill(Paint.valueOf("#23c363")));
    }

    public void setOnMouseClicked() {
        label.setOnMouseClicked(event -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> Platform.runLater(() -> {
                load.setVisible(true);
                try {
                    Stage stage = (Stage) label.getScene().getWindow();
                    RegisterController registerController = new RegisterController();
                    registerController.start(stage);
                } catch (IOException e) {
                    alertAuthDraw.init("Произошла ошибка", "Невозможно сменить сцену..");
                }
            }));
        });
    }
}
