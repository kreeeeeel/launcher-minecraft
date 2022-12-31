package com.application.launcher.design.label;

import com.application.launcher.design.draw.BrowseDraw;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class RecoveryLabel {

    private final Label label;
    private final BrowseDraw browseDraw;

    public RecoveryLabel(Label label, BrowseDraw browseDraw) {
        this.label = label;
        this.browseDraw = browseDraw;

        setOnMouseEntered();
        setOnMouseExited();
        setOnMouseClicked();
    }

    public void setOnMouseEntered() {
        label.setOnMouseEntered(event -> label.setTextFill(Paint.valueOf("#656363")));
    }

    public void setOnMouseExited() {
        label.setOnMouseExited(event -> label.setTextFill(Paint.valueOf("#b2b2b2")));
    }

    public void setOnMouseClicked() {
        label.setOnMouseClicked(event -> browseDraw.init());
    }
}
