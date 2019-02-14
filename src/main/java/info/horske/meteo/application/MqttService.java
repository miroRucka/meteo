package info.horske.meteo.application;

import info.horske.meteo.configuration.MqttClientFactory;
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
    private MeteoDataRepository meteoDataRepository;

    @Autowired
    private MeteoDataAssembler meteoDataAssembler;

    @Value("${spring.mqtt.topic.meteo}")
    private String topic;

    @Autowired
    private MqttClientFactory mqttClientFactory;

    private IMqttClient mqttClient;

    @PostConstruct
    public void setUp() throws MqttException {
        this.mqttClient = mqttClientFactory.createClient();
        mqttClient.setCallback(this);
        mqttClient.subscribe(topic, 1);
    }

    public void sendMeteoData(String meteoData) throws MqttException {
        if (mqttClient == null || !mqttClient.isConnected()) {
            setUp();
        }
        mqttClient.publish(topic, new MqttMessage(meteoData.getBytes()));
    }

    public void publish(String topic, Object object) throws MqttException {
        if (mqttClient == null || !mqttClient.isConnected()) {
            setUp();
        }
        mqttClient.publish(topic, new MqttMessage(String.valueOf(object).getBytes()));
    }


    @Override
    public void connectionLost(Throwable throwable) {
        logger.error("something wrong - lost connection", throwable);
        try {
            setUp();
        } catch (MqttException e) {
            logger.error("reconnect crash", e);
        }
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        logger.info("data was received from mqtt topic {}, size {}", topic, mqttMessage.getPayload().length);
        //MeteoData meteoData = meteoDataAssembler.from(String.valueOf(mqttMessage));
        //meteoDataRepository.create(meteoData, true);
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
