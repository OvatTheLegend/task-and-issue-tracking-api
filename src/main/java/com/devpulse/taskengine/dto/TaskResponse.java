package com.devpulse.taskengine.dto;

import java.time.Instant;
import java.util.UUID;

import com.devpulse.taskengine.model.Priority;
import com.devpulse.taskengine.model.Task;
import com.devpulse.taskengine.model.TaskStatus;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        Priority priority,
        TaskStatus status,
        Instant createdAt) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.id(),
                task.title(),
                task.description(),
                task.priority(),
                task.status(),
                task.createdAt());
    }
}
