package com.teama.server.controllers;

import com.teama.server.dto.daterange.DateRangeRequest;
import com.teama.server.dto.daterange.DateRangeResponse;
import com.teama.server.logic.Heater;
import com.teama.server.logic.HeaterTranscriber;
import com.teama.server.models.*;
import com.teama.server.services.HeaterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/heater_datas")
public class HeaterDataController {
    @Autowired
    private HeaterDataService heaterDataService;
    private Heater heater;
    private final HeaterTranscriber heaterTranscriber = new HeaterTranscriber();

    @GetMapping()
    public List<HeaterData> getAllHeaterData() {
        return heaterDataService.getAllHeaterData();
    }

    @PostMapping("/range")
    public List<DateRangeResponse> getHeaterDataByDateRange(@RequestBody DateRangeRequest dateRange) {
        return heaterDataService.getGasUsageByDateRange(dateRange);
    }
}