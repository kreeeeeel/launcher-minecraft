package com.application.launcher.design.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class PlayerPane {
    private final String name;

    public PlayerPane(String name) {
        this.name = name;
    }

    public Pane getPane(){
        Pane pane = new Pane();
        pane.setLayoutX(14);
        pane.setPrefSize(282, 45);
        pane.setStyle("-fx-background-radius: 5px; -fx-background-color: #ffffff");

        Label label = new Label(name);
        label.setLayoutX(18);
        label.setLayoutY(12);
        label.setPrefSize(218, 22);
        label.setFont(Font.font("Franklin Gothic Medium", 18));
        label.setTextFill(Paint.valueOf("#464646"));

        pane.getChildren().addAll(label);
        return pane;
    }
}
