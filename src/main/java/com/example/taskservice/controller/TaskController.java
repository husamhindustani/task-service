package com.example.taskservice.controller;

import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskDTO;
import com.example.taskservice.dto.UpdateTaskRequest;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Task operations.
 * 
 * Uses DTOs to separate API models from database entities:
 * - TaskDTO: Response object (what clients receive)
 * - CreateTaskRequest: Request for creating tasks
 * - UpdateTaskRequest: Request for updating tasks
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieves all tasks, optionally filtered by status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @Parameter(description = "Filter by task status")
            @RequestParam(required = false) TaskStatus status) {
        
        List<Task> tasks;
        if (status != null) {
            tasks = taskService.getTasksByStatus(status);
        } else {
            tasks = taskService.getAllTasks();
        }
        
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(taskDTOs);
    }

    @Operation(
            summary = "Get task by ID",
            description = "Retrieves a specific task by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskDTO.fromEntity(task));
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with PENDING status. ID and timestamps are auto-generated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @Valid @RequestBody CreateTaskRequest request) {
        Task task = request.toEntity();
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaskDTO.fromEntity(createdTask));
    }

    @Operation(
            summary = "Update a task",
            description = "Updates an existing task's title, description, and/or status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        
        Task taskDetails = new Task();
        taskDetails.setTitle(request.getTitle());
        taskDetails.setDescription(request.getDescription());
        taskDetails.setStatus(request.getStatus());
        
        Task updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(TaskDTO.fromEntity(updatedTask));
    }

    @Operation(
            summary = "Update task status",
            description = "Updates only the status of a task (partial update)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status", required = true)
            @RequestParam TaskStatus status) {
        Task updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(TaskDTO.fromEntity(updatedTask));
    }

    @Operation(
            summary = "Delete a task",
            description = "Permanently deletes a task"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search tasks",
            description = "Search tasks by title (case-insensitive)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed")
    })
    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasks(
            @Parameter(description = "Search query", required = true)
            @RequestParam String q) {
        List<Task> tasks = taskService.searchTasks(q);
        
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(taskDTOs);
    }
}
