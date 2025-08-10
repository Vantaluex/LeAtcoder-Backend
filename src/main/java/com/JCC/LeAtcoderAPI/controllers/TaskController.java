package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.DifficultyObject;
import com.JCC.LeAtcoderAPI.Model.ServiceObjects.UserTaskDTO;
import com.JCC.LeAtcoderAPI.Model.Task.Task;
import com.JCC.LeAtcoderAPI.Model.User.Note;
import com.JCC.LeAtcoderAPI.services.TaskService;
import com.JCC.LeAtcoderAPI.services.UserTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserTaskService userTaskService;

    public TaskController(TaskService taskService, UserTaskService userTaskService) {
        this.taskService = taskService;
        this.userTaskService = userTaskService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<UserTaskDTO> getProblem(
            @PathVariable String taskId,
            Principal principal) {
        String userId = (principal != null) ? principal.getName() : null;

        Task task = taskService.getTaskContent(taskId);
        if(task == null){
            return ResponseEntity.notFound().build();
        }

        Optional<Note> data = userTaskService.getNoteByIds(userId, taskId);
        String noteText = data.map(Note::note).orElse("");
        String codeText = data.map(Note::code).orElse("");

        UserTaskDTO responseDTO = new UserTaskDTO(task, noteText, codeText);

        return ResponseEntity.ok(responseDTO);
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
