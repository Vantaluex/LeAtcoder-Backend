package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAllUserInfo(String userId) {
        ObjectId objectId = new ObjectId(userId);
        Optional<User> user = userRepository.findById(objectId);
        return user.orElse(null);
    }

    public User findOrCreateByGoogleId(String googleId) throws IllegalStateException {
        Optional<User> user = userRepository.findByGoogleId(googleId);
        if (user.isPresent()) return user.get();

        Optional<User> createdUser = userRepository.createByGoogleId(googleId);
        if (createdUser.isPresent()) return createdUser.get();
        else throw new IllegalStateException("failed to create user with googleID: " + googleId);
    }

    private ObjectId getObjectIdAndCheckInDb(String _id) throws NoSuchElementException{
        ObjectId objectId = new ObjectId(_id);
        Optional<User> user = userRepository.findById(objectId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("User with _id " + _id + " not found.");
        }
        return objectId;
    }

    public void editUserName(String _id, String userName) {
        ObjectId objectId = this.getObjectIdAndCheckInDb(_id);
        this.userRepository.editUserName(objectId, userName);
    }

    public void updateUserStats(String _id) {
        ObjectId objectId = this.getObjectIdAndCheckInDb(_id);
        this.userRepository.updateUserStats(objectId, ScrapeService.ScrapeUser(_id));
    }
}
