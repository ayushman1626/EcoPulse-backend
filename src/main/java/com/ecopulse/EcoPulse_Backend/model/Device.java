package com.ecopulse.EcoPulse_Backend.model;

import com.ecopulse.EcoPulse_Backend.enums.DeviceType;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "device")
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "interface_id", referencedColumnName = "id")
    private Interface interfaceEntity;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @Column(length = 255)
    private String location;

    @Column(name = "placement_date")
    private LocalDate placementDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "last_value1", precision = 5, scale = 2)
    private BigDecimal lastValue1;

    @Column(name = "last_value2", precision = 5, scale = 2)
    private BigDecimal lastValue2;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Interface getInterfaceEntity() {
        return interfaceEntity;
    }

    public void setInterfaceEntity(Interface interfaceEntity) {
        this.interfaceEntity = interfaceEntity;
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

    public LocalDate getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(LocalDate placementDate) {
        this.placementDate = placementDate;
    }

    public BigDecimal getLastValue1() {
        return lastValue1;
    }

    public void setLastValue1(BigDecimal lastValue) {
        this.lastValue1 = lastValue;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", interfaceEntity=" + interfaceEntity +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", location='" + location + '\'' +
                ", placementDate=" + placementDate +
                ", isActive=" + isActive +
                ", lastValue=" + lastValue1 +
                ", lastValue2=" + lastValue2 +
                ", lastUpdated=" + lastUpdated +
                ", createdAt=" + createdAt +
                '}';
    }
}


