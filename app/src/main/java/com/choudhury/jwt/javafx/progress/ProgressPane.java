package com.choudhury.jwt.javafx.progress;

import com.choudhury.jwt.javafx.model.TaskModel;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProgressPane extends StackPane {
    private int DELAY_BEFORE_SHOWING_PROGRESS = 200;
    private TaskModel taskModel;
    private FadeTransition fadeIn;
    private FadeTransition fadeOut;
    private CloseableReentrantLock lock = new CloseableReentrantLock();
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Runnable checkTask;
    private final Runnable fadeInTask;
    private Text message;


    public ProgressPane(TaskModel taskModel) {
        this.taskModel = taskModel;
        init();
        fadeInTask = () -> {

            this.setVisible(true);
            fadeIn.play();

        };
        checkTask = () ->
        {
            try (CloseableReentrantLock ignored = lock.open()) {
                try {
                    Thread.sleep(DELAY_BEFORE_SHOWING_PROGRESS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Boolean isRunning = taskModel.runningProperty().getValue();
                if (isRunning) {
                    Platform.runLater(fadeInTask);
                }
            }
        };
    }

    private void init() {
        Region veil = new Region();
        veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85)");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(-1.0f);
        progressIndicator.setMaxSize(150, 150);
        ObservableList<Node> children = this.getChildren();
        progressIndicator.progressProperty().bind(taskModel.progressProperty());
        message = new Text("Please Wait...");
        message.setVisible(true);
        message.setStyle(" -fx-font-weight: normal; -fx-stroke: rgba(10,10,10,0); -fx-font-family: Helvetica; -fx-font-size: 25; -fx-font-smoothing-type: lcd; -fx-stroke-width: 1; -fx-fill: white;");
        //message.textProperty().bind(taskModel.messageProperty());
        children.addAll(veil, progressIndicator, message);
        this.setVisible(false);
        taskModel.runningProperty().addListener(this::runningPropertyChanged);

        fadeIn = new FadeTransition(Duration.millis(250), this);
        fadeIn.setFromValue(0.2);
        fadeIn.setToValue(1.0f);
        fadeIn.setInterpolator(Interpolator.EASE_IN);


        fadeOut = new FadeTransition(Duration.millis(250), this);
        fadeOut.setFromValue(1.0f);
        fadeOut.setInterpolator(Interpolator.EASE_OUT);
        fadeOut.setToValue(0.2);
        fadeOut.setOnFinished((e) -> {
                    this.setVisible(false);
                }
        );

    }

    private void runningPropertyChanged(ObservableValue<? extends Boolean> property, boolean oldValue, boolean newIsRunning) {
        try (CloseableReentrantLock ignored = lock.open()) {
            if (newIsRunning) {
                message.textProperty().setValue(taskModel.messageProperty().getValue());
                executorService.submit(checkTask);
            } else {
                fadeIn.stop();
                fadeOut.play();
            }
        }

    }


}
