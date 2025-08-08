package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.DifficultyObject;
import com.JCC.LeAtcoderAPI.Model.Task.Task;
import com.JCC.LeAtcoderAPI.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<Task> getProblem(@PathVariable String problemId) {
        Task task = taskService.getTaskContent(problemId);

        if(task == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public List<Task> getTaskList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "all") List<String> difficulty,
            @RequestParam(defaultValue = "all") String status,
            Principal principal) {

        int pageIndex = page > 0 ? page - 1 : 0;
        String userId = (principal != null) ? principal.getName() : null;

        return taskService.getTaskList(userId, pageIndex, difficulty, status);
    }

    // GET /problems/difficulties - Keep the existing difficulty stats
    @GetMapping("/difficulties")
    public DifficultyObject getDifficulties() {
        return taskService.getTotalDifficulties();
    }

}
