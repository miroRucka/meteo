package info.horske.meteo.application;

import info.horske.meteo.domain.Temperature;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author rucka
 */
@Component
public class InfluxPointBuilder {

    public Point.Builder addTagIfNotEmpty(Point.Builder point, String tagName, String tagValue) {
        if (tagValue != null && !StringUtils.isEmpty(tagValue)) {
            point.tag(tagName, tagValue);
        }
        return point;
    }

    public Point.Builder addFieldIfNotEmpty(Point.Builder point, String fildName, Double fieldValue) {
        if (fieldValue != null) {
            point.addField(fildName, fieldValue);
        }
        return point;
    }

    public Point.Builder addTemperaturesIfNotEmpty(Point.Builder point, String fildName, List<Temperature> temperatures) {
        if (temperatures == null) return point;
        temperatures.forEach(t -> {
            point.addField(fildName + "_" + t.getKey(), t.getValue());
        });
        return point;
    }
}
