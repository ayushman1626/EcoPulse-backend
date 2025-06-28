package com.ecopulse.EcoPulse_Backend.dtos.inface;

import com.ecopulse.EcoPulse_Backend.model.Interface;
import lombok.Data;

@Data
public class InterfaceDTO {

    private String id;            // Interface ID
    private String name;          // Interface name
    private String description;   // Interface description
    private String ownerId;       // Creator ID
    private String ownerUsername;
    private String role;          // User's role in this interface
    private String createdAt;// Created date (optional, format string)

    public InterfaceDTO(String id, String name, String description, String ownerId, String ownerUsername, String role, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.role = role;
        this.createdAt = createdAt;
    }

    public InterfaceDTO(Interface iface, String role) {
        this.id = iface.getId().toString();
        this.name = iface.getName();
        this.description = iface.getDescription();
        this.ownerId = iface.getCreatedBy().getId().toString();
        this.ownerUsername = iface.getCreatedBy().getUsername();
        this.role = role;
        this.createdAt = iface.getCreatedAt().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserInterfaceDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", role='" + role + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}

