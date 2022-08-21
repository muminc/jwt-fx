package com.choudhury.jwt.fx.editor;

import com.choudhury.jwt.fx.FXUtils;
import com.choudhury.jwt.fx.jwt.model.JWTWindowModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class TestInputWindow extends BorderPane {

    // TODO had to make public to work with modules, fix this to be package protected

    @FXML public TextField sessionName;
    @FXML public TextField clientId;
    @FXML public TextField scope;
    @FXML public TextField oauthURI;
    @FXML public TextField redirectURI;

    @FXML public CheckBox useKerberos;

    @FXML public CheckBox useClientCertificate;

    @FXML public CheckBox nativeKeyStore;

    @FXML public CheckBox allowCircularRedirect;

    @FXML public Button executeButton;




    private JWTWindowModel jwtWindowModel;

    public TestInputWindow(JWTWindowModel jwtWindowModel)  {
        this.jwtWindowModel = jwtWindowModel;

        setPadding(new Insets(4));
        Node node = FXUtils.loadAndInitialize(TestInputWindow.class,"/com/choudhury/jwt/fx/editor/TestInputPanel.fxml", this);

        setCenter(node);
        sessionName.textProperty().bindBidirectional(jwtWindowModel.sessionProperty());
        clientId.textProperty().bindBidirectional(jwtWindowModel.clientIdProperty());
        scope.textProperty().bindBidirectional(jwtWindowModel.scopeProperty());
        oauthURI.textProperty().bindBidirectional(jwtWindowModel.oauthURIProperty());
        redirectURI.textProperty().bindBidirectional(jwtWindowModel.redirectURIProperty());
        useKerberos.selectedProperty().bindBidirectional(jwtWindowModel.kerberosProperty());
        useClientCertificate.selectedProperty().bindBidirectional(jwtWindowModel.clientCertificateProperty());
        nativeKeyStore.selectedProperty().bindBidirectional(jwtWindowModel.nativeKeystoreProperty());
        allowCircularRedirect.selectedProperty().bindBidirectional(jwtWindowModel.allowCircularRedirectProperty());
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
                    String token = jwtWindowModel.obtainToken();
                    Platform.runLater(()->{jwtWindowModel.getTaskModel().setRunning(false); jwtWindowModel.setJWTToken(token);});
                }
                catch (Exception e){
                    Platform.runLater(()-> {
                        jwtWindowModel.getTaskModel().setRunning(false);
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("Unable to obtain token");
                        errorAlert.setContentText(e.getLocalizedMessage());
                        errorAlert.showAndWait();
                    });

                }

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

