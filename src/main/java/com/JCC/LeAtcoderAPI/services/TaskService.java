package com.JCC.LeAtcoderAPI.services;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.DifficultyObject;
import com.JCC.LeAtcoderAPI.Model.Task.Task;
import com.JCC.LeAtcoderAPI.Model.User.Completed;
import com.JCC.LeAtcoderAPI.repositories.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.repositories.UserTaskRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class TaskService {
    private final UserTaskRepository userTaskRepository;
    private final TaskRepository taskRepository;
    private final MongoTemplate mongoTemplate;

    TaskService(TaskRepository taskRepository, UserTaskRepository userTaskRepository, MongoTemplate mongoTemplate) {
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
        this.mongoTemplate = mongoTemplate; // Add to constructor
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


    public List<Task> getTaskList(String userId, int index, List<String> difficulties, String status) {
        Pageable pageable = PageRequest.of(index, 50);

        List<Criteria> allConditions = new ArrayList<>();

        // Part 1: Build difficulty criteria
        List<Criteria> difficultyOrCriteriaList = difficulties.stream()
                .map(difficulty -> {
                    int[] range = DifficultyObject.getRange(difficulty);
                    return Criteria.where("score").gte(range[0]).lte(range[1]);
                })
                .collect(Collectors.toList());

        if (!difficultyOrCriteriaList.isEmpty()) {
            allConditions.add(new Criteria().orOperator(difficultyOrCriteriaList));
        }

        // Part 2: Build status criteria
        if (!"all".equalsIgnoreCase(status)) {
            List<String> completedTaskIds = (userId == null)
                    ? Collections.emptyList()
                    : userTaskRepository.findCompletedListByGoogleId(userId)
                    .orElse(Collections.emptyList())  // Already a List<Completed>
                    .stream()
                    .map(Completed::taskId)
                    .collect(Collectors.toList());

            if ("completed".equalsIgnoreCase(status)) {
                allConditions.add(Criteria.where("_id").in(completedTaskIds));
            } else { // "uncompleted"
                allConditions.add(Criteria.where("_id").nin(completedTaskIds));
            }
        }

        // Part 3: Combine all conditions and execute
        Criteria finalCriteria = new Criteria();
        if (!allConditions.isEmpty()) {
            finalCriteria.andOperator(allConditions.toArray(new Criteria[0]));
        }

        Query query = new Query(finalCriteria).with(pageable);

        return mongoTemplate.find(query, Task.class);
    }
}
