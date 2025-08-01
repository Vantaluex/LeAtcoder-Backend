package com.JCC.LeAtcoderAPI.Model.Task;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "tasks")
public record Task(
        @Id
        String _id,
        String id,
        String url,
        String taskName,
        int score,
        String statement,
        String constraints,
        String input,
        String output,
        List<Sample> samples
){}
