package com.teama.server.logic.threads;

import com.teama.server.logic.Heater;
import com.teama.server.logic.HeaterManager;
import com.teama.server.models.Bungalow;
import com.teama.server.models.HeaterData;
import com.teama.server.repositories.BungalowRepository;

import java.util.List;
import java.util.Optional;

public class HeaterModeThread extends Thread {
    private final int interval;
    private final HeaterManager heaterManager;
    private final BungalowRepository bungalowRepository;

    public HeaterModeThread(int interval, HeaterManager heaterManager, BungalowRepository bungalowRepository) {
        this.interval = interval;
        this.heaterManager = heaterManager;
        this.bungalowRepository = bungalowRepository;
    }
    public void run() {
        while (true) {
            try {
                if (interval > 0) sleep(interval);
                List<Heater> heaters = heaterManager.getHeaters();
                for (int i = 0; i < heaters.size(); i++) {
                    Heater heater = heaters.get(i);
                    HeaterData heaterData = heaterManager.getHeaterData(heater);
                    if (heaterData == null) continue;

                    assignHeatingMode(heater, heaterData, i + 1);
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void assignHeatingMode(Heater heater, HeaterData heaterData, long heaterId) {
        Optional<Bungalow> bungalow = bungalowRepository.findById(heaterId);
        if (bungalow.isEmpty() || bungalow.get().getUser() == null) {
            heater.setCVLevel(0, 0);
            return;
        }

        if (heaterData.getInsideTemperature() < 15) {
            heater.setCVLevel(100,30);
            return;
        } else if (heaterData.getInsideTemperature() > 23.5) {
            heater.setCVLevel(100, 0);
            return;
        }

        double targetTemperature = bungalow.get().getTargetTemperature();
        if (heaterData.getInsideTemperature() < targetTemperature) {
            heater.setCVLevel(100,30);
        } else if (heaterData.getInsideTemperature() > targetTemperature) {
            heater.setCVLevel(100, 0);
        }
    }
}
