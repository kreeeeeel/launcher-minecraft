package com.application.launcher.design.draw;

import com.application.launcher.handler.TokenHandler;
import com.application.launcher.rest.api.ProfileApi;
import com.application.launcher.rest.handler.CapeHandler;
import com.application.launcher.rest.handler.TexturesHandler;
import com.application.launcher.rest.response.TextureType;
import com.application.launcher.utils.FileUtils;
import com.application.launcher.utils.RetrofitUtils;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.application.launcher.controller.MainController.alertMainDraw;
import static java.awt.Image.SCALE_DEFAULT;

public class TextureDraw {

    private final Pane texturesPane;
    private final ImageView textureCloseImg;
    private final ImageView textureSkinImg;
    private final ImageView textureCapeImg;
    private final Button textureChangeSkinBtn;
    private final Button textureSaveSkinBtn;
    private final Button textureChangeCapeBtn;
    private final Button textureSaveCapeBtn;
    private final Button textureRemoveCapeBtn;
    private final Label capeStatusLabel;
    private final ImageView photoImage;
    private final ImageView pencilImg;
    private final Group groupEpilepsy;
    private final Pane loadPane;

    private BufferedImage bufferedImageSkin = null;
    private BufferedImage bufferedImageCape = null;
    private String username;

    private final FileChooser fileChooser;

