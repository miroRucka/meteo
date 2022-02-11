package info.horske.meteo.domain;

/**
 * @author rucka
 */
public interface EnergyDataRepository {

    void create(EnergyData energyData, boolean useSysTimestam);
}
