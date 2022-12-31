package com.application.launcher.design.image;

import com.application.launcher.rest.response.ProfileResponse;
import com.application.launcher.utils.Constant;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;

import static com.application.launcher.utils.Constant.PHOTO;

public class PhotoImage {
    private final Circle circle;

    public PhotoImage(Circle circle) {
        this.circle = circle;
    }

    public void setCircle(ProfileResponse profileResponse) {
        try {
            Image image = new Image(new URL(Constant.URL + PHOTO + profileResponse.getLogin() + ".png").openStream());
            circle.setFill(new ImagePattern(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
