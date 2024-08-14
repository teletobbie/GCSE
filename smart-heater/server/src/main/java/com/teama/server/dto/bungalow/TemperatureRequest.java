package com.teama.server.dto.bungalow;

public class TemperatureRequest {
    private double temperature;

    public TemperatureRequest() { }
    public TemperatureRequest(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
