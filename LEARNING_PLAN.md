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

## Module 10: Operations & Observability ✅ COMPLETED

### Step 10.1: Scaling ✅
- [x] Scale deployment manually with `kubectl scale`
- [x] Understand imperative vs declarative scaling
- [x] Configure Horizontal Pod Autoscaler (HPA)

**Key concepts covered:**
- Imperative scaling: quick, one-time, not tracked in Git
- Declarative scaling: edit manifest, reproducible, GitOps-friendly
- HPA scales based on CPU/memory utilization

**Commands mastered:**
```bash
kubectl scale deployment/task-service --replicas=4
kubectl autoscale deployment task-service --cpu-percent=50 --min=2 --max=5
kubectl get hpa
kubectl top pods
```

### Step 10.2: Rolling Updates ✅
- [x] Update deployment image with `kubectl set image`
- [x] Watch rolling update progress
- [x] Understand maxSurge and maxUnavailable

**Key concepts covered:**
- Rolling updates replace pods gradually (no downtime)
- ReadinessProbe must pass before old pod is terminated
- Default: 25% maxSurge, 25% maxUnavailable

**Commands mastered:**
```bash
kubectl set image deployment/task-service task-service=image:v1.0.2
kubectl rollout status deployment/task-service
kubectl rollout history deployment/task-service
```

### Step 10.3: Rollbacks ✅
- [x] Perform rollback with `kubectl rollout undo`
- [x] Understand revision history
- [x] Record change cause with annotations

**Commands mastered:**
```bash
kubectl rollout undo deployment/task-service
kubectl rollout undo deployment/task-service --to-revision=2
kubectl annotate deployment/task-service kubernetes.io/change-cause="reason"
```

### Step 10.4: Logging ✅
- [x] Access pod logs with various options
- [x] View logs from multiple pods using label selector
- [x] Understand `--previous` flag for crashed containers

**Commands mastered:**
```bash
kubectl logs deployment/task-service
kubectl logs -l app=task-service
kubectl logs -l app=task-service -f --tail=50
kubectl logs deployment/task-service --previous
```

### Step 10.5: HPA & Metrics ✅
- [x] Installed metrics-server for Kind
- [x] Created HPA manifest with scaling behavior
- [x] Understand CPU utilization targets

**Files created:**
- `k8s/hpa.yaml` - HPA with autoscaling/v2 API
- Updated `scripts/setup-cluster.sh` with metrics-server

---

## Module 11: CI/CD Pipeline ✅ COMPLETED

### Step 11.1: Pipeline Design ✅
- [x] Understand CI vs CD
- [x] Design pipeline stages
- [x] Choose GitHub Actions

**Key concepts covered:**
- CI = Build, test, create artifacts on every push
- CD = Deploy artifacts to environments
- Pipeline: Code Push → Build → Test → Docker Build → Push to Registry

### Step 11.2: Continuous Integration ✅
- [x] Created GitHub Actions workflow
- [x] Configured Java build with Maven
- [x] Added Docker build and push step
- [x] Set up GitHub secrets for Docker Hub

**Files created:**
- `.github/workflows/ci-cd.yaml` - Complete CI pipeline

**Key concepts covered:**
- GitHub Actions runs on cloud VMs (ubuntu-latest)
- Jobs run in isolation, artifacts pass files between jobs
- Secrets store sensitive credentials securely
- Git SHA tagging for automatic versioning

### Step 11.3: Continuous Deployment ⏸️ NEXT UP
- [ ] Set up GKE (Google Kubernetes Engine) cluster
- [ ] Configure service account for GitHub Actions
- [ ] Add deployment step to workflow
- [ ] Test end-to-end: push → build → deploy

**Planned approach:**
- Use GKE Autopilot with free credits
- Push-based CD from GitHub Actions

### Step 11.4: GitOps Introduction ✅ (Conceptual)
- [x] Understand Push vs Pull deployment models
- [x] Understand GitOps principles (ArgoCD, Flux)
- [ ] (Optional) Hands-on ArgoCD setup

**Key concepts covered:**
- Push-based: CI/CD pushes to cluster (GitHub Actions)
- Pull-based: Controller in cluster pulls from Git (ArgoCD)
- GitOps benefits: Git as single source of truth, auto-drift correction

---

## Module 11B: Cloud Deployment with GKE ✅ COMPLETED

