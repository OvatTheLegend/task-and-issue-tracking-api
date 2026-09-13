package com.devpulse.taskengine.model;

import java.time.Instant;
import java.util.UUID;

public record Task(
        UUID id,
        String title,
        String description,
        Priority priority,
        TaskStatus status,
        Instant createdAt) {

}
