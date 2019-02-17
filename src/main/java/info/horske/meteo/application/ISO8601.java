package info.horske.meteo.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author rucka
 */
public class ISO8601 {

    private static final DateFormat IN_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
    private static final DateFormat OUT_FORMAT = new SimpleDateFormat("dd.MM. HH:mm");

    public static Date parse(String date) throws ParseException {
        IN_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        return IN_FORMAT.parse(date);
    }

    public static String convertISOtoTime(String date) throws ParseException {
        OUT_FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Bratislava"));
        return OUT_FORMAT.format(parse(date));
    }


}
