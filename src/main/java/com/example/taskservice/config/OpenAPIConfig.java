package com.example.taskservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration.
 * 
 * This configures the API documentation metadata that appears
 * in the Swagger UI at /swagger-ui.html
 */
@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Service API")
                        .version("1.0.0")
                        .description("""
                                A simple task management REST API for learning Docker and Kubernetes.
                                
                                ## Features
                                - Create, read, update, and delete tasks
                                - Filter tasks by status
                                - Search tasks by title
                                
                                ## Learning Project
                                This API is part of a hands-on course covering:
                                - Spring Boot REST APIs
                                - Docker containerization
                                - Kubernetes orchestration
                                """)
                        .contact(new Contact()
                                .name("Task Service Team")
                                .email("support@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local development server")
                ));
    }
}
