package com.application.launcher.design.pane;

import com.application.launcher.design.button.MoreButton;
import com.application.launcher.design.button.PlayButton;
import com.application.launcher.design.draw.PlayersDraw;
import com.application.launcher.design.image.ServerIconImage;
import com.application.launcher.design.label.ServerLabel;
import com.application.launcher.rest.response.ServerResponse;
import com.application.launcher.service.LauncherService;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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
        pane.setPrefSize(763, 175);
        pane.setLayoutX(16);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 25px");

        ServerIconImage serverIconImage = new ServerIconImage();
        ServerLabel serverLabel = new ServerLabel();

        ImageView iconImageView = serverIconImage.getImage(serverResponse.getIcon());
        Label titleLabel = serverLabel.getLabel(serverResponse.getTitle(), "white", 176, 13, 18);

        ImageView versionImageView = serverIconImage.getIcon("version", 176, 42);
        Label versionLabel = serverLabel.getLabel("Версия: " + serverResponse.getVersion(), "#dddddd", 200, 43, 16);

        ImageView playersImageView = serverIconImage.getIcon("players", 176, 68);
        Label playersLabel = serverLabel.getLabel(serverResponse.getOnline() + " из " + serverResponse.getPlayers(), "#dddddd", 203, 69, 16);

        ImageView pvpImageView = serverIconImage.getIcon("pvp", 176, 94);
        Label pvpLabel = serverLabel.getLabel(serverResponse.getPvp(), "#dddddd", 203, 95, 16);

        ImageView sizeImageView = serverIconImage.getIcon("world", 176, 120);
        Label sizeLabel = serverLabel.getLabel(serverResponse.getSize(), "#dddddd", 203, 121, 16);

        ImageView startImageView = serverIconImage.getIcon("start", 176, 146);
        Label startLabel = serverLabel.getLabel(serverResponse.getStart(), "#dddddd", 203, 148, 16);

        ImageView wipeImageView = serverIconImage.getIcon("clear", 457, 42);
        Label wipeLabel = serverLabel.getLabel("Последний вайп: " + serverResponse.getWipe(), "#dddddd", 484, 43, 16);

        ImageView wipeOtherImageView = serverIconImage.getIcon("clear", 457, 68);
        Label wipeOtherLabel = serverLabel.getLabel("Вайп доп.миров: " + serverResponse.getWipeOther(), "#dddddd", 484, 69, 16);


        PlayButton playButton = new PlayButton(launcherService);
        playButton.setOnMouseEntered();
        playButton.setOnMouseExited();
        playButton.setOnMouseClicked(serverResponse.getClient());

        MoreButton moreButton = new MoreButton(playersDraw);
        moreButton.setOnMouseEntered();
        moreButton.setOnMouseExited();
        moreButton.setOnMouseClicked(serverResponse.getTitle(), serverResponse.getIp(), serverResponse.getPort());

        pane.getChildren().addAll(iconImageView, titleLabel, versionImageView, versionLabel, playersImageView, playersLabel, pvpImageView, pvpLabel,
                sizeImageView, sizeLabel, startImageView, startLabel, wipeImageView, wipeLabel, wipeOtherImageView, wipeOtherLabel, playButton.getButton(), moreButton.getButton());
        return pane;
    }
}
