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
        int beginner = this.taskRepository.countByScoreBetween(DifficultyObject.BEGINNER_MIN, DifficultyObject.BEGINNER_MAX);
        int easy = this.taskRepository.countByScoreBetween(DifficultyObject.EASY_MIN, DifficultyObject.EASY_MAX);
        int medium = this.taskRepository.countByScoreBetween(DifficultyObject.MEDIUM_MIN, DifficultyObject.MEDIUM_MAX);
        int hard = this.taskRepository.countByScoreBetween(DifficultyObject.HARD_MIN, DifficultyObject.HARD_MAX);
        int expert = this.taskRepository.countByScoreBetween(DifficultyObject.EXPERT_MIN, DifficultyObject.EXPERT_MAX);
        return new DifficultyObject(beginner, easy, medium, hard, expert);
    }


    public List<Task> getTaskList(int index, int min, int max) {
        Pageable pageable = PageRequest.of(index, 50);
        Page<Task> taskPage = this.taskRepository.findByScoreBetween(min, max, pageable);
        return taskPage.getContent();
    }
}
