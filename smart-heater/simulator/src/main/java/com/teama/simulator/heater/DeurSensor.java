package com.teama.simulator.heater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class DeurSensor {
    private static final Logger logger = LogManager.getLogger();
    private Instant next;
    private boolean deurDicht;

    public DeurSensor() {
        this.next = Instant.MIN;
        this.deurDicht = true;
    }

    public void iterate(Instant currentInstant) {
        if (currentInstant.isAfter(this.next)) {
            if (this.isNacht(currentInstant)) {
                this.deurDicht = true;
            } else {
                this.deurDicht = ThreadLocalRandom.current().nextBoolean();
                int minuten;
                if (this.deurDicht) {
                    minuten = ThreadLocalRandom.current().nextInt(60, 120);
                } else {
                    minuten = ThreadLocalRandom.current().nextInt(1, 5);
                }

                this.next = currentInstant.plus(Duration.ofMinutes((long)minuten));
                logger.debug("Deur update: " + this.deurDicht + " minute: " + minuten);
            }
        }

    }

    private boolean isNacht(Instant currentInstant) {
        LocalDateTime nu = LocalDateTime.ofInstant(currentInstant, ZoneOffset.systemDefault());
        return nu.getHour() > 23 || nu.getHour() < 7;
    }

    public boolean isDicht() {
        return this.deurDicht;
    }

    public void zetVastOpOpen() {
        this.zetVastOp(false);
    }

    public void zetVastOpDicht() {
        this.zetVastOp(true);
    }

    public void zetVastOp(boolean isDicht) {
        this.deurDicht = isDicht;
        this.next = Instant.MAX;
    }
}
