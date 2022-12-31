package com.application.launcher.design.draw;

import com.application.launcher.entity.ConfigEntity;
import com.application.launcher.utils.ConfigUtils;
import com.application.launcher.utils.FileUtils;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsDraw {
    private final TextField textField;
    private final Label label;
    private final CheckBox checkBoxSize;
    private final CheckBox checkBoxAuto;

    private final Pane pane;
    private final Pane paneRemove;

    private final Button clear;
    private final Button folder;

    private final ImageView openImageView;
    private final ImageView closeImageView;
    private final AlertDraw alertDraw;

    public SettingsDraw(TextField textField, Label label, CheckBox checkBoxSize, CheckBox checkBoxAuto, Pane pane, Pane paneRemove, Button clear, Button folder, ImageView openImageView, ImageView closeImageView, AlertDraw alertDraw) {
        this.textField = textField;
        this.label = label;
        this.checkBoxSize = checkBoxSize;
        this.checkBoxAuto = checkBoxAuto;
        this.pane = pane;
        this.paneRemove = paneRemove;
        this.clear = clear;
        this.folder = folder;
        this.openImageView = openImageView;
        this.closeImageView = closeImageView;
        this.alertDraw = alertDraw;
    }

    public void init() {
        ConfigUtils configUtils = new ConfigUtils();
        ConfigEntity configEntity = configUtils.getConfigEntity();

        long usedMBytes = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / (1024 * 1024);

        if (configEntity == null) {
            configEntity = new ConfigEntity();
            configEntity.setSize(usedMBytes / 2);
            configEntity.setFullscreen(true);
            configEntity.setAutoConnect(true);
        }

        if (configEntity.getSize() > usedMBytes){
            configEntity.setSize(usedMBytes / 2);
            configUtils.write();
        }

        ConfigEntity finalConfigEntity = configEntity;
        Platform.runLater(() -> {
            label.setText("Доступно " + usedMBytes + " MB");
            textField.setText(String.valueOf(finalConfigEntity.getSize()));
            checkBoxSize.setSelected(finalConfigEntity.isFullscreen());
            checkBoxAuto.setSelected(finalConfigEntity.isAutoConnect());
        });
    }

    public void setOnMouseEntered(){
        openImageView.setOnMouseEntered(event -> openImageView.setOpacity(0.6));
        closeImageView.setOnMouseEntered(event -> closeImageView.setOpacity(1.0));

        folder.setOnMouseEntered(event -> folder.setStyle("-fx-background-color: #0c2a0c; -fx-background-radius: 5px"));
        clear.setOnMouseEntered(event -> clear.setStyle("-fx-background-color: #540202; -fx-background-radius: 5px"));
    }

    public void setOnMouseExited() {
        openImageView.setOnMouseExited(event -> openImageView.setOpacity(1.0));
        closeImageView.setOnMouseExited(event -> closeImageView.setOpacity(0.6));

        folder.setOnMouseExited(event -> folder.setStyle("-fx-background-color: #134213; -fx-background-radius: 5px"));
        clear.setOnMouseExited(event -> clear.setStyle("-fx-background-color: #8c0606; -fx-background-radius: 5px"));
    }

    public void setOnMouseClicked() {
        openImageView.setOnMouseClicked(event -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);

            pane.setVisible(true);
            fadeTransition.play();
        });

        closeImageView.setOnMouseClicked(event -> pane.setVisible(false));

        folder.setOnMouseClicked(event -> {
            String directory = System.getProperty("user.dir");
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(new File(directory));
                } catch (IOException e) {
                    alertDraw.init("Открытие папки", "Не удалось открыть папку");
                }
            } else {
                alertDraw.init("Не поддерживается", "Не удалось открыть папку");
            }
        });

        clear.setOnMouseClicked(event -> {
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(() -> {
                paneRemove.setVisible(true);

                FileUtils fileUtils = new FileUtils();
                fileUtils.deleteFiles("launcher");
                alertDraw.init("Удаление файлов", "Файлы были удалены..");
                paneRemove.setVisible(false);

            });
        });

    }

    public void setOnAction() {

        checkBoxAuto.setOnAction(event -> {
            ConfigUtils configUtils = new ConfigUtils();
            ConfigEntity configEntity = configUtils.getConfigEntity();
            configEntity.setAutoConnect(checkBoxAuto.isSelected());
            configUtils.write();
        });

        checkBoxSize.setOnAction(event -> {
            ConfigUtils configUtils = new ConfigUtils();
            ConfigEntity configEntity = configUtils.getConfigEntity();
            configEntity.setFullscreen(checkBoxSize.isSelected());
            configUtils.write();
        });

    }

    public void textProperty() {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {

            if (!newValue.matches("[-+]?\\d+")){
                textField.setText(newValue);
                return;
            }

            long usedMBytes = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / (1024 * 1024);
            int value = Integer.parseInt(newValue);

            if(value > usedMBytes) {
                alertDraw.init("Контроль памяти", "Значение не должно быть больше " + usedMBytes + " МБ");
                textField.setText(oldValue);
                return;
            }

            if(value < 512) {
                alertDraw.init("Контроль памяти", "Значение не должно быть меньше 512 МБ");
                textField.setText(oldValue);
                return;
            }

            ConfigUtils configUtils = new ConfigUtils();
            configUtils.init();

            ConfigEntity configEntity = configUtils.getConfigEntity();
            configEntity.setSize(value);
            configUtils.write();

        });
    }
}
