package com.devpulse.taskengine.dto;

import com.devpulse.taskengine.model.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required and cannot be blank") @Size(max = 100, message = "Title must not exceed 100 characters") String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters") String description,

        @NotNull(message = "Priority is required") Priority priority) {

}
