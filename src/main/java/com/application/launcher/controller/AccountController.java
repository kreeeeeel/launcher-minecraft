package com.application.launcher.controller;

import com.application.launcher.Runner;
import com.application.launcher.rest.api.LauncherApi;
import com.application.launcher.rest.api.ProfileApi;
import com.application.launcher.rest.response.ClientResponse;
import com.application.launcher.rest.response.FileResponse;
import com.application.launcher.rest.response.ProfileResponse;
import com.application.launcher.rest.response.ServerResponse;
import com.application.launcher.utils.MD5Files;
import com.application.launcher.utils.TokenHandler;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.ResponseBody;

import org.apache.http.impl.conn.SingleClientConnManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.launcher.utils.Constant.*;

public class AccountController extends Application {

    @FXML
    private Pane top;

    @FXML
    private ImageView alertCloseImg;

    @FXML
    private ImageView alertImg;

    @FXML
    private Label alertMessage;

    @FXML
    private Pane alertPane;

    @FXML
    private Label alertTitle;

    @FXML
    private Label balanceLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button cancelUpdate;

    @FXML
    private ImageView collapseImg;

    @FXML
    private Pane exitAccountPane;

    @FXML
    private Button exitBtn;

    @FXML
    private ImageView exitImg;

    @FXML
    private Pane exitPane;

    @FXML
    private Label fileUpdate;

    @FXML
    private Group loadingGroup;

    @FXML
    private Label loginLabel;

    @FXML
    private Pane paneUpdate;

    @FXML
    private Circle photoCircle;

    @FXML
    private ProgressBar progresUpdate;

    @FXML
    private ImageView rubleImg;

    @FXML
    private AnchorPane serversAnchor;

    @FXML
    private ImageView settingsImg;

    @FXML
    private Label titleUpdate;

    private List<String> list = new ArrayList<>();
    private int folders;
    private int files;

    private double stagePosX;
    private double stagePosY;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/servers.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize(){

        // Mouse events
        mouseOnTop();
        mouseOnExitImg();
        mouseOnCollapseImg();
        mouseOnSettingsImg();
        mouseOnCloseErrorImg();
        mouseOnRubleImg();
        mouseOnExitAccount();
        mouseOnExitBtn();
        mouseOnCancelBtn();

        update();

    }

    public void mouseOnTop() {
        top.setOnMousePressed(event -> {
            Stage stage = (Stage) top.getScene().getWindow();
            stagePosX = stage.getX() - event.getScreenX();
            stagePosY = stage.getY() - event.getScreenY();
        });
        top.setOnMouseDragged(event -> {
            Stage stage = (Stage) top.getScene().getWindow();
            stage.setX(event.getScreenX() + stagePosX);
            stage.setY(event.getScreenY() + stagePosY);
        });
    }

    public void mouseOnExitBtn() {
        exitBtn.setOnMouseEntered(event -> exitBtn.setOpacity(1.0));
        exitBtn.setOnMouseExited(event -> exitBtn.setOpacity(0.6));
        exitBtn.setOnMouseClicked(event -> {
            try {
                Stage stage = (Stage) exitAccountPane.getScene().getWindow();
                AuthController authController = new AuthController();
                authController.start(stage);
            } catch (IOException e) {
                alertShow("Произошла ошибка!", "Попробуйте позже..", true);
            }
        });
    }

    public void mouseOnCancelBtn() {
        cancelBtn.setOnMouseEntered(event -> cancelBtn.setOpacity(1.0));
        cancelBtn.setOnMouseExited(event -> cancelBtn.setOpacity(0.6));
        cancelBtn.setOnMouseClicked(event -> exitPane.setVisible(false));
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

    public void mouseOnSettingsImg() {
        settingsImg.setOnMouseEntered(event -> settingsImg.setOpacity(1.0));
        settingsImg.setOnMouseExited(event -> settingsImg.setOpacity(0.6));
    }


    public void mouseOnCloseErrorImg() {
        alertCloseImg.setOnMouseEntered(event -> alertCloseImg.setOpacity(1.0));
        alertCloseImg.setOnMouseExited(event -> alertCloseImg.setOpacity(0.6));
        alertCloseImg.setOnMouseClicked(event -> alertPane.setVisible(false));
    }
    public void mouseOnRubleImg(){
        rubleImg.setOnMouseEntered(event -> {
            String path = new File(Runner.class.getResource("images/pay.png").getPath()).getAbsolutePath();
            Image image = new Image(path);

            rubleImg.setImage(image);
            rubleImg.setOpacity(1.0);
        });
        rubleImg.setOnMouseExited(event -> {
            String path = new File(Runner.class.getResource("images/balance.png").getPath()).getAbsolutePath();
            Image image = new Image(path);

            rubleImg.setImage(image);
            rubleImg.setOpacity(0.6);
        });
    }

    public void mouseOnExitAccount() {
        exitAccountPane.setOnMouseEntered(event -> exitAccountPane.setOpacity(1.0));
        exitAccountPane.setOnMouseExited(event -> exitAccountPane.setOpacity(0.6));
        exitAccountPane.setOnMouseClicked(event -> {
            exitPane.setVisible(true);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), exitPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
        });
    }

