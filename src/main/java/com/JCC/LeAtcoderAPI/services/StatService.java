package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class StatService {
    private final UserRepository userRepository;

    public StatService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Integer getRank(String username) {
        return userRepository.getUserInfo(username)
                .map(User::rank)
                .orElse(null);
    }

}
