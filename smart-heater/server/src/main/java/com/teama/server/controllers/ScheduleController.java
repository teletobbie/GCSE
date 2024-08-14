package com.teama.server.controllers;

import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.models.Schedule;
import com.teama.server.models.User;
import com.teama.server.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule scheduleRequest) {
        if (scheduleRequest.getBungalow() == null) return ResponseEntity.badRequest().build();
        try {
            Schedule schedule = scheduleService.createSchedule(scheduleRequest);
            return ResponseEntity.ok(schedule);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Schedule> getAllScheduleData() {
        return scheduleService.getAllScheduleData();
    }

    @GetMapping("/bungalow/{id}")
    public ResponseEntity<List<Schedule>> getSchedulesByBungalowId(@PathVariable Long id) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        List<Schedule> schedules = scheduleService.getSchedulesByBungalowId(id);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            Schedule schedule = scheduleService.getScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleRequest) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            Schedule schedule = scheduleService.updateSchedule(id, scheduleRequest);
            return ResponseEntity.ok(schedule);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public void deleteAllScheduleData() {
        scheduleService.deleteAllScheduleData();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Schedule>> deleteScheduleById(@PathVariable Long id) {
        if (id == null || id <= 0) ResponseEntity.badRequest().build();
        scheduleService.deleteScheduleById(id);
        List<Schedule> updatedSchedules = scheduleService.getAllScheduleData();
        return ResponseEntity.ok(updatedSchedules);
    }
}
//here