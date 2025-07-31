package com.JCC.LeAtcoderAPI.repositories;

import com.JCC.LeAtcoderAPI.services.scrapeUser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import com.mongodb.client.result.UpdateResult;
import javax.print.Doc;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Repository
public class UserRepository {
    private MongoCollection<Document> collection;

    public UserRepository(DatabaseClient dbClient) {
        this.collection = dbClient.getDb().getCollection("users");
    }

    public String createUser(String googleId) {
        Bson filter  = eq("googleId", googleId);
        if (this.collection.find(filter).first() != null) {
            return "Existing user";
        }
        // Document userdata = function.takein.userdata(googleId);
        // this.collection.insertOne(userdata); // Ideally call a function of some sorts that takes in userdata
        return "User successfully created!";
    }

    public Document getUserInfo(String username) {
        Bson filter  = eq("Username", username);
        if(this.collection.find(filter).first() == null){
            return scrapeUser.fetch(username);
        }
        return this.collection.find(filter).first();

    }

    public String editUserName(String username, String newusername){
        Bson filter  = eq("Username", username);
        Bson update = set("Username", newusername);
        collection.updateOne(filter, update);
        return "Username successfully updated!";
    }
}
