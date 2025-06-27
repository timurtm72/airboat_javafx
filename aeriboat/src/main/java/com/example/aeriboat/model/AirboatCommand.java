package com.example.aeriboat.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Модель команды для управления ESP32 airboat контроллером.
 * Поддерживает управление углом сервопривода, скоростью BLDC мотора
 * и запрос телеметрии.
 * 
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirboatCommand {
    
    private Integer angle;  // Угол сервопривода (0-180°)
    private Integer speed;  // Скорость BLDC мотора (0-100%)
    private Boolean status; // Запрос телеметрии
    
    public AirboatCommand() {
    }
    
    /**
     * Создает команду управления углом и скоростью.
     *
     * @param angle Угол сервопривода (0-180°)
     * @param speed Скорость BLDC мотора (0-100%)
     */
    public AirboatCommand(int angle, int speed) {
        this.angle = angle;
        this.speed = speed;
    }
    
    /**
     * Создает команду запроса телеметрии.
     *
     * @param requestStatus true для запроса телеметрии
     */
    public AirboatCommand(boolean requestStatus) {
        this.status = requestStatus;
    }
    
    /**
     * Создает полную команду с управлением и запросом телеметрии.
     *
     * @param angle Угол сервопривода (0-180°)
     * @param speed Скорость BLDC мотора (0-100%)
     * @param requestStatus true для запроса телеметрии
     */
    public AirboatCommand(int angle, int speed, boolean requestStatus) {
        this.angle = angle;
        this.speed = speed;
        this.status = requestStatus;
    }
    
    public Integer getAngle() {
        return angle;
    }
    
    public void setAngle(Integer angle) {
        this.angle = angle;
    }
    
    public Integer getSpeed() {
        return speed;
    }
    
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "AirboatCommand{" +
                "angle=" + angle +
                ", speed=" + speed +
                ", status=" + status +
                '}';
    }
} 