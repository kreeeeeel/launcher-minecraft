package com.application.launcher.design.button;

import com.application.launcher.service.LauncherService;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class PlayButton {

    private final Button button;
    private final LauncherService launcherService;

    public PlayButton(LauncherService launcherService) {
        this.launcherService = launcherService;
        button = new Button();
    }

    public Button getButton() {

        button.setText("Начать игру");
        button.setLayoutX(532);
        button.setLayoutY(93);
        button.setPrefSize(131, 29);
        button.setStyle("-fx-background-color: #227322");
        button.setFont(Font.font("Franklin Gothic Medium", 17));
        button.setTextFill(Paint.valueOf("#dddddd"));
        button.setCursor(Cursor.HAND);
        button.setFocusTraversable(false);
        return button;

    }

    public void setOnMouseEntered(){
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #1a571a"));
    }

    public void setOnMouseExited(){
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #227322"));
    }

    public void setOnMouseClicked(String launcher) {
        button.setOnMouseClicked(event -> {
            launcherService.setLauncher(launcher);
            launcherService.init();
        });
    }

}
