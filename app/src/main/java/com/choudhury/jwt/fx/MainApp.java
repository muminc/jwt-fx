package com.choudhury.jwt.fx;

import com.choudhury.jwt.fx.config.AppSettings;
import com.choudhury.jwt.fx.jwt.JWTTabPane;
import com.choudhury.jwt.fx.jwt.api.JWTService;
import com.choudhury.jwt.fx.window.TopBar;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.ServiceLoader;


public class MainApp extends Application {

    private Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Stage window;

    public static TopBar topBar;

    public static BorderPane root;

    public static BorderlessScene borderlessScene;

    private final int screenMinWidth = 800, screenMinHeight = 600;
    private AppSettings appSettings;

    private ObjectMapper objectMapper = new ObjectMapper();
    private JWTTabPane tabPaneWithAdd;
    private File appSettingsFileName;
    private JWTService jwtService;


    private AppSettings loadAppSettings() {
        ServiceLoader<JWTService> loader = ServiceLoader.load(JWTService.class);
        Optional<JWTService> first = loader.findFirst();
        first.ifPresent(service -> jwtService = service);

        if (appSettingsFileName.exists()) {
            try (InputStream fileStream = new FileInputStream(appSettingsFileName)) {
                return objectMapper.readValue(fileStream, AppSettings.class);
            } catch (Exception e) {
                logger.error("Unable to load settings");
            }
        }
        return new AppSettings();
    }

    @Override
    public void init() {
        appSettingsFileName = new File("app.json");
        appSettings = loadAppSettings();
    }

    @Override
    public void start(Stage primaryStage) {

        topBar = new TopBar();
        root = new BorderPane();
        root.setStyle("-fx-border-color: black; -fx-border-width: 1px");

        root.setTop(topBar);


        tabPaneWithAdd = new JWTTabPane(jwtService, appSettings);
        root.setCenter(new AddButtonOverlay(tabPaneWithAdd));

        CustomStage stage = new CustomStage(StageStyle.UNDECORATED);

        //Prepare the Stage
        window = stage;
        window.setTitle("JWT Tool");
        window.setWidth(600);
        window.setHeight(800);
        window.centerOnScreen();
        //window.getIcons().add(InfoTool.getImageFromResourcesFolder("logo.png"));
        window.centerOnScreen();
        window.setOnCloseRequest(cl -> Platform.exit());

        // Borderless Scene
        borderlessScene = new BorderlessScene(window, StageStyle.UNDECORATED, root, screenMinWidth, screenMinHeight);
        ObservableList<String> stylesheets = borderlessScene.getRoot().getStylesheets();
        stylesheets.clear();
        borderlessScene.getStylesheets().add(getClass().getResource("/css/application-custom.css").toExternalForm());
        borderlessScene.setMoveControl(topBar);
        window.setScene(borderlessScene);
        stage.getIcons().add(new Image("/images/ticket-32.png"));

        topBar.textProperty().bind(window.titleProperty());
        stage.showAndAdjust();
    }

    @Override
    public void stop() throws Exception {
        appSettings = new AppSettings();
        tabPaneWithAdd.updateConfig(appSettings);
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(appSettingsFileName, appSettings);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
