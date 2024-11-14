module com.imranpranto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires itextpdf;

    opens com.imranpranto to javafx.fxml;
    exports com.imranpranto;
    exports com.imranpranto.data;
    exports com.imranpranto.model;
 
}
