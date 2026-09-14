package com.devpulse.taskengine.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devpulse.taskengine.dto.CreateTaskRequest;
import com.devpulse.taskengine.dto.TaskResponse;
import com.devpulse.taskengine.dto.UpdateTaskRequest;
import com.devpulse.taskengine.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService service) {
        this.taskService = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest request) {
        TaskResponse created = taskService.createTask(request);

        URI location = URI.create("/api/tasks/" + created.id());

        return ResponseEntity.created(location).body(created);

    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskbyId(@PathVariable UUID id) {
        TaskResponse found = taskService.getTaskById(id);

        return ResponseEntity.ok(found);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id, @RequestBody UpdateTaskRequest request) {
        TaskResponse updated = taskService.updateTask(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
