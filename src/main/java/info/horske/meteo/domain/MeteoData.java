
package info.horske.meteo.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "temperature",
    "pressure",
    "light",
    "humidity",
    "location",
    "locationId",
    "timestamp",
    "note"
})
public class MeteoData {

    @JsonProperty("temperature")
    private List<Temperature> temperature = null;
    @JsonProperty("pressure")
    private Double pressure;
    @JsonProperty("light")
    private Double light;
    @JsonProperty("humidity")
    private Double humidity;
    @JsonProperty("location")
    private String location;
    @JsonProperty("locationId")
    private String locationId;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("note")
    private String note;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("temperature")
    public List<Temperature> getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(List<Temperature> temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("pressure")
    public Double getPressure() {
        return pressure;
    }

    @JsonProperty("pressure")
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @JsonProperty("light")
    public Double getLight() {
        return light;
    }

    @JsonProperty("light")
    public void setLight(Double light) {
        this.light = light;
    }

    @JsonProperty("humidity")
    public Double getHumidity() {
        return humidity;
    }

    @JsonProperty("humidity")
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("locationId")
    public String getLocationId() {
        return locationId;
    }

    @JsonProperty("locationId")
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @JsonProperty("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("note")
    public String getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(String note) {
        this.note = note;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
