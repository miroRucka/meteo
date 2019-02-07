package info.horske.meteo.application;

import info.horske.meteo.domain.MeteoData;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rucka
 */
@RestController
public class MeteoDataController {

    private Logger logger = LoggerFactory.getLogger(MeteoDataController.class);

    @Autowired
    private MeteoDataRepository meteoDataRepository;

    @Autowired
    private MqttService mqttService;

    @Autowired
    private MeteoDataAssembler meteoDataAssembler;

    @RequestMapping(path = "/meteo-data", method = RequestMethod.POST)
    public ResponseEntity meteo(@RequestBody MeteoData meteoData) {
        logger.info("data was received, from point {}", meteoData.getLocationId());
        meteoDataRepository.create(meteoData, true);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/api/sensors", method = RequestMethod.POST)
    public ResponseEntity meteoLegacy(@RequestBody MeteoData meteoData) {
        logger.info("data was received, from point {}", meteoData.getLocationId());
        meteoDataRepository.create(meteoData, true);
        try {
            mqttService.sendMeteoData(meteoDataAssembler.to(meteoData));
        } catch (MqttException e) {
            logger.error("error occurred - send meteo data to mqtt " + e.getMessage());
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
