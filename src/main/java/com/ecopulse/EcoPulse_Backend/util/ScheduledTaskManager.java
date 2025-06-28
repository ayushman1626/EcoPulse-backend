package com.ecopulse.EcoPulse_Backend.util;

import com.ecopulse.EcoPulse_Backend.config.MqttConfig;
import com.ecopulse.EcoPulse_Backend.model.Device;
import com.ecopulse.EcoPulse_Backend.repository.DeviceRepo;
import com.ecopulse.EcoPulse_Backend.service.MqttStatusService;
import com.ecopulse.EcoPulse_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTaskManager {

    @Autowired
    private MqttConfig mqttConfig;

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MqttStatusService mqttStatusService;

    // Runs every 30 seconds
    @Scheduled(fixedRate = 30_000)
    public void markOfflineDevices() {
        if (mqttStatusService.isMqttConnected()) {
            LocalDateTime cutoff = LocalDateTime.now().minusMinutes(1);
            List<Device> outdatedDevices = deviceRepo.findByIsActiveTrueAndLastUpdatedBefore(cutoff);
            for (Device device : outdatedDevices) {
                device.setActive(false);
                deviceRepo.save(device);
            }
        }
    }

    //Remove unverified users every 24 hours
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void removeUnverifiedUsers() {
       userService.removeUnverifiedUsers();
    }
}
