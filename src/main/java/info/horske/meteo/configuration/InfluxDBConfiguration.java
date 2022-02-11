package info.horske.meteo.configuration;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.net.ConnectException;

/**
 * @author rucka
 */
@Configuration
public class InfluxDBConfiguration {

    public static final int CONNECTION_ATTEMPT_LIMIT = 5;
    public static final long WAIT_TO_CONNECT = 5000l;
    public static final long START_WAITING = 10000l;

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
    public InfluxDB connect() throws InterruptedException, ConnectException {
        int connectionAttempt = 0;
        Thread.sleep(START_WAITING);
        while (CONNECTION_ATTEMPT_LIMIT > connectionAttempt) {
            try {
                return connecntInfluxDB();
            } catch (Exception e) {
                connectionAttempt++;
                Thread.sleep(WAIT_TO_CONNECT);
            }
        }
        throw new ConnectException("cannot connect to influxDB");
    }

    private InfluxDB connecntInfluxDB() {
        InfluxDB influxDB = InfluxDBFactory.connect(influxUrl, influxUser, influxPassword);
        influxDB.setDatabase(influxUsesDb);
        logger.info("connected to url {} ping {} ", influxUrl, influxDB.ping());
        return influxDB;
    }

}
