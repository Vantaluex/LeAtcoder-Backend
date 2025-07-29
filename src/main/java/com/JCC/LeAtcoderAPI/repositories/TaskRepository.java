package com.JCC.LeAtcoderAPI.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import static com.mongodb.client.model.Filters.eq;

@Repository
public class TaskRepository {
    private MongoCollection collection;

    public TaskRepository(DatabaseClient dbClient) {
        this.collection = dbClient.getDb().getCollection("tasks");
    }

    public Document getTaskContent(String questionId) {
        Bson filter  = eq("id", questionId);
        return (Document) this.collection.find(filter).first();
    }
}
