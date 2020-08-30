module Kayla.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires com.jfoenix;
    requires AnimateFX;
    requires aws.java.sdk.lex;
    requires aws.java.sdk.core;
    requires jlayer;
    requires java.desktop;
    requires java.xml.bind;

    exports com.rtkay;
    exports com.rtkay.kayla to javafx.fxml;
    opens com.rtkay.kayla to javafx.fxml;
}