package com.ecopulse.EcoPulse_Backend.repository;

import com.ecopulse.EcoPulse_Backend.model.Device;
import com.ecopulse.EcoPulse_Backend.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorReadingRepo extends JpaRepository<SensorReading, Long> {
    // Define custom query methods if needed
    // For example, to find the latest sensor reading for a specific dustbin:
    // Optional<SensorReading> findTopByDustbinIdOrderByTimestampDesc(Integer dustbinId);
    @Query("SELECT s FROM SensorReading s WHERE s.sensor = :sensor AND s.recordedAt >= :fromDate ORDER BY s.recordedAt DESC")
    List<SensorReading> findBySensorAndFromDate(@Param("sensor") Device sensor, @Param("fromDate") LocalDateTime fromDate);
    List<SensorReading> findBySensor(Device sensor);

}
