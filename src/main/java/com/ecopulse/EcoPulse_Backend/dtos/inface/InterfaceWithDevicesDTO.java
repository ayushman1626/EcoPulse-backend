package com.ecopulse.EcoPulse_Backend.dtos.inface;



import com.ecopulse.EcoPulse_Backend.dtos.device.DeviceDTO;
import com.ecopulse.EcoPulse_Backend.model.Device;
import com.ecopulse.EcoPulse_Backend.model.Interface;

import java.util.List;
import java.util.UUID;

public class InterfaceWithDevicesDTO {
    private UUID id;
    private String name;
    private String description;
    private String role;
    private String ownerId;
    private String ownerUsername;
    private List<DeviceDTO> devices;

    public InterfaceWithDevicesDTO(Interface interfaceEntity, List<Device> devices, String role) {
        this.id = interfaceEntity.getId();
        this.name = interfaceEntity.getName();
        this.description = interfaceEntity.getDescription();
        this.role = role;
        this.ownerId = interfaceEntity.getCreatedBy().getId().toString();
        this.ownerUsername = interfaceEntity.getCreatedBy().getUsername();
        this.devices = devices.stream()
                .map(DeviceDTO::new)
                .toList();
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public List<DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDTO> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "InterfaceWithDevicesDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", role='" + role + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", devices=" + devices +
                '}';
    }
}
