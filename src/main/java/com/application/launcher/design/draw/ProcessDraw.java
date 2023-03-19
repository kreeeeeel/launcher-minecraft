package com.application.launcher.design.draw;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessDraw {
    private final Pane pane;
    private final AnchorPane anchorPane;
    private final ScrollPane scrollPane;
    private final Button button;

    private int useLimit;

    public ProcessDraw(Pane pane, AnchorPane anchorPane, ScrollPane scrollPane, Button button) {
        this.pane = pane;
        this.anchorPane = anchorPane;
        this.scrollPane = scrollPane;
        this.button = button;
    }

    public void init() {
        Platform.runLater(() -> {
            useLimit = 0;
            anchorPane.getChildren().clear();
            anchorPane.setPrefHeight(0);

            pane.setVisible(true);
        });
    }

    public void close(){
        Platform.runLater(() -> pane.setVisible(false));
    }

    public void setOnMouseClicked(Process process) {
        button.setOnMouseClicked(event -> process.destroy());
    }

    public void setOnMouseEntered() {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #540202; -fx-background-radius: 5px"));
    }

    public void setOnMouseExited() {
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #8c0606; -fx-background-radius: 5px"));
    }

    public void input(String message) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> Platform.runLater(() -> {

            if (message.length() > anchorPane.getPrefWidth()) anchorPane.setPrefWidth(message.length());

            Text text = new Text(message);
            text.setFont(Font.font("Franklin Gothic Medium", 12));

            if (useLimit++ > 20 || anchorPane.getChildren().size() == 0) {
                Label label = new Label(message);
                label.setLayoutX(0);
                label.setLayoutY(anchorPane.getPrefHeight());
                label.setFont(Font.font("Franklin Gothic Medium", 12));
                label.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());

                anchorPane.getChildren().add(label);
                anchorPane.setPrefHeight(anchorPane.getPrefHeight() + label.getPrefHeight());
                useLimit = 0;
            } else {
                Label label = (Label) anchorPane.getChildren().get(anchorPane.getChildren().size() - 1);

                text.setText(label.getText() + System.lineSeparator() + message);
                label.setText(label.getText() + System.lineSeparator() + message);
                anchorPane.setPrefHeight(anchorPane.getPrefHeight() + text.getLayoutBounds().getHeight() - label.getPrefHeight() + 5);

                label.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight() + 5);
            }
            scrollPane.setVvalue(1.0);
        }));
    }
}
