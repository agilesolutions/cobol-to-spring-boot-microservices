# FluxCD - GitOps for Kubernetes
[<img src="./back.png">](../README.md)

This repository contains a sample application and configuration for GitOps using FluxCD. The application is a MicroServices developed in Java with Spring Boot, and the configuration is set up to deploy the application to a Docker for Windows Kubernetes cluster using FluxCD.
## FluxCD in short
FluxCD is a GitOps tool that automates the deployment of applications to Kubernetes clusters. It continuously monitors a Git repository for changes and applies those changes to the cluster, ensuring that the desired state defined in the Git repository is always reflected in the cluster. This approach allows for version control, collaboration, and easy rollback of changes, making it an ideal solution for managing Kubernetes deployments in a GitOps workflow.    
Operating as a "pull-based" agent inside the cluster, it automates deployment by watching for repository changes, making it highly secure, auditable, and reliable for managing infrastructure and application updates.
## Prerequisites
- A Kubernetes cluster (e.g., Docker for Windows as what I did in this example)
- kubectl installed and configured to access your cluster
- FluxCD installed in your Kubernetes cluster
- GitHub repository to store your application and FluxCD configuration
## Setup FluxCD and Deploy the Application
1. How to bootstrap FluxCD in your Kubernetes cluster:
```bash
flux bootstrap github --owner=agilesolutions --repository=cobol-to-microservices --branch=master --path=fluxCD --personal
```

2. How this FluxCD configuration was initially generated for this sample application:
```bash
flux check --pre
git clone https://github.com/agilesolutions/cobol-to-microservices.git
cd cobol-to-microservices
flux create source git cobol-to-microservices --url=https://github.com/agilesolutions/cobol-to-microservices --branch=master --interval=1m --export > ./fluxCD/cobol-to-microservices.yaml
flux create kustomization account-service --target-namespace=services --source=spring-graphsql --path="./account-service/kustomize/overlays/local" --prune=true --wait=true --interval=30m --retry-interval=2m --health-check-timeout=3m --export > ./fluxCD/account-service-kustomization.yaml
flux create kustomization gateway --target-namespace=services --source=spring-graphsql --path="./gateway/kustomize/overlays/local" --prune=true --wait=true --interval=30m --retry-interval=2m --health-check-timeout=3m --export > ./fluxCD/gateway-kustomization.yaml
...
```
3. How to create initial FluxCD configuration to deploy Prometheus and Grafana for monitoring:
```bash
flux create kustomization metrics --target-namespace=monitoring --source=cobol-to-microservices --path="./kustomize/base" --prune=true --wait=true --interval=30m --retry-interval=2m --health-check-timeout=3m --export > ./fluxCD/metrics-kustomization.yaml
```
4. Navigate to the project directory and build each individual service like here under and setup FluxCD
```bash
cd cobol-to-microservices/gateway
```
3. Build and push the Docker images for the MicroServices to a container registry (e.g., Docker Hub, ECR):
```bash
gradle build
```
5. Monitor the deployment status using FluxCD and kubectl:
```bash
flux get kustomizations
flux get kustomizations --watch
kubectl get all -n services
kubectl get all -n monitoring
flux get sources all
flux get kustomizations
``` 
## Monitoring and Troubleshooting
- Use FluxCD logs to monitor the synchronization process and identify any issues:
```bash
kubectl logs -n flux-system deployment/flux-controller
```
- Check the status of your application and services using kubectl:
```bash
flux get kustomizations --watch
kubectl get all -n services
```
- If you encounter any issues, review the FluxCD documentation and logs for troubleshooting guidance.
- For more information on FluxCD and GitOps, refer to the official FluxCD documentation: https://fluxcd.io/docs/
- For more information on the sample application and its architecture, refer to the README files in the respective service directories (client-service, account-service, gateway).


[<img src="./back.png">](../README.md)
