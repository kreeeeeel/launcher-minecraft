package com.application.launcher.controller;

import com.application.launcher.Runner;
import com.application.launcher.entity.AccountEntity;
import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.request.AuthRequest;
import com.application.launcher.rest.response.TokenResponse;
import com.application.launcher.utils.AccountUtils;
import com.application.launcher.utils.AuthFromRegUtils;
import com.application.launcher.utils.TokenUtils;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.utils.Constant.LOGIN_MAX;
import static com.application.launcher.utils.Constant.LOGIN_MIN;
import static com.application.launcher.utils.Constant.PASSWORD_MAX;
import static com.application.launcher.utils.Constant.PASSWORD_MIN;
import static com.application.launcher.utils.Constant.PHOTO;
import static com.application.launcher.utils.Constant.RECOVERY;
import static com.application.launcher.utils.Constant.URL;

public class AuthController extends Application {

    @FXML private Button auth;
    @FXML private Button yesUrlBtn;
    @FXML private Button noUrlBtn;
    @FXML private Button authChange;

    @FXML private Pane authPane;
    @FXML private Pane alertPane;
    @FXML private Pane urlPane;
    @FXML private Pane changePane;

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
    @FXML private ImageView changeCloseImg;
    @FXML private ImageView changeImg;

    @FXML private TextField login;

    @FXML private PasswordField password;

    @FXML private AnchorPane anchorChange;

    private double stagePosX;
    private double stagePosY;

    private String authLogin;
    private String authPassword;
    private Pane authUserPane;

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

        mouseOnChangeImg();
        mouseOnChangeCloseImg();

        mouseOnFastAuth();

        // Keyboard events
        keyboardOnLogin();
        keyboardOnPassword();

