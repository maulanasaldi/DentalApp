module com.example.appsaldi {
    requires javafx.fxml;
    requires java.sql;
    requires org.jfxtras.styles.jmetro;
    requires java.prefs;
    requires java.desktop;
    requires jdk.jshell;
    requires java.management;
    requires org.controlsfx.controls;
    requires static lombok;
    requires annotations;
    requires jasperreports;


    opens com.example.appsaldi to javafx.fxml;
    exports com.example.appsaldi;
    exports com.example.appsaldi.controller;
    opens com.example.appsaldi.controller to javafx.fxml;

    opens com.example.appsaldi.model to javafx.base;
    exports com.example.appsaldi.model;
    exports com.example.appsaldi.controller.poopupcontroller;
    opens com.example.appsaldi.controller.poopupcontroller to javafx.fxml;
    exports com.example.appsaldi.controller.formcontroller;
    opens com.example.appsaldi.controller.formcontroller to javafx.fxml;
    exports com.example.appsaldi.dao;
    opens com.example.appsaldi.dao to javafx.base;
    exports com.example.appsaldi.controller.pengaturan;
    opens com.example.appsaldi.controller.pengaturan to javafx.fxml;
}