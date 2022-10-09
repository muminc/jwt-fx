package com.choudhury.jwt.fx.editor;

import com.choudhury.jwt.fx.FXUtils;
import com.choudhury.jwt.fx.jwt.model.GrantType;
import com.choudhury.jwt.fx.jwt.model.JWTWindowModel;
import com.choudhury.jwt.fx.model.TaskModel;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;



public class TestInputWindow extends BorderPane {


    @FXML TextField sessionName;
    @FXML TextField clientId;
    @FXML TextField scope;
    @FXML TextField oauthURI;

    @FXML TextField tokenURI;

    @FXML TextField redirectURI;

    @FXML CheckBox useKerberos;

    @FXML CheckBox useClientCertificate;

    @FXML CheckBox nativeKeyStore;

    @FXML CheckBox allowCircularRedirect;

    @FXML Button executeButton;

    @FXML ChoiceBox<GrantType> grantTypes;



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
        tokenURI.textProperty().bindBidirectional(jwtWindowModel.tokenURIProperty());
        redirectURI.textProperty().bindBidirectional(jwtWindowModel.redirectURIProperty());
        useKerberos.selectedProperty().bindBidirectional(jwtWindowModel.kerberosProperty());
        useClientCertificate.selectedProperty().bindBidirectional(jwtWindowModel.clientCertificateProperty());
        nativeKeyStore.selectedProperty().bindBidirectional(jwtWindowModel.nativeKeystoreProperty());
        allowCircularRedirect.selectedProperty().bindBidirectional(jwtWindowModel.allowCircularRedirectProperty());
        grantTypes.setItems(jwtWindowModel.getGrantTypes());
        grantTypes.setValue(jwtWindowModel.getGrantType());
        grantTypes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> jwtWindowModel.grantTypeProperty().setValue(newValue));


        setTextLimit(sessionName,20);
    }




    @FXML
    public void execute(ActionEvent actionEvent){
        executeAction();
    }

    public void executeAction() {
        TaskModel taskModel = jwtWindowModel.getTaskModel();
        taskModel.setMessage("Trying To Obtain Token Please Wait...");
        taskModel.setRunning(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String token = jwtWindowModel.obtainToken();
                    Platform.runLater(()->{
                        taskModel.setRunning(false); jwtWindowModel.setJWTToken(token);});
                }
                catch (Exception e){
                    Platform.runLater(()-> {
                        taskModel.setRunning(false);
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

