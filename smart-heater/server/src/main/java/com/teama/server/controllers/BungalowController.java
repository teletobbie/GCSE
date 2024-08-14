package com.teama.server.controllers;

import com.teama.server.dto.bungalow.BungalowResponse;
import com.teama.server.dto.bungalow.TemperatureRequest;
import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.exceptions.TenantAlreadyHasBungalowException;
import com.teama.server.models.Bungalow;
import com.teama.server.models.User;
import com.teama.server.services.BungalowService;
import com.teama.server.services.UserService;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/bungalows")
public class BungalowController {
    @Autowired
    private BungalowService bungalowService;

    @PostMapping
    public Bungalow createBungalow(@RequestBody Bungalow bungalowRequest) {
        return bungalowService.createBungalow(bungalowRequest);
    }

    @GetMapping
    public List<BungalowResponse> getAllBungalows() {
        return bungalowService.getAllBungalows();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bungalow> getBungalowById(@PathVariable Long id) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            Bungalow bungalow = bungalowService.getBungalowById(id);
            return ResponseEntity.ok(bungalow);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bungalow> updateBungalow(@PathVariable Long id, @RequestBody Bungalow bungalowRequest) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            Bungalow bungalow = bungalowService.updateBungalow(id, bungalowRequest);
            return ResponseEntity.ok(bungalow);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("unassign/{id}")
    public ResponseEntity<Bungalow> unassignTenant(@PathVariable Long id, @RequestBody User user) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            Bungalow bungalow = bungalowService.unassignUser(user.getId());
            return ResponseEntity.ok(bungalow);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("assign/{id}")
    public ResponseEntity<Bungalow> assignTenant(@PathVariable Long id, @RequestBody User user) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            bungalowService.assignUser(id, user.getId());
        } catch (TenantAlreadyHasBungalowException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("temp/{id}")
    public ResponseEntity<Bungalow> setTargetTemperature(@PathVariable Long id, @RequestBody TemperatureRequest temperatureRequest) {
        if (id == null || id <= 0 || temperatureRequest == null) return ResponseEntity.badRequest().build();
        try {
            bungalowService.setTargetTemperature(id, temperatureRequest.getTemperature());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public void deleteAllBungalows() {
        bungalowService.deleteAllBungalows();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Bungalow> deleteBungalowById(@PathVariable Long id) {
        if (id == null || id <= 0) ResponseEntity.badRequest().build();
        bungalowService.deleteBungalowById(id);
        return ResponseEntity.ok().build();
    }
}

