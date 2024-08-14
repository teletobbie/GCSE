package com.teama.server.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;
    private double temperature;

    @ManyToOne()
    @JoinColumn(name = "bungalow_id")
    private Bungalow bungalow;

    public Schedule() {}
    public Schedule(Long id, LocalDateTime startTime, double temperature) {
        this.id = id;
        this.startTime = startTime;
        this.temperature = temperature;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime start) {
        this.startTime = start;
    }

    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Bungalow getBungalow() {
        return bungalow;
    }
    public void setBungalow(Bungalow bungalow) {
        this.bungalow = bungalow;
    }
}
