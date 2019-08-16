package com.choudhury.jwt.fx.window;


import java.io.IOException;

import com.choudhury.jwt.fx.MainApp;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class CloseAppBox extends StackPane {

  //--------------------------------------------------------------

  @FXML
  private JFXButton minimize;

  @FXML
  private JFXButton maxOrNormalize;

  @FXML
  private JFXButton exitApplication;

  /**
   * Constructor.
   */
  public CloseAppBox() {

    // ------------------------------------FXMLLOADER ----------------------------------------
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CloseAppBox.fxml"));
    loader.setController(this);
    loader.setRoot(this);

    try {
      loader.load();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Called as soon as fxml is initialized
   */
  @FXML
  private void initialize() {

    // minimize
    minimize.setOnAction(ac -> MainApp.window.setIconified(true));

    // maximize_normalize
    maxOrNormalize.setOnAction(ac -> MainApp.borderlessScene.maximizeStage());

    // close
    exitApplication.setOnAction(ac -> System.exit(0));

  }

}
