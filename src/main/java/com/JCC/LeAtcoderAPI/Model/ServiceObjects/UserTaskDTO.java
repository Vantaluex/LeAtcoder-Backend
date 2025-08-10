package com.JCC.LeAtcoderAPI.Model.ServiceObjects;

import com.JCC.LeAtcoderAPI.Model.Task.Task;

public record UserTaskDTO( //DTO stands for data transfer objects apparently
                           Task task,
                           String note,
                           String code
) {}