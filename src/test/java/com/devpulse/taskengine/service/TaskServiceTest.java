package com.devpulse.taskengine.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devpulse.taskengine.dto.CreateTaskRequest;
import com.devpulse.taskengine.dto.TaskResponse;
import com.devpulse.taskengine.dto.UpdateTaskRequest;
import com.devpulse.taskengine.exception.TaskNotFoundException;
import com.devpulse.taskengine.model.Priority;
import com.devpulse.taskengine.model.TaskStatus;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundById() {
        UUID nonExistentId = UUID.randomUUID();
        assertThatThrownBy(() -> taskService.getTaskById(nonExistentId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(nonExistentId.toString());

    }

    @Test
    void shouldCreateTaskSuccessfully() {
        CreateTaskRequest request = new CreateTaskRequest("Write Unit Tests", "Test the service layer", Priority.HIGH);

        TaskResponse response = taskService.createTask(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("Write Unit Tests");
        assertThat(response.description()).isEqualTo("Test the service layer");
        assertThat(response.priority()).isEqualTo(Priority.HIGH);
        assertThat(response.status()).isEqualTo(TaskStatus.TODO);
        assertThat(response.createdAt()).isNotNull();

    }

    @Test
    void shouldGetAllTasks() {
        taskService.createTask(new CreateTaskRequest("Task 1", "Desc 1", Priority.LOW));
        taskService.createTask(new CreateTaskRequest("Task 2", "Desc 2", Priority.HIGH));

        List<TaskResponse> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
    }

    @Test
    void shouldGetTaskByIdSuccessfully() {
        TaskResponse created = taskService.createTask(new CreateTaskRequest("Task 1", "Desc 1", Priority.MEDIUM));

        TaskResponse found = taskService.getTaskById(created.id());

        assertThat(found.id()).isEqualTo(created.id());
        assertThat(found.title()).isEqualTo("Task 1");
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        TaskResponse created = taskService.createTask(new CreateTaskRequest("Original", "Original Desc", Priority.LOW));
        UpdateTaskRequest updateRequest = new UpdateTaskRequest("Updated Title", "New Desc", Priority.HIGH,
                TaskStatus.IN_PROGRESS);
        TaskResponse updated = taskService.updateTask(created.id(), updateRequest);
        assertThat(updated.id()).isEqualTo(created.id());
        assertThat(updated.createdAt()).isEqualTo(created.createdAt());
        assertThat(updated.title()).isEqualTo("Updated Title");
        assertThat(updated.status()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        UUID nonExistentId = UUID.randomUUID();
        UpdateTaskRequest updateRequest = new UpdateTaskRequest("Title", "Desc", Priority.LOW, TaskStatus.COMPLETED);
        assertThatThrownBy(() -> taskService.updateTask(nonExistentId, updateRequest))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        TaskResponse created = taskService.createTask(new CreateTaskRequest("Task to delete", "Desc", Priority.LOW));
        taskService.deleteTask(created.id());
        assertThatThrownBy(() -> taskService.getTaskById(created.id()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        UUID nonExistentId = UUID.randomUUID();
        assertThatThrownBy(() -> taskService.deleteTask(nonExistentId))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
