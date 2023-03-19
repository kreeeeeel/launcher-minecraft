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

import static com.application.launcher.controller.MainController.alertMainDraw;

public class PlayersDraw {
    private final Pane pane;
    private final Pane loadPane;
    private final AnchorPane anchorPane;
    private final ImageView imageView;
    private final Label label;

    private String name;
    private String ip = "localhost";
    private int port = 25565;
    private int count = 0;

    public PlayersDraw(Pane pane, Pane loadPane, AnchorPane anchorPane, ImageView imageView, Label label) {
        this.pane = pane;
        this.loadPane = loadPane;
        this.anchorPane = anchorPane;
        this.imageView = imageView;
        this.label = label;
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

            count = 0;
            Platform.runLater(() -> {
                loadPane.setVisible(true);
                anchorPane.getChildren().clear();
            });

            MCQuery mcQuery = new MCQuery(ip, port);
            QueryResponse queryResponse = mcQuery.fullStat();
            mcQuery.close();

            if (queryResponse == null) {
                Platform.runLater(() -> alertMainDraw.init("Недоступен", "Не удалось получить данные.."));
                return;
            }

            if(queryResponse.getPlayerList() == null || queryResponse.getPlayerList().size() == 0){
                Platform.runLater(() -> alertMainDraw.init("Никого нет:(", "В данный момент на сервере нет игроков."));
                return;
            }

            for(String name : queryResponse.getPlayerList()) {

                PlayerPane playerPane = new PlayerPane(name);
                Pane player = playerPane.getPane();
                if (player == null){
                    continue;
                }

                player.setLayoutY(4 + 51 * count);
                Platform.runLater(() -> anchorPane.getChildren().add(player));
                count++;
            }

            Platform.runLater(() -> {
                label.setText(name);
                anchorPane.setPrefHeight(Math.max(387, 4 + 51 * count));
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
