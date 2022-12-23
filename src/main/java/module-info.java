module com.application.launcher {
    requires javafx.controls;
    requires javafx.fxml;

    requires retrofit2;
    requires retrofit2.converter.gson;

    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpmime;
    requires java.desktop;
    requires okhttp3;

    opens com.application.launcher.rest.request to com.google.gson;
    opens com.application.launcher.rest.response to com.google.gson;

    opens com.application.launcher.dto to com.google.gson;
    exports com.application.launcher.dto;

    opens com.application.launcher.entity to com.google.gson;
    exports com.application.launcher.entity;

    opens com.application.launcher.controller to javafx.fxml;
    exports com.application.launcher.controller;

    opens com.application.launcher to javafx.fxml;
    exports com.application.launcher;
    exports com.application.launcher.utils;
    opens com.application.launcher.utils to javafx.fxml;
}