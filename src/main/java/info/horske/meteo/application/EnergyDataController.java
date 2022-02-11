package info.horske.meteo.application;

import info.horske.meteo.domain.EnergyData;
import info.horske.meteo.domain.EnergyDataRepository;
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
@RequestMapping("/api")
public class EnergyDataController {

    private Logger logger = LoggerFactory.getLogger(EnergyDataController.class);

    @Autowired
    private EnergyDataRepository energyDataRepository;

    @RequestMapping(path = "/energy-data", method = RequestMethod.POST)
    public ResponseEntity meteo(@RequestBody EnergyData energyData) {
        energyDataRepository.create(energyData, false);
        logger.info("energy data created...");
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
