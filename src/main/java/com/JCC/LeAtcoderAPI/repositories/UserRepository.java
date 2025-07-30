package com.JCC.LeAtcoderAPI.repositories;

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
        if (this.collection.find(filter).first() == null) {
            // Document userdata = function.takein.userdata(googleId);
            // this.collection.insertOne(userdata); // Ideally call a function of some sorts that takes in userdata
            return "User successfully created!";
        } else {
            return "Existing user";
        }
    }

    public Document getUserInfo(String username) {
        Bson filter  = eq("Username", username);
        return (Document) this.collection.find(filter).first();
    }

    public String editUserName(String username, String newusername){
        Bson filter  = eq("Username", username);
        Bson update = set("Username", newusername);
        collection.updateOne(filter, update);
        return "Username successfully updated!";
    }
}