    public void update() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProfileApi profileApi = retrofit.create(ProfileApi.class);
            profileApi.getProfile(token).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    if(!response.isSuccessful()){
                        alertShow("Ошибка", "Произошла ошибка, при получении данных о пользователе..", true);
                        return;
                    }

                    ProfileResponse profileResponse = response.body();
                    if(profileResponse == null){
                        alertShow("Ошибка авторизации!", "Произошла ошибка, нет полученных данных", true);
                        return;
                    }

                    Platform.runLater(() -> {
                        showLogin(profileResponse.getLogin());
                        showPhoto(profileResponse.getLogin());

                        showBalance(profileResponse.getBalance());
                        showServers(profileResponse.getServers());
                    });
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    alertShow("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..", true);
                }
            });

            loadingGroup.setVisible(false);
        });
    }

    public void showLogin(String login) {

        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime time = LocalTime.now(zoneId);
        int hour = time.getHour();
        login = (hour < 6 ? "Доброй ночи" : hour < 12 ? "Доброго утро" :
                hour < 18 ? "Доброго дня" : "Доброго вечера") + ", " + login;

        Text text = new Text(login);
        text.setFont(Font.font("Franklin Gothic Medium", 18));

        settingsImg.setLayoutX(text.getLayoutBounds().getWidth() + 82);
        loginLabel.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());
        loginLabel.setText(login);
    }

    public void showBalance(BigDecimal balance) {
        String value = String.format(Locale.US, "%,d", balance.longValue());

        Text text = new Text(value);
        text.setFont(Font.font("Franklin Gothic Medium", 18));

        rubleImg.setLayoutX(text.getLayoutBounds().getWidth() + 80);
        balanceLabel.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());
        balanceLabel.setText(value);
    }

    public void showPhoto(String login){
        try {
            Image image = new Image(new URL(URL + PHOTO + login + ".png").openStream());
            photoCircle.setFill(new ImagePattern(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showServers(ServerResponse[] servers){

        serversAnchor.getChildren().clear();
        if(servers == null || servers.length <= 0){

            String path = new File(Runner.class.getResource("images/shoked.png").getPath()).getAbsolutePath();
            Image image = new Image(path);
            ImageView notfound = new ImageView(image);

            notfound.setLayoutX(345);
            notfound.setLayoutY(106);
            notfound.setFitWidth(96);
            notfound.setFitHeight(96);

            Label title = new Label("Не найдено серверов.");
            title.setPrefSize(216, 25);
            title.setLayoutX(285);
            title.setLayoutY(223);
            title.setFont(Font.font("Franklin Gothic Medium", 22));
            title.setTextFill(Paint.valueOf("#e1e1e1"));

            Label description = new Label("В данный момент, не найдено серверов на которых вы бы смогли поиграть(");
            description.setPrefSize(324, 44);
            description.setLayoutX(231);
            description.setLayoutY(248);
            description.setFont(Font.font("Franklin Gothic Medium", 18));
            description.setTextFill(Paint.valueOf("#a4a1a1"));
            description.setTextAlignment(TextAlignment.CENTER);
            description.setWrapText(true);

            serversAnchor.getChildren().addAll(notfound, title, description);
            return;
        }

        int count = 0;
        for(ServerResponse serverResponse : servers) {
            Pane pane = getServerPane(serverResponse);
            pane.setLayoutY(14 + 186*count);

            serversAnchor.getChildren().add(pane);
            count += 1;
        }
        serversAnchor.setPrefHeight(Math.max(494, 14 + 186*count));
    }

    public Pane getServerPane(ServerResponse serverResponse) {

        Pane pane = new Pane();
        pane.setPrefSize(763, 175);
        pane.setLayoutX(16);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 25px");

        ImageView icon = new ImageView();
        icon.setImage(new Image(URL + ICONS + serverResponse.getIcon()));
        icon.setLayoutX(14);
        icon.setLayoutY(13);
        icon.setFitWidth(150);
        icon.setFitHeight(150);

        Label title = getLabel(serverResponse.getTitle(), "white", 176, 13, 18);

        ImageView versionImg = getIcon("version", 176, 42);
        Label versionLabel = getLabel("Версия: " + serverResponse.getVersion(), "#dddddd", 200, 43, 16);

        ImageView playersImg = getIcon("players", 176, 68);
        Label playersLabel = getLabel(serverResponse.getOnline() + " из " + serverResponse.getPlayers(), "#dddddd", 203, 69, 16);

        ImageView pvpImg = getIcon("pvp", 176, 94);
        Label pvpLabel = getLabel(serverResponse.getPvp(), "#dddddd", 203, 95, 16);

        ImageView sizeImg = getIcon("world", 176, 120);
        Label sizeLabel = getLabel(serverResponse.getSize(), "#dddddd", 203, 121, 16);

        ImageView startImg = getIcon("start", 176, 146);
        Label startLabel = getLabel(serverResponse.getStart(), "#dddddd", 203, 148, 16);

        ImageView wipeImg = getIcon("clear", 457, 42);
        Label wipeLabel = getLabel("Последний вайп: " + serverResponse.getWipe(), "#dddddd", 484, 43, 16);

        ImageView wipeOtherImg = getIcon("clear", 457, 68);
        Label wipeOtherLabel = getLabel("Вайп доп.миров: " + serverResponse.getWipeOther(), "#dddddd", 484, 69, 16);

        Button more = new Button("Подробнее");
        more.setLayoutX(494);
        more.setLayoutY(129);
        more.setPrefSize(127, 34);
        more.setStyle("-fx-background-color: #064f5e");
        more.setFont(Font.font("Franklin Gothic Medium", 18));
        more.setTextFill(Paint.valueOf("#c3c3c3"));
        more.setOpacity(0.7);
        more.setCursor(Cursor.HAND);

        more.setOnMouseEntered(event -> more.setOpacity(1.0));
        more.setOnMouseExited(event -> more.setOpacity(0.7));

        Button play = new Button("Играть");
        play.setLayoutX(636);
        play.setLayoutY(129);
        play.setPrefSize(113, 34);
        play.setStyle("-fx-background-color: #134213");
        play.setFont(Font.font("Franklin Gothic Medium", 18));
        play.setTextFill(Paint.valueOf("#c3c3c3"));
        play.setOpacity(0.7);
        play.setCursor(Cursor.HAND);

        play.setOnMouseEntered(event -> play.setOpacity(1.0));
        play.setOnMouseExited(event -> play.setOpacity(0.7));
        play.setOnMouseClicked(event -> getLauncherInfo(serverResponse.getClient()));

        pane.getChildren().addAll(icon, title, versionImg, versionLabel, playersImg, playersLabel, pvpImg, pvpLabel,
                sizeImg, sizeLabel, startImg, startLabel, wipeImg, wipeLabel, wipeOtherImg, wipeOtherLabel, more, play);
        return pane;
    }

    public ImageView getIcon(String name, int x, int y) {
        String path = new File(Runner.class.getResource("images/" + name + ".png").getPath()).getAbsolutePath();
        ImageView imageView = new ImageView(new Image(path));
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    public Label getLabel(String name, String color, int x, int y, int size){
        Text text = new Text(name);
        text.setFont(Font.font("Franklin Gothic Medium", size));

        Label label = new Label(name);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());
        label.setTextFill(Paint.valueOf(color));
        label.setFont(Font.font("Franklin Gothic Medium", size));
        return label;
    }

    public void getLauncherInfo(String launcher){

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            paneUpdate.setVisible(true);
            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LauncherApi launcherApi = retrofit.create(LauncherApi.class);
            launcherApi.getLauncher(token, launcher).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {

                    if(!response.isSuccessful()){
                        //alertShow("Ошибка авторизации!", "Неверно указан логин или пароль!");
                        return;
                    }

                    ClientResponse clientResponse = response.body();
                    updateFiles(launcher, clientResponse);
                }

                @Override
                public void onFailure(Call<ClientResponse> call, Throwable t) {
                    //alertShow("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
                }
            });
        });

    }

    public void updateFiles(String name, ClientResponse clientResponse) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            folders = 0;
            files = 0;

            Platform.runLater(() -> {
                titleUpdate.setText("Обновление файлов..");
                fileUpdate.setText("Подготовка к обновлению..");
            });

            String token = TokenHandler.getTokenType() + " " + TokenHandler.getAccessToken();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LauncherApi launcherApi = retrofit.create(LauncherApi.class);
            MD5Files md5Files = new MD5Files();

            File folder = new File("client");
            if( folder.mkdir() ){
                showUpdate("Создание папки с клиентами.");
            }

            File launcher = new File("client/" + name);
            if( launcher.mkdir() ){
                showUpdate("Создание папки для сборки.");
            }

            for(String fileName : clientResponse.getFolders()){

                File file = new File(fileName);

                if(!file.isDirectory() && !file.mkdir()){
                    return;
                }
                showUpdate(fileName);

                folders += 1;
                double value = (double) folders / (clientResponse.getCountFiles() + clientResponse.getCountFolders());
                showUpdateProgres(value);
            }

            for(FileResponse fileResponse : clientResponse.getFiles()) {

                File file = new File(fileResponse.getPath());
                String hash = null;
                try {
                    hash = md5Files.getHash(file.getAbsolutePath());
                } catch (IOException | NoSuchAlgorithmException e) {
                }

                showUpdate(fileResponse.getPath());
                if(!file.exists() || hash == null || !hash.equals(fileResponse.getMd5()) || file.length() != fileResponse.getSize()){
                    launcherApi.download(token, URLEncoder.encode(fileResponse.getPath(), StandardCharsets.UTF_8)).enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(!response.isSuccessful()) {
                                //alertShow("Ошибка авторизации!", "Неверно указан логин или пароль!");
                                return;
                            }

                            try {
                                ResponseBody responseBody = response.body();
                                InputStream inputStream = new BufferedInputStream(response.body().byteStream());
                                FileOutputStream fileOutputStream = new FileOutputStream(fileResponse.getPath());

                                byte[] buffer = new byte[65536];
                                int line = 0;
                                while ((line = inputStream.read(buffer, 0, buffer.length)) != -1) {
                                    fileOutputStream.write(buffer, 0, line);
                                }
                                inputStream.close();
                                fileOutputStream.close();

                            } catch (IOException ex){
                                System.out.println(ex.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //alertShow("Произошла ошибка!", "Произошла ошибка! Попробуйте позже..");
                        }
                    });
                }

                files += 1;
                double value = (double) (folders + files) / (clientResponse.getCountFiles() + clientResponse.getCountFolders());
                showUpdateProgres(value);
            }
            Platform.runLater(() -> titleUpdate.setText("Индексация файлов.."));
            checkingFiles(name, clientResponse);
        });
    }

    public void checkingFiles(String name, ClientResponse clientResponse) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            MD5Files md5Files = new MD5Files();
            File folder = new File("client");
            if( folder.mkdir() ){
                showUpdate("Создание папки с клиентами.");
            }

            File launcher = new File("client/" + name);
            if( launcher.mkdir() ){
                showUpdate("Проверка файлов..");
            }

            Platform.runLater(() -> progresUpdate.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS));
            indexingFiles("client/" + name);

            Platform.runLater(() -> titleUpdate.setText("Проверка целосности файлов.."));
            for(String path : list) {

                showUpdate(path);
                if(!clientResponse.isPath(path)) {
                    deleteFiles(path);
                }

                File file = new File(path);
                String hash = null;

                if(!file.exists()) {
                    System.out.println(path);
                    alertShow("Произошла ошибка", "Произошла ошибка в проверке файлов..", true);
                    return;
                }

                try {
                    hash = md5Files.getHash(path);
                } catch (IOException | NoSuchAlgorithmException ex) {}

                FileResponse fileResponse = clientResponse.getFileResponse(path);

                if(file.isFile() && (!hash.equals(fileResponse.getMd5()) || fileResponse.getSize() != file.length())){
                    alertShow("Произошла ошибка", "Произошла ошибка в проверке файлов..", true);
                    return;
                }
            }

            paneUpdate.setVisible(false);
        });
    }

    public void indexingFiles(String path){
        File file = new File(path);
        for (File item : file.listFiles()) {

            list.add(item.getPath());
            if(item.isDirectory()) {
                indexingFiles(item.getPath());
            }
        }
    }

    public void deleteFiles(String path) {
        File file = new File(path);
        if(file.isFile()) file.delete();
        if(file.isDirectory()){
            File[] array = file.listFiles();
            for(File temp : array){
                deleteFiles(temp.getPath());
            }
            file.delete();
        }
    }

    public void showUpdateProgres(double value){
        Platform.runLater(() -> progresUpdate.setProgress(value));
    }

    public void showUpdate(String name){
        Platform.runLater(() -> fileUpdate.setText(name));
    }

    public void alertShow(String title, String text, boolean warning) {

        String path = new File(Runner.class.getResource("images/" + (warning ? "warning" : "error") + ".png").getPath()).getAbsolutePath();
        Image image = new Image(path);

        Platform.runLater(() -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), alertPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setByValue(1.0);
            fadeTransition.setAutoReverse(true);

            paneUpdate.setVisible(false);

            alertPane.setVisible(true);
            alertTitle.setText(title);
            alertMessage.setText(text);
            alertImg.setImage(image);

            fadeTransition.play();
        });
    }
}