package info.horske.meteo.application;

import info.horske.meteo.domain.MeteoData;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author rucka
 */
@Repository
public class MeteoDataRepository implements info.horske.meteo.domain.MeteoDataRepository {

    private Logger logger = LoggerFactory.getLogger(MeteoDataRepository.class);

    @Autowired
    private InfluxDB influxDB;

    @Autowired
    private InfluxPointBuilder influxPointBuilder;

    @Override
    public void create(MeteoData meteoData) {
        if (meteoData == null) return;
        Point.Builder result = Point.measurement("meteo-point").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        influxPointBuilder.addTagIfNotEmpty(result, "location", meteoData.getLocation());
        influxPointBuilder.addTagIfNotEmpty(result, "locationId", meteoData.getLocationId());
        influxPointBuilder.addTagIfNotEmpty(result, "note", meteoData.getNote());
        influxPointBuilder.addFieldIfNotEmpty(result, "pressure", meteoData.getPressure());
        influxPointBuilder.addFieldIfNotEmpty(result, "light", meteoData.getLight());
        influxPointBuilder.addFieldIfNotEmpty(result, "humidity", meteoData.getHumidity());
        influxPointBuilder.addTemperaturesIfNotEmpty(result, "temperature", meteoData.getTemperature());
        try {
            influxDB.write(result.build());
        } catch (Exception e) {
            logger.error("error - write to db", e);
        }
    }
}

