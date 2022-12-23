module com.application.launcher {
    requires javafx.controls;
    requires javafx.fxml;

    requires retrofit2;
    requires retrofit2.converter.gson;

    requires com.google.gson;
    requires java.desktop;
    requires okhttp3;

    opens com.application.launcher.rest.request to com.google.gson;
    opens com.application.launcher.rest.response to com.google.gson;

    opens com.application.launcher.controller to javafx.fxml;
    exports com.application.launcher.controller;

    opens com.application.launcher to javafx.fxml;
    exports com.application.launcher;
}