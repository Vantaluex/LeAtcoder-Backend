package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class StatService {
    private final UserRepository userRepository;

    public StatService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
