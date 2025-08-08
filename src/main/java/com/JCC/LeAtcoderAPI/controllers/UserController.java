package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.services.ScrapeService;
import com.JCC.LeAtcoderAPI.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users/{userId}")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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


}
