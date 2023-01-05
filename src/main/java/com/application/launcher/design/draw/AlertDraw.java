package com.application.launcher.design.draw;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class AlertDraw {

    private final List<Pane> list;

    private final Pane pane;
    private final Pane alert;
    private final ImageView imageView;
    private final Label title;
    private final Label message;

    public AlertDraw(List<Pane> list, Pane pane, Pane alert, ImageView imageView, Label title, Label message) {
        this.list = list;
        this.pane = pane;
        this.alert = alert;
        this.imageView = imageView;
        this.title = title;
        this.message = message;

        setOnMouseEntered();
        setOnMouseExited();
        setOnMouseClicked();
    }

    public void setOnMouseEntered() {
        imageView.setOnMouseEntered(event -> imageView.setOpacity(1.0));
    }

    public void setOnMouseExited() {
        imageView.setOnMouseExited(event -> imageView.setOpacity(0.6));
    }

    public void setOnMouseClicked() {
        imageView.setOnMouseClicked(event -> pane.setVisible(false));
    }

    public void init(String alertTitle, String alertMessage) {
        Platform.runLater(() -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);

            for (Pane temp : list) temp.setVisible(false);

            Text text = new Text(alertMessage);
            text.setWrappingWidth(327);
            text.setFont(Font.font("Franklin Gothic Medium", 17));

            pane.setVisible(true);
            title.setText(alertTitle);
            message.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight() + 5);
            message.setText(alertMessage);

            alert.setPrefHeight(message.getPrefHeight() + 68);
            fadeTransition.play();
        });
    }

}
