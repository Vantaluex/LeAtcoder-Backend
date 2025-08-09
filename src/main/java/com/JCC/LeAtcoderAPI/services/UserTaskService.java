package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.DifficultyObject;
import com.JCC.LeAtcoderAPI.Model.User.Completed;
import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import com.JCC.LeAtcoderAPI.repositories.UserTaskRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserTaskService {
    private final UserTaskRepository userTaskRepository;
    UserTaskService(UserTaskRepository userTaskRepository) {
        this.userTaskRepository = userTaskRepository;
    }
    public void addCompletedTask(String userId, Completed completed) {
        userTaskRepository.addCompletedToUser(userId, completed);
    }
    public DifficultyObject getAllCompleted(String userId) {
        int beginner = this.userTaskRepository.getAllCompleted(
                DifficultyObject.BEGINNER_MIN, DifficultyObject.BEGINNER_MAX, userId);

        int easy = this.userTaskRepository.getAllCompleted(
                DifficultyObject.EASY_MIN, DifficultyObject.EASY_MAX, userId);

        int medium = this.userTaskRepository.getAllCompleted(
                DifficultyObject.MEDIUM_MIN, DifficultyObject.MEDIUM_MAX, userId);

        int hard = this.userTaskRepository.getAllCompleted(
                DifficultyObject.HARD_MIN, DifficultyObject.HARD_MAX, userId);

        int expert = this.userTaskRepository.getAllCompleted(
                DifficultyObject.EXPERT_MIN, DifficultyObject.EXPERT_MAX, userId);

        return new DifficultyObject(beginner, easy, medium, hard, expert);
    }
}
