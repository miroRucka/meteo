package info.horske.meteo.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rucka
 */
public class HomeKitOperation {

    private Map<String, Object> data = new HashMap<>();

    public HomeKitOperation(MeteoData meteoData, ObjectMapper objectMapper, InputStream metadataFile) throws IOException {
        List<Map<String, String>> metadata = objectMapper.readValue(metadataFile, List.class);
        parse(meteoData, metadata);
    }

    private void parse(MeteoData meteoData, List<Map<String, String>> metadata) {
        List<Map<String, String>> meta = metadata.stream().filter(m -> getPointId(m).equals(meteoData.getLocationId())).collect(Collectors.toList());
        if (meta != null) {
            meta.forEach(m -> data.put(getTopic(m), getValue(m, meteoData)));
        }
    }

    private String getPointId(Map<String, String> map) {
        return map.get("pointId");
    }

    private String getTopic(Map<String, String> map) {
        return map.get("topic");
    }

    private String getType(Map<String, String> map) {
        return map.get("type");
    }

    private String getSensorKey(Map<String, String> map) {
        return map.get("sensorKey");
    }

    private Object getValue(Map<String, String> map, MeteoData meteoData) {
        String type = getType(map);
        if (type.equals("temp")) {
            Optional<Temperature> temp = meteoData.getTemperature() != null ? meteoData.getTemperature().stream().filter(t -> t.getKey().equals(getSensorKey(map))).findAny() : Optional.<Temperature>empty();
            return temp.isPresent() ? temp.get().getValue() : null;
        } else if (type.equals("hum")) {
            return meteoData.getHumidity();
        }
        return null;
    }

    public Map<String, Object> getMapsTopicToValue() {
        return Collections.unmodifiableMap(data);
    }
}
