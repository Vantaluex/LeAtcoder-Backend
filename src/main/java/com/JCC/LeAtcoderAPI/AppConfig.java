package com.JCC.LeAtcoderAPI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class AppConfig {
    private static final Dotenv dotenv = Dotenv.load();
    @Bean
    public MongoClient mongoClient(@Value("${spring.data.mongodb.uri}") String mongoURI) {
        return MongoClients.create(mongoURI);
    }

    @Bean
    MongoOperations mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "LeAtcoder");
    }
}