### Step 11B.1: GKE Setup ✅
- [x] Create Google Cloud account with free credits
- [x] Install gcloud CLI (`brew install google-cloud-sdk`)
- [x] Authenticate (`gcloud auth login --no-launch-browser`)
- [x] Create project `ci-cd-gke-learn`
- [x] Link billing account
- [x] Enable APIs (container, artifactregistry)
- [x] Create GKE Autopilot cluster (`task-cluster` in `us-central1`)

**Key concepts covered:**
- GKE Autopilot vs Standard (node management, pricing)
- gcloud CLI authentication and project management
- kubectl context switching between Kind and GKE

### Step 11B.2: Manual Deployment to GKE ✅
- [x] Created GKE-specific manifests (`k8s/gke/`)
- [x] Fixed StorageClass for GKE (`standard-rwo`)
- [x] Used LoadBalancer service type (creates Google Cloud LB)
- [x] Deployed PostgreSQL and Task Service
- [x] Verified endpoints working via public IP

**Files created:**
- `k8s/gke/service.yaml` - LoadBalancer service
- `k8s/gke/postgres-statefulset.yaml` - GKE storage class

**GKE Endpoint:** `http://34.71.175.100`

### Step 11B.3: Multi-Platform Docker Builds ✅
- [x] Fixed architecture mismatch (arm64 vs amd64)
- [x] Added QEMU for cross-platform builds
- [x] Created `Dockerfile.ci` for CI builds (uses pre-built JAR)
- [x] Updated `.dockerignore` to allow JAR files
- [x] Verified both linux/amd64 and linux/arm64 in manifest

**Files created/modified:**
- `Dockerfile.ci` - CI-specific Dockerfile (no Maven build)
- `.dockerignore` - Allow JAR files through

### Step 11B.4: Automated CD to GKE ✅
- [x] Create GCP service account (`github-actions`)
- [x] Grant `container.developer` role (least privilege)
- [x] Generate service account key and store in GitHub Secrets (`GKE_SA_KEY`)
- [x] Add deploy job to workflow using `google-github-actions/auth` and `setup-gcloud`
- [x] Test end-to-end: push → build → deploy → live in ~5 minutes

**Key concepts covered:**
- Service accounts vs user accounts (machine identity for automation)
- IAM roles and principle of least privilege
- `kubectl set image` for rolling updates via CI/CD
- Full pipeline: git push → build → test → docker → deploy

---

## Module 12: Production Considerations ✅ COMPLETED

### Step 12.1: Security ✅
- [x] Run containers as non-root (Dockerfile + K8s runAsUser: 100)
- [x] Use security contexts (pod + container: runAsNonRoot, seccomp, readOnlyRootFilesystem, capabilities drop)
- [x] Writable /tmp via emptyDir for Java
- [ ] Network policies (optional)
- [ ] Image scanning (optional)

### Step 12.2: Resource Management ✅
- [x] Resource requests/limits (deployment: 256Mi–512Mi, 250m–500m CPU)
- [x] QoS: Burstable (requests ≠ limits)
- [x] Pod Disruption Budget: `k8s/pdb.yaml` (minAvailable: 1)

### Step 12.3: High Availability ✅
- [x] Pod anti-affinity (deployment: prefer spread across zones via `topology.kubernetes.io/zone`)
- [x] Soft preference (`preferredDuringSchedulingIgnoredDuringExecution`) so scheduling still works with 1 node
- [ ] Database replication: conceptual (primary/replica, failover; use managed DB or operators for production)

### Step 12.4: Backup & Disaster Recovery (Conceptual)
- **Database backup:** pg_dump/pg_basebackup, scheduled jobs, store in GCS/S3
- **Velero:** backs up cluster state (PVs, objects) to object storage; restore to new cluster
- **DR planning:** RTO/RPO, runbooks, test restores

### Step 12.5: Database in Production (Conceptual)
- **Managed DB (Cloud SQL, RDS):** no node/disk management, automated backups, HA options
- **Operators:** CloudNativePG, Zalando Postgres – automate backup, failover, scaling in-cluster
- **Connection pooling:** PgBouncer or similar – reduces DB connections, helps under load

---

## Module 13: Helm ⬜ NOT STARTED

Package manager for Kubernetes: charts = templated manifests + values. One chart, many environments; upgrade/rollback built in.

