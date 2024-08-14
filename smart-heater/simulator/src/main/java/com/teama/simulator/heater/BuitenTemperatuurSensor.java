package com.teama.simulator.heater;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class BuitenTemperatuurSensor {
    private Instant next;
    private double temp;

    public BuitenTemperatuurSensor() {
        this.next = Instant.MIN;
        this.temp = 10.0;
    }

    public void iterate(Instant currentInstant) {
        if (currentInstant.isAfter(this.next)) {
            if (this.isNacht(currentInstant)) {
                if (this.temp > 8.0) {
                    this.temp -= 0.1;
                }
            } else if (this.temp < 14.0) {
                this.temp += 0.1;
            }

            this.next = currentInstant.plus(Duration.ofMinutes((long) ThreadLocalRandom.current().nextInt(5, 25)));
        }

    }

    private boolean isNacht(Instant currentInstant) {
        LocalDateTime nu = LocalDateTime.ofInstant(currentInstant, ZoneOffset.systemDefault());
        return nu.getHour() > 20 || nu.getHour() < 8;
    }

    public double getTemperatuur() {
        return this.temp;
    }

    public void zetVastOp(double temp) {
        this.temp = temp;
        this.next = Instant.MAX;
    }
}
