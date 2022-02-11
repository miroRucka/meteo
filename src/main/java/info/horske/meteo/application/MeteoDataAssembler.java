package info.horske.meteo.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.horske.meteo.domain.MeteoData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author rucka
 */
@Component
public class MeteoDataAssembler {

    private Logger logger = LoggerFactory.getLogger(MeteoDataAssembler.class);

    @Autowired
    private ObjectMapper objectMapper;

    public MeteoData from(String meteoData) {
        try {
            return objectMapper.readValue(meteoData, MeteoData.class);
        } catch (IOException e) {
            logger.error("error data transform json to POJO " + meteoData, e);
            return null;
        }
    }

    public String to(MeteoData meteoData) {
        try {
            return objectMapper.writeValueAsString(meteoData);
        } catch (IOException e) {
            logger.error("error data transform json to POJO " + meteoData, e);
            return null;
        }
    }
}
