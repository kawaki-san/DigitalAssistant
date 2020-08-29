package com.rtkay.kayla;

import animatefx.animation.RotateIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.rtkay.Launcher.cachingClassLoader;

public class Controller implements Initializable {
    @FXML
    private StackPane rootStackPane;
    @FXML
    private Label label;
    private JFXDialog listeningDialog;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
       // label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }

    public void startListening(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource( "/com/rtkay/dialogs/listening_dialog.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setClassLoader(cachingClassLoader);
        GridPane root = loader.load();
        StackPane node = (StackPane) root.getChildren().get(1);
        AnchorPane loadingArcs = (AnchorPane) node.getChildren().get(0);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setBody(root);
        listeningDialog = new JFXDialog(rootStackPane,content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Close");
        button.setOnAction(event->{
            listeningDialog.close();
        });
        listeningDialog.show();
        new RotateIn(loadingArcs).setCycleCount(1000).play(); //just testing, will bind this to the actual listening/audio stream


    }



    public void sendText(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
