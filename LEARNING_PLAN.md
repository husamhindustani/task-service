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

## Module 5: Kubernetes Concepts ✅ COMPLETED

### Step 5.1: Kubernetes Architecture ✅
- [x] Understand the control plane (API server, etcd, scheduler, controller manager)
- [x] Understand worker nodes (kubelet, kube-proxy, container runtime)
- [x] Learn about the declarative model

**Key concepts covered:**
- **Control Plane**: API Server (gateway), etcd (state store), Scheduler (pod placement), Controller Manager (reconciliation)
- **Worker Nodes**: kubelet (pod management), kube-proxy (networking), container runtime (containerd)
- Desired state vs current state - Kubernetes continuously reconciles
- Docker Engine vs containerd distinction

### Step 5.2: Core Resources - Pods ✅
- [x] Understand what a Pod is
- [x] Learn Pod lifecycle
- [x] Understand multi-container pods (sidecars)

**Key concepts covered:**
- Pods as smallest deployable unit
- Container co-location and shared network namespace
- Why not create Pods directly (use Deployments)

### Step 5.3: Core Resources - Deployments ✅
- [x] Understand Deployments and ReplicaSets
- [x] Create a Deployment manifest
- [x] Learn about replicas and scaling
- [x] Understand rolling updates

**Key concepts covered:**
- Deployments manage ReplicaSets manage Pods
- Self-healing - failed pods are replaced
- Declarative updates vs imperative

### Step 5.4: Core Resources - Services ✅
- [x] Understand Service types (ClusterIP, NodePort, LoadBalancer)
- [x] Create a Service manifest
- [x] Learn about service discovery (DNS)
- [x] Understand label selectors

**Service types understood:**
| Type | Use Case |
|------|----------|
| ClusterIP | Internal communication |
| NodePort | Development/testing external access |
| LoadBalancer | Production external access (cloud) |

### Step 5.5: Configuration - ConfigMaps & Secrets ✅
- [x] Understand externalized configuration
- [x] Create ConfigMaps for app config
- [x] Create Secrets for sensitive data
- [x] Mount as environment variables

**Key insight:**
- Secrets are base64 encoded, NOT encrypted by default
- Enable "encryption at rest" in etcd for true security

---

## Module 6: Local Kubernetes Setup ✅ COMPLETED

### Step 6.1: Choose Your Local Cluster ✅
- [x] Chose Kind (Kubernetes in Docker) for multi-node support
- [x] Configured 3-node cluster (1 control-plane, 2 workers)
- [x] Enabled containerd image store

**Kind vs kubeadm:**
| kubeadm | Kind |
|---------|------|
| Real VMs/bare metal | Docker containers as nodes |
| Production bootstrapping | Local dev/testing |
| Heavier | Lightweight |

### Step 6.2: kubectl Essentials ✅
- [x] Learn kubectl syntax: `kubectl <verb> <resource>`
- [x] Master essential commands
- [x] Understand output formats (-o yaml, -o wide)

**Key commands mastered:**
```bash
kubectl get pods -n <namespace> -o wide
kubectl describe pod <name>
kubectl logs <pod-name>
kubectl exec -it <pod-name> -- /bin/sh
kubectl apply -f <file.yaml>
kubectl delete -f <file.yaml>
kubectl wait --for=condition=ready
kubectl rollout restart deployment/<name>
kubectl set image deployment/<name> <container>=<image>
```

### Step 6.3: Namespaces ✅
- [x] Understand namespace isolation
- [x] Created `task-service-ns` namespace
- [x] Created namespace manifest for reproducibility

**Key concepts covered:**
- kubectl context and kubeconfig (~/.kube/config)
- Taints and tolerations (NoSchedule on control-plane)

---

## Module 7: Deploying the Java Service (Stateless) ✅ COMPLETED

