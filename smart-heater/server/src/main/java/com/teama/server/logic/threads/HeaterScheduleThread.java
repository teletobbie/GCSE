package com.teama.server.logic.threads;

import com.teama.server.logic.Heater;
import com.teama.server.logic.HeaterManager;
import com.teama.server.models.Bungalow;
import com.teama.server.models.HeaterData;
import com.teama.server.models.Schedule;
import com.teama.server.repositories.BungalowRepository;
import com.teama.server.services.ScheduleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HeaterScheduleThread extends Thread {
    private final int interval;
    private final HeaterManager heaterManager;
    private final ScheduleService scheduleService;
    private final BungalowRepository bungalowRepository;

    public HeaterScheduleThread(int interval, HeaterManager heaterManager, ScheduleService scheduleService, BungalowRepository bungalowRepository) {
        this.interval = interval;
        this.heaterManager = heaterManager;
        this.scheduleService = scheduleService;
        this.bungalowRepository = bungalowRepository;
    }

    public void run() {
        while (true) {
            try {
                if (interval > 0) sleep(interval);
                List<Heater> heaters = heaterManager.getHeaters();
                for (int i = 0; i < heaters.size(); i++) {
                    Heater heater = heaters.get(i);
                    handleHeaterSchedules(i + 1, heater);
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void handleHeaterSchedules(long heaterId, Heater heater) {
        List<Schedule> schedules = scheduleService.getSchedulesByBungalowId(heaterId);
        for (Schedule schedule : schedules) {
            if (!shouldStartSchedule(schedule, heater)) continue;
            startSchedule(schedule, heaterId);
            deleteSchedule(schedule);
            break;
        }
    }

    private boolean shouldStartSchedule(Schedule schedule, Heater heater) {
        HeaterData heaterData = heaterManager.getHeaterData(heater);
        LocalDateTime timeCurrent = heaterData.getDateGenerated();
        LocalDateTime timeTarget = schedule.getStartTime();

        // Plus 5 minutes is not exactly how we want the temperature to work.
        // This does not yet take into consideration how long it takes to reach the target.
        return timeCurrent.plusMinutes(5).isAfter(timeTarget);
    }

    private void startSchedule(Schedule schedule, long bungalowId) {
        Optional<Bungalow> bungalow = bungalowRepository.findById(bungalowId);
        if (bungalow.isEmpty()) return;

        Bungalow existingBungalow = bungalow.get();
        existingBungalow.setTargetTemperature(schedule.getTemperature());

        bungalowRepository.save(existingBungalow);
    }

    private void deleteSchedule(Schedule schedule) {
        scheduleService.deleteScheduleById(schedule.getId());
    }
}