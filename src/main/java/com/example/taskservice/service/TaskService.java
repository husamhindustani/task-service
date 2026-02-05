package com.example.taskservice.service;

import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Task business logic.
 * 
 * Why a service layer?
 * - Separates business logic from controllers (HTTP handling)
 * - Easier to test in isolation
 * - Can be reused by multiple controllers or other services
 * - Transaction management happens here
 * 
 * @Service: Marks this as a Spring-managed service component
 * @Transactional: Ensures database operations are wrapped in transactions
 */
@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Constructor injection (preferred over @Autowired on fields).
     * Spring automatically injects the TaskRepository bean.
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get all tasks, ordered by creation date (newest first).
     */
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Get a specific task by ID.
     * Throws TaskNotFoundException if not found.
     */
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * Create a new task.
     */
    public Task createTask(Task task) {
        // Ensure new tasks start with PENDING status
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    /**
     * Update an existing task.
     */
    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = getTaskById(id);
        
        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        
        if (taskDetails.getStatus() != null) {
            existingTask.setStatus(taskDetails.getStatus());
        }
        
        return taskRepository.save(existingTask);
    }

    /**
     * Update only the status of a task.
     */
    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task task = getTaskById(id);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    /**
     * Delete a task by ID.
     */
    public void deleteTask(Long id) {
        Task task = getTaskById(id); // Verify it exists
        taskRepository.delete(task);
    }

    /**
     * Get all tasks with a specific status.
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    /**
     * Search tasks by title.
     */
    @Transactional(readOnly = true)
    public List<Task> searchTasks(String query) {
        return taskRepository.findByTitleContainingIgnoreCase(query);
    }
}
