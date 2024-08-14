package com.teama.server.dto.bungalow;

import com.teama.server.enums.BungalowType;
import com.teama.server.models.HeaterData;
import com.teama.server.models.User;

public class BungalowResponse {
    private Long id;
    private String name;
    private BungalowType type;
    private double targetTemperature;
    private boolean isMovement;
    private User user;
    private HeaterData heater;

    public BungalowResponse() { }
    public BungalowResponse(Long id, String name, BungalowType type, double targetTemperature, boolean isMovement, User user, HeaterData heater) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.targetTemperature = targetTemperature;
        this.isMovement = isMovement;
        this.user = user;
        this.heater = heater;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BungalowType getType() {
        return type;
    }
    public void setType(BungalowType type) {
        this.type = type;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }
    public void setTargetTemperature(double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public boolean getIsMovement() {
        return isMovement;
    }
    public void setIsMovement(boolean isMovement) {
        this.isMovement = isMovement;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public HeaterData getHeater() {
        return heater;
    }
    public void setHeater(HeaterData heater) {
        this.heater = heater;
    }
}