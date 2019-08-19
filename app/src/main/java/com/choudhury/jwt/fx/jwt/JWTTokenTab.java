package com.choudhury.jwt.fx.jwt;

import com.choudhury.jwt.fx.config.WindowSettings;
import com.choudhury.jwt.fx.jwt.model.JWTWindowModel;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class JWTTokenTab extends Tab {


    private final ImageView noneBusyGraphic;
    private ProgressIndicator busyIndicator;
    private StackPane stackPane;
    private JWTWindowModel jwtWindowModel;
    private JwtTokenPanel content;


    public JWTTokenTab(JWTWindowModel jwtWindowModel) {
        this.jwtWindowModel = jwtWindowModel;

        textProperty().bind(jwtWindowModel.sessionProperty());
        content = new JwtTokenPanel(jwtWindowModel);
        setContent(content);
        stackPane = new StackPane();
        stackPane.setPrefHeight(20);
        stackPane.setPrefWidth(20);
        noneBusyGraphic = new ImageView(new Image(JWTTokenTab.class.getResourceAsStream("/images/ticket-16.png")));
        busyIndicator = new ProgressIndicator();
        busyIndicator.setPrefHeight(20);
        busyIndicator.setPrefWidth(20);
        ObservableList<Node> children = stackPane.getChildren();
        children.add(noneBusyGraphic);
        setGraphic(stackPane);
        jwtWindowModel.getTaskModel().runningProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                children.add(busyIndicator);
            } else {
                children.remove(busyIndicator);
            }
        });


    }

    public JWTTokenTab(String text) {
        this(new JWTWindowModel(text));

    }

    public void executeAction() {
        content.executeAction();
    }

    public void updateConfig(WindowSettings windowSettings) {
        jwtWindowModel.updateConfig(windowSettings);
    }
}

