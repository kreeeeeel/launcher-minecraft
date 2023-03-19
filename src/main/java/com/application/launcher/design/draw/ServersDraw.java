package com.application.launcher.design.draw;

import com.application.launcher.design.image.ServerNotFoundImage;
import com.application.launcher.design.label.ServerNotFoundLabel;
import com.application.launcher.design.pane.ServerPane;
import com.application.launcher.rest.response.ServerResponse;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ServersDraw {

    private final PlayersDraw playersDraw;
    private final AnchorPane anchorPane;

    public ServersDraw(PlayersDraw playersDraw, AnchorPane anchorPane) {
        this.playersDraw = playersDraw;
        this.anchorPane = anchorPane;
    }

    public void setServers(ServerResponse[] servers) {

        anchorPane.getChildren().clear();
        if (servers == null || servers.length == 0) {
            notFound();
            return;
        }

        int count = 0;
        for (ServerResponse serverResponse : servers) {

            ServerPane serverPane = new ServerPane(serverResponse, playersDraw);
            Pane pane = serverPane.getPane();
            pane.setLayoutY(8 + 143 * count);

            anchorPane.getChildren().add(pane);
            count += 1;

        }
        anchorPane.setPrefHeight(Math.max(494, 8 + 143 * count));
    }

    public void notFound() {

        Platform.runLater(() -> {

            ImageView notFoundImageView = new ImageView();
            Label notFoundTitleLabel = new Label();
            Label notFoundDescriptionLabel = new Label();

            ServerNotFoundImage serverNotFoundImage = new ServerNotFoundImage(notFoundImageView);
            serverNotFoundImage.setImageView();

            ServerNotFoundLabel serverNotFoundLabel = new ServerNotFoundLabel(notFoundTitleLabel, notFoundDescriptionLabel);
            serverNotFoundLabel.setTitle();
            serverNotFoundLabel.setDescription();

            anchorPane.getChildren().addAll(notFoundImageView, notFoundTitleLabel, notFoundDescriptionLabel);
            anchorPane.setPrefHeight(494);

        });

    }
}
