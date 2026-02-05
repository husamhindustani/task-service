package com.example.taskservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a task is not found.
 * 
 * @ResponseStatus: Tells Spring to return 404 NOT FOUND 
 * when this exception is thrown from a controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
