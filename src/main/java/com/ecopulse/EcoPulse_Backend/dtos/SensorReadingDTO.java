package com.ecopulse.EcoPulse_Backend.dtos;

import lombok.Data;

@Data
public class SensorReadingDTO {
    private String value1;
    private String value2;
    private String deviceId;
    private String timestamp;

    public SensorReadingDTO(String deviceId, String value1,String value2, String timestamp) {
        this.deviceId = deviceId;
        this.value1 = value1;
        this.value2 = value2;
        this.timestamp = timestamp;
    }
}
