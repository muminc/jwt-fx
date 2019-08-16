package com.choudhury.jwt.fx.editor;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

public class SignatureArea extends BorderPane {

    private final TextField signatureStatus;
    private final TextField errorMessage;

    private final BooleanProperty signatureOk = new SimpleBooleanProperty();
    private TextArea signatureTextArea;

    public SignatureArea() {

        signatureStatus = new TextField();
        signatureStatus.setEditable(false);

        errorMessage = new TextField();
        errorMessage.setEditable(false);
        errorMessage.setVisible(false);

        signatureTextArea = new TextArea();
        signatureTextArea.textProperty().addListener((observable, oldValue, newValue) -> updateSignatureStatus(signatureOk.get()));

        signatureOk.addListener((observable, oldValue, newValue) -> updateSignatureStatus(newValue));
        errorMessage.visibleProperty().bind(Bindings.isNotEmpty(errorMessage.textProperty()));


        updateSignatureStatus(signatureOk.get());
        VBox vBox = new VBox();
        vBox.getChildren().addAll(signatureStatus,errorMessage);

        setTop(vBox);
        setCenter(signatureTextArea);
    }

    private void updateSignatureStatus(boolean status) {
        if (StringUtils.isEmpty(signatureTextArea.getText())){
            signatureStatus.setText("Enter key to verify token signature");
            signatureStatus.setStyle("-fx-background-color: white; -fx-text-fill: black");
        }
        else if (status){
            signatureStatus.setText("Signature Verified");
            signatureStatus.setStyle("-fx-background-color: green; -fx-text-fill: white");
        }
        else {
            signatureStatus.setStyle("-fx-background-color: red; -fx-text-fill: white");
            signatureStatus.setText("Signature Check Failed !!!");
        }
    }

    public boolean isSignatureOk() {
        return signatureOk.get();
    }

    public BooleanProperty signatureOkProperty() {
        return signatureOk;
    }

    public StringProperty signatureProperty(){
        return signatureTextArea.textProperty();
    }

    public StringProperty errorMessageProperty(){
        return errorMessage.textProperty();
    }
}
