package com.example.aeriboat;

import com.example.aeriboat.model.AirboatCommand;
import com.example.aeriboat.model.AirboatTelemetry;
import com.example.aeriboat.service.UdpClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Главный контроллер JavaFX для управления ESP32 airboat.
 * Обеспечивает пользовательский интерфейс для управления углом сервопривода,
 * скоростью BLDC мотора и отображения телеметрии.
 * 
 * @since 1.0
 */
public class AirboatController {
    
    // Элементы управления подключением
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectButton;
    @FXML
    private Button disconnectButton;
    @FXML
    private Circle connectionStatus;
    @FXML
    private Label connectionLabel;
    
    // Элементы управления углом сервопривода
    @FXML
    private Slider angleSlider;
    @FXML
    private Label angleValueLabel;
    
    // Элементы управления скоростью мотора
    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedValueLabel;
    
    // Кнопки управления
    @FXML
    private Button stopButton;
    @FXML
    private Button centerButton;
    
    // Элементы телеметрии
    @FXML
    private Label wifiSignalLabel;
    @FXML
    private Label batteryVoltageLabel;
    @FXML
    private Label latitudeLabel;
    @FXML
    private Label longitudeLabel;
    @FXML
    private Label courseLabel;
    @FXML
    private ProgressBar batteryProgressBar;
    @FXML
    private ProgressBar wifiSignalProgressBar;
    
    // Лог
    @FXML
    private TextArea logArea;
    
    private final UdpClient udpClient;
    private final ScheduledExecutorService scheduler;
    private int currentAngle = 90;
    private int currentSpeed = 0;
    
    public AirboatController() {
        this.udpClient = new UdpClient();
        this.scheduler = Executors.newScheduledThreadPool(2);
    }
    
    /**
     * Инициализация контроллера после загрузки FXML.
     */
    @FXML
    private void initialize() {
        // Настройка значений по умолчанию
        ipField.setText("192.168.4.1");
        portField.setText("12345");
        
        // Настройка слайдеров
        setupSliders();
        
        // Настройка состояния подключения
        updateConnectionStatus(false);
        
        // Логирование
        logMessage("Приложение запущено");
        logMessage("Для подключения введите IP и порт ESP32, затем нажмите 'Подключиться'");
        
        // Автоматический запрос телеметрии каждые 2 секунды
        scheduler.scheduleAtFixedRate(this::requestTelemetryIfConnected, 5, 2, TimeUnit.SECONDS);
    }
    
