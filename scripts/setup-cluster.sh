#!/bin/bash
# Setup script for Kind cluster with Ingress support
# Usage: ./scripts/setup-cluster.sh

set -e  # Exit on error

CLUSTER_NAME="task-cluster"

echo "=== Step 1: Delete existing cluster (if any) ==="
kind delete cluster --name $CLUSTER_NAME 2>/dev/null || true

echo "=== Step 2: Create Kind cluster ==="
kind create cluster --name $CLUSTER_NAME --config kind-config.yaml

echo "=== Step 3: Install nginx Ingress Controller ==="
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

echo "=== Step 4: Patch Ingress Controller for control-plane scheduling ==="
kubectl patch deployment ingress-nginx-controller -n ingress-nginx --patch-file k8s/ingress-nginx-patch.yaml

echo "=== Step 5: Wait for Ingress Controller to be ready ==="
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=120s

echo "=== Step 6: Apply application manifests ==="
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/postgres-statefulset.yaml

echo "=== Step 7: Wait for PostgreSQL to be ready ==="
kubectl wait --namespace task-service-ns \
  --for=condition=ready pod \
  --selector=app=postgres \
  --timeout=120s

echo "=== Step 8: Apply application deployment and ingress ==="
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/ingress.yaml

echo "=== Step 9: Wait for task-service to be ready ==="
kubectl wait --namespace task-service-ns \
  --for=condition=ready pod \
  --selector=app=task-service \
  --timeout=120s

echo ""
echo "=== Setup Complete! ==="
echo ""
echo "Access your application:"
echo "  - Via Ingress:  curl http://localhost/actuator/health"
echo "  - Via NodePort: curl http://localhost:30080/actuator/health"
echo ""
echo "Check pods: kubectl get pods -n task-service-ns"
