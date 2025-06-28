package com.ecopulse.EcoPulse_Backend.dtos.device;

import com.ecopulse.EcoPulse_Backend.enums.DeviceType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Device type is required")
    private DeviceType type;

    @NotBlank(message = "Location is required")
    private String location;

    public @NotBlank(message = "Name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Device type is required") DeviceType getType() {
        return type;
    }

    public void setType(@NotBlank(message = "Device type is required") DeviceType type) {
        this.type = type;
    }

    public @NotBlank(message = "Location is required") String getLocation() {
        return location;
    }

    public void setLocation(@NotBlank(message = "Location is required") String location) {
        this.location = location;
    }
}
