module Kayla.main {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.rtkay to javafx.fxml;
    exports com.rtkay;
    exports com.rtkay.kayla to javafx.fxml;
    opens com.rtkay.kayla to javafx.fxml;
}