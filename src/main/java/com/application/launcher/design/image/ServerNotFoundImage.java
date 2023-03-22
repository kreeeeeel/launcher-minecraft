package com.application.launcher.design.image;

import com.application.launcher.utils.FileUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ServerNotFoundImage {

    private final ImageView imageView;

    public ServerNotFoundImage(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setImageView() {
        Image image = new Image(FileUtils.getResource("shoked"));
        imageView.setImage(image);
        imageView.setLayoutX(345);
        imageView.setLayoutY(106);
        imageView.setFitWidth(96);
        imageView.setFitHeight(96);
    }

}
