package com.rtkay.kayla;

import animatefx.animation.RotateIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.rtkay.audio.StreamMicAudio;
import com.rtkay.bot.KaylaEngine;
import com.rtkay.utils.Bubble;
import com.rtkay.utils.SpeechDirection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.rtkay.Launcher.cachingClassLoader;
import static com.rtkay.bot.KaylaEngine.getContentResult;
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
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();


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
        postContentThread.setOnSucceeded(event -> {
            speechBubbles.add(new Bubble(getContentResult().getInputTranscript(), SpeechDirection.RIGHT));
            speechBubbles.add(new Bubble(getContentResult().getMessage(), SpeechDirection.LEFT));
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
        speechBubbles.add(new Bubble(content, SpeechDirection.RIGHT));
        postContentThread.setOnSucceeded(event -> {
            speechBubbles.add(new Bubble(getTextResult().getMessage(), SpeechDirection.LEFT));
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
        setFocusToRootContainer();
        KaylaEngine.BuildKayla();
        initUI();
        speechBubbles.add(new Bubble("Lorem ipsum dolor sit amet.", SpeechDirection.RIGHT));

        for (int i = 0; i <100 ; i++) {
            if(i%2==0){
                speechBubbles.add(new Bubble("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce rhoncus quam mauris, a gravida enim tincidunt sit amet. ", SpeechDirection.RIGHT));

            } else {
                speechBubbles.add(new Bubble("In vel metus nec velit fermentum accumsan. Nulla aliquam nulla eu lectus molestie ullamcorper. Etiam ut eros eu augue tristique ultrices. Curabitur sit amet quam dignissim, tincidunt nisi nec, egestas eros. It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.", SpeechDirection.LEFT));
            }
        }
        speechBubbles.add(new Bubble("Lorem ipsum dolor sit amet.", SpeechDirection.LEFT));


    }

    private void initUI() {
        chatBox.setSpacing(10);
        Bindings.bindContentBidirectional(speechBubbles, chatBox.getChildren());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.prefWidthProperty().bind(chatBox.prefWidthProperty().subtract(5));
        scrollPane.setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if(change.wasAdded()){
                    scrollPane.setVvalue(scrollPane.getVmax());
                }
            }
        });
    }

    private void setFocusToRootContainer() {
        initFocus();
    }

}
