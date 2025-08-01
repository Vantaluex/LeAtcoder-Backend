package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class getRank {

    private final UserRepository userRepository;

    @Autowired
    public getRank(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String fetch(String username) {
        Document userDoc = userRepository.getUserInfo(username);
        if (userDoc == null) {
            return null;
        }
        return userDoc.getString("rank");
    }
}
