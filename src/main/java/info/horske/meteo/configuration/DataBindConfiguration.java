package info.horske.meteo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rucka
 */
@Configuration
public class DataBindConfiguration {

    @Bean
    public ObjectMapper createMapper() {
        return new ObjectMapper();
    }
}
