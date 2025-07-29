package com.JCC.LeAtcoderAPI;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import com.mongodb.client.MongoClient;

public class DatabaseClient {
    private static final Dotenv dotenv = Dotenv.load();
    private final MongoDatabase dbInstance;
    public DatabaseClient() {
        String URI = dotenv.get("MONGO_CONNECTION_URI");
        try {
            MongoClient mongoClient = MongoClients.create(URI);
            this.dbInstance = mongoClient.getDatabase("LeAtCoder");
        } finally {
            System.out.println("bitch");
        }
    }

    public MongoDatabase getDb() {
        return this.dbInstance;
    }
}
