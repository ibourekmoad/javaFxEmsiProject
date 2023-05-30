module com.employerfx.employeeapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    requires poi;
    requires poi.ooxml;

    opens com.employerfx.employeeapp to javafx.fxml;
    opens com.employerfx.employeeapp.entities to javafx.base;
    exports com.employerfx.employeeapp;
    exports com.employerfx.employeeapp.view;
    opens com.employerfx.employeeapp.view to javafx.fxml;
}