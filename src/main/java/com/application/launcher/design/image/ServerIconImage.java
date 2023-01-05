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

        FileUtils fileUtils = new FileUtils();
        ImageView markImage = new ImageView();

        String name =
                mark.equals("TEST") ? "test" :
                    mark.equals("BETA") ? "beta" :
                        mark.equals("OBT") ? "obt" :
                                mark.equals("WIPE") ? "wipe" :
                                        "new";

        markImage.setImage(new Image(fileUtils.getResource(name)));
        markImage.setLayoutX(92);
        markImage.setLayoutY(102);
        //markImage.setFitWidth(30);
        //markImage.setFitHeight(14);
        return markImage;
    }

    public ImageView getIcon(String name, int x, int y) {
        FileUtils fileUtils = new FileUtils();
        ImageView imageView = new ImageView(new Image(fileUtils.getResource(name)));
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        return imageView;
    }

}