### Step 7.1: Create Deployment Manifest ✅
- [x] Write the Deployment YAML with 2 replicas
- [x] Set resource requests and limits
- [x] Reference image from Docker Hub
- [x] Apply and verify

### Step 7.2: Create Service Manifest ✅
- [x] Write NodePort Service YAML (port 30080)
- [x] Verify connectivity via curl

### Step 7.3: Add Health Probes ✅
- [x] Configured liveness, readiness, and startup probes
- [x] Probes target Spring Boot Actuator endpoints

**Probe behavior understood:**
| Probe | Fails → |
|-------|---------|
| startupProbe | Container killed |
| readinessProbe | Removed from Service endpoints |
| livenessProbe | Container restarted |

### Step 7.4: External Configuration ✅
- [x] Created ConfigMap for application properties
- [x] Created Secret for database credentials
- [x] Mounted as environment variables via envFrom

**Kind-specific learning:**
- NodePort not accessible on localhost without port mapping
- Added `extraPortMappings` to kind-config.yaml
- Alternative: use `kubectl port-forward`

---

## Module 8: Deploying PostgreSQL (Stateful) ✅ COMPLETED

### Step 8.1: Understanding StatefulSets ✅
- [x] Learn differences from Deployments
- [x] Understand stable network identity (postgres-0, postgres-1)
- [x] Understand stable storage (per-pod PVC)

**Demonstrated data loss with emptyDir** - then fixed with PersistentVolumes

### Step 8.2: Persistent Volumes ✅
- [x] Understand PV, PVC, and StorageClass relationship
- [x] Dynamic provisioning via StorageClass
- [x] Kind uses `local-path` provisioner (data lost on node failure)

**PV/PVC analogy:**
- PV = Physical apartment
- PVC = Lease request
- StorageClass = Apartment building type

### Step 8.3: Create StatefulSet Manifest ✅
- [x] Wrote StatefulSet YAML for PostgreSQL
- [x] Configured volumeClaimTemplates (auto-creates PVC per pod)
- [x] Applied and verified data persistence

### Step 8.4: Headless Service ✅
- [x] Created headless Service (clusterIP: None)
- [x] Understand DNS records: `postgres-0.postgres-headless.task-service-ns.svc.cluster.local`

**Why headless:** Enables direct pod addressing for primary/replica scenarios

### Step 8.5: Connect App to Database ✅
- [x] Verified connectivity via ConfigMap
- [x] Tested data persistence (delete pod → data survives)

**Production considerations discussed:**
- Local storage = node failure loses data
- Use network-attached storage (EBS, GCP PD) or managed databases (RDS)

---

## Module 9: Kubernetes Networking ✅ COMPLETED

### Step 9.1: Pod Networking ✅
- [x] Understand Pod IP addresses
- [x] Learn no-NAT guarantee between pods
- [x] CNI plugins (kindnet in Kind)

**Key insight:** Service ClusterIP ≠ Pod IP
- Service IP is stable, load-balanced
- Pod IP changes on restart

### Step 9.2: Service Discovery ✅
- [x] Deep dive into Kubernetes DNS (CoreDNS)
- [x] Explored /etc/resolv.conf in pods
- [x] Understand search domains and ndots:5

**DNS resolution flow:**
```
postgres → postgres.task-service-ns.svc.cluster.local → ClusterIP → Pod IP
```

### Step 9.3: Ingress ✅
- [x] Installed nginx Ingress Controller
- [x] Created Ingress manifest with host-based routing
- [x] Fixed controller scheduling on control-plane node
- [x] Created permanent fix with patch file

**Files created:**
- `k8s/ingress.yaml` - Ingress resource
- `k8s/ingress-nginx-patch.yaml` - Fix for Kind
- `scripts/setup-cluster.sh` - Full automation script

**Key concepts covered:**
- HTTP Host header for routing
- Layer 7 vs Layer 4 load balancing
- nodeSelector and tolerations

