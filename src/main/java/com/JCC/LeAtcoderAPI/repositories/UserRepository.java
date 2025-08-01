package com.JCC.LeAtcoderAPI.repositories;

import com.JCC.LeAtcoderAPI.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Check if user exists by userId
    @Query("{'_id': ?0}")
    boolean checkUserByUserId(String userId);

    // Check if user exists by googleId
    @Query("{'googleId': ?0}")
    boolean checkUserByGoogleId(String googleId);

    // Get userId by googleId
    @Query(value = "{'googleId': ?0}", fields = "{'_id': 1}")
    Optional<String> getUserIdByGoogleId(String googleId); //if user doesn't exist, it returns Optional.empty();

    // Get all user info by id
    @Query("{'_id': ?0}")
    Optional<User> getUserInfo(String id);

    // Update username
    @Query("{'_id': ?0}")
    @Update("{'$set': {'username': ?1}}")
    void editUserName(String id, String name);

    // Update user rank
    @Query("{'_id': ?0}")
    @Update("{'$set': {'rank': ?1}}")
    void updateUserRank(String id, int rank);

    // Additional helper method - find user by googleId
    @Query("{'googleId': ?0}")
    Optional<User> findByGoogleId(String googleId);
}
