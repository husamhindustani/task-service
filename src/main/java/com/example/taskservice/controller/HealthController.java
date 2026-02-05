package com.example.taskservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom health/info endpoints.
 * 
 * Note: Spring Boot Actuator provides built-in health endpoints at /actuator/health
 * This controller provides additional custom endpoints.
 */
@RestController
public class HealthController {

    private final LocalDateTime startTime = LocalDateTime.now();

    /**
     * Simple root endpoint to verify the service is running.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Task Service");
        response.put("status", "running");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    /**
     * Liveness probe endpoint.
     * 
     * In Kubernetes, this tells the kubelet if the container is alive.
     * If this fails, Kubernetes will restart the container.
     * 
     * This should be a simple check that the application is running.
     */
    @GetMapping("/health/live")
    public ResponseEntity<Map<String, String>> liveness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }

    /**
     * Readiness probe endpoint.
     * 
     * In Kubernetes, this tells the kubelet if the container is ready
     * to receive traffic. If this fails, the pod is removed from
     * service load balancers.
     * 
     * This could include checks for database connectivity, etc.
     */
    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, String>> readiness() {
        // In a real app, you might check database connectivity here
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }

    /**
     * Service info endpoint.
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Task Service");
        response.put("version", "1.0.0");
        response.put("startedAt", startTime);
        response.put("uptime", java.time.Duration.between(startTime, LocalDateTime.now()).toString());
        return ResponseEntity.ok(response);
    }
}