### Bonus: CORS and Swagger Fix ✅
- [x] Added CorsConfig.java for browser requests
- [x] Added OpenApiConfig.java with multiple server options
- [x] Fixed Swagger UI to work via Ingress

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
│       ├── java/
│       │   └── com/example/taskservice/
│       │       ├── config/
│       │       │   ├── CorsConfig.java      # CORS configuration
│       │       │   └── OpenApiConfig.java   # Swagger server config
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── repository/
│       │       └── TaskServiceApplication.java
│       └── resources/
│           ├── application.yaml  # Spring configuration
│           └── db/migration/     # Flyway migrations
├── pom.xml                       # Maven build file
├── Dockerfile                    # Multi-stage container build
├── .dockerignore                 # Files to exclude from build
├── docker-compose.yaml           # Local development
├── kind-config.yaml              # Kind cluster configuration
├── k8s/                          # Kubernetes manifests
│   ├── namespace.yaml
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── deployment.yaml           # Task service deployment
│   ├── service.yaml              # NodePort service
│   ├── postgres-statefulset.yaml # PostgreSQL with PVC
│   ├── ingress.yaml              # Ingress routing
│   └── ingress-nginx-patch.yaml  # Kind-specific fix
├── scripts/
│   └── setup-cluster.sh          # Cluster setup automation
└── .github/workflows/            # (To be created in Module 11)
    └── deploy.yaml               # CI/CD pipeline
```

### Progress Tracker

| Module | Status | Notes |
|--------|--------|-------|
| Module 1: Java Service | ✅ Completed | With DTOs, Swagger, Git setup |
| Module 2: Docker Fundamentals | ✅ Completed | Multi-stage builds, layer caching |
| Module 3: Docker Compose | ✅ Completed | Service discovery, healthchecks |
| Module 4: Container Registry | ✅ Completed | Pushed to Docker Hub |
| Module 5: Kubernetes Concepts | ✅ Completed | Architecture, Pods, Deployments, Services, ConfigMaps, Secrets |
| Module 6: Local K8s Setup | ✅ Completed | Kind with 3 nodes, kubectl, namespaces |
| Module 7: Deploy Stateless | ✅ Completed | Deployment, Service, Probes, ConfigMap/Secret |
| Module 8: Deploy Stateful | ✅ Completed | StatefulSet, PV/PVC, Headless Service |
| Module 9: K8s Networking | ✅ Completed | DNS, Ingress, CORS fix |
| Module 10: Operations | ⬜ Not Started | Next up! |
| Module 11: CI/CD | ⬜ Not Started | |
| Module 12: Production | ⬜ Not Started | |

---

## Docker Hub Repository

**Image:** `husamhindustani/task-service:1.0.2`

**Versions:**
| Version | Changes |
|---------|---------|
| 1.0.0 | Initial release |
| 1.0.1 | Added CORS support |
| 1.0.2 | Fixed OpenAPI server configuration for Ingress |

---

## Files Created

### Kubernetes Manifests (k8s/)
| File | Purpose |
|------|---------|
| namespace.yaml | Namespace definition |
| configmap.yaml | Application configuration |
| secret.yaml | Database credentials |
| deployment.yaml | Task service deployment |
| service.yaml | NodePort service |
| postgres-statefulset.yaml | PostgreSQL with persistent storage |
| ingress.yaml | Ingress routing rules |
| ingress-nginx-patch.yaml | Fix for Kind scheduling |

### Scripts (scripts/)
| File | Purpose |
|------|---------|
| setup-cluster.sh | Full cluster setup automation |

### Configuration
| File | Purpose |
|------|---------|
| kind-config.yaml | Kind cluster with port mappings |

---

## Next Steps

Ready for **Module 10: Operations & Observability** where you'll learn:
- Manual and auto-scaling (HPA)
- Rolling updates and rollbacks
- Logging and monitoring basics

Type "start module 10" to continue!
