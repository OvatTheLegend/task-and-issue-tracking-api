package com.devpulse.taskengine.dto;

import com.devpulse.taskengine.model.Priority;

public record CreateTaskRequest(
        String title,
        String description,
        Priority priority) {

}
