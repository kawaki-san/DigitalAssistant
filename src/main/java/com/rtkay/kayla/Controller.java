package com.rtkay.kayla;

import animatefx.animation.AnimationFX;
import animatefx.animation.RotateIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import com.rtkay.audio.StreamMicAudio;
import com.rtkay.bot.KaylaEngine;
import com.rtkay.utils.Bubble;
import com.rtkay.utils.SpeechDirection;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.rtkay.Launcher.cachingClassLoader;

public class Controller implements Initializable {
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox chatBox;
    @FXML
    private TextField txtUserInput;
    @FXML
    private StackPane rootStackPane;
    private KaylaEngine kayla;
    private Service<Void> postContentThread;
    private JFXDialog listeningDialog;
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();


    @FXML
    private void startListening(Event mouseEvent) throws IOException {
        URL resource = getClass().getResource("/com/rtkay/dialogs/listening_dialog.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setClassLoader(cachingClassLoader);
        GridPane root = loader.load();
        StackPane node = (StackPane) root.getChildren().get(1);
        AnchorPane loadingArcs = (AnchorPane) node.getChildren().get(0);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setBody(root);
        listeningDialog = new JFXDialog(rootStackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton button = new JFXButton("Close");
        button.setOnAction(event -> {
            listeningDialog.close();
        });
        listeningDialog.setEffect(null);
        listeningDialog.show();
        final StreamMicAudio audioSession = new StreamMicAudio();
        postContentThread = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws LineUnavailableException {
                        kayla.sendVoice(audioSession.getStreamFromMic());
                        return null;
                    }
                };
            }
        };
        progressBar.visibleProperty().bind(postContentThread.runningProperty());
        postContentThread.setOnRunning(event -> {
            new RotateIn(loadingArcs).setCycleCount(AnimationFX.INDEFINITE).play();
        });
        postContentThread.setOnSucceeded(event -> {
            speechBubbles.add(new Bubble(kayla.getContentResult().getInputTranscript(), SpeechDirection.RIGHT));
            speechBubbles.add(new Bubble(kayla.getContentResult().getMessage(), SpeechDirection.LEFT));
        });
        audioSession.setDialog(listeningDialog);
        postContentThread.restart();

    }

    public void sendText(ActionEvent actionEvent) {
        String content = txtUserInput.getText();
        postContentThread = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        if (!content.isEmpty()) {
                            kayla.sendText(content);
                            updateMessage(kayla.getTextResult().getMessage());
                        }
                        return null;
                    }
                };
            }
        };
        speechBubbles.add(new Bubble(content, SpeechDirection.RIGHT));
        progressBar.visibleProperty().bind(postContentThread.runningProperty());
        progressBar.progressProperty().bind(postContentThread.progressProperty());
        txtUserInput.clear();
        postContentThread.messageProperty().addListener((observable, oldValue, newValue) -> {

            speechBubbles.add(new Bubble(newValue, SpeechDirection.LEFT));
        });
        //bind the sent message property to the UI
        postContentThread.restart();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        kayla = new KaylaEngine();
        initUI();
     progressBar.setVisible(false);
    }

    private void initUI() {
        scrollPane.setPadding(new Insets(10));
        chatBox.setSpacing(16);
        Bindings.bindContentBidirectional(speechBubbles, chatBox.getChildren());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.prefWidthProperty().bind(chatBox.prefWidthProperty().subtract(5));
        scrollPane.setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    scrollPane.vvalueProperty().bind(chatBox.heightProperty());
                }
            }
        });
    }
}
