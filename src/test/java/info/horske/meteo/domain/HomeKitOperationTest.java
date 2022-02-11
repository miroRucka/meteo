package info.horske.meteo.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author rucka
 */

public class HomeKitOperationTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void parseTest() throws IOException {
        File input = new ClassPathResource("input.json").getFile();
        InputStream metadata = new ClassPathResource("metadata.json").getInputStream();
        MeteoData meteoData = objectMapper.readValue(input, MeteoData.class);
        HomeKitOperation parser = new HomeKitOperation(meteoData, objectMapper, metadata);
        Map<String, Object> result = parser.getMapsTopicToValue();
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.get("meteo-bridge/temp/officein"));
        Assertions.assertNotNull(result.get("meteo-bridge/hum/officein"));
        Assertions.assertEquals(result.get("meteo-bridge/temp/officein"), 20.8);
        Assertions.assertEquals(result.get("meteo-bridge/hum/officein"), 43.3);
    }
}
