package com.teama.simulator.heater;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class BewegingsSensor {
    private Instant laatsteBewegingGedetecteerd;
    private Instant beweegTot;
    private Instant beweegVanaf;

    public BewegingsSensor(Instant currentInstant) {
        this.beweegTot = Instant.MIN;
        this.beweegVanaf = Instant.MAX;
        this.laatsteBewegingGedetecteerd = currentInstant;
    }

    public void iterate(Instant currentInstant) {
        if (currentInstant.isAfter(this.beweegVanaf)) {
            this.laatsteBewegingGedetecteerd = currentInstant;
            this.beweegTot = this.beweegVanaf.plus(Duration.ofMinutes((long) ThreadLocalRandom.current().nextInt(5, 45)));
            this.beweegVanaf = Instant.MAX;
        }

        if (currentInstant.isBefore(this.beweegTot) && currentInstant.isAfter(this.laatsteBewegingGedetecteerd.plusSeconds(60L))) {
            this.laatsteBewegingGedetecteerd = currentInstant;
        }

        if (this.beweegVanaf == Instant.MAX && !currentInstant.isBefore(this.beweegTot)) {
            LocalDateTime nu = LocalDateTime.ofInstant(currentInstant, ZoneOffset.systemDefault());
            if (nu.getHour() > 7) {
                this.beweegVanaf = currentInstant.plus(Duration.ofMinutes((long)ThreadLocalRandom.current().nextInt(60, 120)));
            }
        }

    }

    public Instant getLaatsteBewegingGedetecteerd() {
        return this.laatsteBewegingGedetecteerd;
    }
}

