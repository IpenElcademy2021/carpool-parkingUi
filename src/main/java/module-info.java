module com.example.loginpage {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires json.simple;
    requires okhttp3;
    requires lombok;
    requires java.mail;
    requires slf4j.api;
    requires org.apache.poi.poi;


    opens com.example.loginpage to javafx.fxml;
    exports com.example.loginpage;
    exports com.example.loginpage.models;
}