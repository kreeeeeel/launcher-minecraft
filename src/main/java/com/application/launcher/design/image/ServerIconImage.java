package com.application.launcher.design.image;

import com.application.launcher.utils.FileUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import static com.application.launcher.constant.Constant.ICONS;
import static com.application.launcher.constant.Constant.API;

public class ServerIconImage {

    public Circle getCircle(String path) {
        Circle circle = new Circle();
        circle.setLayoutX(65);
        circle.setLayoutY(65);
        circle.setRadius(55);
        circle.setFill(new ImagePattern(new Image(API + ICONS + path)));
        circle.setStroke(Paint.valueOf("black"));
        return circle;
    }

    public ImageView getMark(String mark) {

        ImageView markImage = new ImageView();

        markImage.setImage(new Image(FileUtils.getResource(mark)));
        markImage.setLayoutX(92);
        markImage.setLayoutY(102);
        return markImage;
    }

    public ImageView getIcon(String name, int x, int y) {
        ImageView imageView = new ImageView(new Image(FileUtils.getResource(name)));
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        return imageView;
    }

}
