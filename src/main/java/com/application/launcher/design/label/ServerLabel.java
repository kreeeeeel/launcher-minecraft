package com.application.launcher.design.label;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ServerLabel {

    public Label getLabel(String name, String color, int x, int y, int size) {
        Text text = new Text(name);
        text.setFont(Font.font("Franklin Gothic Medium", size));

        Label label = new Label(name);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());
        label.setTextFill(Paint.valueOf(color));
        label.setFont(Font.font("Franklin Gothic Medium", size));
        return label;
    }

}
