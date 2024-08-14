package com.teama.server.dto.user;

import com.teama.server.enums.Role;
import com.teama.server.models.Bungalow;

public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private Role role;
    private Bungalow bungalow;

    public UserResponse() {}
    public UserResponse(Long id, String email, String name, Role role, Bungalow bungalow) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.bungalow = bungalow;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public Bungalow getBungalow() {
        return bungalow;
    }
    public void setBungalow(Bungalow bungalow) {
        this.bungalow = bungalow;
    }
}