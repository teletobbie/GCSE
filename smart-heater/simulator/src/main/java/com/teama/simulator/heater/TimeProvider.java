package com.teama.simulator.heater;

import java.time.Instant;

public enum TimeProvider {
    REALTIME,
    FAST_TIME {
        private final int speed = 24;

        public Instant currentInstant() {
            Instant clock = Instant.now();
            long millisSinceStart = clock.toEpochMilli() - this.realStartTime.toEpochMilli();
            return Instant.ofEpochMilli(this.simulateStartTime.toEpochMilli() + 24L * millisSinceStart);
        }

        public String toString() {
            return "24x";
        }
    };

    protected long offsetInMillis;
    protected Instant realStartTime;
    protected Instant simulateStartTime;

    private TimeProvider() {
        this.offsetInMillis = 0L;
        this.realStartTime = Instant.now();
        this.simulateStartTime = Instant.now();
    }

    public Instant currentInstant() {
        return Instant.now().minusMillis(this.offsetInMillis);
    }

    public void init(Instant simulateStartTime) {
        this.realStartTime = Instant.now();
        this.offsetInMillis = this.realStartTime.toEpochMilli() - simulateStartTime.toEpochMilli();
        this.simulateStartTime = simulateStartTime;
    }
}
