package info.horske.meteo.application;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import info.horske.meteo.domain.MeteoData;
import org.bson.Document;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
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
    private MongoDatabase mongoDatabase;

    @Autowired
    private InfluxPointBuilder influxPointBuilder;

    @Autowired
    private MeteoDataAssembler meteoDataAssembler;

    @Value("${spring.influx.sensor.table}")
    private String influxSensorTable;

    @Value("${spring.mongodb.sensor.table}")
    private String mongoSensorTable;

    @Value("${spring.influx.db}")
    private String influxUsesDb;

    @Override
    public void create(MeteoData meteoData, boolean useSysTimestam) {
        if (meteoData == null) return;
        Point.Builder result = Point.measurement(influxSensorTable).time(useSysTimestam ? System.currentTimeMillis() : meteoData.getTimestamp(), TimeUnit.MILLISECONDS);
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

    @Override
    public List<MeteoData> readAll() {
        FindIterable<Document> data = mongoDatabase.getCollection("sensors").find();
        return meteoDataAssembler.fromList(data);

    }

    @Override
    public List<MeteoData> readLatestByDate(Date fromTime) {
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("timestamp", BasicDBObjectBuilder.start("$gte", fromTime).get());
        FindIterable<Document> data = mongoDatabase.getCollection("sensors").find(gtQuery);
        return meteoDataAssembler.fromList(data);
    }

    @Override
    public info.horske.meteo.application.MeteoData readLast(String locationId) {
        int limit = 1;
        String select = "SELECT * FROM \"" + influxSensorTable + "\" WHERE locationId = '" + locationId + "' ORDER BY time DESC LIMIT " + limit;
        Query query = new Query(select, influxUsesDb);
        QueryResult queryResult = influxDB.query(query);
        queryResult.getResults();
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<info.horske.meteo.application.MeteoData> meteoDatas = resultMapper.toPOJO(queryResult, info.horske.meteo.application.MeteoData.class);
        return meteoDatas != null ? meteoDatas.stream().findAny().get() : null;
    }


    //@PostConstruct
    public void onDestroy() throws Exception {
    }
}

