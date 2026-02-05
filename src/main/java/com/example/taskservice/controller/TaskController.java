package com.example.taskservice.controller;

import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskDTO;
import com.example.taskservice.dto.UpdateTaskRequest;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Task operations.
 * 
 * Now uses DTOs to separate API models from database entities:
 * - TaskDTO: Response object (what clients receive)
 * - CreateTaskRequest: Request for creating tasks
 * - UpdateTaskRequest: Request for updating tasks
 * 
 * This provides:
 * - Control over what data is exposed
 * - Separation between API contract and database schema
 * - Different validation rules for create vs update
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks
     * List all tasks, optionally filtered by status.
     * 
     * @param status Optional query parameter to filter by status
     * @return List of TaskDTOs
     */
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(required = false) TaskStatus status) {
        
        List<Task> tasks;
        if (status != null) {
            tasks = taskService.getTasksByStatus(status);
        } else {
            tasks = taskService.getAllTasks();
        }
        
        // Convert entities to DTOs
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(taskDTOs);
    }

    /**
     * GET /api/tasks/{id}
     * Get a specific task by ID.
     * 
     * @param id Task ID from URL path
     * @return TaskDTO if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskDTO.fromEntity(task));
    }

    /**
     * POST /api/tasks
     * Create a new task.
     * 
     * @param request CreateTaskRequest from request body (JSON)
     * @return Created TaskDTO with 201 status
     * 
     * Note: Client can only set title and description.
     * ID, status, and timestamps are handled by the system.
     */
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskRequest request) {
        // Convert request DTO to entity
        Task task = request.toEntity();
        
        // Create the task
        Task createdTask = taskService.createTask(task);
        
        // Return response DTO
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaskDTO.fromEntity(createdTask));
    }

    /**
     * PUT /api/tasks/{id}
     * Update an existing task.
     * 
     * @param id Task ID to update
     * @param request UpdateTaskRequest with updated data
     * @return Updated TaskDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        
        // Create a task entity with the update data
        Task taskDetails = new Task();
        taskDetails.setTitle(request.getTitle());
        taskDetails.setDescription(request.getDescription());
        taskDetails.setStatus(request.getStatus());
        
        Task updatedTask = taskService.updateTask(id, taskDetails);
        
        return ResponseEntity.ok(TaskDTO.fromEntity(updatedTask));
    }

    /**
     * PATCH /api/tasks/{id}/status
     * Update only the status of a task.
     * 
     * Using PATCH for partial updates (only changing status)
     * 
     * @param id Task ID
     * @param status New status
     * @return Updated TaskDTO
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {
        Task updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(TaskDTO.fromEntity(updatedTask));
    }

    /**
     * DELETE /api/tasks/{id}
     * Delete a task.
     * 
     * @param id Task ID to delete
     * @return 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/tasks/search
     * Search tasks by title.
     * 
     * @param q Search query
     * @return Matching TaskDTOs
     */
    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasks(@RequestParam String q) {
        List<Task> tasks = taskService.searchTasks(q);
        
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(taskDTOs);
    }
}
