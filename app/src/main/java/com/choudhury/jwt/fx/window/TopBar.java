package com.choudhury.jwt.fx.window;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * The Top bar of the application Window.
 *
 * @author GOXR3PLUS
 */
public class TopBar extends BorderPane {

  // ----------------------------------------------

  @FXML
  private Label label;

  // ----------------------------------------------

  /** The logger. */
  private Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Constructor.
   */
  public TopBar() {

    //---------------------FXML LOADER---------------------------------
    URL resource = getClass().getResource( "/fxml/TopBar.fxml");
    FXMLLoader loader = new FXMLLoader(resource);
    loader.setController(this);
    loader.setRoot(this);



    try {
      loader.load();
    } catch (IOException ex) {
      logger.log(Level.WARNING, "", ex);
    }
  }

  /**
   * Called as soon as .fxml is initialized [[SuppressWarningsSpartan]]
   */
  @FXML
  private void initialize() {
//    ImageView node = new ImageView("/images/ticket-20.png");
//    node.setStyle("-fx-border-width: 1px");
//    HBox hBox = new HBox(new VBox(2),node);
//    hBox.setSpacing(5);
//    hBox.setAlignment(Pos.CENTER_RIGHT);
//    setLeft(hBox);
    //Root
    setRight(new CloseAppBox());


  }

  public void setTitle(String title) {
    label.setText(title);
  }

  public StringProperty textProperty(){
    return label.textProperty();
  }

}
