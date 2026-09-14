package com.devpulse.taskengine.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.devpulse.taskengine.dto.TaskResponse;
import com.devpulse.taskengine.model.Priority;
import com.devpulse.taskengine.model.TaskStatus;
import com.devpulse.taskengine.service.TaskService;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private TaskService taskService;

        @Test
        void shouldCreateTaskAndReturn201() throws Exception {
                UUID id = UUID.randomUUID();
                TaskResponse fakeResponse = new TaskResponse(
                                id,
                                "Buy Milk",
                                "Whole milk",
                                Priority.LOW,
                                TaskStatus.TODO,
                                Instant.now());

                when(taskService.createTask(any())).thenReturn(fakeResponse);

                String requestJson = """
                                {
                                    "title": "Buy Milk",
                                    "description": "Whole milk",
                                    "priority": "LOW"
                                }
                                """;

                mockMvc.perform(post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", "/api/tasks/" + id))
                                .andExpect(jsonPath("$.id").value(id.toString()))
                                .andExpect(jsonPath("$.title").value("Buy Milk"))
                                .andExpect(jsonPath("$.priority").value("LOW"))
                                .andExpect(jsonPath("$.status").value("TODO"));
        }

        @Test
        void shouldGetAllTasksAndReturn200() throws Exception {
                TaskResponse task1 = new TaskResponse(UUID.randomUUID(), "Task 1", "Desc 1", Priority.LOW,
                                TaskStatus.TODO,
                                Instant.now());

                TaskResponse task2 = new TaskResponse(UUID.randomUUID(), "Task 2", "Desc 2", Priority.HIGH,
                                TaskStatus.IN_PROGRESS, Instant.now());

                when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

                mockMvc.perform(get("/api/tasks"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$[0].title").value("Task 1"))
                                .andExpect(jsonPath("$[1].title").value("Task 2"));
        }

        @Test
        void shouldGetTaskByIdAndReturn200() throws Exception {
                UUID id = UUID.randomUUID();
                TaskResponse fakeTask = new TaskResponse(id, "Specific Task", "Desc", Priority.MEDIUM, TaskStatus.TODO,
                                Instant.now());

                when(taskService.getTaskById(id)).thenReturn(fakeTask);

                mockMvc.perform(get("/api/tasks/" + id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id.toString()))
                                .andExpect(jsonPath("$.title").value("Specific Task"));

        }

        @Test
        void shouldUpdateTaskAndReturn200() throws Exception {
                UUID id = UUID.randomUUID();
                TaskResponse updatedResponse = new TaskResponse(id, "Updated Title", "Updated Desc", Priority.HIGH,
                                TaskStatus.IN_PROGRESS, Instant.now());

                when(taskService.updateTask(org.mockito.ArgumentMatchers.eq(id), any())).thenReturn(updatedResponse);

                String updateJson = """
                                {
                                    "title": "Updated Title",
                                    "description": "Updated Desc",
                                    "priority": "HIGH",
                                    "status": "IN_PROGRESS"
                                }
                                """;

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/tasks/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Title"))
                                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        }

        @Test
        void shouldDeleteTaskAndReturn204() throws Exception {
                UUID id = UUID.randomUUID();

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/tasks/" + id))
                                .andExpect(status().isNoContent()); // Asserts HTTP 204 No Content!
        }
}