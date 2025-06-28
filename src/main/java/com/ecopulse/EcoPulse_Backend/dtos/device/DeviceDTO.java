package com.ecopulse.EcoPulse_Backend.dtos.device;


import com.ecopulse.EcoPulse_Backend.enums.DeviceType;
import com.ecopulse.EcoPulse_Backend.model.Device;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DeviceDTO {
    private String id;
    private String name;
    private DeviceType type;
    private String location;
    private BigDecimal lastValue1;
    private BigDecimal lastValue2;
    private LocalDateTime lastUpdated;
    private String onlineStatus;
    private String interfaceId;
    private String interfaceName;

    public DeviceDTO(Device device) {
        this.id = device.getId().toString();
        this.name = device.getName();
        this.type = device.getType();
        this.location = device.getLocation();
        this.lastValue1 = device.getLastValue1();
        this.lastValue2 = device.getLastValue2();
        this.lastUpdated = device.getLastUpdated();
        this.interfaceId = device.getInterfaceEntity().getId().toString();
        this.interfaceName = device.getInterfaceEntity().getName();
        this.onlineStatus = device.getActive() ? "Online" : "Offline";
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

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getLastValue1() {
        return lastValue1;
    }

    public void setLastValue1(BigDecimal lastValue1) {
        this.lastValue1 = lastValue1;
    }

    public BigDecimal getLastValue2() {
        return lastValue2;
    }
    public void setLastValue2(BigDecimal lastValue2) {
        this.lastValue2 = lastValue2;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }
    public void setOnlineStatus(String isActive) {
        this.onlineStatus = isActive;
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", lastValue=" + lastValue1 +
                ", lastValue2=" + lastValue2 +
                ", lastUpdated=" + lastUpdated +
                ", interfaceId='" + interfaceId + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", isActive='" + onlineStatus + '\'' +
                '}';
    }
}
