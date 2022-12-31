package com.application.launcher.design.image;

import com.application.launcher.utils.FileUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.application.launcher.constant.Constant.ICONS;
import static com.application.launcher.constant.Constant.API;

public class ServerIconImage {

    public ImageView getImage(String path) {
        ImageView icon = new ImageView();
        icon.setImage(new Image(API + ICONS + path));
        icon.setLayoutX(14);
        icon.setLayoutY(13);
        icon.setFitWidth(150);
        icon.setFitHeight(150);
        return icon;
    }

    public ImageView getIcon(String name, int x, int y) {
        FileUtils fileUtils = new FileUtils();
        ImageView imageView = new ImageView(new Image(fileUtils.getResource(name)));
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

}
