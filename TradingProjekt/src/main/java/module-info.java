module com.example.javafxtest {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires json.simple;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.sql;

    opens com.example.javafxtest to javafx.fxml;
    exports com.example.javafxtest;
    exports InfoWindow;

    opens InfoWindow to javafx.fxml;
    exports ConfirmWindow;
    opens ConfirmWindow to javafx.fxml;

    exports ClosedTrades;
    opens ClosedTrades to javafx.fxml;
    exports Analyse;
    opens Analyse to javafx.fxml;

    exports ClosedTrades.InDepthStats;
    opens ClosedTrades.InDepthStats to javafx.fxml;



}