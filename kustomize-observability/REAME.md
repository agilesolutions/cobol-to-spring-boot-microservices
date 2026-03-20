# Notes & Best Practices
The Grafana LGTM Stack container is great for:
- local/dev environments
- demos, like this one here

## For production:
- split into separate components (Loki, Tempo, Mimir, Grafana)
- add persistent volumes
- configure ingress + auth

## Consider adding:
- NetworkPolicy (restrict OTLP ingestion)
- ResourceQuota in observability namespace
- PodSecurityContext (runAsNonRoot)