### Step 13.1: Helm basics
- [ ] Install Helm (`brew install helm`)
- [ ] Understand: chart, release, values, template rendering
- [ ] Commands: `helm install`, `helm upgrade`, `helm rollback`, `helm uninstall`, `helm list`

### Step 13.2: Create a chart from existing manifests
- [ ] `helm create task-service` (scaffold) or create chart by hand
- [ ] Move/copy deployment, service, configmap, etc. into `templates/` with Go templating
- [ ] Add `values.yaml` for image tag, replica count, resources, env
- [ ] Install on Kind: `helm install task-service ./charts/task-service -n task-service-ns --create-namespace`
- [ ] Change values and run `helm upgrade`

### Step 13.3: Use a community chart
- [ ] Add repo: `helm repo add bitnami https://charts.bitnami.com/bitnami`
- [ ] Install PostgreSQL: `helm install postgres bitnami/postgresql --set ...`
- [ ] Compare with our hand-written StatefulSet; understand chart values

### Step 13.4: Integrate with CI/CD (optional)
- [ ] In GitHub Actions: install Helm, run `helm upgrade --install` with image tag from build
- [ ] Or keep `kubectl set image` and use Helm only for local/scripted installs

**Key concepts:** Chart structure (`Chart.yaml`, `values.yaml`, `templates/`), `{{ .Values.xxx }}`, release lifecycle, `helm template` for dry-run.

---

## Module 14: Secrets Management ⬜ NOT STARTED

Store secrets outside the cluster (or in a dedicated store) and sync them into Kubernetes. Avoid plain secrets in Git or long-lived static K8s Secrets.

### Step 14.1: Why external secrets
- [ ] Limits of in-cluster Secrets (base64, in etcd, in Git if templated)
- [ ] Production pattern: single source of truth (Vault, GCP Secret Manager, AWS Secrets Manager)
- [ ] Sync model: operator creates/updates K8s Secrets from external store

### Step 14.2: Choose a backend (one path)
- [ ] **GCP Secret Manager:** Create secret in GCP, grant GKE workload access (Workload Identity or SA key)
- [ ] **HashiCorp Vault:** Run Vault (dev mode or server), configure Kubernetes auth
- [ ] **AWS Secrets Manager:** If using EKS; IAM roles for service accounts

### Step 14.3: External Secrets Operator (ESO)
- [ ] Install ESO in cluster: `helm install external-secrets oci://registry-1.docker.io/bitnamicharts/external-secrets`
- [ ] Create a `SecretStore` or `ClusterSecretStore` pointing at your backend (GCP/Vault/AWS)
- [ ] Create `ExternalSecret` resources that reference the store and map external secret keys to a K8s Secret
- [ ] Replace existing `task-service-secrets` with an ExternalSecret; remove static `k8s/secret.yaml` from Git (or keep a placeholder)

### Step 14.4: Integrate with Task Service
- [ ] Store DB credentials in backend; ESO syncs to `task-service-secrets`
- [ ] Verify app still starts and connects; rotate secret in backend and confirm ESO updates

**Key concepts:** SecretStore, ExternalSecret, refresh interval, least-privilege IAM for the operator and backend access.

---

## Module 15: Service Mesh ⬜ NOT STARTED

Service mesh adds security (mTLS), traffic control (retries, splitting, canary), and observability at the network layer between pods. Pick one mesh (e.g. Istio or Linkerd).

### Step 15.1: What a service mesh does
- [ ] Sidecar proxy per pod (or per-node) intercepts pod traffic
- [ ] mTLS between services without app code changes
- [ ] Traffic management: retries, timeouts, canary/percentage splits
- [ ] Observability: golden metrics (latency, traffic, errors) from the mesh

### Step 15.2: Install mesh on Kind (choose one)
- [ ] **Linkerd:** Lighter; `linkerd install | kubectl apply -f -`; inject namespace
- [ ] **Istio:** More features; `istioctl install`; inject namespace or enable sidecar for task-service-ns

### Step 15.3: mTLS and policy
- [ ] Enable strict mTLS (mesh-wide or per namespace)
- [ ] Verify traffic is encrypted (e.g. see certs in proxy or mesh dashboard)
- [ ] Optional: AuthorizationPolicy (Istio) or Server/Client resources (Linkerd) to restrict who can call what

