package com.choudhury.jwt.fx.jwt;

import com.choudhury.jwt.fx.editor.ClaimsArea;
import com.choudhury.jwt.fx.editor.EditorArea;
import com.choudhury.jwt.fx.editor.SignatureArea;
import com.choudhury.jwt.fx.editor.TestInputWindow;
import com.choudhury.jwt.fx.jwt.model.JWTEditorModel;
import com.choudhury.jwt.fx.jwt.model.JWTWindowModel;
import com.choudhury.jwt.fx.model.TaskModel;
import com.choudhury.jwt.fx.progress.ProgressPane;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class JwtTokenPanel extends BorderPane {

    private TestInputWindow inputWindow;

    public JwtTokenPanel(JWTWindowModel jwtWindowModel) {
        SplitPane horizontalSplit = new SplitPane();
        horizontalSplit.setOrientation(Orientation.HORIZONTAL);



        TaskModel taskModel = jwtWindowModel.getTaskModel();
        SplitPane jwtParts = new SplitPane();
        jwtParts.setOrientation(Orientation.VERTICAL);
        TextArea algorithm = new TextArea();
        algorithm.setId("algorithm");
        ClaimsArea body = new ClaimsArea();
        SignatureArea signatureArea = new SignatureArea();

        algorithm.setEditable(false);
        jwtParts.getItems().add(algorithm);
        jwtParts.getItems().add(body);
        jwtParts.getItems().add(signatureArea);
        jwtParts.setDividerPositions(0.15, 0.7, 0.15);


        EditorArea textArea = new EditorArea();
        ObservableList<Node> horizontal = horizontalSplit.getItems();
        horizontal.add(textArea);
        horizontal.add(jwtParts);

        BorderPane mainContent = new BorderPane();
        inputWindow = new TestInputWindow(jwtWindowModel);
        //mainContent.setTop(inputWindow);
        mainContent.setCenter(horizontalSplit);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(mainContent, new ProgressPane(taskModel));


        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.F5) {

                    ke.consume(); // <-- stops passing the event to next node
                    inputWindow.executeAction();
                }
            }
        });

        setCenter(stackPane);
        JWTEditorModel model = new JWTEditorModel(textArea.caretSelectionBind(), algorithm.textProperty(), body.caretSelectionBind(), body.expireProperty(), signatureArea.signatureProperty(), signatureArea.signatureOkProperty(), signatureArea.errorMessageProperty());

        jwtWindowModel.setJwtEditorModel(model);
    }


    public void executeAction() {
        inputWindow.executeAction();
    }
}
