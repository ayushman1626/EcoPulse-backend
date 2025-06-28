package com.ecopulse.EcoPulse_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sensor_reading")
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id", nullable = false)
    private Device sensor;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal value1;

    @Column(nullable = true, precision = 5, scale = 2)
    private BigDecimal value2;

    @Column(name = "recorded_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime recordedAt = LocalDateTime.now();

    public SensorReading() {
        // Required by JPA
    }


    public SensorReading(Device device, BigDecimal value1, BigDecimal value2, LocalDateTime now) {
        this.sensor = device;
        this.value1 = value1;
        this.value2 = value2;
        this.recordedAt = now;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getSensor() {
        return sensor;
    }

    public void setSensor(Device sensor) {
        this.sensor = sensor;
    }

    public BigDecimal getValue1() {
        return value1;
    }

    public void setValue1(BigDecimal value1) {
        this.value1 = value1;
    }

    public BigDecimal getValue2() {
        return value2;
    }
    public void setValue2(BigDecimal value2) {
        this.value2 = value2;
    }
    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "id=" + id +
                ", sensor=" + sensor +
                ", value=" + value1 +
                ", value2=" + value2 +
                ", recordedAt=" + recordedAt +
                '}';
    }
}
