package info.horske.meteo.configuration;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author rucka
 */
@Component
public class MqttClientFactory {

    private Logger logger = LoggerFactory.getLogger(MqttClientFactory.class);

    @Value("${spring.mqtt.url}")
    String mqttUrl;

    @Value("${spring.mqtt.user}")
    String mqttUser;

    @Value("${spring.mqtt.password}")
    String mqttPassword;

    @Value("${spring.mqtt.clientId}")
    String mqttClientId;

    public IMqttClient createClient() throws MqttException {
        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(mqttUser);
        conOpt.setPassword(mqttPassword.toCharArray());
        MqttClient mqttClient = new MqttClient(mqttUrl, mqttClientId, new MemoryPersistence());
        mqttClient.connect(conOpt);
        logger.info(">> reconnected to url {} clientId {} ", mqttUrl, mqttClientId);
        return mqttClient;
    }
}
