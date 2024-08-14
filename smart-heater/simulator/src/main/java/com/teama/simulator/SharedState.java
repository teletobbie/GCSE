package com.teama.simulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.teama.simulator.heater.Action;
import com.teama.simulator.heater.Status;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SharedState {
    private static final Logger logger = LogManager.getLogger();
    private final ConcurrentLinkedQueue<Action> actieLijst = new ConcurrentLinkedQueue<>();
    private volatile Status laatsteStatus = new Status();

    public SharedState() {
    }

    public void put(Action action) {
        if (!this.actieLijst.isEmpty()) {
            logger.info("Er ontstaat een action wachtrij.");
        }

        this.actieLijst.add(action);
    }

    public Optional<Action> geefAction() {
        return Optional.ofNullable(this.actieLijst.poll());
    }

    public void updateStatus(Status nieuw) {
        this.laatsteStatus = nieuw;
    }

    public Status geefStatus() {
        return this.laatsteStatus;
    }
}
