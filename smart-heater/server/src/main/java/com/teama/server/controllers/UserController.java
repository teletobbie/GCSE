package com.teama.server.controllers;

import com.teama.server.dto.user.UserResponse;
import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.models.User;
import com.teama.server.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userRequest) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            User user = userService.updateUser(id, userRequest);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUserDetails(@PathVariable Long id, @RequestBody User userRequest) {
        if (id == null || id <= 0) return ResponseEntity.badRequest().build();
        try {
            User user = userService.updateUserName(id, userRequest);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        if (id == null || id <= 0) ResponseEntity.badRequest().build();
        try {
            userService.deleteUserById(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}