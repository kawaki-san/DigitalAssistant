module Kayla.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires com.jfoenix;
    requires AnimateFX;

    exports com.rtkay;
    exports com.rtkay.kayla to javafx.fxml;
    opens com.rtkay.kayla to javafx.fxml;
}