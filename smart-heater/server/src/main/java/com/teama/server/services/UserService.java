package com.teama.server.services;

import com.teama.server.dto.user.UserResponse;
import com.teama.server.enums.Role;
import com.teama.server.models.User;
import com.teama.server.repositories.UserRepository;
import com.teama.server.exceptions.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BungalowService bungalowService;

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole(), user.getBungalow());
    }

    public User createUser(User user) {
        return userRepository.save(new User(null, user.getEmail(), user.getName(), user.getRole(), passwordEncoder.encode(user.getPassword())));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new EntityNotFoundException(User.class.getName(), id);

        return toResponse(user.get());
    }

    public User updateUser(Long id, User userDetails) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new EntityNotFoundException(User.class.getName(), id);

        User existingUser = user.get();
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());

        return userRepository.save(existingUser);
    }

    public User updateUserName(Long id, User userDetails) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new EntityNotFoundException(User.class.getName(), id);

        User existingUser = user.get();
        existingUser.setName(userDetails.getName());

        return userRepository.save(existingUser);
    }


    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void deleteUserById(Long id) throws EntityNotFoundException {
        bungalowService.unassignUser(id);
        userRepository.deleteById(id);
    }
}
