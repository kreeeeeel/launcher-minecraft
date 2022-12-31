package com.application.launcher.design.image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ExitImage {

    private final ImageView imageView;

    public ExitImage(ImageView imageView) {
        this.imageView = imageView;

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
        imageView.setOnMouseClicked(event -> System.exit(1));
    }

}
