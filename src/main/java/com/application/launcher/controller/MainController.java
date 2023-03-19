package com.application.launcher.controller;

import com.application.launcher.Runner;
import com.application.launcher.design.draw.*;
import com.application.launcher.design.image.CollapseImage;
import com.application.launcher.design.image.ExitImage;
import com.application.launcher.design.image.PayImage;
import com.application.launcher.design.pane.TopPane;
import com.application.launcher.service.ProfileService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController extends Application {

    @FXML private ImageView exitImg;
    @FXML private ImageView collapseImg;
    @FXML private ImageView alertCloseImg;
    @FXML private ImageView rubleImg;
    @FXML private ImageView settingsCloseImg;
    @FXML private ImageView settingsImg;
    @FXML private ImageView playersCloseImg;

    @FXML private Label alertMessage;
    @FXML private Label alertTitle;
    @FXML private Label balanceLabel;
    @FXML private Label loginLabel;
    @FXML private Label settingsRamLabel;
    @FXML private Label urlContent;
    @FXML private Label urlLabel;
    @FXML private Label playersServerLabel;
    @FXML private Label fileUpdate;
    @FXML private Label titleUpdate;

    @FXML private Pane alertPane;
    @FXML private Pane alertPaneMain;
    @FXML private Pane exitPane;
    @FXML private Pane removeClientPane;
    @FXML private Pane settingsPane;
    @FXML private Pane top;
    @FXML private Pane urlPane;
    @FXML private Pane loadPane;
    @FXML private Pane playersPane;
    @FXML private Pane processPane;
    @FXML private Pane paneUpdate;

    @FXML private ProgressBar progressUpdate;

    @FXML private RadioButton boxLaunchAuto;
    @FXML private RadioButton boxLaunchFullScreen;

    @FXML private Button exitAccountBtn;
    @FXML private Button cancelBtn;
    @FXML private Button exitBtn;
    @FXML private Button noUrlBtn;
    @FXML private Button settingsClear;
    @FXML private Button settingsOpenFolder;
    @FXML private Button yesUrlBtn;
    @FXML private Button processBtn;

    @FXML private Circle photoCircle;
    @FXML private AnchorPane serversAnchor;
    @FXML private AnchorPane processAnchor;
    @FXML private AnchorPane playersAnchor;
    @FXML private TextField settingsRamText;
    @FXML private Slider settingsRamSlider;
    @FXML private ScrollPane processScrollPane;

    public static ProcessDraw processDraw;
    public static AlertDraw alertMainDraw;

    public static Label fileUpdateS;
    public static Label titleUpdateS;
    public static ProgressBar progressUpdateS;
    public static Pane paneUpdateS;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {

        /*
        JavaFX это хуйня полная которая не умеет инициализировать статичные переменные сама, котагым жеме
         */
        fileUpdateS = fileUpdate;
        titleUpdateS = titleUpdate;
        progressUpdateS = progressUpdate;
        paneUpdateS = paneUpdate;

        List<Pane> list = new ArrayList<>();
        list.add(paneUpdate);
        list.add(loadPane);

        processDraw = new ProcessDraw(processPane, processAnchor, processScrollPane, processBtn);
        processDraw.setOnMouseEntered();
        processDraw.setOnMouseExited();

        alertMainDraw = new AlertDraw(
                list,
                alertPane,
                alertPaneMain,
                alertCloseImg,
                alertTitle,
                alertMessage
        );
        BrowseDraw browseDraw = new BrowseDraw(
                null,
                null,
                urlPane,
                urlLabel,
                urlContent,
                yesUrlBtn,
                noUrlBtn
        );
        ExitImage exitImage = new ExitImage(exitImg);
        exitImage.setOnMouseEntered();
        exitImage.setOnMouseExited();
        exitImage.setOnMouseClicked();

        CollapseImage collapseImage = new CollapseImage(collapseImg);
        collapseImage.setOnMouseEntered();
        collapseImage.setOnMouseExited();
        collapseImage.setOnMouseClicked();

        TopPane topPane = new TopPane(top);
        topPane.setOnMouseDragged();
        topPane.setOnMousePressed();

        ExitAccountDraw exitAccountDraw = new ExitAccountDraw(
                exitAccountBtn,
                exitBtn,
                cancelBtn,
                exitPane
        );
        exitAccountDraw.setOnMouseEntered();
        exitAccountDraw.setOnMouseExited();
        exitAccountDraw.setOnMouseClicked();

        PayImage payImage = new PayImage(
                rubleImg,
                browseDraw
        );
        payImage.setOnMouseEntered();
        payImage.setOnMouseExited();
        payImage.setOnMouseClicked();

        PlayersDraw playersDraw = new PlayersDraw(
                playersPane,
                loadPane,
                playersAnchor,
                playersCloseImg,
                playersServerLabel
        );
        playersDraw.setOnMouseEntered();
        playersDraw.setOnMouseExited();
        playersDraw.setOnMouseClicked();

        ProfileService profileService = new ProfileService(
                loginLabel,
                balanceLabel,
                settingsImg,
                rubleImg,
                serversAnchor,
                photoCircle,
                loadPane,
                playersDraw
        );
        profileService.init();

        SettingsDraw settingsDraw = new SettingsDraw(
                settingsRamText,
                settingsRamLabel,
                settingsRamSlider,
                boxLaunchFullScreen,
                boxLaunchAuto,
                settingsPane,
                removeClientPane,
                settingsClear,
                settingsOpenFolder,
                settingsImg,
                settingsCloseImg
        );
        settingsDraw.init();
        settingsDraw.setOnMouseEntered();
        settingsDraw.setOnMouseExited();
        settingsDraw.setOnMouseClicked();
        settingsDraw.setOnAction();
        settingsDraw.textProperty();

    }

}
