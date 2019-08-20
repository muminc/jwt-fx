package com.choudhury.jwt.fx.jwt.model;

import com.choudhury.jwt.fx.config.WindowSettings;
import com.choudhury.jwt.fx.jwt.api.JWTService;
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
    private final BooleanProperty nativeKeystore = new SimpleBooleanProperty();
    private final BooleanProperty allowCircularRedirect = new SimpleBooleanProperty();


    private TaskModel taskModel;
    private JWTEditorModel jwtEditorModel;
    private JWTService jwtService;

    public JWTWindowModel(JWTService jwtService, String session) {
        this.jwtService = jwtService;
        this.session.setValue(session);
        this.taskModel = new TaskModel();

    }

    public JWTWindowModel(JWTService jwtService, WindowSettings windowSettings) {
        this(jwtService, windowSettings.getSession());
        session.setValue(windowSettings.getSession());
        clientId.setValue(windowSettings.getClientId());
        scope.setValue(windowSettings.getScope());
        oauthURI.setValue(windowSettings.getOauthURI());
        redirectURI.setValue(windowSettings.getRedirectURI());
        kerberos.setValue(windowSettings.isKerberos());
        clientCertificate.setValue(windowSettings.isClientCertificate());
        nativeKeystore.set(windowSettings.isNativeKeyStore());
        allowCircularRedirect.setValue(windowSettings.isAllowCircularRedirect());
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


    public boolean isNativeKeystore() {
        return nativeKeystore.get();
    }

    public BooleanProperty nativeKeystoreProperty() {
        return nativeKeystore;
    }

    public boolean isAllowCircularRedirect() {
        return allowCircularRedirect.get();
    }

    public BooleanProperty allowCircularRedirectProperty() {
        return allowCircularRedirect;
    }

    public void updateConfig(WindowSettings windowSettings){
        windowSettings.setSession(session.getValue());
        windowSettings.setScope(scope.getValue());
        windowSettings.setClientId(clientId.getValue());
        windowSettings.setOauthURI(oauthURI.getValue());
        windowSettings.setRedirectURI(redirectURI.getValue());

        windowSettings.setKerberos(kerberos.getValue());
        windowSettings.setClientCertificate(clientCertificate.getValue());
        windowSettings.setNativeKeyStore(nativeKeystore.getValue());
        windowSettings.setAllowCircularRedirect(allowCircularRedirect.getValue());
    }

    public String obtainToken(){
        if (jwtService==null){
            throw new RuntimeException("No JWT Service Implementation has been registered");
        }
        return jwtService.obtainToken(getOauthURI(), getRedirectURI(), getClientId(), getScope(), isKerberos(), isClientCertificate(),isNativeKeystore(), isAllowCircularRedirect());
    }


}
