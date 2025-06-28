package com.ecopulse.EcoPulse_Backend.config;

import com.ecopulse.EcoPulse_Backend.service.MqttService;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    @Autowired
    private MqttService mqttService;

    @Value("${mqtt.broker.url}")
    private String MQTT_BROKER;
    @Value("${mqtt.client.id}")
    private String CLIENT_ID;

    @Value("${mqtt.topics}")
    private String[] TOPICS;

    @Value("${mqtt.username}")
    private String MQTT_USERNAME;
    @Value("${mqtt.password}")
    private String MQTT_PASSWORD;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{MQTT_BROKER});
        options.setUserName(MQTT_USERNAME);
        options.setPassword(MQTT_PASSWORD.toCharArray());
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    //This part listens for messages published to the MQTT broker on the topicThis part listens for messages published to the MQTT broker on the topic
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }
    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttAdapter() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID, mqttClientFactory(), TOPICS);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler messageHandler() {
        return message -> {
            try {
                if (message.getPayload() == null) {
                    logger.warn("MQTT message has null payload");
                    return;
                }
                String payload = message.getPayload().toString();
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                logger.info("Received MQTT message on topic [{}]: {}", topic, payload);
                logger.info("Headers: {}", message.getHeaders());

                mqttService.handleIncomingData(payload);

            } catch (Exception ex) {
                logger.error("Error processing MQTT message", ex);
            }
        };
    }

}
