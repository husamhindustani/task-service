#!/bin/bash
# Setup script for Kind cluster with Ingress and HPA support
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

echo "=== Step 5: Install Metrics Server (for HPA) ==="
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
# Patch for Kind (insecure TLS required)
kubectl patch deployment metrics-server -n kube-system --type='json' \
  -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--kubelet-insecure-tls"}]'

echo "=== Step 6: Wait for Ingress Controller to be ready ==="
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=120s

echo "=== Step 7: Apply application manifests ==="
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/postgres-statefulset.yaml

echo "=== Step 8: Wait for PostgreSQL to be ready ==="
kubectl wait --namespace task-service-ns \
  --for=condition=ready pod \
  --selector=app=postgres \
  --timeout=120s

echo "=== Step 9: Apply application deployment, service, ingress, and HPA ==="
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/ingress.yaml
kubectl apply -f k8s/hpa.yaml

echo "=== Step 10: Wait for task-service to be ready ==="
kubectl wait --namespace task-service-ns \
  --for=condition=ready pod \
  --selector=app=task-service \
  --timeout=120s

echo "=== Step 11: Set default namespace ==="
kubectl config set-context --current --namespace=task-service-ns

echo ""
echo "=== Setup Complete! ==="
echo ""
echo "Access your application:"
echo "  - Via Ingress:  curl http://localhost/actuator/health"
echo "  - Via NodePort: curl http://localhost:30080/actuator/health"
echo "  - Swagger UI:   http://localhost/swagger-ui.html"
echo ""
echo "Useful commands:"
echo "  kubectl get pods              # List pods (namespace already set)"
echo "  kubectl get hpa               # Check autoscaler status"
echo "  kubectl top pods              # View resource usage"
echo "  kubectl logs -l app=task-service -f  # Follow logs"
