package com.application.launcher.design.draw;

import com.application.launcher.Runner;
import com.application.launcher.entity.AccountEntity;
import com.application.launcher.entity.ConfigEntity;
import com.application.launcher.handler.AccountHandler;
import com.application.launcher.service.AuthService;
import com.application.launcher.utils.ConfigUtils;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.controller.AuthController.alertAuthDraw;

public class FastAuthDraw {

    private final ImageView openImageView;
    private final ImageView closeImageView;
    private final Pane pane;
    private final Button button;
    private final AnchorPane anchorPane;

    private final Label titleAuth;
    private final Pane paneAuth;

    private int count = 0;

    public FastAuthDraw(ImageView openImageView, ImageView closeImageView, Pane pane, Button button, AnchorPane anchorPane, Label titleAuth, Pane paneAuth) {
        this.openImageView = openImageView;
        this.closeImageView = closeImageView;
        this.pane = pane;
        this.button = button;
        this.anchorPane = anchorPane;
        this.titleAuth = titleAuth;
        this.paneAuth = paneAuth;
    }

    public void setOnMouseEntered() {
        closeImageView.setOnMouseEntered(event -> closeImageView.setOpacity(1.0));
        openImageView.setOnMouseEntered(event -> openImageView.setOpacity(1.0));

        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #0b280b; -fx-background-radius: 5px"));
    }

    public void setOnMouseExited() {
        closeImageView.setOnMouseExited(event -> closeImageView.setOpacity(0.6));
        openImageView.setOnMouseExited(event -> openImageView.setOpacity(0.5));

        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #123f12; -fx-background-radius: 5px"));
    }

    public void setOnMouseClicked() {
        closeImageView.setOnMouseClicked(event -> pane.setVisible(false));
        openImageView.setOnMouseClicked(event -> {
            ConfigUtils configUtils = new ConfigUtils();
            configUtils.init();
            draw(configUtils.getConfigEntity(), true);
        });
    }

    public void draw(ConfigEntity configEntity, boolean animation) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            button.setDisable(true);
            count = 0;

            AccountHandler.pane = null;
            AccountHandler.usernameLabel = null;
            AccountHandler.lastStartLabel = null;

            AccountHandler.username = null;
            AccountHandler.password = null;

            if (configEntity == null ||  configEntity.getAccounts() == null || configEntity.getAccounts().size() == 0) {
                alertAuthDraw.init("Пусто :(", "У вас нет сохраненных аккаунтов..");
                return;
            }

            Platform.runLater(() -> anchorPane.getChildren().clear());
            for (AccountEntity accountEntity : configEntity.getAccounts()) {
                Platform.runLater(() -> {
                    Pane paneUser = getPane(accountEntity);
                    Label label = getLabel(accountEntity.getUsername());
                    Label labelLast = getLast(accountEntity.getLast());
                    ImageView imageView = getRemoveImage(configEntity, accountEntity);

                    paneUser.getChildren().addAll(labelLast, label, imageView);
                    anchorPane.getChildren().add(paneUser);
                });
                count++;
            }

            if (count == 0) {
                alertAuthDraw.init("Пусто :(", "У вас нет сохраненных аккаунтов..");
            }

            if (animation) {
                anchorPane.setPrefHeight(Math.max(count * 50, 304));

                FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
                fadeTransition.setFromValue(0.0);
                fadeTransition.setByValue(1.0);
                fadeTransition.setAutoReverse(true);

                pane.setVisible(true);
                fadeTransition.play();
            }
        });

    }

    public Pane getPane(AccountEntity accountEntity) {

        Pane paneG = new Pane();
        paneG.setPrefSize(300, 45);
        paneG.setLayoutX(13);
        paneG.setLayoutY( (count-1) * 50 );
        paneG.setStyle("-fx-background-color: white; -fx-background-radius: 5px");
        paneG.setCursor(Cursor.HAND);

        paneG.setOnMouseEntered(event ->
                paneG.setStyle("-fx-background-radius: 5px;-fx-background-color: " + ((paneG == AccountHandler.pane) ? "#217fdc" : "#b9b8b8")));
        paneG.setOnMouseExited(event ->
                paneG.setStyle("-fx-background-radius: 5px;-fx-background-color: " + ((paneG == AccountHandler.pane) ? "#2388ec" : "white")));
        paneG.setOnMouseClicked(event -> {

            if (AccountHandler.pane != null) {
                AccountHandler.usernameLabel.setTextFill(Paint.valueOf("#544646"));
                AccountHandler.lastStartLabel.setTextFill(Paint.valueOf("#493d3d"));
                AccountHandler.pane.setStyle("-fx-background-radius: 5px;-fx-background-color: white");
            }

            paneG.setStyle("-fx-background-radius: 5px;-fx-background-color: #217fdc");

            AccountHandler.username = accountEntity.getUsername();
            AccountHandler.password = accountEntity.getPassword();

            AccountHandler.pane = paneG;
            AccountHandler.usernameLabel = (Label) paneG.getChildren().get(1);
            AccountHandler.lastStartLabel = (Label) paneG.getChildren().get(0);

            AccountHandler.usernameLabel.setTextFill(Paint.valueOf("#f8f8f8"));
            AccountHandler.lastStartLabel.setTextFill(Paint.valueOf("#f8f8f8"));

            button.setDisable(false);
            button.setOnMouseClicked(mouse -> {
                AuthService authService = new AuthService(accountEntity.getUsername(), accountEntity.getPassword(), titleAuth, paneAuth, pane);
                authService.init();
            });
        });
        return paneG;
    }


    public Label getLabel(String username) {
        Label label = new Label(username);
        label.setPrefSize(208, 24);
        label.setLayoutX(14);
        label.setLayoutY(4);
        label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
        label.setTextFill(Paint.valueOf("#544646"));
        return label;
    }

    public Label getLast(Long date) {
        Label label = new Label("Последний запуск: " + ( date != null ? new SimpleDateFormat("dd.MM.yyyy в HH:mm", new Locale("ru", "RU")).format(new Date(date)) : "Неизвестно"));
        label.setLayoutX(14);
        label.setLayoutY(24);
        label.setFont(Font.font("Arial", 13));
        label.setTextFill(Paint.valueOf("#493d3d"));
        return label;
    }

    public ImageView getRemoveImage(ConfigEntity configEntity, AccountEntity accountEntity) {
        try {
            ImageView imageView = new ImageView(Objects.requireNonNull(Runner.class.getResource("images/remove.png")).toURI().toString());
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            imageView.setLayoutX(262);
            imageView.setLayoutY(8);
            imageView.setOpacity(0.6);

            imageView.setOnMouseEntered(event -> imageView.setOpacity(1.0));
            imageView.setOnMouseExited(event -> imageView.setOpacity(0.6));
            imageView.setOnMouseClicked(event -> {
                configEntity.getAccounts().removeIf(account -> account.getUsername().equals(accountEntity.getUsername()));

                ConfigUtils configUtils = new ConfigUtils();
                configUtils.setConfigEntity(configEntity);
                configUtils.write();

                draw(configEntity, false);
            });
            return imageView;
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
