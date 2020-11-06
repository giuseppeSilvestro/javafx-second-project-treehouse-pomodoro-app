package com.teamtreehouse.pomodoro.controllers;

import com.teamtreehouse.pomodoro.model.Attempt;
import com.teamtreehouse.pomodoro.model.AttemptKind;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Home {

    @FXML private VBox container;
    @FXML private Label title;

    private Attempt currentAttempt;
    private StringProperty timerText;
    private Timeline timeline;

    public Home() {
        timerText = new SimpleStringProperty();
        setTimerText(0);
    }

    public String getTimerText() {
        return timerText.get();
    }

    public StringProperty timerTextProperty() {
        return timerText;
    }

    public void setTimerText(String timerText) {
        this.timerText.set(timerText);
    }

    public void setTimerText(int remainingSeconds) {
        int minutes = remainingSeconds/60;
        int seconds = remainingSeconds%60;
        setTimerText(String.format("%02d:%02d", minutes, seconds));
    }

    private void prepareAttempt(AttemptKind kind) {
        clearAttemptStyles();
        currentAttempt = new Attempt(kind, "");
        addAttemptStyle(kind);
        title.setText(kind.getDisplayName());
        setTimerText(currentAttempt.getRemainingSeconds());
        //TODO: this is creating multiple timelines we need to fix this!
        timeline = new Timeline();
        timeline.setCycleCount(kind.getTotalSeconds());
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            currentAttempt.tick();
            setTimerText(currentAttempt.getRemainingSeconds());
        }));
    }

    public void playTimer(){
        timeline.play();
    }

    public void pauseTimer(){
        timeline.pause();
    }

    private void addAttemptStyle(AttemptKind kind) {
        container.getStyleClass().add(kind.toString().toLowerCase());
    }

    private void clearAttemptStyles() {
        for (AttemptKind kind :
                AttemptKind.values()) {
            container.getStyleClass().remove(kind.toString().toLowerCase());
        }
    }


    public void DEBUG(ActionEvent actionEvent) {
        System.out.println("Hello!");
    }

    public void handleRestart(ActionEvent actionEvent) {
        prepareAttempt(AttemptKind.FOCUS);
        playTimer();
    }
}
