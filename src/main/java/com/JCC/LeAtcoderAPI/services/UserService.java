package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAllUserInfo(String userId) {
        ObjectId objectId = new ObjectId(userId);
        Optional<User> user = userRepository.findById(objectId);
        return user.orElse(null);
    }

    public boolean checkUserExistsByGoogleId(String googleId) {
        Optional<User> user = userRepository.findByGoogleId(googleId);
        return user.isPresent();
    }

    public User getUserByGoogleId(String googleId) {
        Optional<User> user = userRepository.findByGoogleId(googleId);
        if (user.isPresent()) {
            return user.get();
        } else {
           throw new Error("googleId: " + googleId + "does not exist");
        }
    }

    public User insertAndGetUser(String googleId, String userName) {
        // doesn't return shit right now... fix stat service
        return new User(
                "",
                "",
                "",
                0,
                0,
                0,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    private ObjectId getObjectIdAndCheckInDb(String id) {
        ObjectId objectId = new ObjectId(id);
        Optional<User> user = userRepository.findById(objectId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("User with ID " + id + " not found.");
        }
        return objectId;
    }

    public void editUserName(String id, String userName) {
        ObjectId objectId = this.getObjectIdAndCheckInDb(id);
        this.userRepository.editUserName(objectId, userName);
    }

    public void updateUserRank(String id, int rank) {
        ObjectId objectId = this.getObjectIdAndCheckInDb(id);
        this.userRepository.updateUserRank(objectId, rank);
    }

    public void updateUserPercentile(String id, double percentile) {
        ObjectId objectId = this.getObjectIdAndCheckInDb(id);
        this.userRepository.updateUserPercentile(objectId, percentile);
    }

    public void updateUserRating(String id, int rating) {
        ObjectId objectId = this.getObjectIdAndCheckInDb(id);
        this.userRepository.updateUserRating(objectId, rating);
    }
}