    public TextureDraw(Pane texturesPane, ImageView textureCloseImg, ImageView textureSkinImg, ImageView textureCapeImg, Button textureChangeSkinBtn, Button textureSaveSkinBtn, Button textureChangeCapeBtn, Button textureSaveCapeBtn, Button textureRemoveCapeBtn, Label capeStatusLabel, ImageView photoImage, ImageView pencilImg, Group groupEpilepsy, Pane loadPane) {
        this.texturesPane = texturesPane;
        this.textureCloseImg = textureCloseImg;
        this.textureSkinImg = textureSkinImg;
        this.textureCapeImg = textureCapeImg;
        this.textureChangeSkinBtn = textureChangeSkinBtn;
        this.textureSaveSkinBtn = textureSaveSkinBtn;
        this.textureChangeCapeBtn = textureChangeCapeBtn;
        this.textureSaveCapeBtn = textureSaveCapeBtn;
        this.textureRemoveCapeBtn = textureRemoveCapeBtn;
        this.capeStatusLabel = capeStatusLabel;
        this.photoImage = photoImage;
        this.pencilImg = pencilImg;
        this.groupEpilepsy = groupEpilepsy;
        this.loadPane = loadPane;

        setOnMouseEntered();
        setOnMouseExited();
        setOnMouseClicked();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Png files (*.png)", "*.png");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTextureSkin(BufferedImage bufferedImage) {
        try {
            bufferedImageSkin = bufferedImage;

            BufferedImage bufferedImageResult = new BufferedImage(128, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = bufferedImageResult.getGraphics();

            int width = bufferedImage.getWidth() / 64;
            int height = bufferedImage.getHeight() / 64;

            graphics.drawImage(bufferedImage.getSubimage(width * 8, height * 8, width * 8, height * 8), 32, 0, 64, 64, null);
            graphics.drawImage(bufferedImage.getSubimage(width * 20, height * 20, width * 8, height * 12), 32, 64, 64, 96, null);
            graphics.drawImage(bufferedImage.getSubimage(width * 44, height * 20, width * 4, height * 12), 0, 64, 32, 96, null);
            graphics.drawImage(bufferedImage.getSubimage(width * 44, height * 20, width * 4, height * 12), 96, 64, 32, 96, null);
            graphics.drawImage(bufferedImage.getSubimage(width * 4, height * 20, width * 4, height * 12), 32, 160, 32, 96, null);
            graphics.drawImage(bufferedImage.getSubimage(width * 4, height * 20, width * 4, height * 12), 64, 160, 32, 96, null);
            graphics.drawImage(bufferedImage.getSubimage(width * 40, height * 8, width * 8, height * 8), 32, 0, 64, 64, null);

            File file = new File("temp.png");
            ImageIO.write(bufferedImageResult, "png", file);
            textureSkinImg.setImage(new Image(file.toURI().toString()));

            BufferedImage resizedImage = new BufferedImage(32, 32, SCALE_DEFAULT);
            graphics = resizedImage.createGraphics();
            graphics.drawImage(bufferedImage.getSubimage(8, 8, 8, 8), 0, 0, 32, 32, null);
            ImageIO.write(resizedImage, "png", file);
            photoImage.setImage(new Image(file.toURI().toString()));
            if (file.delete()){
                System.out.println("Temp file deleted!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTextureCape(BufferedImage bufferedImage) {
        try {
            bufferedImageCape = bufferedImage;

            BufferedImage bufferedImageResult = new BufferedImage(128, 256, 2);
            Graphics graphics = bufferedImageResult.getGraphics();
            int width = bufferedImage.getWidth() / 64;
            int height = bufferedImage.getHeight() / 32;
            graphics.drawImage(bufferedImage.getSubimage(width, height, width * 10, height * 16), 0, 0, 128, 256, null);

            File file = new File("temp.png");
            ImageIO.write(bufferedImageResult, "png", file);
            textureCapeImg.setImage(new Image(file.toURI().toString()));
            if (file.delete()){
                System.out.println("Temp file deleted!");
            }

            Platform.runLater(() -> {
                textureCapeImg.setLayoutX(16);
                textureCapeImg.setLayoutY(14);
                textureCapeImg.setFitWidth(101);
                textureCapeImg.setFitHeight(178);

                textureSaveCapeBtn.setDisable(false);
                textureRemoveCapeBtn.setDisable(false);
                capeStatusLabel.setVisible(false);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnMouseEntered() {
        groupEpilepsy.setOnMouseEntered(event -> {
            pencilImg.setVisible(true);
            photoImage.setOpacity(0.4);
        });
        textureCloseImg.setOnMouseEntered(event -> textureCloseImg.setOpacity(1.0));
        textureChangeSkinBtn.setOnMouseEntered(event -> textureChangeSkinBtn.setStyle("-fx-background-color: #0f330f; -fx-background-radius: 5px"));
        textureChangeCapeBtn.setOnMouseEntered(event -> textureChangeCapeBtn.setStyle("-fx-background-color: #0f330f; -fx-background-radius: 5px"));
        textureSaveSkinBtn.setOnMouseEntered(event -> textureSaveSkinBtn.setStyle("-fx-background-color: #055b75; -fx-background-radius: 5px"));
        textureSaveCapeBtn.setOnMouseEntered(event -> textureSaveCapeBtn.setStyle("-fx-background-color: #055b75; -fx-background-radius: 5px"));
        textureRemoveCapeBtn.setOnMouseEntered(event -> textureRemoveCapeBtn.setStyle("-fx-background-color: #670404;-fx-background-radius: 5px"));
    }

    public void setOnMouseExited(){
        groupEpilepsy.setOnMouseExited(event -> {
            pencilImg.setVisible(false);
            photoImage.setOpacity(1.0);
        });
        textureCloseImg.setOnMouseExited(event -> textureCloseImg.setOpacity(0.6));
        textureChangeSkinBtn.setOnMouseExited(event -> textureChangeSkinBtn.setStyle("-fx-background-color: #134213; -fx-background-radius: 5px"));
        textureChangeCapeBtn.setOnMouseExited(event -> textureChangeCapeBtn.setStyle("-fx-background-color: #134213; -fx-background-radius: 5px"));
        textureSaveSkinBtn.setOnMouseExited(event -> textureSaveSkinBtn.setStyle("-fx-background-color: #067597; -fx-background-radius: 5px"));
        textureSaveCapeBtn.setOnMouseExited(event -> textureSaveCapeBtn.setStyle("-fx-background-color: #067597; -fx-background-radius: 5px"));
        textureRemoveCapeBtn.setOnMouseExited(event -> textureRemoveCapeBtn.setStyle("-fx-background-color: #8c0606;-fx-background-radius: 5px"));
    }

    public void setOnMouseClicked() {
        photoImage.setOnMouseClicked(event -> openEdit());
        pencilImg.setOnMouseClicked(event -> openEdit());
        textureCloseImg.setOnMouseClicked(event -> texturesPane.setVisible(false));

        textureSaveSkinBtn.setOnMouseClicked(event -> fileAction(false, true));
        textureSaveCapeBtn.setOnMouseClicked(event -> fileAction(false, false));
        textureChangeSkinBtn.setOnMouseClicked(event -> fileAction(true, true));
        textureChangeCapeBtn.setOnMouseClicked(event -> fileAction(true, false));

        textureRemoveCapeBtn.setOnMouseClicked(event -> {
            Platform.runLater(() -> loadPane.setVisible(true));
            ProfileApi profileApi = RetrofitUtils.getRetrofit().create(ProfileApi.class);
            profileApi.deleteCape(TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken()).enqueue(new CapeHandler());
        });
    }

    public void fileAction(boolean upload, boolean skin) {
        try {
            fileChooser.setTitle((upload ? "Upload " : "Save ") + (skin ? "skin :" : "cape : ") + username);
            fileChooser.setInitialFileName(username + (skin ? "_skin" : "_cape") + ".png");
            File file = upload ?
                    fileChooser.showOpenDialog(textureChangeSkinBtn.getScene().getWindow()) :
                    fileChooser.showSaveDialog(textureSaveCapeBtn.getScene().getWindow());

            if (file != null){
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    alertMainDraw.init("Ошибка чтения файла", "Не удалось получить данные из файла..");
                    return;
                }

                if (skin && (image.getWidth() != 64 || image.getHeight() != 64)) {
                    alertMainDraw.init("Ошибка разрешения файла", "Разрешение файла должно быть 64x64!");
                    return;
                }

                if ((!skin && file.length() > 250000) || (skin && file.length() > 500000)) {
                    alertMainDraw.init("Ошибка размера файла", "Размер файла должен быть меньше " + (!skin ? "250" : "500") + " КБ!");
                    return;
                }

                if (upload) {
                    Platform.runLater(() -> loadPane.setVisible(true));

                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", UUID.randomUUID() + ".png", RequestBody.create(MediaType.parse("image/*"), file));
                    ProfileApi profileApi = RetrofitUtils.getRetrofit().create(ProfileApi.class);
                    profileApi.uploadTexture(TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken(), filePart, skin ? TextureType.SKIN : TextureType.CAPE).enqueue(new TexturesHandler());
                } else {
                    ImageIO.write(skin ? bufferedImageSkin : bufferedImageCape, "png", file);
                    alertMainDraw.init("Сохранение файла", "Файл скина был успешно сохранён!");
                }
            }

        } catch (IOException e) {
            alertMainDraw.init("Ошибка чтения файла", "Не удалось получить данные из файла..");
        }
    }


    public void openEdit(){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), texturesPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setByValue(1.0);
        fadeTransition.setAutoReverse(true);

        texturesPane.setVisible(true);
        fadeTransition.play();
    }

    public void removeCap() {
        Platform.runLater( () -> {
            textureCapeImg.setLayoutX(20);
            textureCapeImg.setLayoutY(49);
            textureCapeImg.setFitWidth(81);
            textureCapeImg.setFitHeight(90);
            textureCapeImg.setImage(new Image(new FileUtils().getResource("notfound")));

            bufferedImageCape = null;

            textureSaveCapeBtn.setDisable(true);
            textureRemoveCapeBtn.setDisable(true);
            capeStatusLabel.setVisible(true);
        });
    }
}
