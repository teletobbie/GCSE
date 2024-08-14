package com.teama.server.dto;

public class CV {
    private int pumpLevel;
    private int burnerLevel;

    public CV(int pumpLevel, int burnerLevel) {
        this.pumpLevel = pumpLevel;
        this.burnerLevel = burnerLevel;
    }

    public int getPumpLevel() {
        return pumpLevel;
    }

    public int getBurnerLevel() {
        return burnerLevel;
    }

    public void setPumpLevel(int pumpLevel) {
        this.pumpLevel = pumpLevel;
    }

    public void setBurnerLevel(int burnerLevel) {
        this.burnerLevel = burnerLevel;
    }
}
