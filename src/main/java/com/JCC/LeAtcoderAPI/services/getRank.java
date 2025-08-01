package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.User;
import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class getRank {

    private final UserRepository userRepository;

    @Autowired
    public getRank(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Integer fetch(String username) {
        return userRepository.getUserInfo(username)
                .map(User::rank)
                .orElse(null);
    }

}
