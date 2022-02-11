package info.horske.meteo.application;

import info.horske.meteo.domain.EnergyData;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author rucka
 */
@Repository
public class EnergyDataRepository implements info.horske.meteo.domain.EnergyDataRepository {

    private Logger logger = LoggerFactory.getLogger(EnergyDataController.class);

    @Autowired
    private InfluxDB influxDB;

    @Value("${spring.influx.energy.table}")
    private String influxEnergyTable;

    @Value("${spring.influx.db}")
    private String influxUsesDb;

    @Autowired
    private InfluxPointBuilder influxPointBuilder;

    @Override
    public void create(EnergyData energyData, boolean useSysTimestam) {
        if (energyData == null) return;
        Point.Builder result = Point.measurement(influxEnergyTable).time(useSysTimestam ? System.currentTimeMillis() : energyData.date.getTime(), TimeUnit.MILLISECONDS);
        influxPointBuilder.addTagIfNotEmpty(result, "location", "Na_Barek_6");
        influxPointBuilder.addFieldIfNotEmpty(result, "nt", energyData.nt);
        influxPointBuilder.addFieldIfNotEmpty(result, "vt", energyData.vt);
        try {
            influxDB.write(result.build());
        } catch (Exception e) {
            logger.error("error - write to db", e);
        }
    }
}
