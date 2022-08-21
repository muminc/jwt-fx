package com.choudhury.jwt.fx.editor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CaretSelectionBind;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.Collection;

public class EditorArea extends BorderPane {
    private final StyleClassedTextArea area;

    public EditorArea() {

        area = new StyleClassedTextArea();
        area.getStylesheets().add(ClaimsArea.class.getResource("/com/choudhury/jwt/fx/css/manual-highlighting.css").toExternalForm());
        area.setId("token-input");
        area.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                area.clearStyle(0, newValue.length());
                int firstDotPosition = newValue.indexOf(".");
                if (firstDotPosition > -1) {
                    area.setStyleClass(0, firstDotPosition, "red");
                    int secondDotPosition = newValue.indexOf(".", firstDotPosition + 1);
                    if (secondDotPosition > -1) {
                        area.setStyleClass(firstDotPosition + 1, secondDotPosition, "blue");
                        area.setStyleClass(secondDotPosition + 1, newValue.length(), "green");
                    }
                }

            }
        });
        VirtualizedScrollPane<StyleClassedTextArea> vsPane = new VirtualizedScrollPane<>(area);
        VBox.setVgrow(vsPane, Priority.ALWAYS);
        area.setWrapText(true);
        VBox vbox = new VBox(vsPane);
        setCenter(vbox);
    }

    public CaretSelectionBind<Collection<String>, String, Collection<String>> caretSelectionBind() {
        return area.getCaretSelectionBind();
    }
}
