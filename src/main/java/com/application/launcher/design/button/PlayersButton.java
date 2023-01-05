package com.application.launcher.design.button;

import com.application.launcher.design.draw.PlayersDraw;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class PlayersButton {

    private final Button button;
    private final PlayersDraw playersDraw;

    public PlayersButton(PlayersDraw playersDraw) {
        this.playersDraw = playersDraw;

        button = new Button();
    }

    public Button getButton() {

        button.setText("Список игроков");
        button.setLayoutX(374);
        button.setLayoutY(93);
        button.setPrefSize(152, 22);
        button.setStyle("-fx-background-color: #167288");
        button.setFont(Font.font("Franklin Gothic Medium", 17));
        button.setTextFill(Paint.valueOf("#dddddd"));
        button.setCursor(Cursor.HAND);
        button.setFocusTraversable(false);
        return button;

    }

    public void setOnMouseEntered(){
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #0e4957"));
    }

    public void setOnMouseExited(){
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #167288"));
    }

    public void setOnMouseClicked(String name, String ip, int port) {
        button.setOnMouseClicked(event -> {
            playersDraw.setName(name);
            playersDraw.setServer(ip, port);

            playersDraw.init();
        });
    }

}
