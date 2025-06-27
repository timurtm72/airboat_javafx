package com.example.aeriboat.service;

import com.example.aeriboat.model.AirboatCommand;
import com.example.aeriboat.model.AirboatTelemetry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;


/**
 * UDP клиент для связи с ESP32 airboat контроллером.
 * Обеспечивает отправку команд управления и получение телеметрии.
 * 
 * @since 1.0
 */
public class UdpClient {
    

    private static final int DEFAULT_PORT = 12345;
    private static final String DEFAULT_IP = "192.168.4.1";
    private static final int BUFFER_SIZE = 1024;
    private static final int SOCKET_TIMEOUT = 5000; // 5 секунд
    
    private final JsonService jsonService;
    private DatagramSocket socket;
    private String esp32Ip;
    private int esp32Port;
    private boolean connected;
    
    public UdpClient() {
        this.jsonService = new JsonService();
        this.esp32Ip = DEFAULT_IP;
        this.esp32Port = DEFAULT_PORT;
        this.connected = false;
    }
    
    /**
     * Устанавливает параметры подключения к ESP32.
     *
     * @param ip IP адрес ESP32
     * @param port UDP порт ESP32
     */
    public void setConnectionParams(String ip, int port) {
        this.esp32Ip = ip;
        this.esp32Port = port;
    }
    
    /**
     * Устанавливает соединение с ESP32.
     *
     * @return true если соединение установлено успешно
     * @throws IOException если произошла ошибка при создании сокета
     */
    public boolean connect() throws IOException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            
            socket = new DatagramSocket();
            socket.setSoTimeout(SOCKET_TIMEOUT);
            connected = true;
            System.out.println("UDP соединение установлено с " + esp32Ip + ":" + esp32Port);
            return true;
        } catch (IOException e) {
            connected = false;
            System.err.println("Ошибка установки UDP соединения: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Закрывает соединение с ESP32.
     */
    public void disconnect() {
        connected = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("UDP соединение закрыто");
        }
    }
    
    /**
     * Отправляет команду на ESP32 асинхронно.
     *
     * @param command Команда для отправки
     * @return CompletableFuture который завершится при отправке
     */
    public CompletableFuture<Void> sendCommandAsync(AirboatCommand command) {
        return CompletableFuture.runAsync(() -> {
            try {
                sendCommand(command);
            } catch (IOException e) {
                System.err.println("Ошибка отправки команды: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Отправляет команду на ESP32.
     *
     * @param command Команда для отправки
     * @throws IOException если произошла ошибка при отправке
     */
    public void sendCommand(AirboatCommand command) throws IOException {
        if (!connected || socket == null || socket.isClosed()) {
            throw new IOException("UDP соединение не установлено");
        }
        
        try {
            String jsonCommand = jsonService.toJson(command);
            byte[] buffer = jsonCommand.getBytes();
            
            InetAddress address = InetAddress.getByName(esp32Ip);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, esp32Port);
            
            socket.send(packet);
            System.out.println("Отправлена команда: " + jsonCommand);
            
        } catch (Exception e) {
            System.err.println("Ошибка отправки команды: " + command + ", " + e.getMessage());
            throw new IOException("Не удалось отправить команду", e);
        }
    }
    
    /**
     * Запрашивает телеметрию с ESP32 асинхронно.
     *
     * @return CompletableFuture с телеметрией
     */
    public CompletableFuture<AirboatTelemetry> requestTelemetryAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return requestTelemetry();
            } catch (IOException e) {
                System.err.println("Ошибка запроса телеметрии: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Запрашивает телеметрию с ESP32.
     *
     * @return Телеметрия от ESP32 или null если произошла ошибка
     * @throws IOException если произошла ошибка при запросе
     */
    public AirboatTelemetry requestTelemetry() throws IOException {
        if (!connected || socket == null || socket.isClosed()) {
            throw new IOException("UDP соединение не установлено");
        }
        
        try {
            // Отправляем запрос телеметрии
            AirboatCommand telemetryRequest = new AirboatCommand(true);
            sendCommand(telemetryRequest);
            
            // Ожидаем ответ
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            socket.receive(packet);
            
            String response = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Получена телеметрия: " + response);
            
            return jsonService.fromJsonSafe(response, AirboatTelemetry.class);
            
        } catch (SocketTimeoutException e) {
            System.err.println("Таймаут при запросе телеметрии");
            return null;
        } catch (IOException e) {
            System.err.println("Ошибка запроса телеметрии: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Проверяет состояние соединения.
     *
     * @return true если соединение активно
     */
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }
    
    /**
     * Возвращает текущий IP адрес ESP32.
     *
     * @return IP адрес
     */
    public String getEsp32Ip() {
        return esp32Ip;
    }
    
    /**
     * Возвращает текущий порт ESP32.
     *
     * @return UDP порт
     */
    public int getEsp32Port() {
        return esp32Port;
    }
} 