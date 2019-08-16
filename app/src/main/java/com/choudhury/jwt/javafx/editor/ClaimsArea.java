package com.choudhury.jwt.javafx.editor;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CaretSelectionBind;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import java.time.Duration;
import java.util.Collection;

public class ClaimsArea extends BorderPane {


    private final StyleClassedTextArea area;
    Label popupMsg = new Label();
    private StringProperty expireProperty = new SimpleStringProperty();

    public ClaimsArea() {


        area = new StyleClassedTextArea();

        area.getStylesheets().add(ClaimsArea.class.getResource("/css/manual-highlighting.css").toExternalForm());
        area.setStyle("-fx-font-size: 18pt;");

        area.setEditable(false);
        area.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                area.setStyleClass(0, newValue.length(), "blue");
            }
        });

        Popup popup = new Popup();

        popupMsg.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 5;");
        popup.getContent().add(popupMsg);

        area.setMouseOverTextDelay(Duration.ofMillis(300));
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
            int chIdx = e.getCharacterIndex();
            Point2D pos = e.getScreenPosition();
            popupMsg.textProperty().bind(Bindings.concat("Expires : ").concat(expireProperty));
            popup.show(area, pos.getX(), pos.getY() + 10);
        });
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            popup.hide();
        });

        VirtualizedScrollPane<StyleClassedTextArea> vsPane = new VirtualizedScrollPane<>(area);
        VBox.setVgrow(vsPane, Priority.ALWAYS);
        area.setWrapText(true);
        VBox vbox = new VBox(vsPane);


        TextField header = new TextField("Body");
        header.setEditable(false);
        setTop(header);

        setCenter(vbox);
    }

    public CaretSelectionBind<Collection<String>, String, Collection<String>> caretSelectionBind() {
        return area.getCaretSelectionBind();
    }

    public StringProperty expireProperty() {
        return expireProperty;
    }
}
