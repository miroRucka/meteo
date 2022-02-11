package info.horske.meteo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author rucka
 */
public class EnergyData {
    public Double vt;
    public Double nt;
    @JsonFormat(pattern = "dd.MM.yyyy")
    public Date date;
}
