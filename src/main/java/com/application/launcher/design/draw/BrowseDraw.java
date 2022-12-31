package com.application.launcher.design.draw;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowseDraw {

    private String url;
    private String message;

    private final Pane pane;
    private final Label label;
    private final Label content;
    private final Button yes;
    private final Button no;

    private final AlertDraw alertDraw;

    public BrowseDraw(String url, String message, Pane pane, Label label, Label content, Button yes, Button no, AlertDraw alertDraw) {
        this.url = url;
        this.message = message;
        this.pane = pane;
        this.label = label;
        this.content = content;
        this.yes = yes;
        this.no = no;

        this.alertDraw = alertDraw;

        setOnMouseEntered();
        setOnMouseExited();
        setOnMouseClicked();
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOnMouseEntered(){
        yes.setOnMouseEntered(event -> yes.setStyle("-fx-background-color: #0e310e; -fx-background-radius: 5px"));
        no.setOnMouseEntered(event -> no.setStyle("-fx-background-color: #500404; -fx-background-radius: 5px"));
    }

    public void setOnMouseExited(){
        yes.setOnMouseExited(event -> yes.setStyle("-fx-background-color: #134213; -fx-background-radius: 5px"));
        no.setOnMouseExited(event -> no.setStyle("-fx-background-color: #8c0606; -fx-background-radius: 5px"));
    }

    public void setOnMouseClicked() {
        yes.setOnMouseClicked(event -> {
            try {
                if(Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(url));
                } else {
                    alertDraw.init("Не поддерживается", "Невозможно перейти по ссылке");
                }
            } catch (IOException | URISyntaxException e) {
                alertDraw.init("Произошла ошибка", "Невозможно перейти по ссылке");
            }
            pane.setVisible(false);
        });
        no.setOnMouseClicked(event -> pane.setVisible(false));
    }

    public void init(){
        Platform.runLater(() -> {

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);

            label.setText(url);
            content.setText(message);

            pane.setVisible(true);
            fadeTransition.play();

        });
    }
}
