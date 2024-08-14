package com.teama.server.services;

import com.teama.server.dto.bungalow.BungalowResponse;
import com.teama.server.exceptions.TenantAlreadyHasBungalowException;
import com.teama.server.logic.HeaterManager;
import com.teama.server.models.Bungalow;
import com.teama.server.models.HeaterData;
import com.teama.server.models.User;
import com.teama.server.repositories.BungalowRepository;
import com.teama.server.repositories.UserRepository;
import com.teama.server.exceptions.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BungalowService {
    @Autowired
    private BungalowRepository bungalowRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HeaterManager heaterManager;

    public Bungalow createBungalow(Bungalow bungalow) {
        bungalow.setId(null);
        return bungalowRepository.save(bungalow);
    }

    public List<BungalowResponse> getAllBungalows() {
        return bungalowRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BungalowResponse toResponse(Bungalow bungalow) {
        HeaterData heaterData = heaterManager.getHeaterData(Math.toIntExact(bungalow.getId()));
        boolean isMovement = heaterData != null && heaterData.getDateGenerated().isEqual(heaterData.getMovementSensor());

        return new BungalowResponse(
                bungalow.getId(),
                bungalow.getName(),
                bungalow.getType(),
                bungalow.getTargetTemperature(),
                isMovement,
                bungalow.getUser(),
                heaterData);
    }

    public Bungalow getBungalowById(Long id) throws EntityNotFoundException {
        Optional<Bungalow> bungalow = bungalowRepository.findById(id);
        if (bungalow.isEmpty()) throw new EntityNotFoundException(Bungalow.class.getName(), id);

        return bungalow.get();
    }

    public Bungalow updateBungalow(Long id, Bungalow bungalowDetails) throws EntityNotFoundException, IllegalArgumentException {
        Optional<Bungalow> bungalow = bungalowRepository.findById(id);
        if (bungalow.isEmpty()) throw new EntityNotFoundException(Bungalow.class.getName(), id);

        Bungalow existingBungalow = bungalow.get();

        String newName = bungalowDetails.getName();
        if (newName != null) existingBungalow.setName(newName);

        User newUser = bungalowDetails.getUser();
        if (newUser != null) {
            if (newUser.getId() == null || newUser.getId() <= 0) throw new IllegalArgumentException();

            Optional<User> user = userRepository.findById(newUser.getId());
            if (user.isEmpty()) throw new EntityNotFoundException(User.class.getName(), newUser.getId());

            existingBungalow.setUser(user.get());
        }

        return bungalowRepository.save(existingBungalow);
    }

    public void assignUser(Long bungalowId, Long userId) throws TenantAlreadyHasBungalowException {
        Optional<Bungalow> bungalow = bungalowRepository.findById(bungalowId);
        if (bungalow.isEmpty()) return;

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) return;

        Bungalow existingBungalow = bungalow.get();
        User existingUser = user.get();
        if (existingUser.getBungalow() != null) throw new TenantAlreadyHasBungalowException();

        existingBungalow.setUser(existingUser);
        bungalowRepository.save(existingBungalow);
    }

    public Bungalow unassignUser(Long userId) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new EntityNotFoundException(User.class.getName(), userId);;

        Bungalow bungalow = user.get().getBungalow();
        if (bungalow == null) throw new EntityNotFoundException(Bungalow.class.getName(), 0L);

        bungalow.setUser(null);
        bungalowRepository.save(bungalow);

        return bungalow;
    }

    public void setTargetTemperature(Long bungalowId, double temperature) throws EntityNotFoundException {
        Optional<Bungalow> bungalow = bungalowRepository.findById(bungalowId);
        if (bungalow.isEmpty()) throw new EntityNotFoundException(Bungalow.class.getName(), bungalowId);

        Bungalow existingBungalow = bungalow.get();
        existingBungalow.setTargetTemperature(temperature);
        bungalowRepository.save(existingBungalow);
    }

    public void deleteAllBungalows() {
        bungalowRepository.deleteAll();
    }

    public void deleteBungalowById(Long id) {
        bungalowRepository.deleteById(id);
    }
}