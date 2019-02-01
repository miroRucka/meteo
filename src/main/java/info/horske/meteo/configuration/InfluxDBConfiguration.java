package info.horske.meteo.configuration;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rucka
 */
@Configuration
public class InfluxDBConfiguration {

    private Logger logger = LoggerFactory.getLogger(InfluxDBConfiguration.class);

    @Value("${spring.influx.url}")
    private String influxUrl;

    @Value("${spring.influx.user}")
    private String influxUser;

    @Value("${spring.influx.password}")
    private String influxPassword;

    @Value("${spring.influx.db}")
    private String influxUsesDb;


    @Bean
    public InfluxDB connect() {
        InfluxDB influxDB = InfluxDBFactory.connect(influxUrl, influxUser, influxPassword);
        influxDB.setDatabase(influxUsesDb);
        logger.info("connected to url {} ping {} ", influxUrl, influxDB.ping());
        return influxDB;
    }
}
