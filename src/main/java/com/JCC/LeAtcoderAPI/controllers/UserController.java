package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.DifficultyObject;
import com.JCC.LeAtcoderAPI.Model.ServiceObjects.UserProgressDTO;
import com.JCC.LeAtcoderAPI.Model.User.Completed;
import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.services.ScrapeService;
import com.JCC.LeAtcoderAPI.services.TaskService;
import com.JCC.LeAtcoderAPI.services.UserService;
import com.JCC.LeAtcoderAPI.services.UserTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/users/{userId}")
public class UserController {

    private final UserService userService;
    private final UserTaskService userTaskService;
    private final TaskService taskService;

    public UserController(UserService userService, UserTaskService userTaskService, TaskService taskService) {
        this.userService = userService;
        this.userTaskService = userTaskService;
        this.taskService = taskService;
    }

    private boolean isAuthorized(String requestedUserId, String authenticatedUserId) {
        return authenticatedUserId.equals(requestedUserId);
    }

    @GetMapping
    public ResponseEntity<User> getUserInfo(
            @PathVariable String userId,
            @AuthenticationPrincipal String authenticatedUserId) {

        if (!isAuthorized(userId, authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userService.getAllUserInfo(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refreshUserInfo(
            @PathVariable String userId,
            @AuthenticationPrincipal String authenticatedUserId) {

        if (!isAuthorized(userId, authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(!ScrapeService.checkValidUserName(userId)){
            return ResponseEntity.notFound().build();
        }
        userService.updateUserStats(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/completed")
    public ResponseEntity<Void> addCompletedTask(
            @PathVariable String userId,
            @RequestParam String problemId,
            @RequestParam int score,
            @AuthenticationPrincipal String authenticatedUserId) {

        if (!isAuthorized(userId, authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Completed completedTask = new Completed(problemId, score);
        userTaskService.addCompletedTask(userId, completedTask);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/progress")
    public UserProgressDTO getFinishedTasksProgress(
            @PathVariable String userId,
            @AuthenticationPrincipal String authenticatedUserId) {

        if (!isAuthorized(userId, authenticatedUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        DifficultyObject userCompletedTasks = userTaskService.getAllCompleted(userId);
        DifficultyObject totalTasks = taskService.getTotalDifficulties();

        return new UserProgressDTO(userCompletedTasks, totalTasks);
    }
}
