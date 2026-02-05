package com.example.taskservice.dto;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Response DTO for Task.
 * 
 * This is what clients receive when requesting task data.
 * We control exactly what fields are exposed.
 */
@Schema(description = "Task response object")
public class TaskDTO {

    @Schema(description = "Unique task identifier", example = "1")
    private Long id;

    @Schema(description = "Task title", example = "Learn Docker")
    private String title;

    @Schema(description = "Detailed task description", example = "Complete Module 2 of the course")
    private String description;

    @Schema(description = "Current task status", example = "PENDING")
    private TaskStatus status;

    @Schema(description = "When the task was created", example = "2026-02-05T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "When the task was last updated", example = "2026-02-05T14:45:00")
    private LocalDateTime updatedAt;

    // Default constructor
    public TaskDTO() {
    }

    // All-args constructor
    public TaskDTO(Long id, String title, String description, TaskStatus status,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory method to create DTO from Entity.
     * This is where we control the mapping.
     */
    public static TaskDTO fromEntity(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
