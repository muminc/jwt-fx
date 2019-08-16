package com.choudhury.jwt.javafx;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class AddButtonOverlay extends BorderPane {

  public AddButtonOverlay(TabPaneWithAdd tabPaneWithAdd) {
    final Button addButton = new Button("+");
    addButton.setOnAction(tabPaneWithAdd.getNewTabAction());

    AnchorPane anchor = new AnchorPane();

    anchor.getChildren().addAll(addButton);

    AnchorPane.setTopAnchor(addButton, 1.0);
    AnchorPane.setRightAnchor(addButton, 1.0);
    anchor.setPickOnBounds(false);

    StackPane stackPane = new StackPane(tabPaneWithAdd);
    stackPane.getChildren().add(anchor);
    stackPane.setPickOnBounds(false);

    setCenter(stackPane);
  }


}

