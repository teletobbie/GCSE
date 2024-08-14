package com.teama.server.controllers;


import com.teama.server.dto.CV;
import com.teama.server.dto.connection.ConnectionResponse;
import com.teama.server.dto.connection.ConnectionRequest;
import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.exceptions.HeaterConnectionException;
import com.teama.server.exceptions.NoHeaterException;
import com.teama.server.logic.HeaterTranscriber;
import com.teama.server.models.*;
import com.teama.server.repositories.BungalowRepository;
import com.teama.server.services.BungalowService;
import com.teama.server.services.HeaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/heaters")
public class HeaterController {
    @Autowired
    private HeaterService heaterService;

    @Autowired
    private BungalowRepository bungalowRepository;

    @PostMapping
    public ResponseEntity<List<ConnectionResponse>> connectToHeaters() {
        List<ConnectionResponse> responses = new ArrayList<>();
        String connectionString = "smart-heater-simulator-";
        for (int i = 1; i <= bungalowRepository.findAll().size(); i++) {
            try {
                responses.add(heaterService.connectToHeater(new ConnectionRequest(connectionString + i, 7777, "0")));
            } catch (HeaterConnectionException ignore) { }
        }
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/connect")
    public ResponseEntity<ConnectionResponse> connectToHeater(@RequestBody ConnectionRequest connectionRequest) {
        try {
            ConnectionResponse response = heaterService.connectToHeater(connectionRequest);
            return ResponseEntity.ok(response);
        } catch (HeaterConnectionException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeaterData> getHeaterData(@PathVariable Integer id) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            HeaterData heaterData = heaterService.getHeaterData(id);
            return ResponseEntity.ok(heaterData);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<HeaterData>> getHeaterDatas() {
        List<HeaterData> heaterDatas = heaterService.getHeaterDatas();
        return ResponseEntity.ok(heaterDatas);
    }
}