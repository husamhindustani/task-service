# =============================================================================
# Dockerfile for Task Service
# =============================================================================
# This is a MULTI-STAGE build:
# - Stage 1 (builder): Compiles the application using Maven
# - Stage 2 (runtime): Runs the application with minimal JRE
#
# Why multi-stage?
# - Build image: ~500MB (includes Maven, full JDK, source code)
# - Runtime image: ~200MB (only JRE + your JAR)
# - Smaller = faster deploys, less storage, smaller attack surface
# =============================================================================

# =============================================================================
# STAGE 1: BUILD
# =============================================================================
# Use Maven image with JDK 21 to compile the application
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml first (for dependency caching)
# Docker caches layers - if pom.xml hasn't changed, dependencies won't re-download
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
# -B = batch mode (non-interactive)
# dependency:go-offline = download all dependencies
RUN mvn dependency:go-offline -B

# Now copy source code
COPY src ./src

# Build the application
# -DskipTests = skip tests for faster build (tests should run in CI)
# package = compile + package into JAR
RUN mvn package -DskipTests -B

# =============================================================================
# STAGE 2: RUNTIME
# =============================================================================
# Use slim JRE image (no compiler, no Maven - just runtime)
FROM eclipse-temurin:21-jre-alpine

# Add labels for container metadata
LABEL maintainer="Task Service Team"
LABEL version="1.0.0"
LABEL description="Task Management REST API"

# Create a non-root user for security
# Running as root in containers is a security risk
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR from the builder stage
# --from=builder references the first stage
COPY --from=builder /app/target/task-service-1.0.0.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Document that the container listens on port 8080
# This is informational - you still need -p to publish the port
EXPOSE 8080

# Health check - Docker/K8s will use this to verify container health
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM tuning for containers
# -XX:+UseContainerSupport = respect container memory limits
# -XX:MaxRAMPercentage=75 = use up to 75% of container memory for heap
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
# Using exec form (JSON array) - preferred for signal handling
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
