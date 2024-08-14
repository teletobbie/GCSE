package com.teama.server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "heater_datas")
public class HeaterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double pressure;
    private double outsideTemperature;
    private double insideTemperature;
    private boolean doorSensor;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime movementSensor;
    private double gasUsage;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateGenerated;

    @ManyToOne()
    @JoinColumn(name = "bungalow_id")
    private Bungalow bungalow;

    public HeaterData() {}
    public HeaterData(double pressure, double outsideTemperature, double insideTemperature, boolean doorSensor, LocalDateTime movementSensor, double gasUsage, LocalDateTime dateGenerated) {
        this.pressure = pressure;
        this.outsideTemperature = outsideTemperature;
        this.insideTemperature = insideTemperature;
        this.doorSensor = doorSensor;
        this.movementSensor = movementSensor;
        this.gasUsage = gasUsage;
        this.dateGenerated = dateGenerated;
    }

    public double getPressure() {
        return pressure;
    }
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    public double getOutsideTemperature() {
        return outsideTemperature;
    }
    public void setOutsideTemperature(double outsideTemperature) {
        this.outsideTemperature = outsideTemperature;
    }
    public double getInsideTemperature() {
        return insideTemperature;
    }
    public void setInsideTemperature(double insideTemperature) {
        this.insideTemperature = insideTemperature;
    }
    public boolean isDoorSensor() {
        return doorSensor;
    }
    public void setDoorSensor(boolean doorSensor) {
        this.doorSensor = doorSensor;
    }
    public LocalDateTime getMovementSensor() {
        return movementSensor;
    }
    public void setMovementSensor(LocalDateTime movementSensor) {
        this.movementSensor = movementSensor;
    }
    public double getGasUsage() {
        return gasUsage;
    }
    public void setGasUsage(double gasUsage) {
        this.gasUsage = gasUsage;
    }
    public LocalDateTime getDateGenerated() {
        return dateGenerated;
    }
    public void setDateGenerated(LocalDateTime dateGenerated) {
        this.dateGenerated = dateGenerated;
    }
    public Bungalow getBungalow() {
        return bungalow;
    }
    public void setBungalow(Bungalow bungalow) {
        this.bungalow = bungalow;
    }


    public void printData() {
        System.out.println("--------Data--------");
        System.out.println("Generated on " + dateGenerated);
        System.out.println("Pressure: " + pressure);
        System.out.println("Gas usage " + gasUsage);
        System.out.println("Current temperature: " + insideTemperature);
        System.out.println("Outside temperature: " + outsideTemperature);
        System.out.println("Last movement at " + movementSensor);
        System.out.println("Door open? " + doorSensor);
        System.out.println("--------End--------\n");
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
