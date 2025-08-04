package com.JCC.LeAtcoderAPI.repositories;

import com.JCC.LeAtcoderAPI.Model.User.User;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Additional helper method - find user by googleId
    @Query("{'googleId': ?0}")
    Optional<User> findByGoogleId(String googleId);

    // Get all user info by id
    Optional<User> findById(ObjectId Id);

    // Update username
    @Query("{'_id': ?0}")
    @Update("{'$set': {'username': ?1}}")
    void editUserName(ObjectId id, String name);

    // Update user rank
    @Query("{'_id': ?0}")
    @Update("{'$set': {'rank': ?1}}")
    void updateUserRank(ObjectId id, int rank);

    @Query("{'_id': ?0}")
    @Update("{'$set': {'percentile': ?1}}")
    void updateUserPercentile(ObjectId id, double percentile);

    @Query("{'_id': ?0}")
    @Update("{'$set': {'rating': ?1}}")
    void updateUserRating(ObjectId id, int rating);

}
