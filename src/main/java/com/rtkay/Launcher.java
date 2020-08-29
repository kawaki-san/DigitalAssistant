package com.rtkay;

import com.rtkay.utils.MyClassLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Launcher extends Application {
    public static ClassLoader cachingClassLoader = new MyClassLoader(FXMLLoader.getDefaultClassLoader());

    @Override
    public void start(Stage stage) throws Exception {
        URL resource = getClass().getResource("main.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setClassLoader(cachingClassLoader);
        Parent root = loader.load();
        stage.setTitle("Kayla");
        stage.setScene(new Scene(root,410,710));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
