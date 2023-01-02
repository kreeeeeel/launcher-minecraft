package com.application.launcher.design.draw;

import com.application.launcher.design.pane.PlayerPane;
import com.application.launcher.query.MCQuery;
import com.application.launcher.query.QueryResponse;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayersDraw {
    private final Pane pane;
    private final Pane loadPane;
    private final AnchorPane anchorPane;
    private final ImageView imageView;
    private final Label label;
    private final AlertDraw alertDraw;

    private String name;
    private String ip = "localhost";
    private int port = 25565;
    private int countForX, countForY;

    public PlayersDraw(Pane pane, Pane loadPane, AnchorPane anchorPane, ImageView imageView, Label label, AlertDraw alertDraw) {
        this.pane = pane;
        this.loadPane = loadPane;
        this.anchorPane = anchorPane;
        this.imageView = imageView;
        this.label = label;
        this.alertDraw = alertDraw;
    }

    @Override
    public String toString(){
        return "pane: " + pane + " ip: " + ip + " port: " + port + " name: " + name;
    }

    public void setServer(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void init() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            Platform.runLater(() -> loadPane.setVisible(true));

            countForX = 0;
            countForY = 0;

            Platform.runLater(() -> anchorPane.getChildren().clear());
            MCQuery mcQuery = new MCQuery(ip, port);
            QueryResponse queryResponse = mcQuery.fullStat();

            if (queryResponse == null) {
                Platform.runLater(() -> alertDraw.init("Недоступен", "Не удалось получить данные.."));
                return;
            }

            if(queryResponse.getPlayerList() == null || queryResponse.getPlayerList().size() == 0){
                Platform.runLater(() -> alertDraw.init("Никого нет:(", "В данный момент на сервере нет игроков."));
                return;
            }

            for(String name : queryResponse.getPlayerList()) {

                PlayerPane playerPane = new PlayerPane(name);
                Pane player = playerPane.getPane();
                if (player == null){
                    continue;
                }

                player.setLayoutX(16 + 215 * countForX);
                player.setLayoutY(8 + 48 * countForY);

                if(countForX++ > 2) {
                    countForX = 0;
                    countForY++;
                }
                Platform.runLater(() -> anchorPane.getChildren().add(player));
            }
            Platform.runLater(() -> {
                label.setText(name + " | " + queryResponse.getOnlinePlayers() + " из " + queryResponse.getMaxPlayers());
                anchorPane.setPrefHeight(Math.max(387, 8 + 48 * countForY));
                loadPane.setVisible(false);

                FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
                fadeTransition.setFromValue(0.0);
                fadeTransition.setByValue(1.0);
                fadeTransition.setAutoReverse(true);

                pane.setVisible(true);
                fadeTransition.play();
            });
        });

    }

    public void setOnMouseEntered() {
        imageView.setOnMouseEntered(event -> imageView.setOpacity(1.0));
    }

    public void setOnMouseExited() {
        imageView.setOnMouseExited(event -> imageView.setOpacity(0.6));
    }

    public void setOnMouseClicked() {
        imageView.setOnMouseClicked(event -> pane.setVisible(false));
    }

    public void setName(String name) {
        this.name = name;
    }
}
