module com.example.icesp4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires jdk.compiler;
    requires javafx.media;
    requires java.desktop;
    requires java.prefs;

    requires java.sql;
    requires com.google.gson;

    opens com.example.icesp4 to javafx.fxml;
    opens com.example.icesp4.db to com.google.gson;
    exports com.example.icesp4;
    exports com.example.icesp4.db;

    exports SpaceInvaders;
    opens SpaceInvaders to javafx.fxml, javafx.graphics;

}