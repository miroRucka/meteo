package info.horske.meteo.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import info.horske.meteo.domain.MeteoData;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
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

    public List<MeteoData> fromList(FindIterable<Document> data) {
        if (data == null) return Collections.emptyList();
        List<MeteoData> result = new ArrayList<>();
        for (Document sensorData : data) {
            sensorData.remove("_id");
            sensorData.remove("_v");
            JsonWriterSettings writerSettings;
            writerSettings = JsonWriterSettings.builder().dateTimeConverter((value, writer) -> writer.writeNumber(String.valueOf(value))).build();
            result.add(from(String.valueOf(sensorData.toJson(writerSettings))));
        }
        return result;
    }
}
