package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.User.Completed;
import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.services.ScrapeService;
import com.JCC.LeAtcoderAPI.services.UserService;
import com.JCC.LeAtcoderAPI.services.UserTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users/{userId}")
public class UserController {

    private final UserService userService;
    private final UserTaskService userTaskService;

    public UserController(UserService userService, UserTaskService userTaskService) {
        this.userService = userService;
        this.userTaskService = userTaskService;
    }

    @GetMapping
    public ResponseEntity<User> getUserInfo(@PathVariable String userId) {
        User user = userService.getAllUserInfo(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refreshUserInfo(@PathVariable String userId) {
        if(!ScrapeService.checkValidUserName(userId)){
            return ResponseEntity.notFound().build();
        }
        userService.updateUserStats(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/completed") //ERM HOW DO WE MAKE THIS SAFE...? :/
    public ResponseEntity<Void> addCompletedTask(
            @PathVariable String userId,
            @RequestParam Completed problemId) {
        userTaskService.addCompletedTask(userId, problemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
