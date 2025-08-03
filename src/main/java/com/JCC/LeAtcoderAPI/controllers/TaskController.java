package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.DifficultyObject;
import com.JCC.LeAtcoderAPI.Model.Task.Task;
import com.JCC.LeAtcoderAPI.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{problemId}")
    public Task getProblem(@PathVariable String problemId) {
        return taskService.getTaskContent(problemId);
    }

    @GetMapping("/getTaskList/{page}/{difficulty}/{completed}")
    public List<Task> getTaskList(
            @PathVariable int page,
            @PathVariable String difficulty,
            @PathVariable boolean completed) {

        int[] scoreRange = DifficultyObject.getRange(difficulty);
        int pageIndex = page - 1;

        return taskService.getTaskList(pageIndex, scoreRange[0], scoreRange[1]);
    }

    // GET /problems/difficulties - Keep the existing difficulty stats
    @GetMapping("/difficulties")
    public DifficultyObject getDifficulties() {
        return taskService.getTotalDifficulties();
    }

}
