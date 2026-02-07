package com.example.taskservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration.
 * 
 * Configures the API documentation with proper server URLs
 * for both local development and Kubernetes deployment.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Task Service API")
                .version("1.0.0")
                .description("REST API for managing tasks"))
            .servers(List.of(
                // Relative URL - uses whatever host/port the browser accessed
                new Server().url("/").description("Current server"),
                // Explicit servers for different environments
                new Server().url("http://localhost").description("Kubernetes Ingress (port 80)"),
                new Server().url("http://localhost:30080").description("Kubernetes NodePort"),
                new Server().url("http://localhost:8080").description("Local development")
            ));
    }
}
