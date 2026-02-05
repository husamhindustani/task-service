# Docker & Kubernetes Learning Course

A hands-on course to learn containerization and orchestration by building and deploying a Java service with PostgreSQL.

---

## Course Overview

**What you'll build:** A REST API service in Java (Spring Boot) with PostgreSQL database

**What you'll learn:**
- Docker: Containerizing applications
- Docker Compose: Multi-container local development
- Kubernetes: Orchestrating containers at scale
- CI/CD: Automating the pipeline
- Production patterns: Health checks, scaling, secrets, releases

**Prerequisites:**
- Java basics (we'll use Spring Boot)
- Basic command line knowledge
- Docker Desktop installed
- A code editor

---

## Module 1: The Java Service

### Step 1.1: Project Setup
- [ ] Create Spring Boot project with required dependencies
- [ ] Understand Maven/Gradle project structure
- [ ] Configure application properties

**Key concepts:**
- Spring Boot auto-configuration
- Maven dependency management
- Application profiles (dev, prod)

### Step 1.2: Build the REST API
- [ ] Create a simple domain model (e.g., Task/Todo)
- [ ] Implement JPA entity and repository
- [ ] Create REST controller with CRUD operations
- [ ] Add health check endpoint

**Endpoints to build:**
```
GET    /api/tasks          - List all tasks
POST   /api/tasks          - Create a task
GET    /api/tasks/{id}     - Get a task
PUT    /api/tasks/{id}     - Update a task
DELETE /api/tasks/{id}     - Delete a task
GET    /actuator/health    - Health check
```

### Step 1.3: Database Configuration
- [ ] Configure Spring Data JPA for PostgreSQL
- [ ] Understand connection pooling (HikariCP)
- [ ] Set up Flyway/Liquibase for database migrations

**Key concepts:**
- DataSource configuration
- JPA/Hibernate basics
- Database migrations

### Step 1.4: Test the Application
- [ ] Run PostgreSQL locally (or use H2 for now)
- [ ] Start the application with `mvn spring-boot:run`
- [ ] Test endpoints with curl or Postman

---

## Module 2: Docker Fundamentals

### Step 2.1: Understanding Containers
- [ ] Learn what containers are vs VMs
- [ ] Understand Docker architecture (daemon, client, registry)
- [ ] Learn about images, containers, layers

**Key concepts:**
- Container isolation (namespaces, cgroups)
- Image layers and caching
- Container lifecycle

### Step 2.2: Writing a Dockerfile
- [ ] Create a basic Dockerfile for the Java app
- [ ] Understand Dockerfile instructions (FROM, COPY, RUN, CMD, ENTRYPOINT)
- [ ] Learn about base image selection

**Dockerfile instructions to learn:**
```dockerfile
FROM      - Base image
WORKDIR   - Set working directory
COPY      - Copy files into image
RUN       - Execute commands during build
ENV       - Set environment variables
EXPOSE    - Document ports
CMD       - Default command to run
ENTRYPOINT - Main executable
```

### Step 2.3: Multi-Stage Builds
- [ ] Understand why multi-stage builds matter
- [ ] Create a multi-stage Dockerfile (build + runtime)
- [ ] Compare image sizes

**Key concepts:**
- Build-time vs runtime dependencies
- Image size optimization
- Security (smaller attack surface)

### Step 2.4: Building and Running Containers
- [ ] Build the Docker image: `docker build`
- [ ] Run the container: `docker run`
- [ ] Understand port mapping, volumes, environment variables
- [ ] Learn container debugging: `docker logs`, `docker exec`

**Essential commands:**
```bash
docker build -t myapp:v1 .
docker run -p 8080:8080 myapp:v1
docker ps
docker logs <container-id>
docker exec -it <container-id> /bin/sh
docker stop <container-id>
docker rm <container-id>
```

### Step 2.5: Docker Networking Basics
- [ ] Understand Docker networks
- [ ] Learn how containers communicate
- [ ] Create custom networks

---

## Module 3: Docker Compose

### Step 3.1: Introduction to Compose
- [ ] Understand what Docker Compose solves
- [ ] Learn YAML syntax basics
- [ ] Create your first docker-compose.yaml

**Key concepts:**
- Service definitions
- Declarative infrastructure
- Development vs production configurations

### Step 3.2: Multi-Container Setup
- [ ] Define the Java service in Compose
- [ ] Add PostgreSQL as a service
- [ ] Configure service dependencies (`depends_on`)

### Step 3.3: Volumes and Persistence
- [ ] Understand volume types (named, bind mounts)
- [ ] Configure PostgreSQL data persistence
- [ ] Mount configuration files

**Key concepts:**
- Data persistence
- Volume lifecycle
- Bind mounts for development

### Step 3.4: Environment Configuration
- [ ] Use environment variables in Compose
- [ ] Create `.env` files
- [ ] Understand variable substitution

### Step 3.5: Running the Full Stack
- [ ] Start everything: `docker-compose up`
- [ ] Test the application with database
- [ ] Learn compose commands: `up`, `down`, `logs`, `ps`

**Essential commands:**
```bash
docker-compose up -d
docker-compose logs -f app
docker-compose ps
docker-compose down
docker-compose down -v  # Also remove volumes
```

---

## Module 4: Kubernetes Concepts

### Step 4.1: Kubernetes Architecture
- [ ] Understand the control plane (API server, etcd, scheduler, controller manager)
- [ ] Understand worker nodes (kubelet, kube-proxy, container runtime)
- [ ] Learn about the declarative model

**Key concepts:**
- Desired state vs current state
- Reconciliation loops
- API-driven architecture

### Step 4.2: Core Resources - Pods
- [ ] Understand what a Pod is
- [ ] Learn Pod lifecycle
- [ ] Create a Pod manifest (YAML)
- [ ] Run and inspect a Pod

**Key concepts:**
- Pods as the atomic unit
- Container co-location
- Pod networking (shared localhost)

### Step 4.3: Core Resources - Deployments
- [ ] Understand Deployments and ReplicaSets
- [ ] Create a Deployment manifest
- [ ] Learn about replicas and scaling
- [ ] Understand rolling updates

**Key concepts:**
- Declarative updates
- Rollback capability
- Self-healing

### Step 4.4: Core Resources - Services
- [ ] Understand Service types (ClusterIP, NodePort, LoadBalancer)
- [ ] Create a Service manifest
- [ ] Learn about service discovery (DNS)
- [ ] Understand label selectors

**Key concepts:**
- Stable network endpoints
- Load balancing
- Service discovery

### Step 4.5: Configuration - ConfigMaps & Secrets
- [ ] Understand externalized configuration
- [ ] Create ConfigMaps for app config
- [ ] Create Secrets for sensitive data
- [ ] Mount as environment variables or files

**Key concepts:**
- Separation of config from code
- Secret management
- Configuration updates

---

## Module 5: Local Kubernetes Setup

### Step 5.1: Choose Your Local Cluster
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

### Step 5.2: kubectl Essentials
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

### Step 5.3: Namespaces
- [ ] Understand namespace isolation
- [ ] Create a namespace for your project
- [ ] Set default namespace context

---

## Module 6: Deploying the Java Service (Stateless)

### Step 6.1: Push Image to Registry
- [ ] Tag your Docker image
- [ ] Push to Docker Hub (or local registry)
- [ ] Understand image pull policies

**Commands:**
```bash
docker tag myapp:v1 yourusername/myapp:v1
docker push yourusername/myapp:v1
```

### Step 6.2: Create Deployment Manifest
- [ ] Write the Deployment YAML
- [ ] Configure replicas
- [ ] Set resource requests and limits
- [ ] Apply and verify

### Step 6.3: Create Service Manifest
- [ ] Write the Service YAML
- [ ] Choose appropriate Service type
- [ ] Apply and verify connectivity

### Step 6.4: Add Health Probes
- [ ] Understand liveness vs readiness probes
- [ ] Configure HTTP probes for Spring Boot Actuator
- [ ] Test probe behavior

**Key concepts:**
- Liveness: Is the app alive? (restart if not)
- Readiness: Can it handle traffic? (remove from load balancer if not)
- Startup probes: For slow-starting apps

### Step 6.5: External Configuration
- [ ] Create ConfigMap for application properties
- [ ] Create Secret for database credentials
- [ ] Mount as environment variables
- [ ] Verify configuration is applied

---

## Module 7: Deploying PostgreSQL (Stateful)

### Step 7.1: Understanding StatefulSets
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

### Step 7.2: Persistent Volumes
- [ ] Understand PersistentVolume (PV) and PersistentVolumeClaim (PVC)
- [ ] Learn about storage classes
- [ ] Create a PVC for PostgreSQL

**Key concepts:**
- Storage provisioning (static vs dynamic)
- Access modes (ReadWriteOnce, ReadWriteMany)
- Reclaim policies

### Step 7.3: Create StatefulSet Manifest
- [ ] Write StatefulSet YAML for PostgreSQL
- [ ] Configure volumeClaimTemplates
- [ ] Apply and verify

### Step 7.4: Headless Service
- [ ] Understand headless Services for StatefulSets
- [ ] Create headless Service manifest
- [ ] Understand DNS records created

### Step 7.5: Connect App to Database
- [ ] Update ConfigMap with database URL
- [ ] Verify connectivity
- [ ] Test data persistence (delete pod, verify data remains)

---

## Module 8: Kubernetes Networking

### Step 8.1: Pod Networking
- [ ] Understand Pod IP addresses
- [ ] Learn about CNI plugins
- [ ] Test pod-to-pod communication

### Step 8.2: Service Discovery
- [ ] Understand Kubernetes DNS
- [ ] Learn service FQDN format
- [ ] Test DNS resolution from pods

**DNS format:**
```
<service-name>.<namespace>.svc.cluster.local
```

### Step 8.3: Ingress
- [ ] Understand Ingress resource
- [ ] Install an Ingress controller (nginx)
- [ ] Create Ingress manifest
- [ ] Configure host-based routing

**Key concepts:**
- Layer 7 load balancing
- TLS termination
- Path-based routing

---

## Module 9: Operations & Observability

### Step 9.1: Scaling
- [ ] Scale deployment manually: `kubectl scale`
- [ ] Understand Horizontal Pod Autoscaler (HPA)
- [ ] Configure HPA based on CPU/memory

**Commands:**
```bash
kubectl scale deployment myapp --replicas=5
kubectl autoscale deployment myapp --min=2 --max=10 --cpu-percent=80
```

### Step 9.2: Rolling Updates
- [ ] Update deployment image
- [ ] Watch rolling update progress
- [ ] Understand update strategies

**Commands:**
```bash
kubectl set image deployment/myapp myapp=yourusername/myapp:v2
kubectl rollout status deployment/myapp
kubectl rollout history deployment/myapp
```

### Step 9.3: Rollbacks
- [ ] Perform a rollback
- [ ] Understand revision history
- [ ] Best practices for safe rollouts

**Commands:**
```bash
kubectl rollout undo deployment/myapp
kubectl rollout undo deployment/myapp --to-revision=2
```

### Step 9.4: Logging
- [ ] Access pod logs
- [ ] Understand log aggregation concepts
- [ ] (Optional) Set up centralized logging

### Step 9.5: Monitoring
- [ ] Understand metrics collection
- [ ] (Optional) Deploy Prometheus + Grafana
- [ ] Create basic dashboards

---

## Module 10: CI/CD Pipeline

### Step 10.1: Pipeline Design
- [ ] Understand CI vs CD
- [ ] Design your pipeline stages
- [ ] Choose a CI/CD tool (GitHub Actions)

**Pipeline stages:**
```
Code Push → Build → Test → Image Build → Push → Deploy
```

### Step 10.2: Continuous Integration
- [ ] Create GitHub Actions workflow
- [ ] Configure Java build and test
- [ ] Add Docker build step

### Step 10.3: Continuous Deployment
- [ ] Add image push to registry
- [ ] Configure kubectl in CI
- [ ] Deploy to Kubernetes

### Step 10.4: GitOps Introduction
- [ ] Understand GitOps principles
- [ ] (Optional) Set up ArgoCD
- [ ] Declarative deployments from Git

---

## Module 11: Production Considerations

### Step 11.1: Security
- [ ] Run containers as non-root
- [ ] Use security contexts
- [ ] Implement network policies
- [ ] Scan images for vulnerabilities

### Step 11.2: Resource Management
- [ ] Set appropriate resource requests/limits
- [ ] Understand QoS classes
- [ ] Configure pod disruption budgets

### Step 11.3: High Availability
- [ ] Deploy across availability zones
- [ ] Configure pod anti-affinity
- [ ] Database replication strategies

### Step 11.4: Backup & Disaster Recovery
- [ ] Database backup strategies
- [ ] Velero for cluster backup
- [ ] Disaster recovery planning

### Step 11.5: Database in Production
- [ ] Consider managed databases (RDS, Cloud SQL)
- [ ] Database operators (CloudNativePG, Zalando)
- [ ] Connection pooling (PgBouncer)

---

## Quick Reference

### File Structure
```
my-java-service/
├── src/                          # Java source code
├── pom.xml                       # Maven build file
├── Dockerfile                    # Container definition
├── docker-compose.yaml           # Local development
├── k8s/
│   ├── namespace.yaml
│   ├── configmap.yaml
│   ├── secrets.yaml
│   ├── app-deployment.yaml
│   ├── app-service.yaml
│   ├── postgres-statefulset.yaml
│   ├── postgres-service.yaml
│   └── ingress.yaml
└── .github/workflows/
    └── deploy.yaml               # CI/CD pipeline
```

### Progress Tracker

| Module | Status | Notes |
|--------|--------|-------|
| Module 1: Java Service | ⬜ Not Started | |
| Module 2: Docker Fundamentals | ⬜ Not Started | |
| Module 3: Docker Compose | ⬜ Not Started | |
| Module 4: Kubernetes Concepts | ⬜ Not Started | |
| Module 5: Local K8s Setup | ⬜ Not Started | |
| Module 6: Deploy Stateless | ⬜ Not Started | |
| Module 7: Deploy Stateful | ⬜ Not Started | |
| Module 8: K8s Networking | ⬜ Not Started | |
| Module 9: Operations | ⬜ Not Started | |
| Module 10: CI/CD | ⬜ Not Started | |
| Module 11: Production | ⬜ Not Started | |

---

## Let's Begin!

When you're ready, we'll start with **Module 1: The Java Service**. 

Each step will include:
1. Explanation of concepts
2. Hands-on implementation
3. Verification steps
4. Troubleshooting tips

Type "start module 1" or "let's begin" to start the course!