    /**
     * Настройка слайдеров управления.
     */
    private void setupSliders() {
        // Слайдер угла сервопривода (0-180°)
        angleSlider.setMin(0);
        angleSlider.setMax(180);
        angleSlider.setValue(90);
        angleSlider.setMajorTickUnit(30);
        angleSlider.setMinorTickCount(5);
        angleSlider.setShowTickLabels(true);
        angleSlider.setShowTickMarks(true);
        
        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentAngle = newVal.intValue();
            angleValueLabel.setText(currentAngle + "°");
            sendControlCommand();
        });
        
        // Слайдер скорости мотора (0-100%)
        speedSlider.setMin(0);
        speedSlider.setMax(100);
        speedSlider.setValue(0);
        speedSlider.setMajorTickUnit(20);
        speedSlider.setMinorTickCount(4);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentSpeed = newVal.intValue();
            speedValueLabel.setText(currentSpeed + "%");
            sendControlCommand();
        });
        
        // Установка начальных значений лейблов
        angleValueLabel.setText("90°");
        speedValueLabel.setText("0%");
    }
    
    /**
     * Обработчик кнопки подключения.
     */
    @FXML
    private void onConnectButtonClick() {
        try {
            String ip = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            
            if (ip.isEmpty()) {
                logMessage("Ошибка: Введите IP адрес ESP32");
                return;
            }
            
            udpClient.setConnectionParams(ip, port);
            boolean connected = udpClient.connect();
            
            if (connected) {
                updateConnectionStatus(true);
                logMessage("Подключено к " + ip + ":" + port);
            } else {
                logMessage("Ошибка подключения к " + ip + ":" + port);
            }
            
        } catch (NumberFormatException e) {
            logMessage("Ошибка: Некорректный порт");
        } catch (IOException e) {
            logMessage("Ошибка подключения: " + e.getMessage());
            updateConnectionStatus(false);
        }
    }
    
    /**
     * Обработчик кнопки отключения.
     */
    @FXML
    private void onDisconnectButtonClick() {
        udpClient.disconnect();
        updateConnectionStatus(false);
        logMessage("Отключено от ESP32");
    }
    
    /**
     * Обработчик кнопки стоп (устанавливает скорость в 0).
     */
    @FXML
    private void onStopButtonClick() {
        speedSlider.setValue(0);
        logMessage("Команда СТОП - скорость установлена в 0%");
    }
    
    /**
     * Обработчик кнопки центр (устанавливает угол в 90°).
     */
    @FXML
    private void onCenterButtonClick() {
        angleSlider.setValue(90);
        logMessage("Команда ЦЕНТР - угол установлен в 90°");
    }
    
    /**
     * Отправляет команду управления на ESP32.
     */
    private void sendControlCommand() {
        if (!udpClient.isConnected()) {
            return;
        }
        
        AirboatCommand command = new AirboatCommand(currentAngle, currentSpeed);
        udpClient.sendCommandAsync(command)
                .thenRun(() -> Platform.runLater(() -> 
                    logMessage("Отправлено: угол=" + currentAngle + "°, скорость=" + currentSpeed + "%")))
                .exceptionally(throwable -> {
                    Platform.runLater(() -> 
                        logMessage("Ошибка отправки команды: " + throwable.getMessage()));
                    return null;
                });
    }
    
    /**
     * Запрашивает телеметрию если подключение активно.
     */
    private void requestTelemetryIfConnected() {
        if (!udpClient.isConnected()) {
            return;
        }
        
        udpClient.requestTelemetryAsync()
                .thenAccept(telemetry -> Platform.runLater(() -> updateTelemetry(telemetry)))
                .exceptionally(throwable -> {
                    Platform.runLater(() -> 
                        logMessage("Ошибка получения телеметрии: " + throwable.getMessage()));
                    return null;
                });
    }
    
    /**
     * Обновляет отображение телеметрии.
     *
     * @param telemetry Данные телеметрии
     */
    private void updateTelemetry(AirboatTelemetry telemetry) {
        if (telemetry == null) {
            return;
        }
        
        // Обновляем лейблы
        wifiSignalLabel.setText(telemetry.getWifiSignalLevel() + " dBm");
        batteryVoltageLabel.setText(String.format("%.2f В", telemetry.getBatteryVoltageVolts()));
        latitudeLabel.setText(String.valueOf(telemetry.getLatitude()));
        longitudeLabel.setText(String.valueOf(telemetry.getLongitude()));
        courseLabel.setText(telemetry.getCourse() + "°");
        
        // Обновляем прогресс-бары
        // WiFi сигнал: -30 dBm (отличный) до -90 dBm (плохой)
        double wifiPercent = Math.max(0, Math.min(100, (90 + telemetry.getWifiSignalLevel()) / 60.0 * 100));
        wifiSignalProgressBar.setProgress(wifiPercent / 100.0);
        
        // Батарея: 3000-4200 мВ (типичный диапазон для Li-Po)
        double batteryPercent = Math.max(0, Math.min(100, 
            (telemetry.getBatteryVoltage() - 3000) / 1200.0 * 100));
        batteryProgressBar.setProgress(batteryPercent / 100.0);
    }
    
    /**
     * Обновляет статус подключения в интерфейсе.
     *
     * @param connected true если подключен
     */
    private void updateConnectionStatus(boolean connected) {
        if (connected) {
            connectionStatus.setFill(Color.GREEN);
            connectionLabel.setText("Подключено");
            connectButton.setDisable(true);
            disconnectButton.setDisable(false);
            ipField.setDisable(true);
            portField.setDisable(true);
        } else {
            connectionStatus.setFill(Color.RED);
            connectionLabel.setText("Отключено");
            connectButton.setDisable(false);
            disconnectButton.setDisable(true);
            ipField.setDisable(false);
            portField.setDisable(false);
        }
    }
    
    /**
     * Добавляет сообщение в лог.
     *
     * @param message Сообщение для логирования
     */
    private void logMessage(String message) {
        Platform.runLater(() -> {
            String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
            logArea.appendText("[" + timestamp + "] " + message + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }
    
    /**
     * Освобождает ресурсы при закрытии приложения.
     */
    public void shutdown() {
        udpClient.disconnect();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
} 