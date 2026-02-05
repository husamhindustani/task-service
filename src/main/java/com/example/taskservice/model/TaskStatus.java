package com.example.taskservice.model;

/**
 * Enum representing the possible states of a Task.
 * 
 * Using an enum for status provides:
 * - Type safety (can't set invalid status)
 * - Clear documentation of valid values
 * - Easy to extend with new statuses
 * 
 * In the database, this will be stored as a STRING (see @Enumerated in Task.java)
 */
public enum TaskStatus {
    
    PENDING,      // Task has been created but not started
    IN_PROGRESS,  // Task is currently being worked on
    COMPLETED,    // Task has been finished
    CANCELLED     // Task was cancelled and won't be completed
}
