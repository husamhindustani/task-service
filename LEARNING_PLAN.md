# Docker & Kubernetes Learning Course

A hands-on course to learn containerization and orchestration by building and deploying a Java service with PostgreSQL.

---

## Course Overview

**What you'll build:** A REST API service in Java (Spring Boot) with PostgreSQL database

**What you'll learn:**
- Docker: Containerizing applications
- Docker Compose: Multi-container local development
- Container Registry: Storing and distributing images
- Kubernetes: Orchestrating containers at scale
- CI/CD: Automating the pipeline
- Production patterns: Health checks, scaling, secrets, releases

**Prerequisites:**
- Java basics (we'll use Spring Boot)
- Basic command line knowledge
- Docker Desktop installed
- A code editor

---

## Module 1: The Java Service ✅ COMPLETED

### Step 1.1: Project Setup ✅
- [x] Create Spring Boot project with required dependencies
- [x] Understand Maven/Gradle project structure
- [x] Configure application properties

**Key concepts covered:**
- Spring Boot auto-configuration
- Maven dependency management (`pom.xml`)
- Application configuration via `application.yaml`
- Environment variable substitution with defaults: `${VAR:default}`

### Step 1.2: Build the REST API ✅
- [x] Create a simple domain model (Task entity)
- [x] Implement JPA entity and repository
- [x] Create REST controller with CRUD operations
- [x] Add health check endpoint

**Endpoints built:**
```
GET    /api/tasks          - List all tasks (with filtering)
POST   /api/tasks          - Create a task
GET    /api/tasks/{id}     - Get a task
PUT    /api/tasks/{id}     - Update a task
DELETE /api/tasks/{id}     - Delete a task
GET    /actuator/health    - Health check
GET    /swagger-ui.html    - API documentation
```

### Step 1.3: Database Configuration ✅
- [x] Configure Spring Data JPA for PostgreSQL
- [x] Understand connection pooling (HikariCP)
- [x] Set up Flyway for database migrations

**Key concepts covered:**
- DataSource configuration with environment variables
- JPA/Hibernate ORM - mapping Java objects to database tables
- Flyway migrations - version control for database schema
- Spring Data repositories - automatic CRUD implementation

### Step 1.4: Test the Application ✅
- [x] Run PostgreSQL locally
- [x] Start the application with `java -jar`
- [x] Test endpoints with curl

### Step 1.5: Enhancements Added ✅
- [x] DTOs (Data Transfer Objects) - separate API models from entities
- [x] OpenAPI/Swagger documentation
- [x] Git repository setup with `.gitignore`
- [x] Proper validation annotations

**Lessons Learned:**
- `export` is required for environment variables to be inherited by child processes
- Maven mirrors can interfere with dependency resolution
- PostgreSQL user/role management on macOS (superuser vs regular user)

---

## Module 2: Docker Fundamentals ✅ COMPLETED

### Step 2.1: Understanding Containers ✅
- [x] Learn what containers are vs VMs
- [x] Understand Docker architecture (daemon, client, registry)
- [x] Learn about images, containers, layers

**Key concepts covered:**
- Docker Daemon: The background service that manages containers
- Docker Client: CLI that talks to the daemon
- Container isolation (namespaces, cgroups)
- Image layers and caching
- Container lifecycle

### Step 2.2: Writing a Dockerfile ✅
- [x] Create a Dockerfile for the Java app
- [x] Understand Dockerfile instructions
- [x] Learn about base image selection

**Dockerfile instructions learned:**
```dockerfile
FROM      - Base image
WORKDIR   - Set working directory
COPY      - Copy files into image
RUN       - Execute commands during build
ENV       - Set environment variables
EXPOSE    - Document ports (metadata only)
USER      - Run as non-root user
HEALTHCHECK - Container health monitoring
ENTRYPOINT - Main executable
```

### Step 2.3: Multi-Stage Builds ✅
- [x] Understand why multi-stage builds matter
- [x] Create a multi-stage Dockerfile (build + runtime)
- [x] Compare image sizes

**Key concepts covered:**
- Build stage: Uses full Maven/JDK image (~500MB)
- Runtime stage: Uses slim JRE Alpine image (~175MB)
- Only the final stage becomes the output image
- Intermediate stages are discarded (not cached for reuse)
- Layer caching: Copy `pom.xml` before `src/` for better cache hits

### Step 2.4: Building and Running Containers ✅
- [x] Build the Docker image: `docker build`
- [x] Run the container: `docker run`
- [x] Understand port mapping, environment variables
- [x] Learn container debugging: `docker logs`, `docker exec`

**Essential commands mastered:**
```bash
docker build -t task-service:1.0.0 .
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=... task-service:1.0.0
docker ps
docker logs <container-id>
docker exec -it <container-id> /bin/sh
docker stop <container-id>
docker rm <container-id>
```

### Step 2.5: Docker Networking Basics ✅
- [x] Understand Docker networks
- [x] Learn `host.docker.internal` for container-to-host communication

**Key concept:**
- `host.docker.internal`: DNS name that resolves to the host machine's IP
- Used when container needs to reach services running on the host (e.g., local PostgreSQL)

---

## Module 3: Docker Compose ✅ COMPLETED

### Step 3.1: Introduction to Compose ✅
- [x] Understand what Docker Compose solves
- [x] Learn YAML syntax basics
- [x] Create docker-compose.yaml

**Key concepts covered:**
- Service definitions - each service = one container
- Declarative infrastructure - describe desired state
- Docker Compose does NOT bundle services into one image

### Step 3.2: Multi-Container Setup ✅
- [x] Define the Java service in Compose
- [x] Add PostgreSQL as a service
- [x] Configure service dependencies (`depends_on` with `condition: service_healthy`)

**Architecture understood:**
```
┌─────────────────────────────────────────────────┐
│            Docker Compose Network               │
│                                                 │
│  ┌─────────────┐         ┌─────────────┐       │
│  │     app     │  ────►  │     db      │       │
│  │  (Java)     │         │ (PostgreSQL)│       │
│  │  Port 8080  │         │  Port 5432  │       │
│  └─────────────┘         └─────────────┘       │
└─────────────────────────────────────────────────┘
```

### Step 3.3: Volumes and Persistence ✅
- [x] Understand volume types (named volumes)
- [x] Configure PostgreSQL data persistence
- [x] Understand volume lifecycle

**Key concepts covered:**
- Named volumes persist beyond container lifecycle
- `docker-compose down -v` removes volumes (data loss!)
- Volumes survive Docker daemon restarts
- Volume data stored in `/var/lib/docker/volumes/`

### Step 3.4: Environment Configuration ✅
- [x] Use environment variables in Compose
- [x] Service discovery via Docker DNS (containers reference each other by service name)

**Key insight:**
- Container `app` connects to `db:5432` (internal Docker network)
- Host connects to `localhost:5433` (mapped port)
- Internal container ports ≠ host-mapped ports

### Step 3.5: Running the Full Stack ✅
- [x] Start everything: `docker-compose up -d`
- [x] Test the application with database
- [x] Learn compose commands

**Essential commands mastered:**
```bash
docker-compose up -d          # Start in background
docker-compose logs -f app    # Follow logs
docker-compose ps             # List services
docker-compose exec app sh    # Shell into container
docker-compose down           # Stop and remove containers
docker-compose down -v        # Also remove volumes
docker-compose restart app    # Restart a service
docker network ls             # List networks
docker network inspect <name> # Inspect network details
```

### Step 3.6: Health Checks ✅
- [x] Configure healthcheck in docker-compose.yaml
- [x] Use `depends_on` with `condition: service_healthy`
- [x] Understand `pg_isready` for PostgreSQL health

---

## Module 4: Container Registry ✅ COMPLETED

### Step 4.1: Understanding Registries ✅
- [x] Learn what a container registry is
- [x] Compare registry options (Docker Hub, GHCR, ECR, GCR)

**Key concept:**
- Registry = "GitHub for container images"
- Stores and distributes Docker images
- Required for Kubernetes to pull images

### Step 4.2: Docker Hub Setup ✅
- [x] Create Docker Hub account
- [x] Login via CLI: `docker login`

### Step 4.3: Image Tagging ✅
- [x] Understand tagging format: `username/image:version`
- [x] Tag local image for registry

**Tagging best practices:**
| Tag | Purpose |
|-----|---------|
| `1.0.0` | Specific version (semantic versioning) |
| `sha-abc123` | Git commit SHA (traceability) |
| `latest` | Avoid in production (ambiguous) |

### Step 4.4: Push and Pull ✅
- [x] Push image to Docker Hub
- [x] Verify image accessible on registry

**Commands used:**
```bash
docker tag task-service:1.0.0 husamhindustani/task-service:1.0.0
docker push husamhindustani/task-service:1.0.0
docker pull husamhindustani/task-service:1.0.0
```

**Key insight:**
- `hh-app:latest` (from docker-compose) and `task-service:1.0.0` (manual build) are separate images
- Both built from same Dockerfile, contain only Java app (not PostgreSQL)

---

## Module 5: Kubernetes Concepts

### Step 5.1: Kubernetes Architecture
- [ ] Understand the control plane (API server, etcd, scheduler, controller manager)
- [ ] Understand worker nodes (kubelet, kube-proxy, container runtime)
- [ ] Learn about the declarative model

**Key concepts:**
- Desired state vs current state
- Reconciliation loops
- API-driven architecture

### Step 5.2: Core Resources - Pods
- [ ] Understand what a Pod is
- [ ] Learn Pod lifecycle
- [ ] Create a Pod manifest (YAML)
- [ ] Run and inspect a Pod

**Key concepts:**
- Pods as the atomic unit
- Container co-location
- Pod networking (shared localhost)

### Step 5.3: Core Resources - Deployments
- [ ] Understand Deployments and ReplicaSets
- [ ] Create a Deployment manifest
- [ ] Learn about replicas and scaling
- [ ] Understand rolling updates

**Key concepts:**
- Declarative updates
- Rollback capability
- Self-healing

### Step 5.4: Core Resources - Services
- [ ] Understand Service types (ClusterIP, NodePort, LoadBalancer)
- [ ] Create a Service manifest
- [ ] Learn about service discovery (DNS)
- [ ] Understand label selectors

**Key concepts:**
- Stable network endpoints
- Load balancing
- Service discovery

### Step 5.5: Configuration - ConfigMaps & Secrets
- [ ] Understand externalized configuration
- [ ] Create ConfigMaps for app config
- [ ] Create Secrets for sensitive data
- [ ] Mount as environment variables or files

**Key concepts:**
- Separation of config from code
- Secret management
- Configuration updates

---

## Module 6: Local Kubernetes Setup

### Step 6.1: Choose Your Local Cluster
- [ ] Option A: Enable Kubernetes in Docker Desktop
- [ ] Option B: Install Minikube
- [ ] Option C: Install Kind (Kubernetes in Docker)
- [ ] Verify cluster is running

**Commands to verify:**
```bash
kubectl cluster-info
kubectl get nodes
kubectl get namespaces
```

### Step 6.2: kubectl Essentials
- [ ] Learn kubectl syntax: `kubectl <verb> <resource>`
- [ ] Master essential commands
- [ ] Understand output formats (yaml, json, wide)

**Essential commands:**
```bash
kubectl get pods
kubectl get deployments
kubectl get services
kubectl describe pod <name>
kubectl logs <pod-name>
kubectl exec -it <pod-name> -- /bin/sh
kubectl apply -f <file.yaml>
kubectl delete -f <file.yaml>
```

### Step 6.3: Namespaces
- [ ] Understand namespace isolation
- [ ] Create a namespace for your project
- [ ] Set default namespace context

---

## Module 7: Deploying the Java Service (Stateless)

### Step 7.1: Create Deployment Manifest
- [ ] Write the Deployment YAML
- [ ] Configure replicas
- [ ] Set resource requests and limits
- [ ] Reference image from Docker Hub
- [ ] Apply and verify

### Step 7.2: Create Service Manifest
- [ ] Write the Service YAML
- [ ] Choose appropriate Service type
- [ ] Apply and verify connectivity

### Step 7.3: Add Health Probes
- [ ] Understand liveness vs readiness probes
- [ ] Configure HTTP probes for Spring Boot Actuator
- [ ] Test probe behavior

**Key concepts:**
- Liveness: Is the app alive? (restart if not)
- Readiness: Can it handle traffic? (remove from load balancer if not)
- Startup probes: For slow-starting apps

### Step 7.4: External Configuration
- [ ] Create ConfigMap for application properties
- [ ] Create Secret for database credentials
- [ ] Mount as environment variables
- [ ] Verify configuration is applied

---

## Module 8: Deploying PostgreSQL (Stateful)

### Step 8.1: Understanding StatefulSets
- [ ] Learn differences from Deployments
- [ ] Understand stable network identity
- [ ] Understand stable storage

**Key differences:**
| Deployment | StatefulSet |
|------------|-------------|
| Random pod names | Ordered pod names (db-0, db-1) |
| Shared storage (if any) | Per-pod storage |
| Parallel startup | Sequential startup |
| Any-order termination | Reverse-order termination |

### Step 8.2: Persistent Volumes
- [ ] Understand PersistentVolume (PV) and PersistentVolumeClaim (PVC)
- [ ] Learn about storage classes
- [ ] Create a PVC for PostgreSQL

**Key concepts:**
- Storage provisioning (static vs dynamic)
- Access modes (ReadWriteOnce, ReadWriteMany)
- Reclaim policies

### Step 8.3: Create StatefulSet Manifest
- [ ] Write StatefulSet YAML for PostgreSQL
- [ ] Configure volumeClaimTemplates
- [ ] Apply and verify

### Step 8.4: Headless Service
- [ ] Understand headless Services for StatefulSets
- [ ] Create headless Service manifest
- [ ] Understand DNS records created

### Step 8.5: Connect App to Database
- [ ] Update ConfigMap with database URL
- [ ] Verify connectivity
- [ ] Test data persistence (delete pod, verify data remains)

---

## Module 9: Kubernetes Networking

### Step 9.1: Pod Networking
- [ ] Understand Pod IP addresses
- [ ] Learn about CNI plugins
- [ ] Test pod-to-pod communication

### Step 9.2: Service Discovery
- [ ] Understand Kubernetes DNS
- [ ] Learn service FQDN format
- [ ] Test DNS resolution from pods

**DNS format:**
```
<service-name>.<namespace>.svc.cluster.local
```

### Step 9.3: Ingress
- [ ] Understand Ingress resource
- [ ] Install an Ingress controller (nginx)
- [ ] Create Ingress manifest
- [ ] Configure host-based routing

**Key concepts:**
- Layer 7 load balancing
- TLS termination
- Path-based routing

---

## Module 10: Operations & Observability

### Step 10.1: Scaling
- [ ] Scale deployment manually: `kubectl scale`
- [ ] Understand Horizontal Pod Autoscaler (HPA)
- [ ] Configure HPA based on CPU/memory

**Commands:**
```bash
kubectl scale deployment myapp --replicas=5
kubectl autoscale deployment myapp --min=2 --max=10 --cpu-percent=80
```

### Step 10.2: Rolling Updates
- [ ] Update deployment image
- [ ] Watch rolling update progress
- [ ] Understand update strategies

**Commands:**
```bash
kubectl set image deployment/myapp myapp=yourusername/myapp:v2
kubectl rollout status deployment/myapp
kubectl rollout history deployment/myapp
```

### Step 10.3: Rollbacks
- [ ] Perform a rollback
- [ ] Understand revision history
- [ ] Best practices for safe rollouts

**Commands:**
```bash
kubectl rollout undo deployment/myapp
kubectl rollout undo deployment/myapp --to-revision=2
```

### Step 10.4: Logging
- [ ] Access pod logs
- [ ] Understand log aggregation concepts
- [ ] (Optional) Set up centralized logging

### Step 10.5: Monitoring
- [ ] Understand metrics collection
- [ ] (Optional) Deploy Prometheus + Grafana
- [ ] Create basic dashboards

---

## Module 11: CI/CD Pipeline

### Step 11.1: Pipeline Design
- [ ] Understand CI vs CD
- [ ] Design your pipeline stages
- [ ] Choose a CI/CD tool (GitHub Actions)

**Pipeline stages:**
```
Code Push → Build → Test → Image Build → Push → Deploy
```

### Step 11.2: Continuous Integration
- [ ] Create GitHub Actions workflow
- [ ] Configure Java build and test
- [ ] Add Docker build step

### Step 11.3: Continuous Deployment
- [ ] Add image push to registry
- [ ] Configure kubectl in CI
- [ ] Deploy to Kubernetes

### Step 11.4: GitOps Introduction
- [ ] Understand GitOps principles
- [ ] (Optional) Set up ArgoCD
- [ ] Declarative deployments from Git

---

## Module 12: Production Considerations

### Step 12.1: Security
- [ ] Run containers as non-root (already done in Dockerfile)
- [ ] Use security contexts
- [ ] Implement network policies
- [ ] Scan images for vulnerabilities

### Step 12.2: Resource Management
- [ ] Set appropriate resource requests/limits
- [ ] Understand QoS classes
- [ ] Configure pod disruption budgets

### Step 12.3: High Availability
- [ ] Deploy across availability zones
- [ ] Configure pod anti-affinity
- [ ] Database replication strategies

### Step 12.4: Backup & Disaster Recovery
- [ ] Database backup strategies
- [ ] Velero for cluster backup
- [ ] Disaster recovery planning

### Step 12.5: Database in Production
- [ ] Consider managed databases (RDS, Cloud SQL)
- [ ] Database operators (CloudNativePG, Zalando)
- [ ] Connection pooling (PgBouncer)

---

## Troubleshooting Guide

Common issues encountered during this course and their solutions:

### Maven Issues
| Problem | Solution |
|---------|----------|
| Corporate mirror returning invalid content | Add explicit Maven Central repository to `pom.xml` |
| Corrupted local cache | Delete `~/.m2/repository` and rebuild |
| Missing dependency version | Check if managed by parent POM or add explicit version |

### PostgreSQL Issues
| Problem | Solution |
|---------|----------|
| Role "postgres" does not exist | Use your macOS username or create the postgres role |
| Cannot SET ROLE | Connect as superuser to create databases |
| Port 5432 already in use | Use different port mapping (e.g., 5433:5432) |

### Docker Issues
| Problem | Solution |
|---------|----------|
| Image not found when tagging | Check actual image name with `docker images` |
| Container can't reach host DB | Use `host.docker.internal` instead of `localhost` |
| Build cache not working | Ensure `COPY pom.xml` comes before `COPY src/` |

### Environment Variable Issues
| Problem | Solution |
|---------|----------|
| App not picking up env vars | Use `export` or `set -a` before sourcing `.env` |
| Variables work in shell but not in Java | Child processes need exported variables |

---

## Quick Reference

### File Structure
```
task-service/
├── src/                          # Java source code
│   └── main/
│       ├── java/                 # Application code
│       └── resources/
│           ├── application.yaml  # Spring configuration
│           └── db/migration/     # Flyway migrations
├── pom.xml                       # Maven build file
├── Dockerfile                    # Container definition
├── .dockerignore                 # Files to exclude from build
├── docker-compose.yaml           # Local development
├── .env                          # Environment variables (git-ignored)
├── .gitignore                    # Git exclusions
├── k8s/                          # (To be created)
│   ├── namespace.yaml
│   ├── configmap.yaml
│   ├── secrets.yaml
│   ├── app-deployment.yaml
│   ├── app-service.yaml
│   ├── postgres-statefulset.yaml
│   ├── postgres-service.yaml
│   └── ingress.yaml
└── .github/workflows/            # (To be created)
    └── deploy.yaml               # CI/CD pipeline
```

### Progress Tracker

| Module | Status | Notes |
|--------|--------|-------|
| Module 1: Java Service | ✅ Completed | With DTOs, Swagger, Git setup |
| Module 2: Docker Fundamentals | ✅ Completed | Multi-stage builds, layer caching |
| Module 3: Docker Compose | ✅ Completed | Service discovery, healthchecks |
| Module 4: Container Registry | ✅ Completed | Pushed to Docker Hub |
| Module 5: Kubernetes Concepts | ⬜ Not Started | Next up! |
| Module 6: Local K8s Setup | ⬜ Not Started | |
| Module 7: Deploy Stateless | ⬜ Not Started | |
| Module 8: Deploy Stateful | ⬜ Not Started | |
| Module 9: K8s Networking | ⬜ Not Started | |
| Module 10: Operations | ⬜ Not Started | |
| Module 11: CI/CD | ⬜ Not Started | |
| Module 12: Production | ⬜ Not Started | |

---

## Docker Hub Repository

**Image:** `husamhindustani/task-service:1.0.0`

---

## Next Steps

Ready for **Module 5: Kubernetes Concepts** where you'll learn:
- Kubernetes architecture (control plane, worker nodes)
- Core resources: Pods, Deployments, Services
- ConfigMaps and Secrets

Type "start module 5" to continue!
