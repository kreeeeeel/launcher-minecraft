package com.application.launcher.controller;

import com.application.launcher.Runner;
import com.application.launcher.utils.TokenHandler;
import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.request.AuthRequest;
import com.application.launcher.rest.response.TokenResponse;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.utils.Constant.*;

public class AuthController extends Application {

    @FXML private Button auth;
    @FXML private Button yesUrlBtn;
    @FXML private Button noUrlBtn;

    @FXML private Pane authPane;
    @FXML private Pane alertPane;
    @FXML private Pane urlPane;

    @FXML private Label authTitle;
    @FXML private Label alertMessage;
    @FXML private Label alertTitle;
    @FXML private Label urlContent;
    @FXML private Label urlLabel;
    @FXML private Label recoveryLabel;
    @FXML private Label registerLabel;

    @FXML private ImageView collapseImg;
    @FXML private ImageView alertCloseImg;
    @FXML private ImageView alertImg;
    @FXML private ImageView exitImg;

    @FXML private TextField login;

    @FXML private PasswordField password;

    private double stagePosX;
    private double stagePosY;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setScene(scene);
        stage.show();

        // For Used to move the scene
        scene.setOnMousePressed(event -> {
            stagePosX = stage.getX() - event.getScreenX();
            stagePosY = stage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + stagePosX);
            stage.setY(event.getScreenY() + stagePosY);
        });
    }

    @FXML
    public void initialize() {

        // Mouse events
        mouseOnExitImg();
        mouseOnCollapseImg();
        mouseOnAuth();
        mouseOnCloseErrorImg();

        mouseOnRecoveryLabel();
        mouseOnRegisterLabel();

        // Keyboard events
        keyboardOnLogin();
        keyboardOnPassword();

    }

    public void mouseOnExitImg() {
        exitImg.setOnMouseEntered(event -> exitImg.setOpacity(1.0));
        exitImg.setOnMouseExited(event -> exitImg.setOpacity(0.6));
        exitImg.setOnMouseClicked(event -> System.exit(1));
    }

    public void mouseOnCollapseImg(){
        collapseImg.setOnMouseEntered(event -> collapseImg.setOpacity(1.0));
        collapseImg.setOnMouseExited(event -> collapseImg.setOpacity(0.6));
        collapseImg.setOnMouseClicked(event -> {
            Stage stage = (Stage) collapseImg.getScene().getWindow();
            stage.setIconified(true);
        });
    }

    public void mouseOnCloseErrorImg() {
        alertCloseImg.setOnMouseEntered(event -> alertCloseImg.setOpacity(1.0));
        alertCloseImg.setOnMouseExited(event -> alertCloseImg.setOpacity(0.6));
        alertCloseImg.setOnMouseClicked(event -> alertPane.setVisible(false));
    }

    public void mouseOnAuth() {
        auth.setOnMouseEntered(event -> auth.setStyle("-fx-background-color: #0d6f34"));
        auth.setOnMouseExited(event -> auth.setStyle("-fx-background-color: #099e47"));
        auth.setOnMouseClicked(event -> authorization());
    }

    public void mouseOnRecoveryLabel() {
        recoveryLabel.setOnMouseEntered(event -> recoveryLabel.setTextFill(Paint.valueOf("#656363")));
        recoveryLabel.setOnMouseExited(event -> recoveryLabel.setTextFill(Paint.valueOf("#b2b2b2")));
        recoveryLabel.setOnMouseClicked(event -> followingALink(URL + RECOVERY, "Вы действительно хотите перейти по ссылке для восстановления пароля?"));
    }

    public void mouseOnRegisterLabel() {
        registerLabel.setOnMouseEntered(event -> registerLabel.setTextFill(Paint.valueOf("#158640")));
        registerLabel.setOnMouseExited(event -> registerLabel.setTextFill(Paint.valueOf("#23c363")));
        registerLabel.setOnMouseClicked(event -> followingALink(URL + REGISTER, "Вы действительно хотите перейти по ссылке для регистрации?"));
    }

    public void keyboardOnLogin(){
        login.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                authorization();
            }
        });
    }

    public void keyboardOnPassword(){
        password.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                authorization();
            }
        });
    }

    public void authorization() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            String username = login.getText().trim();
            String pass = password.getText().trim();

            if (username.length() < LOGIN_MIN || username.length() > LOGIN_MAX) {
                alertShow("Корректность данных", "Длина логина от " + LOGIN_MIN + " до " + LOGIN_MAX + " символов.", true);
                return;
            }

            if (pass.length() < PASSWORD_MIN || pass.length() > PASSWORD_MAX) {
                alertShow("Корректность данных", "Длина пароля от " + LOGIN_MIN + " до " + LOGIN_MAX + " символов.", true);
                return;
            }

            authPane.setVisible(true);
            Platform.runLater(() -> authTitle.setText(username));

            AuthRequest authRequest = new AuthRequest();
            authRequest.setUsername(username);
            authRequest.setPassword(pass);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthApi authApi = retrofit.create(AuthApi.class);
            authApi.auth(authRequest).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                    if(!response.isSuccessful()){
                        alertShow("Ошибка авторизации!", "Неверно указан логин или пароль!");
                        return;
                    }

                    TokenResponse tokenResponse = response.body();
                    if(tokenResponse == null){
                        alertShow("Ошибка авторизации!", "Не удалось получить данные..");
                        return;
                    }

                    TokenHandler.setAccessToken(tokenResponse.getAccessToken());
                    TokenHandler.setTokenType(tokenResponse.getTokenType());
                    Platform.runLater(() -> {
                        try {
                            Stage stage = (Stage) auth.getScene().getWindow();

                            AccountController accountController = new AccountController();
                            accountController.start(stage);
                        } catch (IOException e) {
                            e.printStackTrace();
                            alertShow("Произошла ошибка!", "Произошла ошибка в загрузке сцены!");
                        }
                    });
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    alertShow("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
                }
            });
        });
    }

    public void alertShow(String title, String text){
        alertShow(title, text, false);
    }

    public void alertShow(String title, String text, boolean warning) {

        String path = new File(Objects.requireNonNull(Runner.class.getResource("images/" + (warning ? "warning" : "error") + ".png")).getPath()).getAbsolutePath();
        Image image = new Image(path);

        Platform.runLater(() -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), alertPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);

            authPane.setVisible(false);

            alertPane.setVisible(true);
            alertTitle.setText(title);
            alertMessage.setText(text);
            alertImg.setImage(image);

            fadeTransition.play();
        });
    }

    public void followingALink(String url, String content){
        Platform.runLater(() -> {

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), urlPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);

            urlLabel.setText(url);
            urlContent.setText(content);

            yesUrlBtn.setOnMouseEntered(event -> yesUrlBtn.setStyle("-fx-background-color: #0e310e; -fx-background-radius: 5px"));
            yesUrlBtn.setOnMouseExited(event -> yesUrlBtn.setStyle("-fx-background-color: #134213; -fx-background-radius: 5px"));
            yesUrlBtn.setOnMouseClicked(event -> {

                try {
                    URI uri = new URI(url);
                    if(Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(uri);
                    } else {
                        alertShow("Не поддерживается", "Невозможно перейти по ссылке", false);
                    }
                } catch (IOException | URISyntaxException e) {
                    alertShow("Произошла ошибка", "Невозможно перейти по ссылке", false);
                }
                urlPane.setVisible(false);

            });
            noUrlBtn.setOnMouseEntered(event -> noUrlBtn.setStyle("-fx-background-color: #500404; -fx-background-radius: 5px"));
            noUrlBtn.setOnMouseExited(event -> noUrlBtn.setStyle("-fx-background-color: #8c0606; -fx-background-radius: 5px"));
            noUrlBtn.setOnMouseClicked(event -> urlPane.setVisible(false));

            urlPane.setVisible(true);
            fadeTransition.play();
        });
    }
}
