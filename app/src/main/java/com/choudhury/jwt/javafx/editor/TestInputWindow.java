package com.choudhury.jwt.javafx.editor;

import com.choudhury.jwt.javafx.FXUtils;
import com.choudhury.jwt.javafx.jwt.model.JWTWindowModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;



public class TestInputWindow extends BorderPane {

    @FXML TextField sessionName;
    @FXML TextField clientId;
    @FXML TextField scope;
    @FXML TextField oauthURI;
    @FXML TextField redirectURI;

    @FXML CheckBox useKerberos;

    @FXML CheckBox useClientCertificate;

    @FXML
    ChoiceBox keyStoreSource;

    @FXML CheckBox allowUnrestrictedRedirect;

    @FXML
    Button executeButton;




    private JWTWindowModel jwtWindowModel;

    public TestInputWindow(JWTWindowModel jwtWindowModel) {
        this.jwtWindowModel = jwtWindowModel;

        setPadding(new Insets(4));
        Node node = FXUtils.loadAndInitialize("/fxml/InputPanel.fxml", this);
        setCenter(node);
        sessionName.textProperty().bindBidirectional(jwtWindowModel.sessionPropertyProperty());
        setTextLimit(sessionName,20);

    }




    @FXML
    public void execute(ActionEvent actionEvent){
        executeAction();
    }

    public void executeAction() {
        jwtWindowModel.getTaskModel().setMessage("Trying To Obtain Token Please Wait...");
        jwtWindowModel.getTaskModel().setRunning(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()->{jwtWindowModel.getTaskModel().setRunning(false); jwtWindowModel.setJWTToken("Foo");});
            }
        }).start();
    }

    private void setTextLimit(TextField textField, int limit){
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (textField.getText().length() >= limit) {
                    textField.setText(textField.getText().substring(0, limit));
                }
            }
        });
    }
}
