package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.Task.Task;
import com.JCC.LeAtcoderAPI.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTaskContent(String taskId) {
        return this.taskRepository.findByTaskId(taskId);
    }
}
