package com.choudhury.jwt.fx.jwt;

import com.choudhury.jwt.fx.TabPaneWithAdd;
import com.choudhury.jwt.fx.config.AppSettings;
import com.choudhury.jwt.fx.config.WindowSettings;
import com.choudhury.jwt.fx.jwt.model.JWTWindowModel;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class JWTTabPane extends TabPaneWithAdd {

    private static final String TAB_TITLE = "JWT Tab";
    private final EventHandler<ActionEvent> newTabAction;
    private int tabCounter = 1;
    private final AppSettings appSettings;

    public JWTTabPane(AppSettings appSettings) {
        this.appSettings = appSettings;
        setTabDragPolicy(TabDragPolicy.REORDER);

        newTabAction = newTabAction -> addNewTab(TAB_TITLE);
        final ObservableList<Tab> tabs = getTabs();
        tabs.addListener((ListChangeListener<Tab>) c -> {
            if (tabs.size() == 1) {
                tabs.get(0).setClosable(false);
            } else if (tabs.size() == 2) {
                for (Tab tab : tabs) {
                    tab.setClosable(true);
                }
            }
        });

        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.F5) {

                    ke.consume(); // <-- stops passing the event to next node
                    Tab selectedItem = getSelectionModel().getSelectedItem();
                    if (selectedItem instanceof JWTTokenTab) {
                        ((JWTTokenTab) selectedItem).executeAction();
                    }

                }
            }
        });

        List<WindowSettings> windowSettings = appSettings.getWindowSettings();
        for (WindowSettings windowSetting : windowSettings) {
            addNewTab(windowSetting);
        }

        if (tabs.size()==0){
            addNewTab(TAB_TITLE);
        }
    }

    @Override
    public EventHandler<ActionEvent> getNewTabAction() {
        return newTabAction;
    }

    private void addNewTab(String title) {
        ObservableList<Tab> tabs = this.getTabs();
        JWTTokenTab tab = new JWTTokenTab(title + " " + tabCounter++);
        tab.setClosable(tabs.size() > 1);
        tabs.add(tab);
        this.getSelectionModel().select(tab);
        Platform.runLater(() -> this.requestFocus());

    }

    private void addNewTab(WindowSettings windowSettings) {
        ObservableList<Tab> tabs = this.getTabs();
        JWTWindowModel jwtWindowModel = new JWTWindowModel(windowSettings);
        JWTTokenTab tab = new JWTTokenTab(jwtWindowModel);
        tab.setClosable(tabs.size() > 1);
        tabs.add(tab);
        this.getSelectionModel().select(tab);
    }

    public void updateConfig(AppSettings appSettings) {

        List<WindowSettings> windowSettingsList = appSettings.getWindowSettings();
        ObservableList<Tab> tabs = getTabs();
        for (Tab tab : tabs) {
            WindowSettings windowSettings = new WindowSettings();
            ((JWTTokenTab)tab).updateConfig(windowSettings);
            windowSettingsList.add(windowSettings);
        }
    }
}
