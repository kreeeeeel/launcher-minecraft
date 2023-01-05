package com.application.launcher.design.pane;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;

import static com.application.launcher.constant.Constant.API;
import static com.application.launcher.constant.Constant.PHOTO;

public class PlayerPane {
    private final String name;

    public PlayerPane(String name) {
        this.name = name;
    }

    public Pane getPane(){
        try {
            Pane pane = new Pane();
            pane.setLayoutX(14);
            pane.setPrefSize(282, 45);
            pane.setStyle("-fx-background-radius: 5px; -fx-background-color: #ffffff");

            Circle circle = new Circle();
            circle.setLayoutX(26);
            circle.setLayoutY(23);
            circle.setRadius(19);
            circle.setFill(new ImagePattern(new Image(new URL(API + PHOTO + name + ".png").openStream())));
            circle.setStroke(Paint.valueOf("gray"));

            Label label = new Label(name);
            label.setLayoutX(55);
            label.setLayoutY(12);
            label.setPrefSize(218, 22);
            label.setFont(Font.font("Franklin Gothic Medium", 18));
            label.setTextFill(Paint.valueOf("#464646"));

            pane.getChildren().addAll(circle, label);
            return pane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
