package info.horske.meteo.configuration;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @author rucka
 */
@Configuration
public class MongoDBConfiguration {

    private Logger logger = LoggerFactory.getLogger(MongoDBConfiguration.class);

    @Value("${spring.mongodb.url}")
    private String mongoUrl;

    @Value("${spring.mongodb.user}")
    private String mongoUser;

    @Value("${spring.mongodb.password}")
    private String mongoPassword;

    @Value("${spring.mongodb.db}")
    private String mongoDb;

    @Bean
    public MongoDatabase createMongoClient() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://admin:DT3EK93t@horske.info"));
        return mongoClient.getDatabase(mongoDb);
    }

}
