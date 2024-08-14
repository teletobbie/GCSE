package com.teama.server.logic;

import com.teama.server.logic.threads.HeaterDataThread;
import com.teama.server.logic.threads.HeaterModeThread;
import com.teama.server.logic.threads.HeaterScheduleThread;
import com.teama.server.models.HeaterData;
import com.teama.server.repositories.BungalowRepository;
import com.teama.server.services.HeaterDataService;
import com.teama.server.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HeaterManager implements CommandLineRunner {
    static final int HEATER_DATA_INTERVAL = 4 * 1000; // 4 seconds
    static final int HEATER_MODE_INTERVAL = 4 * 1000; // 4 seconds
    static final int HEATER_SCHE_INTERVAL = 4 * 1000; // 4 seconds

    private final ArrayList<Heater> heaters;

    @Autowired
    private HeaterDataService heaterDataService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private BungalowRepository bungalowRepository;

    private HeaterManager() {
        this.heaters = new ArrayList<>();
    }

    @Override
    public void run(String... args) {
        Thread dataThread = new HeaterDataThread(HEATER_DATA_INTERVAL, this, heaterDataService);
        Thread modeThread = new HeaterModeThread(HEATER_MODE_INTERVAL, this, bungalowRepository);
        Thread scheThread = new HeaterScheduleThread(HEATER_SCHE_INTERVAL, this, scheduleService, bungalowRepository);

        dataThread.start();
        modeThread.start();
        scheThread.start();
    }

    public void addHeater(Heater heater) {
        if (heaters.contains(heater)) return;
        heaters.add(heater);
    }

    public ArrayList<Heater> getHeaters() {
        return heaters;
    }

    public HeaterData getHeaterData(int id) {
        if (id <= 0 || id > heaters.size()) return null;
        return getHeaterData(heaters.get(id - 1));
    }

    public HeaterData getHeaterData(Heater heater) {
        return HeaterTranscriber.transcribeToHeaterData(heater.getStatus());
    }

    public List<HeaterData> getHeaterDatas() {
        return heaters.stream().map(this::getHeaterData).collect(Collectors.toList());
    }
}