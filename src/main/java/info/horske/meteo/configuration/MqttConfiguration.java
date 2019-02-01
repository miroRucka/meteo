package info.horske.meteo.configuration;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rucka
 */
@Configuration
public class MqttConfiguration {

    private Logger logger = LoggerFactory.getLogger(MqttConfiguration.class);

    @Value("${spring.mqtt.url}")
    private String mqttUrl;

    @Value("${spring.mqtt.user}")
    private String mqttUser;

    @Value("${spring.mqtt.password}")
    private String mqttPassword;

    @Value("${spring.mqtt.clientId}")
    private String mqttClientId;

    @Bean
    public IMqttClient createClient() throws MqttException {
        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(mqttUser);
        conOpt.setPassword(mqttPassword.toCharArray());
        MqttClient mqttClient = new MqttClient(mqttUrl, mqttClientId, new MemoryPersistence());
        mqttClient.connect(conOpt);
        logger.info("connected to url {} clientId {} ", mqttUrl, mqttClientId);
        return mqttClient;
    }
}
