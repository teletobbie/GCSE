package com.teama.server.services;

import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.models.Bungalow;
import com.teama.server.models.Schedule;
import com.teama.server.repositories.BungalowRepository;
import com.teama.server.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private BungalowRepository bungalowRepository;

    public Schedule createSchedule(Schedule schedule) throws EntityNotFoundException {
        schedule.setId(null);

        // Might need a DTO to only add the bungalow id; makes things slightly nicer for the frontend
        Optional<Bungalow> bungalow = bungalowRepository.findById(schedule.getBungalow().getId());
        if (bungalow.isEmpty()) throw new EntityNotFoundException(Bungalow.class.getName(), schedule.getBungalow().getId());

        schedule.setBungalow(bungalow.get());

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllScheduleData() {
        return scheduleRepository.findAll();
    }
    public List<Schedule> getSchedulesByBungalowId(Long bungalowId) {
        return getAllScheduleData().stream().filter(s -> Objects.equals(bungalowId, s.getBungalow().getId())).toList();
    }
    public Schedule getScheduleById(Long id) throws EntityNotFoundException {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (schedule.isEmpty()) throw new EntityNotFoundException(Schedule.class.getName(), id);

        return schedule.get();
    }

    public Schedule updateSchedule(Long id, Schedule scheduleDetails) throws EntityNotFoundException {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (schedule.isEmpty()) throw new EntityNotFoundException(Schedule.class.getName(), id);

        Schedule existingSchedule = schedule.get();
        existingSchedule.setStartTime(scheduleDetails.getStartTime());
        existingSchedule.setTemperature(scheduleDetails.getTemperature());

        return scheduleRepository.save(existingSchedule);
    }

    public void deleteAllScheduleData() {
        scheduleRepository.deleteAll();
    }

    public void deleteScheduleById(Long id) {
        scheduleRepository.deleteById(id);
    }
}
