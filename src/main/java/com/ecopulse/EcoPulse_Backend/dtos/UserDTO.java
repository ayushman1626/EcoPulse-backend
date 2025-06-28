package com.ecopulse.EcoPulse_Backend.dtos;


import com.ecopulse.EcoPulse_Backend.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String username;
    private LocalDateTime createdAt;

    // Constructor
    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.createdAt = user.getCreatedAt();
        this.fullName = user.getFullName();
    }

    // Getters
    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt;}
}

