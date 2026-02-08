#!/bin/bash
# Delete the GKE Autopilot cluster (stops billing for cluster resources)
# Usage: ./scripts/delete-gke-cluster.sh
#
# Optional env vars (must match setup-gke-cluster.sh):
#   GKE_PROJECT=ci-cd-gke-learn
#   GKE_REGION=us-central1
#   GKE_CLUSTER_NAME=task-cluster

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$REPO_ROOT"

GKE_PROJECT="${GKE_PROJECT:-ci-cd-gke-learn}"
GKE_REGION="${GKE_REGION:-us-central1}"
GKE_CLUSTER_NAME="${GKE_CLUSTER_NAME:-task-cluster}"

echo "=== Deleting GKE cluster ==="
echo "  Project: $GKE_PROJECT"
echo "  Region:  $GKE_REGION"
echo "  Cluster: $GKE_CLUSTER_NAME"
echo ""

if ! gcloud container clusters describe "$GKE_CLUSTER_NAME" --region="$GKE_REGION" --project="$GKE_PROJECT" &>/dev/null; then
  echo "Cluster $GKE_CLUSTER_NAME not found. Nothing to delete."
  exit 0
fi

echo "This will delete the cluster and all workloads. External IP will be released."
read -p "Continue? (y/N) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
  echo "Aborted."
  exit 1
fi

gcloud container clusters delete "$GKE_CLUSTER_NAME" \
  --region="$GKE_REGION" \
  --project="$GKE_PROJECT" \
  --quiet

echo ""
echo "Cluster deleted. To recreate and deploy: ./scripts/setup-gke-cluster.sh"
echo "To recreate (replace existing):         ./scripts/setup-gke-cluster.sh --delete-first"
