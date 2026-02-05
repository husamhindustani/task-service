package com.example.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Task Service application.
 * 
 * @SpringBootApplication is a convenience annotation that combines:
 * - @Configuration: Marks this class as a source of bean definitions
 * - @EnableAutoConfiguration: Tells Spring Boot to configure beans based on classpath
 * - @ComponentScan: Tells Spring to scan for components in this package and below
 */
@SpringBootApplication
public class TaskServiceApplication {

    public static void main(String[] args) {
        // SpringApplication.run() bootstraps the application:
        // 1. Creates the ApplicationContext
        // 2. Registers all beans
        // 3. Starts the embedded web server
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}