        //
        eventFromRegister();

    }

    public void mouseOnExitImg() {
        exitImg.setOnMouseEntered(event -> exitImg.setOpacity(1.0));
        exitImg.setOnMouseExited(event -> exitImg.setOpacity(0.6));
        exitImg.setOnMouseClicked(event -> System.exit(1));
    }

    public void eventFromRegister() {
        if (!AuthFromRegUtils.register){
            return;
        }

        login.setText(AuthFromRegUtils.username);
        password.setText(AuthFromRegUtils.password);

        authorization();

        AuthFromRegUtils.username = null;
        AuthFromRegUtils.password = null;
        AuthFromRegUtils.register = false;
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
        registerLabel.setOnMouseClicked(event -> {
            try {
                Stage stage = (Stage) registerLabel.getScene().getWindow();
                RegisterController registerController = new RegisterController();
                registerController.start(stage);
            } catch (IOException e) {
                alertShow("Произошла ошибка", "Невозможно сменить сцену..", false);
            }
        });
    }

    public void mouseOnChangeImg(){
        changeImg.setOnMouseEntered(event -> changeImg.setOpacity(1.0));
        changeImg.setOnMouseExited(event -> changeImg.setOpacity(0.5));
        changeImg.setOnMouseClicked(event -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), changePane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);

            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            progressIndicator.setLayoutX(131);
            progressIndicator.setLayoutY(126);
            progressIndicator.setPrefSize(77, 90);

            anchorChange.getChildren().clear();
            anchorChange.getChildren().add(progressIndicator);

            changePane.setVisible(true);

            fadeTransition.play();

            accounts();
        });
    }

    public void mouseOnChangeCloseImg() {
        changeCloseImg.setOnMouseEntered(event -> changeCloseImg.setOpacity(1.0));
        changeCloseImg.setOnMouseExited(event -> changeCloseImg.setOpacity(0.6));
        changeCloseImg.setOnMouseClicked(event -> changePane.setVisible(false));
    }

    public void mouseOnFastAuth() {
        authChange.setOnMouseEntered(event -> authChange.setStyle("-fx-background-color: #0e310e; -fx-background-radius: 5px"));
        authChange.setOnMouseExited(event -> authChange.setStyle("-fx-background-color: #134213; -fx-background-radius: 5px"));
        authChange.setOnMouseClicked(event -> authorization(authLogin, authPassword));
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

    public void accounts(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            authLogin = null;
            authPassword = null;
            authUserPane = null;

            authChange.setDisable(true);

            AccountUtils accountUtils = new AccountUtils();
            List<AccountEntity> list = accountUtils.getAccounts();

            Platform.runLater(() -> anchorChange.getChildren().clear());
            if(list == null || list.size() == 0) {
                alertShow("Пусто :(", "У вас нет сохраненных аккаунтов..", true);
                return;
            }

            int count = 0;
            for (AccountEntity accountEntity : list) {

                Image image = null;
                try {
                    image = new Image(new URL(URL + PHOTO + accountEntity.getUsername() + ".png").openStream());
                } catch (IOException e) {
                    accountUtils.remove(accountEntity);
                    continue;
                }

                Pane pane = new Pane();
                pane.setPrefSize(300, 45);
                pane.setLayoutX(13);
                pane.setLayoutY(50*count);
                pane.setStyle("-fx-background-color: white; -fx-background-radius: 5px");
                pane.setCursor(Cursor.HAND);

                Circle circle = new Circle();
                circle.setLayoutX(26);
                circle.setLayoutY(23);
                circle.setRadius(19);
                circle.setStroke(Paint.valueOf("#544646"));
                circle.setFill(new ImagePattern(image));

                Label label = new Label(accountEntity.getUsername());
                label.setPrefSize(208, 24);
                label.setLayoutX(54);
                label.setLayoutY(10);
                label.setFont(Font.font("Franklin Gothic Medium", 18));
                label.setTextFill(Paint.valueOf("#544646"));

                ImageView imageView = new ImageView(new File(Runner.class.getResource("images/remove.png").getPath()).getAbsolutePath());
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setLayoutX(262);
                imageView.setLayoutY(8);
                imageView.setOpacity(0.6);

                pane.setOnMouseEntered(event ->
                        pane.setStyle("-fx-background-radius: 5px;-fx-background-color: " + ((pane == authUserPane) ? "#217fdc" : "#b9b8b8")));
                pane.setOnMouseExited(event ->
                        pane.setStyle("-fx-background-radius: 5px;-fx-background-color: " + ((pane == authUserPane) ? "#2388ec" : "white")));
                pane.setOnMouseClicked(event -> {

                    if (authUserPane != null){
                        authUserPane.setStyle("-fx-background-radius: 5px;-fx-background-color: white");
                    }

                    pane.setStyle("-fx-background-radius: 5px;-fx-background-color: #217fdc");

                    authLogin = accountEntity.getUsername();
                    authPassword = accountEntity.getPassword();
                    authUserPane = pane;

                    authChange.setDisable(false);
                });

                imageView.setOnMouseEntered(event -> imageView.setOpacity(1.0));
                imageView.setOnMouseExited(event -> imageView.setOpacity(0.6));
                imageView.setOnMouseClicked(event -> {
                    accountUtils.remove(accountEntity);
                    accounts();
                });

                count += 1;
                pane.getChildren().addAll(circle, label, imageView);
                Platform.runLater(() -> anchorChange.getChildren().add(pane));
            }
            anchorChange.setPrefHeight(Math.max(count * 50, 304));

            if(count == 0){
                alertShow("Пусто :(", "У вас нет сохраненных аккаунтов..", true);
            }
        });
    }

    public void authorization(){

        String username = login.getText().trim();
        String pass = password.getText().trim();

        authorization(username, pass);

    }

    public void authorization(String username, String pass) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            Platform.runLater(() -> changePane.setVisible(false));

            if (username.length() < LOGIN_MIN || username.length() > LOGIN_MAX) {
                alertShow("Корректность данных", "Длина логина от " + LOGIN_MIN + " до " + LOGIN_MAX + " символов.", true);
                return;
            }

            if (pass.length() < PASSWORD_MIN || pass.length() > PASSWORD_MAX) {
                alertShow("Корректность данных", "Длина пароля от " + LOGIN_MIN + " до " + LOGIN_MAX + " символов.", true);
                return;
            }

            Platform.runLater(() -> {
                authTitle.setText(username);
                authPane.setVisible(true);
            });

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

                    AccountUtils accountUtils = new AccountUtils();
                    AccountEntity accountEntity = new AccountEntity();

                    accountEntity.setUsername(username);
                    accountEntity.setPassword(pass);

                    //accountUtils.add(accountEntity);

                    TokenUtils.setAccessToken(tokenResponse.getAccessToken());
                    TokenUtils.setTokenType(tokenResponse.getTokenType());
                    Platform.runLater(() -> {
                        try {
                            Stage stage = (Stage) auth.getScene().getWindow();

                            AccountController accountController = new AccountController();
                            accountController.start(stage);
                        } catch (IOException e) {
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
            changePane.setVisible(false);

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
