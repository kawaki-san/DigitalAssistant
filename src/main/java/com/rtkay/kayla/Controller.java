package com.rtkay.kayla;

import animatefx.animation.RotateIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.rtkay.audio.StreamMicAudio;
import com.rtkay.bot.KaylaEngine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.rtkay.Launcher.cachingClassLoader;
import static com.rtkay.bot.KaylaEngine.getTextResult;

public class Controller implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox chatBox;
    @FXML
    private JFXTextField txtUserInput;
    @FXML
    private JFXButton btnNotifications;
    @FXML
    private StackPane rootStackPane;

    private Service<Void> postContentThread;
    private JFXDialog listeningDialog;


    public void startListening(MouseEvent mouseEvent) throws IOException {
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
                        KaylaEngine.sendVoice(audioSession.getStreamFromMic());
                        return null;
                    }
                };
            }
        };
        postContentThread.setOnRunning(event -> {
            new RotateIn(loadingArcs).setCycleCount(1000).play(); //just set a high number for this, havent found a way to bind the actual data
        });

        audioSession.setDialog(listeningDialog);


        //bind the sent message property to the UI
        /*
         *node.textProperty().bind(postTextThread.messageProperty)
         * */

        postContentThread.restart();

    }

    private void initFocus() {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        btnNotifications.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                rootStackPane.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
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
                            KaylaEngine.sendText(content);
                            updateMessage(getTextResult().getMessage());
                        }
                        return null;
                    }
                };
            }
        };
        txtUserInput.clear();
        postContentThread.setOnSucceeded(event -> {
            //node.textProperty().unbind();
        });
        //bind the sent message property to the UI
        /*
         *node.textProperty().bind(postTextThread.messageProperty)
         * */
        postContentThread.restart();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        scrollPane.vvalueProperty().bind(chatBox.heightProperty());
        setFocusToRootContainer();
        KaylaEngine.BuildKayla();
    }

    private void setFocusToRootContainer() {
        initFocus();
    }

}
