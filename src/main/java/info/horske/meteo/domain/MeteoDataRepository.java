package info.horske.meteo.domain;

import java.util.List;
import java.util.Date;

/**
 * @author rucka
 */
public interface MeteoDataRepository {

    void create(MeteoData meteoData, boolean useSysTimestam);

    List<MeteoData> readAll();

    List<MeteoData> readLatestByDate(Date fromTime);

    info.horske.meteo.application.MeteoData readLast(String pointId);

}
