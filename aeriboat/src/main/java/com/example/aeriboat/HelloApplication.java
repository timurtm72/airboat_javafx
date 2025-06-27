package com.example.aeriboat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Главное приложение для управления ESP32 airboat.
 * Загружает интерфейс и инициализирует основные компоненты системы.
 * 
 * @since 1.0
 */
public class HelloApplication extends Application {
    
    private AirboatController controller;
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("airboat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        
        // Получаем контроллер для корректного завершения приложения
        controller = fxmlLoader.getController();
        
        stage.setTitle("ESP32 Airboat Control System v1.0");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(700);
        
        // Обработчик закрытия приложения
        stage.setOnCloseRequest(event -> {
            if (controller != null) {
                controller.shutdown();
            }
        });
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}