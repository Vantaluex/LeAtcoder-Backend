package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.DifficultyObject;
import com.JCC.LeAtcoderAPI.Model.Task.Task;
import com.JCC.LeAtcoderAPI.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTaskContent(String taskId) {
        return this.taskRepository.findByid(taskId);
    }

    public DifficultyObject getTotalDifficulties() {
        int beginner = this.taskRepository.countByScoreBetween(0, 200);
        int easy = this.taskRepository.countByScoreBetween(201, 300);
        int medium = this.taskRepository.countByScoreBetween(301, 450);
        int hard = this.taskRepository.countByScoreBetween(451, 650);
        int expert = this.taskRepository.countByScoreBetween(651, 1500);
        return new DifficultyObject(beginner, easy, medium, hard, expert);
    }

    public List<Task> getTaskList(int index, int min, int max) {
        Pageable pageable = PageRequest.of(index, 50);
        Page<Task> taskPage = this.taskRepository.findByScoreBetween(min, max, pageable);
        return taskPage.getContent();
    }
}
