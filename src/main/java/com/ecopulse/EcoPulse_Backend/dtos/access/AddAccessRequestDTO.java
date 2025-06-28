package com.ecopulse.EcoPulse_Backend.dtos.access;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddAccessRequestDTO {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "User role is required")
    private String role; // Role to assign (e.g., "ADMIN", "USER")

    public @NotBlank(message = "username is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "username is required") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Device type is required") String getRole() {
        return role;
    }

    public void setRole(@NotBlank(message = "Device type is required") String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AddAccessRequestDTO{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
