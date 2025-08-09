package com.JCC.LeAtcoderAPI.Model.ServiceObjects;

public record UserProgressDTO( //DTO stands for data transfer objects apparently
        DifficultyObject userProgress,
        DifficultyObject totalTasks
) {}