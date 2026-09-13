package com.devpulse.taskengine.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.devpulse.taskengine.dto.CreateTaskRequest;
import com.devpulse.taskengine.dto.TaskResponse;
import com.devpulse.taskengine.dto.UpdateTaskRequest;
import com.devpulse.taskengine.exception.TaskNotFoundException;
import com.devpulse.taskengine.model.Task;
import com.devpulse.taskengine.model.TaskStatus;

@Service
public class TaskService {
    private final Map<UUID, Task> taskStore = new ConcurrentHashMap<>();

    public TaskResponse createTask(CreateTaskRequest request) {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Task newTask = new Task(id, request.title(), request.description(), request.priority(), TaskStatus.TODO, now);

        taskStore.putIfAbsent(id, newTask);

        return TaskResponse.from(newTask);
    }

    public List<TaskResponse> getAllTasks() {
        return taskStore.values().stream()
                .map(task -> TaskResponse.from(task)) // or .map(TaskResponse::from)
                .toList();
    }

    public TaskResponse getTaskById(UUID id) {
        Task task = Optional.ofNullable(taskStore.get(id))
                .orElseThrow(() -> new TaskNotFoundException(id));

        return TaskResponse.from(task);
    }

    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {
        Task existing = Optional.ofNullable(taskStore.get(id))
                .orElseThrow(() -> new TaskNotFoundException(id));

        Task updatedTask = new Task(id, request.title(), request.description(), request.priority(), request.status(),
                existing.createdAt());

        taskStore.put(id, updatedTask);

        return TaskResponse.from(updatedTask);
    }

    public void deleteTask(UUID id) {
        Task removed = taskStore.remove(id);

        if (removed == null) {
            throw new TaskNotFoundException(id);
        }
    }
}
