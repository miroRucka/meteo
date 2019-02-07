package info.horske.meteo.application;

import info.horske.meteo.domain.MeteoData;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author rucka
 */
@Service
public class MqttService implements MqttCallback {

    private Logger logger = LoggerFactory.getLogger(MqttService.class);

    @Autowired
    private IMqttClient mqttClient;

    @Autowired
    private MeteoDataRepository meteoDataRepository;

    @Autowired
    private MeteoDataAssembler meteoDataAssembler;

    @Value("${spring.mqtt.topic.meteo}")
    private String topic;

    @PostConstruct
    public void setUp() throws MqttException {
        mqttClient.setCallback(this);
        mqttClient.subscribe(topic, 1);

    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.error("something wrong - lost connection", throwable);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        logger.info("data was received, size {}", mqttMessage.getPayload().length);
        MeteoData meteoData = meteoDataAssembler.from(String.valueOf(mqttMessage));
        meteoDataRepository.create(meteoData, true);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        if (mqttClient == null || !mqttClient.isConnected()) return;
        logger.info("shutdown from url {} clientId {} ", mqttClient.getServerURI(), mqttClient.getClientId());
        mqttClient.disconnect();
    }
}
