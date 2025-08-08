package com.JCC.LeAtcoderAPI.services;

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

}
