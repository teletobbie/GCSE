package com.teama.server.models;

import com.teama.server.enums.BungalowType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "bungalows")
public class Bungalow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BungalowType type;
    private double targetTemperature;

    @OneToOne(optional = true)
    @JoinColumn
    private User user;

    @OneToMany(mappedBy = "bungalow")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "bungalow")
    private List<HeaterData> heaterDatas;

    public Bungalow() {}
    public Bungalow(Long id, String name, BungalowType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.targetTemperature = 20f;
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
        if (targetTemperature > 23.5f) targetTemperature = 23.5f;
        if (targetTemperature < 15.0f) targetTemperature = 15.0f;
        this.targetTemperature = targetTemperature;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
