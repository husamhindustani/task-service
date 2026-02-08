#!/bin/bash
# Create GKE Autopilot cluster and deploy Task Service (GKE-specific manifests)
# Usage: ./scripts/setup-gke-cluster.sh [--delete-first]
#
# Optional env vars (defaults shown):
#   GKE_PROJECT=ci-cd-gke-learn
#   GKE_REGION=us-central1
#   GKE_CLUSTER_NAME=task-cluster
#
# To delete cluster only: ./scripts/delete-gke-cluster.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$REPO_ROOT"

GKE_PROJECT="${GKE_PROJECT:-ci-cd-gke-learn}"
GKE_REGION="${GKE_REGION:-us-central1}"
GKE_CLUSTER_NAME="${GKE_CLUSTER_NAME:-task-cluster}"

DELETE_FIRST=false
if [[ "${1:-}" == "--delete-first" ]]; then
  DELETE_FIRST=true
fi

echo "=== GKE cluster setup ==="
echo "  Project: $GKE_PROJECT"
echo "  Region:  $GKE_REGION"
echo "  Cluster: $GKE_CLUSTER_NAME"
echo ""

# Check gcloud is available
if ! command -v gcloud &>/dev/null; then
  echo "Error: gcloud CLI not found. Install: brew install google-cloud-sdk"
  exit 1
fi

# Ensure gke-gcloud-auth-plugin for kubectl
if ! gcloud components list --filter="id:gke-gcloud-auth-plugin" --format="value(state.name)" 2>/dev/null | grep -q "Installed"; then
  echo "=== Installing gke-gcloud-auth-plugin (required for kubectl) ==="
  gcloud components install gke-gcloud-auth-plugin --quiet
fi
export USE_GKE_GCLOUD_AUTH_PLUGIN=True

echo "=== Step 1: Set GCP project ==="
gcloud config set project "$GKE_PROJECT"

if [[ "$DELETE_FIRST" == true ]]; then
  echo "=== Step 2a: Delete existing cluster (if any) ==="
  if gcloud container clusters describe "$GKE_CLUSTER_NAME" --region="$GKE_REGION" &>/dev/null; then
    gcloud container clusters delete "$GKE_CLUSTER_NAME" --region="$GKE_REGION" --quiet
    echo "Cluster deleted. Waiting 30s before recreate..."
    sleep 30
  else
    echo "No existing cluster found."
  fi
fi

echo "=== Step 2: Enable required APIs (if not already) ==="
gcloud services enable container.googleapis.com --project="$GKE_PROJECT"

echo "=== Step 3: Create GKE Autopilot cluster ==="
if gcloud container clusters describe "$GKE_CLUSTER_NAME" --region="$GKE_REGION" &>/dev/null; then
  echo "Cluster $GKE_CLUSTER_NAME already exists. Use --delete-first to replace, or skip to deploy."
else
  gcloud container clusters create-auto "$GKE_CLUSTER_NAME" \
    --region="$GKE_REGION" \
    --project="$GKE_PROJECT"
  echo "Cluster created."
fi

echo "=== Step 4: Get cluster credentials ==="
gcloud container clusters get-credentials "$GKE_CLUSTER_NAME" \
  --region="$GKE_REGION" \
  --project="$GKE_PROJECT"

echo "=== Step 5: Apply base manifests (namespace, secret, configmap) ==="
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/configmap.yaml

echo "=== Step 6: Apply GKE PostgreSQL (standard-rwo storage) ==="
kubectl apply -f k8s/gke/postgres-statefulset.yaml

echo "=== Step 7: Wait for PostgreSQL to be ready ==="
kubectl wait --namespace task-service-ns \
  --for=condition=ready pod \
  --selector=app=postgres \
  --timeout=120s

echo "=== Step 8: Apply application deployment, GKE LoadBalancer service, PDB, HPA ==="
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/gke/service.yaml
kubectl apply -f k8s/pdb.yaml
kubectl apply -f k8s/hpa.yaml

echo "=== Step 9: Wait for task-service pods to be ready ==="
kubectl wait --namespace task-service-ns \
  --for=condition=ready pod \
  --selector=app=task-service \
  --timeout=180s

echo "=== Step 10: Set default namespace ==="
kubectl config set-context --current --namespace=task-service-ns

echo ""
echo "=== GKE setup complete! ==="
echo ""
echo "Getting LoadBalancer external IP (may take 1–2 minutes)..."
EXTERNAL_IP=""
for i in {1..24}; do
  EXTERNAL_IP=$(kubectl get svc task-service -n task-service-ns -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || true)
  if [[ -n "$EXTERNAL_IP" ]]; then
    break
  fi
  echo "  Waiting for external IP... ($i/24)"
  sleep 10
done
echo ""
if [[ -n "$EXTERNAL_IP" ]]; then
  echo "Application is available at:"
  echo "  http://$EXTERNAL_IP/actuator/health"
  echo "  http://$EXTERNAL_IP/api/tasks"
  echo "  http://$EXTERNAL_IP/swagger-ui/index.html"
else
  echo "External IP not yet assigned. Check with:"
  echo "  kubectl get svc task-service -n task-service-ns"
fi
echo ""
echo "Useful commands:"
echo "  kubectl get pods"
echo "  kubectl get svc"
echo "  kubectl get pdb"
echo ""
echo "To delete this cluster: ./scripts/delete-gke-cluster.sh"
