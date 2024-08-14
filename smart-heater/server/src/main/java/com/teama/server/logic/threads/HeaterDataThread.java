package com.teama.server.logic.threads;

import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.logic.Heater;
import com.teama.server.logic.HeaterManager;
import com.teama.server.models.HeaterData;
import com.teama.server.services.HeaterDataService;

import java.util.List;

public class HeaterDataThread extends Thread {
    private final int interval;
    private final HeaterManager heaterManager;
    private final HeaterDataService heaterDataService;

    public HeaterDataThread(int interval, HeaterManager heaterManager, HeaterDataService heaterDataService) {
        this.interval = interval;
        this.heaterManager = heaterManager;
        this.heaterDataService = heaterDataService;
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

                    saveHeaterData(heaterData, i + 1);
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void saveHeaterData(HeaterData heaterData, long heaterId) {
        if (heaterData == null || heaterId <= 0) return;
        try {
            heaterDataService.createHeaterDataEntry(heaterData, heaterId);
        } catch (EntityNotFoundException ex) {
            System.err.println("Trying to add heater data entry for non-existent bungalow! id: " + heaterId);
        }
    }
}
