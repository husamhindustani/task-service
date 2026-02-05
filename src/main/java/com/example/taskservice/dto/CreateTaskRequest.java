package com.example.taskservice.dto;

import com.example.taskservice.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new Task.
 * 
 * Only contains fields that clients are allowed to set when creating.
 * - No id (auto-generated)
 * - No status (defaults to PENDING)
 * - No timestamps (auto-generated)
 */
@Schema(description = "Request body for creating a new task")
public class CreateTaskRequest {

    @Schema(
            description = "Task title",
            example = "Learn Docker",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Schema(
            description = "Detailed task description (optional)",
            example = "Complete Module 2 of the Docker/Kubernetes course"
    )
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    // Default constructor
    public CreateTaskRequest() {
    }

    // Constructor with fields
    public CreateTaskRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Convert this request to a Task entity.
     * Status and timestamps will be set by the entity/service.
     */
    public Task toEntity() {
        Task task = new Task();
        task.setTitle(this.title);
        task.setDescription(this.description);
        return task;
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
