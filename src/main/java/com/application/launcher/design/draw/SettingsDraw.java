package com.application.launcher.design.draw;

import com.application.launcher.entity.ConfigEntity;
import com.application.launcher.utils.ConfigUtils;
import com.application.launcher.utils.FileUtils;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.controller.MainController.alertMainDraw;

public class SettingsDraw {
    private final TextField textField;
    private final Label label;
    private final Slider slider;
    private final RadioButton checkBoxSize;
    private final RadioButton checkBoxAuto;
    private final RadioButton checkLogger;

    private final Pane pane;
    private final Pane paneRemove;

    private final Button clear;
    private final Button folder;

    private final ImageView openImageView;
    private final ImageView closeImageView;

    private long maxMemory;

    public SettingsDraw(TextField textField, Label label, Slider slider, RadioButton checkBoxSize, RadioButton checkBoxAuto, RadioButton checkLogger, Pane pane, Pane paneRemove, Button clear, Button folder, ImageView openImageView, ImageView closeImageView) {
        this.textField = textField;
        this.label = label;
        this.slider = slider;
        this.checkBoxSize = checkBoxSize;
        this.checkBoxAuto = checkBoxAuto;
        this.checkLogger = checkLogger;
        this.pane = pane;
        this.paneRemove = paneRemove;
        this.clear = clear;
        this.folder = folder;
        this.openImageView = openImageView;
        this.closeImageView = closeImageView;
    }

    public void init() {
        ConfigEntity configEntity = ConfigUtils.loadEntity();

        maxMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / (1024 * 1024);

        if (configEntity != null && configEntity.getSize() == 0) {
            configEntity.setSize(maxMemory / 2);
            configEntity.setAutoConnect(false);
            configEntity.setFullscreen(false);
        }

        if (configEntity != null && configEntity.getSize() > maxMemory) {
            configEntity.setSize(maxMemory / 2);
            ConfigUtils.write(configEntity);
        }

        Platform.runLater(() -> {
            label.setText("Выделяется " + (configEntity != null ? configEntity.getSize() : 0) + " МБ памяти");
            textField.setText(String.valueOf(configEntity != null ? configEntity.getSize() : 0));

            checkBoxSize.setSelected(configEntity != null && configEntity.isFullscreen());
            checkBoxAuto.setSelected(configEntity != null && configEntity.isAutoConnect());
            checkLogger.setSelected(configEntity != null && configEntity.isLoggerGame());

            slider.setMax(maxMemory);
            slider.setValue(configEntity != null ? configEntity.getSize() : 0);
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
                    alertMainDraw.init("Открытие папки", "Не удалось открыть папку");
                }
            } else {
                alertMainDraw.init("Не поддерживается", "Не удалось открыть папку");
            }
        });

        clear.setOnMouseClicked(event -> {
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(() -> {
                paneRemove.setVisible(true);

                FileUtils.deleteFiles("launcher");
                alertMainDraw.init("Удаление файлов", "Операция прошла успешно!");
                paneRemove.setVisible(false);

            });
        });

    }

    public void setOnAction() {

        checkBoxAuto.setOnAction(event -> {
            checkBoxAuto.setFocusTraversable(false);

            ConfigEntity configEntity = ConfigUtils.loadEntity();
            if (configEntity != null) {
                configEntity.setAutoConnect(checkBoxAuto.isSelected());
                ConfigUtils.write(configEntity);
            }
        });

        checkBoxSize.setOnAction(event -> {
            checkBoxAuto.setFocusTraversable(false);
            ConfigEntity configEntity = ConfigUtils.loadEntity();
            if (configEntity != null) {
                configEntity.setFullscreen(checkBoxSize.isSelected());
                ConfigUtils.write(configEntity);
            }
        });

        checkLogger.setOnAction(event -> {
            checkLogger.setFocusTraversable(false);

            ConfigEntity configEntity = ConfigUtils.loadEntity();
            if (configEntity != null) {
                configEntity.setLoggerGame(checkLogger.isSelected());
                ConfigUtils.write(configEntity);
            }
        });


        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setFocusTraversable(false);
            textField.setText(String.valueOf((long) slider.getValue()));
        });
    }

    public void textProperty() {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {

            if (!newValue.matches("[-+]?\\d+")){
                textField.setText(newValue);
                return;
            }

            int value = Integer.parseInt(newValue);

            if(value > maxMemory) {
                alertMainDraw.init("Контроль памяти", "Выбранное значение должно быть не больше " + maxMemory + " МБ");
                textField.setText(oldValue);
                return;
            }

            if(value < 0) {
                alertMainDraw.init("Контроль памяти", "Выбранное значение не должно быть меньше 0 МБ");
                textField.setText(oldValue);
                return;
            }


            ConfigEntity configEntity = ConfigUtils.loadEntity();
            if (configEntity != null) {
                configEntity.setSize(value);
                ConfigUtils.write(configEntity);
            }

            label.setText("Выделяется " + value + " МБ памяти");
            slider.setValue(value);

        });
    }
}
