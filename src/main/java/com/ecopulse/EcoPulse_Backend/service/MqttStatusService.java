package com.ecopulse.EcoPulse_Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Service;

@Service
public class MqttStatusService {

    private final MqttPahoMessageDrivenChannelAdapter mqttAdapter;

    @Autowired
    public MqttStatusService(MqttPahoMessageDrivenChannelAdapter mqttAdapter) {
        this.mqttAdapter = mqttAdapter;
    }

    public boolean isMqttConnected() {
        return mqttAdapter.isRunning();
    }
}
