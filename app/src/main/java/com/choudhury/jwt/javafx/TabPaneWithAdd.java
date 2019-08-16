package com.choudhury.jwt.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TabPane;

public abstract class TabPaneWithAdd extends TabPane {
    public abstract EventHandler<ActionEvent> getNewTabAction();
}
