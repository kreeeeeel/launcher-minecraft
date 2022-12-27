package com.application.launcher.controller;

import com.application.launcher.Runner;
import com.application.launcher.entity.AccountEntity;
import com.application.launcher.rest.api.AuthApi;
import com.application.launcher.rest.request.RegisterRequest;
import com.application.launcher.rest.response.TokenResponse;
import com.application.launcher.utils.AccountUtils;
import com.application.launcher.utils.AuthFromRegUtils;
import com.application.launcher.utils.TokenUtils;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.utils.Constant.*;

public class RegisterController extends Application {

    @FXML private ImageView collapseImg;
    @FXML private ImageView exitImg;
    @FXML private ImageView alertCloseImg;
    @FXML private ImageView alertImg;

    @FXML private Label alertMessage;
    @FXML private Label alertTitle;
    @FXML private Label authLabel;
    @FXML private Label registerTitle;

    @FXML private Pane alertPane;
    @FXML private Pane registerPane;

    @FXML private TextField email;
    @FXML private TextField login;
    @FXML private TextField name;

    @FXML private PasswordField password;
    @FXML private PasswordField passwordConfirm;

    @FXML private Button register;

    private double stagePosX;
    private double stagePosY;

    private Retrofit retrofit;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/register.fxml"));
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

         retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Mouse events
        mouseOnExitImg();
        mouseOnCollapseImg();
        mouseOnRegister();
        mouseOnCloseErrorImg();

        mouseOnAuthLabel();

        // Keyboard events
        keyboardOnEmail();
        keyboardOnLogin();
        keyboardOnName();

        keyboardOnPassword();
        keyboardOnPasswordConfirm();
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

    public void mouseOnRegister() {
        register.setOnMouseEntered(event -> register.setStyle("-fx-background-color: #0d6f34"));
        register.setOnMouseExited(event -> register.setStyle("-fx-background-color: #099e47"));
        register.setOnMouseClicked(event -> registration());
    }

    public void mouseOnAuthLabel() {
        authLabel.setOnMouseEntered(event -> authLabel.setTextFill(Paint.valueOf("#158640")));
        authLabel.setOnMouseExited(event -> authLabel.setTextFill(Paint.valueOf("#23c363")));
        authLabel.setOnMouseClicked(event -> {
            try {
                Stage stage = (Stage) authLabel.getScene().getWindow();
                AuthController authController = new AuthController();
                authController.start(stage);
            } catch (IOException e) {
                alertShow("Произошла ошибка", "Невозможно сменить сцену..", false);
            }
        });
    }

    public void keyboardOnEmail(){
        email.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                registration();
            }
        });
    }

    public void keyboardOnLogin(){
        login.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                registration();
            }
        });
    }

    public void keyboardOnName(){
        name.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                registration();
            }
        });
    }

    public void keyboardOnPassword(){
        password.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                registration();
            }
        });
    }

    public void keyboardOnPasswordConfirm(){
        passwordConfirm.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                registration();
            }
        });
    }

    public void registration() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            String mail = email.getText().trim();
            String username = login.getText().trim();
            String fullName = name.getText().trim();

            String pass = password.getText().trim();
            String passConfirm = passwordConfirm.getText().trim();

            if(!mail.matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")) {
                alertShow("Неверный формат почты", "Вы указали неверный формат почты..");
                return;
            }

            if(!username.matches("^[a-zA-Z0-9]+$")){
                alertShow("Неверный формат логина", "Логин может содержать только символы и цифры..");
                return;
            }

            if(fullName.length() < NAME_MIN || fullName.length() > NAME_MAX){
                alertShow("Неверный формат имени", "Имя должно содержать от " + NAME_MIN + " до " + NAME_MAX + " символов..");
                return;
            }

            if(!pass.matches("(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")){
                alertShow("Неверный формат пароля", "Неверно указан формат пароля!");
                return;
            }

            if(!pass.equals(passConfirm)){
                alertShow("Нет совпадений", "Ваши указанные пароли не совпадают..");
                return;
            }

            Platform.runLater(() -> registerPane.setVisible(true));

            RegisterRequest registerRequest = new RegisterRequest(
                    fullName,
                    username,
                    mail,
                    pass
            );

            AuthApi authApi = retrofit.create(AuthApi.class);
            authApi.register(registerRequest).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if(!response.isSuccessful()) {
                            switch (response.errorBody().string()) {
                                case "Username is already taken!": {
                                    alertShow("Имя пользователя", "Даннное имя пользователя занято!");
                                    break;
                                }
                                case "Email Address already in use!": {
                                    alertShow("E-mail адрес", "Даннный e-mail адрес занято!");
                                    break;
                                }
                                default: {
                                    alertShow("Ошибка регистрации", "Произошла ошибка, попробуйте позже..");
                                    break;
                                }
                            }

                            return;
                        }

                        Platform.runLater(() -> {
                            try {
                                AuthFromRegUtils.username = username;
                                AuthFromRegUtils.password = pass;
                                AuthFromRegUtils.register = true;

                                Stage stage = (Stage) register.getScene().getWindow();
                                AuthController authController = new AuthController();
                                authController.start(stage);
                            } catch (IOException e) {
                                e.printStackTrace();
                                alertShow("Произошла ошибка!", "Произошла ошибка в загрузке сцены!");
                            }
                        });

                    } catch (NullPointerException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
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

            registerPane.setVisible(false);

            alertPane.setVisible(true);
            alertTitle.setText(title);
            alertMessage.setText(text);
            alertImg.setImage(image);

            fadeTransition.play();
        });
    }
}
