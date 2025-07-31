package com.JCC.LeAtcoderAPI.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
public record User(
    @Id
    String _id,
    String googleId,
    String userName,
    int rating,
    List<Completed> completedList,
    List<Note> noteList
){}
