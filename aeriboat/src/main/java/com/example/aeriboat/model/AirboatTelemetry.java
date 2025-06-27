package com.example.aeriboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Модель телеметрии от ESP32 airboat контроллера.
 * Содержит данные о состоянии устройства.
 * 
 * @since 1.0
 */
public class AirboatTelemetry {
    
    @JsonProperty("wifi_signal_level")
    private long wifiSignalLevel;  // Уровень сигнала Wi-Fi
    
    @JsonProperty("battery_voltage")
    private long batteryVoltage;   // Напряжение батареи (мВ)
    
    @JsonProperty("latitude")
    private long latitude;         // Широта (GPS)
    
    @JsonProperty("longitude")
    private long longitude;        // Долгота (GPS)
    
    @JsonProperty("course")
    private long course;           // Курс (0-360°)
    
    public AirboatTelemetry() {
    }
    
    public long getWifiSignalLevel() {
        return wifiSignalLevel;
    }
    
    public void setWifiSignalLevel(long wifiSignalLevel) {
        this.wifiSignalLevel = wifiSignalLevel;
    }
    
    public long getBatteryVoltage() {
        return batteryVoltage;
    }
    
    public void setBatteryVoltage(long batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }
    
    public long getLatitude() {
        return latitude;
    }
    
    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
    
    public long getLongitude() {
        return longitude;
    }
    
    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
    
    public long getCourse() {
        return course;
    }
    
    public void setCourse(long course) {
        this.course = course;
    }
    
    /**
     * Возвращает напряжение батареи в вольтах.
     *
     * @return Напряжение в вольтах
     */
    public double getBatteryVoltageVolts() {
        return batteryVoltage / 1000.0;
    }
    
    @Override
    public String toString() {
        return "AirboatTelemetry{" +
                "wifiSignalLevel=" + wifiSignalLevel +
                ", batteryVoltage=" + batteryVoltage +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", course=" + course +
                '}';
    }
} 