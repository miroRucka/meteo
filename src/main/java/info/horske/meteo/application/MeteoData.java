package info.horske.meteo.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * @author rucka
 */
@Measurement(name = "meteo-sensor-point")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeteoData {

    @Column(name = "temperature_t1")
    public Double temperature_t1;
    @Column(name = "temperature_t2")
    public Double temperature_t2;
    @Column(name = "temperature_t3")
    public Double temperature_t3;
    @Column(name = "temperature_t4")
    public Double temperature_t4;
    @Column(name = "temperature_t5")
    public Double temperature_t5;
    @Column(name = "temperature_t6")
    public Double temperature_t6;

    @Column(name = "pressure")
    public Double pressure;
    @Column(name = "light")
    public Double light;
    @Column(name = "humidity")
    public Double humidity;
    @Column(name = "location", tag = true)
    public String location;
    @Column(name = "locationId", tag = true)
    public String locationId;
    @Column(name = "note", tag = true)
    public String note;
    @Column(name = "time", tag = true)
    public String timeISO;


    public String getTimeFormatted() {
        if (timeISO != null && !timeISO.isEmpty()) {
            try {
                return ISO8601.convertISOtoTime(timeISO);
            } catch (ParseException e) {
                LoggerFactory.getLogger(MeteoData.class).error("error - parsing date", e);
                return timeISO;
            }

        }
        return timeISO;
    }
}
