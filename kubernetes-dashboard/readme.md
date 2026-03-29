# Setting up k8s dashboard
Instructions to set up the Kubernetes Dashboard on a local cluster (e.g., Minikube, Kind, docker-desktop, etc.):
Read [How to Set Up Kubernetes Dashboard Securely](https://oneuptime.com/blog/post/2026-01-25-kubernetes-dashboard-secure-setup/view)
# Install the Kubernetes Dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml
# Create a Service Account
kubectl create serviceaccount dashboard-admin-sa
kubectl create clusterrolebinding dashboard-admin-sa --clusterrole=cluster-admin --serviceaccount=default:dashboard-admin-sa
# Get the Bearer Token
kubectl create token dashboard-admin-sa
kubectl create secret generic dashboard-admin-sa-token --type=kubernetes.io/service-account-token --from-literal=token=$(kubectl create token dashboard-admin-sa)
# Access the Dashboard
kubectl proxy
# Open the following URL in your web browser:
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#!/login
# Declaratively create secret with the token
```
apiVersion: v1
kind: Secret
metadata:
  name: dashboard-admin-sa-token
  annotations:
    kubernetes.io/service-account.name: dashboard-admin-sa
type: kubernetes.io/service-account-token
```
# Apply the secret
kubectl apply -f dashboard-admin-sa-token.yaml
# Get the token from the secret
kubectl get secret dashboard-admin-sa-token -o jsonpath="{.data.token}" | base64 --decode
# logs for the dashboard
kubectl logs -n kubernetes-dashboard -l k8s-app=kubernetes-dashboard -- tail=100
kubectl logs -n kubernetes-dashboard deployment/kubernetes-dashboard
# Disable login ui
kubectl patch deployment kubernetes-dashboard -n kubernetes-dashboard --patch-file patch.yaml
# restart
kubectl rollout restart deployment kubernetes-dashboard -n kubernetes-dashboard
