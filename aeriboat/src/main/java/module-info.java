module com.example.aeriboat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    
    // Jackson JSON библиотека
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens com.example.aeriboat to javafx.fxml;
    
    // Открываем модели для JavaFX и Jackson
    opens com.example.aeriboat.model to javafx.base, com.fasterxml.jackson.databind;
    
    exports com.example.aeriboat;
}