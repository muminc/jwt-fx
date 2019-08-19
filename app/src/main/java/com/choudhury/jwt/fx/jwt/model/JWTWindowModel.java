package com.choudhury.jwt.fx.jwt.model;

import com.choudhury.jwt.fx.config.WindowSettings;
import com.choudhury.jwt.fx.model.TaskModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JWTWindowModel {

    private final StringProperty session = new SimpleStringProperty();
    private final StringProperty scope = new SimpleStringProperty();
    private final StringProperty clientId = new SimpleStringProperty();
    private final StringProperty oauthURI = new SimpleStringProperty();
    private final StringProperty redirectURI = new SimpleStringProperty();
    private final BooleanProperty kerberos = new SimpleBooleanProperty();
    private final BooleanProperty clientCertificate = new SimpleBooleanProperty();
    private final BooleanProperty unrestrictedRedirect = new SimpleBooleanProperty();


    private TaskModel taskModel;
    private JWTEditorModel jwtEditorModel;

    public JWTWindowModel(String session) {
        this.session.setValue(session);
        this.taskModel = new TaskModel();
    }

    public JWTWindowModel(WindowSettings windowSettings) {
        session.setValue(windowSettings.getSession());
        clientId.setValue(windowSettings.getClientId());
        scope.setValue(windowSettings.getScope());
        oauthURI.setValue(windowSettings.getOauthURI());
        redirectURI.setValue(windowSettings.getRedirectURI());
        kerberos.setValue(windowSettings.isKerberos());
        clientCertificate.setValue(windowSettings.isClientCertificate());
        unrestrictedRedirect.setValue(windowSettings.isUnrestrictedRedirect());
        this.taskModel = new TaskModel();
    }



    public String getSession() {
        return session.get();
    }

    public StringProperty sessionProperty() {
        return session;
    }

    public TaskModel getTaskModel() {
        return taskModel;
    }

    public void setJWTToken(String token){
        jwtEditorModel.setToken(token);
    }

    public void setJwtEditorModel(JWTEditorModel jwtEditorModel) {
        this.jwtEditorModel = jwtEditorModel;
    }

    public String getScope() {
        return scope.get();
    }

    public StringProperty scopeProperty() {
        return scope;
    }

    public String getClientId() {
        return clientId.get();
    }

    public StringProperty clientIdProperty() {
        return clientId;
    }

    public String getOauthURI() {
        return oauthURI.get();
    }

    public StringProperty oauthURIProperty() {
        return oauthURI;
    }

    public String getRedirectURI() {
        return redirectURI.get();
    }

    public StringProperty redirectURIProperty() {
        return redirectURI;
    }

    public boolean isKerberos() {
        return kerberos.get();
    }

    public BooleanProperty kerberosProperty() {
        return kerberos;
    }

    public boolean isClientCertificate() {
        return clientCertificate.get();
    }

    public BooleanProperty clientCertificateProperty() {
        return clientCertificate;
    }

    public boolean isUnrestrictedRedirect() {
        return unrestrictedRedirect.get();
    }

    public BooleanProperty unrestrictedRedirectProperty() {
        return unrestrictedRedirect;
    }

    public void updateConfig(WindowSettings windowSettings){
        windowSettings.setSession(session.getValue());
        windowSettings.setScope(scope.getValue());
        windowSettings.setClientId(clientId.getValue());
        windowSettings.setOauthURI(oauthURI.getValue());
        windowSettings.setRedirectURI(redirectURI.getValue());

        windowSettings.setKerberos(kerberos.getValue());
        windowSettings.setClientCertificate(clientCertificate.getValue());
        windowSettings.setUnrestrictedRedirect(unrestrictedRedirect.getValue());
    }


}
