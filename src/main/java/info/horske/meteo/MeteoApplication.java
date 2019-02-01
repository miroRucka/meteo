package info.horske.meteo;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeteoApplication {

    public static void main(String[] args) throws MqttException {
        SpringApplication.run(MeteoApplication.class, args);
    }

}

