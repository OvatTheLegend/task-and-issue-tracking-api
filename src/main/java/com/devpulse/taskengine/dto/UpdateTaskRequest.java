package com.devpulse.taskengine.dto;

import com.devpulse.taskengine.model.Priority;
import com.devpulse.taskengine.model.TaskStatus;

public record UpdateTaskRequest(
                String title,
                String description,
                Priority priority,
                TaskStatus status) {

}
