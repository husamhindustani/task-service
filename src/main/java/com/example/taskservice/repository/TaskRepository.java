package com.example.taskservice.repository;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Task entity.
 * 
 * Spring Data JPA magic explained:
 * - By extending JpaRepository, we get CRUD operations for free:
 *   - save(), findById(), findAll(), delete(), count(), etc.
 * - Spring creates the implementation at runtime (no code needed!)
 * - We can add custom query methods using naming conventions
 * 
 * JpaRepository<Task, Long>:
 * - Task: The entity type this repository manages
 * - Long: The type of the entity's ID field
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find all tasks with a specific status.
     * 
     * Spring Data JPA derives the query from the method name:
     * findByStatus -> SELECT * FROM tasks WHERE status = ?
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find all tasks whose title contains a given string (case-insensitive).
     * 
     * Method name parsing:
     * - findBy: Start of query
     * - Title: Field to query
     * - ContainingIgnoreCase: LIKE %value% (case insensitive)
     */
    List<Task> findByTitleContainingIgnoreCase(String title);

    /**
     * Find all tasks ordered by creation date (newest first).
     */
    List<Task> findAllByOrderByCreatedAtDesc();
}
