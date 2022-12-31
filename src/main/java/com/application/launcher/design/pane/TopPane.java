package com.application.launcher.design.pane;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TopPane {
    private Pane top;

    private double stagePosX;
    private double stagePosY;

    public TopPane(Pane top) {
        this.top = top;
    }

    public void setOnMousePressed(){
        top.setOnMousePressed(event -> {
            Stage stage = (Stage) top.getScene().getWindow();
            stagePosX = stage.getX() - event.getScreenX();
            stagePosY = stage.getY() - event.getScreenY();
        });
    }

    public void setOnMouseDragged() {
        top.setOnMouseDragged(event -> {
            Stage stage = (Stage) top.getScene().getWindow();
            stage.setX(event.getScreenX() + stagePosX);
            stage.setY(event.getScreenY() + stagePosY);
        });
    }
}
