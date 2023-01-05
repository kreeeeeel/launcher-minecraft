package com.application.launcher.design.pane;

import com.application.launcher.design.button.PlayButton;
import com.application.launcher.design.button.PlayersButton;
import com.application.launcher.design.draw.PlayersDraw;
import com.application.launcher.design.image.ServerIconImage;
import com.application.launcher.design.label.ServerLabel;
import com.application.launcher.query.MCQuery;
import com.application.launcher.rest.response.ServerResponse;
import com.application.launcher.service.LauncherService;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class ServerPane {
    private final ServerResponse serverResponse;
    private final LauncherService launcherService;
    private final PlayersDraw playersDraw;

    public ServerPane(ServerResponse serverResponse, LauncherService launcherService, PlayersDraw playersDraw) {
        this.serverResponse = serverResponse;
        this.launcherService = launcherService;
        this.playersDraw = playersDraw;
    }

    public Pane getPane() {

        Pane pane = new Pane();
        pane.setPrefSize(673, 136);
        pane.setLayoutX(65);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 5px");

        pane.setOnMouseEntered(event -> pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 5px"));
        pane.setOnMouseExited(event -> pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 5px"));

        ServerIconImage serverIconImage = new ServerIconImage();
        ServerLabel serverLabel = new ServerLabel();

        MCQuery mcQuery = new MCQuery(serverResponse.getIp(), serverResponse.getPort());

        Circle iconCircle = serverIconImage.getCircle(serverResponse.getIcon());
        Label titleLabel = serverLabel.getLabel(serverResponse.getTitle(), "#dadada", 131, 14, 17);

        ImageView versionImageView = serverIconImage.getIcon("version", 130, 40);
        Label versionLabel = serverLabel.getLabel("Версия  " + serverResponse.getVersion(), "#c6c6c6", 157, 41, 15);

        ImageView pvpImageView = serverIconImage.getIcon("pvp", 130, 65);
        Label pvpLabel = serverLabel.getLabel("PVP: " + serverResponse.getPvp(), "#c6c6c6", 157, 66, 15);

        ImageView playersImageView = serverIconImage.getIcon("players", 130, 90);
        Label playersLabel = serverLabel.getLabel(mcQuery.fullStat().getOnlinePlayers() + " игроков", "#c6c6c6", 157, 91, 15);

        ImageView worldImageView = serverIconImage.getIcon("world", 375, 40);
        Label worldLabel = serverLabel.getLabel(serverResponse.getSize(), "#c6c6c6", 402, 41, 15);

        ImageView startImageView = serverIconImage.getIcon("start", 375, 65);
        Label startLabel = serverLabel.getLabel(serverResponse.getStart(), "#c6c6c6", 402, 66, 15);

        Label wipeLabel = serverLabel.getLabel("Вайп: " + serverResponse.getWipe(), "#8d8d8d", 498, 16, 14);
        ImageView markImageView = serverIconImage.getMark(serverResponse.getMark());

        PlayButton playButton = new PlayButton(launcherService);
        playButton.setOnMouseEntered();
        playButton.setOnMouseExited();
        playButton.setOnMouseClicked(serverResponse.getClient());

        PlayersButton playersButton = new PlayersButton(playersDraw);
        playersButton.setOnMouseEntered();
        playersButton.setOnMouseExited();
        playersButton.setOnMouseClicked(serverResponse.getTitle(), serverResponse.getIp(), serverResponse.getPort());

        pane.getChildren().addAll(iconCircle, titleLabel, versionLabel, versionImageView, pvpImageView, pvpLabel, playersImageView, playersLabel, startImageView, startLabel, worldLabel, worldImageView, wipeLabel, markImageView, playButton.getButton(), playersButton.getButton());

        return pane;
    }
}
