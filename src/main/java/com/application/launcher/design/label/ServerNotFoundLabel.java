package com.application.launcher.design.label;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ServerNotFoundLabel {
    private final Label title;
    private final Label description;

    public ServerNotFoundLabel(Label title, Label description) {
        this.title = title;
        this.description = description;
    }

    public void setTitle() {
        title.setText("Не найдено серверов.");
        title.setPrefSize(216, 25);
        title.setLayoutX(285);
        title.setLayoutY(223);
        title.setFont(Font.font("Franklin Gothic Medium", 22));
        title.setTextFill(Paint.valueOf("#e1e1e1"));
    }

    public void setDescription() {
        description.setText("В данный момент, не найдено серверов на которых вы бы смогли поиграть(");
        description.setPrefSize(324, 44);
        description.setLayoutX(231);
        description.setLayoutY(248);
        description.setFont(Font.font("Franklin Gothic Medium", 18));
        description.setTextFill(Paint.valueOf("#a4a1a1"));
        description.setTextAlignment(TextAlignment.CENTER);
        description.setWrapText(true);
    }
}
