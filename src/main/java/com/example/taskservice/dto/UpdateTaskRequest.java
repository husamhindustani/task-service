package com.example.taskservice.dto;

import com.example.taskservice.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing Task.
 * 
 * Contains fields that clients are allowed to modify.
 * - No id (comes from URL path)
 * - No timestamps (auto-updated)
 * - Status CAN be changed (unlike create)
 */
@Schema(description = "Request body for updating an existing task")
public class UpdateTaskRequest {

    @Schema(
            description = "Task title",
            example = "Learn Docker and Kubernetes",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Schema(
            description = "Detailed task description (optional)",
            example = "Complete all modules of the course"
    )
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Schema(
            description = "Task status",
            example = "IN_PROGRESS"
    )
    private TaskStatus status;

    // Default constructor
    public UpdateTaskRequest() {
    }

    // Constructor with fields
    public UpdateTaskRequest(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
