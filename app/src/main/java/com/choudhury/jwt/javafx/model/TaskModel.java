package com.choudhury.jwt.javafx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskModel {
    private SimpleStringProperty messageProperty=new SimpleStringProperty("");
    private SimpleBooleanProperty runningProperty=new SimpleBooleanProperty(false);
    private SimpleDoubleProperty progressProperty=new SimpleDoubleProperty(-1d);

    public void setRunning(boolean runningState)
    {
        this.runningProperty.setValue(runningState);
    }

    public BooleanProperty runningProperty()
    {
        return runningProperty;
    }

    public SimpleDoubleProperty progressProperty()
    {
        return progressProperty;
    }

    public void setMessage(String message)
    {
        this.messageProperty.setValue(message);
    }

    public StringProperty messageProperty()
    {
        return messageProperty;
    }

}
