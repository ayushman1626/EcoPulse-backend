package com.ecopulse.EcoPulse_Backend.service;

import com.ecopulse.EcoPulse_Backend.dtos.SensorReadingDTO;
import com.ecopulse.EcoPulse_Backend.enums.DeviceType;
import com.ecopulse.EcoPulse_Backend.model.Device;
import com.ecopulse.EcoPulse_Backend.model.SensorReading;
import com.ecopulse.EcoPulse_Backend.repository.DeviceRepo;
import com.ecopulse.EcoPulse_Backend.repository.SensorReadingRepo;
import com.ecopulse.EcoPulse_Backend.util.DeviceStreamManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MqttService {
    @Autowired
    private SensorReadingRepo sensorDataRepo;

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private DeviceStreamManager streamManager;

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);
    //mosquitto_pub -t "reading/waste-level" -m "{\"sensor_id\":\"5fac3f05-4494-4e52-84a5-0a77f23a1c8d\",\"value\":86}"
    //mosquitto_pub -h broker.hivemq.com -p 1883 -t reading/waste-level -m "{\"sensor_id\":\"5fac3f05-4494-4e52-84a5-0a77f23a1c8d\", \"value\":42}"
    public void handleIncomingData(String payload){
        try {
            // Parse the incoming JSON payload
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);

            UUID deviceId = UUID.fromString(node.get("sensor_id").asText());
            BigDecimal value1 = node.get("value1").decimalValue();
            BigDecimal value2 = node.get("value2") != null ? node.get("value2").decimalValue() : null;

            //System.out.println("Received data for sensor_id: " + deviceId + " with value: " + value);


            // Validate device ID and value and
            Device device = deviceRepo.findById(deviceId)
                    .orElseThrow(() -> new IllegalArgumentException("Device not found: " + deviceId));
            if (value1 == null || value1.compareTo(BigDecimal.ZERO) < 0 ) {
                logger.error("Invalid sensor value1: {}", value1);
                throw new IllegalArgumentException("Invalid sensor value: " + value1);
            }
            //If the device is a smart bin, value2 must be non-negative and not null
            if ( device.getType().compareTo(DeviceType.SMART_BIN) == 0
                    && (value2 == null || value2.compareTo(BigDecimal.ZERO) < 0)){
                logger.error("Invalid sensor value2: {}", value2);
                throw new IllegalArgumentException("Invalid sensor value2: " + value2);
            }


            // 1. Save to sensor_data
            SensorReading data = new SensorReading(device, value1, value2, LocalDateTime.now());
            sensorDataRepo.save(data);
            logger.info("Sensor data saved: {}", data);

            // 2. Update last value in device and set active
            device.setActive(true);
            device.setLastValue1(value1);
            if ( device.getType().compareTo(DeviceType.SMART_BIN) == 0
                    && value2 != null) {
                device.setLastValue2(value2);
            }
            device.setLastUpdated(LocalDateTime.now());
            deviceRepo.save(device); // or load & save
            logger.info("Device updated: {}", device);

            // 3. Push to active SSE clients
            SensorReadingDTO dataDto = new SensorReadingDTO(
                    device.getId().toString(),data.getValue1().toString(),
                    data.getValue2() != null ? data.getValue2().toString() : null,
                    data.getRecordedAt().toString());

            streamManager.broadcast(deviceId, dataDto);
            // ... rest of your logic
        } catch (Exception e) {
            logger.error("Failed to parse payload: {}", payload, e);
            // Optionally, handle or rethrow
        }
    }


}
