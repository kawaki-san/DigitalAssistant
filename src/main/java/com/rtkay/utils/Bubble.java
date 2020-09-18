package com.rtkay.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

public class Bubble extends HBox {
    private Color DEFAULT_SENDER_COLOR = Color.rgb(0, 126, 229);
    private Color DEFAULT_RECEIVER_COLOR = Color.LIGHTGRAY;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND;
    private String message;
    private SpeechDirection direction;
    private Label displayedText;
    private SVGPath directionIndicator;
    private DropShadow dropShadow = new DropShadow();


    public Bubble(String message, SpeechDirection direction) {
        this.message = message;
        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }

    private void initialiseDefaults() {
        DEFAULT_SENDER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_SENDER_COLOR, new CornerRadii(5, 0, 5, 5, false), Insets.EMPTY));
        DEFAULT_RECEIVER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_RECEIVER_COLOR, new CornerRadii(0, 5, 5, 5, false), Insets.EMPTY));
    }

    private void setupElements() {
        dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        displayedText = new Label(message);
        displayedText.setMinHeight(Region.USE_PREF_SIZE);
        displayedText.setWrapText(true);
        displayedText.setFont(new Font("Inter V", 14));
        displayedText.setPadding(new Insets(5));
        directionIndicator = new SVGPath();
        if (direction == SpeechDirection.LEFT) {
            configureForReceiver();
        } else if(direction== SpeechDirection.RIGHT) {
            configureForSender();
        } else {
            configureNewDate();
        }
    }

    private void configureForSender() {
        displayedText.setBackground(DEFAULT_SENDER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        displayedText.setTextFill(Color.rgb(255, 255, 255));
        VBox vBox = new VBox(2);
        HBox time = new HBox();
        vBox.getChildren().add(displayedText);
        time.setAlignment(Pos.BASELINE_RIGHT);
        Label label = new Label("13:05");
        label.setFont(new Font("Inter V", 12));
        time.getChildren().add(label);
        vBox.getChildren().add(time);
        directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SENDER_COLOR);
        HBox container = new HBox(vBox, directionIndicator);
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        vBox.getChildren().get(0).setEffect(dropShadow);
        setAlignment(Pos.CENTER_RIGHT);
    }

    private void configureForReceiver() {
        displayedText.setBackground(DEFAULT_RECEIVER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_LEFT);
        directionIndicator.setContent("M0 0 L10 0 L10 10 Z");
        directionIndicator.setFill(DEFAULT_RECEIVER_COLOR);
        HBox container = new HBox(directionIndicator, displayedText);
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_LEFT);
    }
    private void configureNewDate(){
        displayedText.setAlignment(Pos.CENTER);
        HBox container = new HBox(displayedText);
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER);
    }
}