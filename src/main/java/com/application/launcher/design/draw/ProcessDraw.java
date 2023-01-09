package com.application.launcher.design.draw;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ProcessDraw {
    private final Label label;
    private final Pane pane;
    private final AnchorPane anchorPane;
    private final ScrollPane scrollPane;
    private final Button button;

    public ProcessDraw(Label label, Pane pane, AnchorPane anchorPane, ScrollPane scrollPane, Button button) {
        this.label = label;
        this.pane = pane;
        this.anchorPane = anchorPane;
        this.scrollPane = scrollPane;
        this.button = button;
    }

    public void init() {
        Platform.runLater(() -> {
            label.setText("");
            anchorPane.setPrefHeight(394);

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
        Platform.runLater(() -> {
            label.setText(label.getText().isEmpty() ? message : label.getText() + System.lineSeparator() + message);

            Text text = new Text(label.getText());
            text.setFont(Font.font("Franklin Gothic Medium", 12));

            label.setPrefSize(text.getLayoutBounds().getWidth() + 50, text.getLayoutBounds().getHeight() + 50);
            anchorPane.setPrefSize(label.getPrefWidth(), label.getPrefHeight());

            scrollPane.setVvalue(scrollPane.getVmax());

        });
    }
}
