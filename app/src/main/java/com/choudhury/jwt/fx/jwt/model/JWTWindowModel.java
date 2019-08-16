package com.choudhury.jwt.fx.jwt.model;

import com.choudhury.jwt.fx.model.TaskModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JWTWindowModel {

    private StringProperty sessionProperty = new SimpleStringProperty();
    private TaskModel taskModel;
    private JWTEditorModel jwtEditorModel;

    public JWTWindowModel(String session) {
        sessionProperty.setValue(session);
        this.taskModel = new TaskModel();
    }



    public String getSessionProperty() {
        return sessionProperty.get();
    }

    public StringProperty sessionPropertyProperty() {
        return sessionProperty;
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
}
