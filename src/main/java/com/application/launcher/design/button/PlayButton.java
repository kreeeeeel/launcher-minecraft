package com.application.launcher.design.button;

import com.application.launcher.service.LauncherService;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class PlayButton {

    private Button button;

    public PlayButton() {
        button = new Button();
    }

    public Button getButton() {

        button.setText("Играть");
        button.setLayoutX(636);
        button.setLayoutY(129);
        button.setPrefSize(113, 34);
        button.setStyle("-fx-background-color: #227322");
        button.setFont(Font.font("Franklin Gothic Medium", 18));
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

    public void setOnMouseClicked(LauncherService launcherService) {
        button.setOnMouseClicked(event -> launcherService.init());
    }

}
