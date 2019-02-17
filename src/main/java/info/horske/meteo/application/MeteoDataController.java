package info.horske.meteo.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.horske.meteo.domain.HomeKitOperation;
import info.horske.meteo.domain.MeteoData;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author rucka
 */
@RestController
@RequestMapping("/api")
public class MeteoDataController {

    private Logger logger = LoggerFactory.getLogger(MeteoDataController.class);

    @Autowired
    private MeteoDataRepository meteoDataRepository;

    @Autowired
    private MqttService mqttService;

    @Autowired
    private MeteoDataAssembler meteoDataAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:metadata.json")
    private Resource resourceFile;


    @RequestMapping(path = "/meteo-data", method = RequestMethod.POST)
    public ResponseEntity meteo(@RequestBody MeteoData meteoData) {
        logger.info("data was received, from point {}", meteoData.getLocationId());
        meteoDataRepository.create(meteoData, true);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/sensors", method = RequestMethod.POST)
    public ResponseEntity meteoLegacy(@RequestBody MeteoData meteoData) throws IOException {
        logger.info("data was received, from point {}", meteoData.getLocationId());
        meteoDataRepository.create(meteoData, true);
        try {
            mqttService.sendMeteoData(meteoDataAssembler.to(meteoData));
            HomeKitOperation parser = new HomeKitOperation(meteoData, objectMapper, resourceFile.getInputStream());
            Map<String, Object> values = parser.getMapsTopicToValue();
            if (values != null) {
                for (Map.Entry<String, Object> k : values.entrySet()) {
                    mqttService.publish(k.getKey(), k.getValue());
                }
            }
        } catch (MqttException e) {
            logger.error("error occurred - send meteo data to mqtt " + e.getMessage());
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/sensors/{locationId}/last", method = RequestMethod.GET)
    public info.horske.meteo.application.MeteoData meteoLegacy(@PathVariable("locationId") String locationId) {
        return meteoDataRepository.readLast(locationId);
    }

}
