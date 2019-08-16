package com.choudhury.jwt.fx;

import com.choudhury.jwt.fx.jwt.JWTTabPane;
import com.choudhury.jwt.fx.window.TopBar;
import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;
import javafx.application.Application;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainApp extends Application {

    public static Stage window;

    public static TopBar topBar;

    public static BorderPane root;

    public static BorderlessScene borderlessScene;

    private final int screenMinWidth = 800, screenMinHeight = 600;


    @Override
    public void start(Stage primaryStage) {

        topBar = new TopBar();
        root = new BorderPane();
        root.setStyle("-fx-border-color: black; -fx-border-width: 1px");

        root.setTop(topBar);


        root.setCenter(new AddButtonOverlay(new JWTTabPane()));

        CustomStage stage = new CustomStage(StageStyle.UNDECORATED);

        //Prepare the Stage
        window = stage;
        window.setTitle("JWT Tool");
        window.setWidth(600);
        window.setHeight(800);
        window.centerOnScreen();
        //window.getIcons().add(InfoTool.getImageFromResourcesFolder("logo.png"));
        window.centerOnScreen();
        window.setOnCloseRequest(cl -> System.exit(0));

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


    public static void main(String[] args) {
        launch(args);
    }


}
