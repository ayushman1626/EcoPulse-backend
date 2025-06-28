package com.ecopulse.EcoPulse_Backend.dtos.access;

import com.example.demo.model.User;
import com.example.demo.model.enums.Role;

public class InterfaceAccessDTO {
    private String userId;
    private String username;
    private String fullName;
    private String role;

    public InterfaceAccessDTO(User user, Role role) {
        this.userId = user.getId().toString();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.role = role.name();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "InterfaceAccessDTO{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