### Step 15.4: Traffic control (optional)
- [ ] VirtualService (Istio) or ServiceProfile (Linkerd): retries, timeouts
- [ ] Canary: route a percentage of traffic to a new version

### Step 15.5: Observability (optional)
- [ ] Use mesh metrics/dashboards for latency and success rate
- [ ] Optional: integrate with Prometheus/Grafana if you add monitoring later

**Key concepts:** Sidecar injection, mTLS, mesh authority/trust domain, traffic split, observability from proxies. Best tried after you’re comfortable with one service and want to prepare for many services.

---

## Other core areas (optional – add if you want)

Topics not yet in the learning plan. Say which (if any) you want added as modules.

| Area | What it is | Why consider it |
|------|------------|------------------|
| **Monitoring & observability** | Prometheus (metrics), Grafana (dashboards), Alertmanager; optionally OpenTelemetry/Jaeger (tracing). | HPA + logs; this adds metrics, graphs, and alerts for load, errors, SLOs. |
| **GitOps (hands-on)** | Argo CD or Flux: deploy from Git, drift correction, audit. | Turn Module 11 GitOps concept into a real workflow. |
| **Kustomize** | Base + overlays (dev/staging/prod) with `kubectl apply -k`. | Multi-environment without Helm; lighter option. |

**Already added as modules:** Helm (13), Secrets Management (14), Service Mesh (15).

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
│   ├── setup-cluster.sh          # Kind cluster setup
│   ├── setup-gke-cluster.sh      # GKE cluster create + deploy
│   └── delete-gke-cluster.sh     # GKE cluster delete
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
| Module 10: Operations | ✅ Completed | Scaling, Rolling Updates, Rollbacks, HPA, Logging |
| Module 11: CI/CD | ✅ Completed | GitHub Actions, Docker Hub push, GitOps concepts |
| Module 11B: GKE CD | ✅ Completed | Full CI/CD to GKE! |
| Module 12: Production | ✅ Completed | Security, PDB, HA anti-affinity, QoS |
| Module 13: Helm | ⬜ Not Started | Charts, values, releases |
| Module 14: Secrets Management | ⬜ Not Started | ESO, Vault/GCP Secret Manager |
| Module 15: Service Mesh | ⬜ Not Started | Istio/Linkerd, mTLS, traffic control |

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
| pdb.yaml | Pod Disruption Budget (minAvailable: 1) |

### Scripts (scripts/)
| File | Purpose |
|------|---------|
| setup-cluster.sh | Kind cluster: create, Ingress, metrics-server, deploy app, PDB |
| setup-gke-cluster.sh | GKE Autopilot: create cluster, deploy app (LoadBalancer, GKE manifests) |
| delete-gke-cluster.sh | Delete GKE cluster (stops billing) |

### CI/CD (.github/workflows/)
| File | Purpose |
|------|---------|
| ci-cd.yaml | GitHub Actions pipeline (build, test, Docker push, GKE deploy) |

### Configuration
| File | Purpose |
|------|---------|
| kind-config.yaml | Kind cluster with port mappings |

---

## Next Steps

**🎉 Core course complete!** Modules 1–12 done. Module 13 (Helm) and optional topics available.

**What you have:**
- Java REST API + PostgreSQL, containerized and orchestrated on Kind and GKE
- Full CI/CD: push to main → build → test → Docker push → deploy to GKE
- Production-style settings: security contexts, PDB, HA anti-affinity, resource limits

**GKE (when cluster is running):**  
- Endpoint: `http://<EXTERNAL_IP>` — get IP with `kubectl get svc task-service -n task-service-ns`

**Next (optional):** Module 13 – Helm; Module 14 – Secrets Management; Module 15 – Service Mesh. See “Other core areas” for Monitoring, GitOps, Kustomize.

**Scripts:**
- Kind: `./scripts/setup-cluster.sh` — create/recreate local cluster
- GKE: `./scripts/setup-gke-cluster.sh` — create cluster and deploy
- GKE: `./scripts/setup-gke-cluster.sh --delete-first` — delete and recreate
- GKE: `./scripts/delete-gke-cluster.sh` — delete cluster (stops billing)

**⚠️ Remember:** Delete the GKE cluster when not in use to avoid charges (use `delete-gke-cluster.sh` or the `gcloud` command above).

**GitHub Secrets configured:** `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`, `GKE_SA_KEY`
