package com.application.launcher.design.image;

import com.application.launcher.constant.Constant;
import com.application.launcher.design.draw.BrowseDraw;
import com.application.launcher.utils.FileUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PayImage {

    private final ImageView imageView;
    private final BrowseDraw browseDraw;

    public PayImage(ImageView imageView, BrowseDraw browseDraw) {
        this.imageView = imageView;
        this.browseDraw = browseDraw;
    }

    public void setOnMouseEntered() {
        imageView.setOnMouseEntered(event -> imageView.setImage(new Image(FileUtils.getResource("pay"))));
    }

    public void setOnMouseExited() {
        imageView.setOnMouseExited(event -> imageView.setImage(new Image(FileUtils.getResource("balance"))));
    }

    public void setOnMouseClicked() {
        imageView.setOnMouseClicked(event -> {
            BrowseDraw browse = browseDraw;
            browse.setUrl(Constant.WEB + Constant.PAY);
            browse.setMessage("Вы действительно хотите перейти по ссылке для пополнения баланса?");

            browse.init();
        });
    }
}
