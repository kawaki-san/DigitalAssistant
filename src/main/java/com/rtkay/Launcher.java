package com.rtkay;

import com.rtkay.utils.MyClassLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.net.URL;


public class Launcher extends Application {
    public static ClassLoader cachingClassLoader = new MyClassLoader(FXMLLoader.getDefaultClassLoader());

    @Override
    public void start(Stage stage) throws Exception {
        JMetro jMetro = new JMetro(Style.DARK);
        URL resource = getClass().getResource("main.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setClassLoader(cachingClassLoader);
        Parent root = loader.load();
        stage.setTitle("Kayla");
        stage.setOpacity(0.85);
        Scene scene = new Scene(root, 400, 800);
        jMetro.setScene(scene);
        jMetro.setAutomaticallyColorPanes(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
