package com.example.aeriboat.service;

import com.example.aeriboat.model.AirboatCommand;
import com.example.aeriboat.model.AirboatTelemetry;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON сервис для сериализации/десериализации объектов с использованием Jackson.
 * Обеспечивает надежную работу с JSON данными и обработку ошибок.
 * 
 * @since 1.0
 */
public class JsonService {
    
    private final ObjectMapper objectMapper;
    
    public JsonService() {
        this.objectMapper = new ObjectMapper();
        // Не включать null значения в JSON
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    
    /**
     * Преобразует объект в JSON строку.
     *
     * @param object Объект для сериализации
     * @return JSON строка
     * @throws JsonProcessingException если произошла ошибка сериализации
     */
    public String toJson(Object object) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.err.println("Ошибка сериализации объекта в JSON: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Преобразует команду в JSON строку (для обратной совместимости).
     *
     * @param command Команда для сериализации
     * @return JSON строка
     */
    public String toJson(AirboatCommand command) {
        try {
            return toJson((Object) command);
        } catch (JsonProcessingException e) {
            // Возврат к простой реализации в случае ошибки
            System.err.println("Fallback к простой JSON сериализации");
            return createSimpleCommandJson(command);
        }
    }
    
    /**
     * Преобразует JSON строку в объект указанного типа.
     *
     * @param json JSON строка
     * @param clazz Класс для десериализации
     * @param <T> Тип возвращаемого объекта
     * @return Объект указанного типа
     * @throws IOException если произошла ошибка десериализации
     */
    public <T> T fromJson(String json, Class<T> clazz) throws IOException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            System.err.println("Ошибка десериализации JSON в объект " + clazz.getSimpleName() + ": " + e.getMessage());
            
            // Fallback для телеметрии
            if (clazz == AirboatTelemetry.class) {
                System.err.println("Fallback к простому парсингу телеметрии");
                return clazz.cast(parseSimpleTelemetry(json));
            }
            
            throw e;
        }
    }
    
    /**
     * Безопасное преобразование JSON в объект с обработкой ошибок.
     *
     * @param json JSON строка
     * @param clazz Класс для десериализации
     * @param <T> Тип возвращаемого объекта
     * @return Объект указанного типа или null при ошибке
     */
    public <T> T fromJsonSafe(String json, Class<T> clazz) {
        try {
            return fromJson(json, clazz);
        } catch (IOException e) {
            System.err.println("Не удалось десериализовать JSON: " + json);
            return null;
        }
    }
    
    /**
     * Простая реализация создания JSON для команды (fallback).
     *
     * @param command Команда
     * @return JSON строка
     */
    private String createSimpleCommandJson(AirboatCommand command) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        boolean hasElements = false;
        
        if (command.getAngle() != null) {
            json.append("\"angle\":").append(command.getAngle());
            hasElements = true;
        }
        
        if (command.getSpeed() != null) {
            if (hasElements) {
                json.append(",");
            }
            json.append("\"speed\":").append(command.getSpeed());
            hasElements = true;
        }
        
        if (command.getStatus() != null) {
            if (hasElements) {
                json.append(",");
            }
            json.append("\"status\":").append(command.getStatus());
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Простой парсинг телеметрии (fallback).
     *
     * @param json JSON строка
     * @return Объект телеметрии
     */
    private AirboatTelemetry parseSimpleTelemetry(String json) {
        AirboatTelemetry telemetry = new AirboatTelemetry();
        
        try {
            // Удаляем скобки и пробелы
            String content = json.trim().replaceAll("[{}\\s]", "");
            
            // Разделяем по запятым
            String[] pairs = content.split(",");
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].replaceAll("\"", "").trim();
                    String value = keyValue[1].replaceAll("\"", "").trim();
                    
                    try {
                        long longValue = Long.parseLong(value);
                        
                        switch (key) {
                            case "wifi_signal_level":
                                telemetry.setWifiSignalLevel(longValue);
                                break;
                            case "battery_voltage":
                                telemetry.setBatteryVoltage(longValue);
                                break;
                            case "latitude":
                                telemetry.setLatitude(longValue);
                                break;
                            case "longitude":
                                telemetry.setLongitude(longValue);
                                break;
                            case "course":
                                telemetry.setCourse(longValue);
                                break;
                        }
                    } catch (NumberFormatException e) {
                        // Игнорируем некорректные значения
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка в fallback парсинге телеметрии: " + e.getMessage());
        }
        
        return telemetry;
    }
    
    /**
     * Возвращает настроенный ObjectMapper для дополнительных операций.
     *
     * @return ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
} 