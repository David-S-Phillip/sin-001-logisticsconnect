# Curl commands for life
Before executing any of the curl commands ensure the following microservices are active and running on there specified ports.
## Ingestion service
*IngestionServiceApp (ingestion-service) port: 7050*

**Check Service Health**
```bash
curl http://localhost:7050/health
# -> OK (Verifies the Ingestion Service is active and running)
```
```bash
curl http://localhost:7050/hubs
# -> Fetches the full list of cleaned hubs parsed directly from the CSV file.
```


## Hub Service
*HubServiceApp (hub-service) port: 7051*
**Checks Service Health**
```bash
curl http://localhost:7051/health
# -> OK (Verifies the Hub Service is active and running)
```
```bash
curl http://localhost"7051/hub/005"
# -> Verifies if hub ID 005 exists in the RAM cache and returns its details.
```

## Delay service
*DelayStageServiceApp (delay-stage-service) port: 7052*
**Checks Service Health**
```bash
curl http://localhost:7052/health
# -> OK (Verifies the Delay Service is active and running)
```
```bash
curl -X POST http://localhost:7052/delay-stage/005 \
  -H "Content-Type: application/json" \
  -d '{"stage": 5}'
```

```bash
curl http://localhost:7052/delay-stage/005
# -> Retrieves the current active delay penalty level for hub 005.
```

## Transit Service
*TransitServiceApp (transit-service) port: 7053*
**Check Service Health**
```bash
curl http://localhost:7053/health
# -> OK (Verifies the Transit Service is active and running)
```
```bash
curl http://localhost:7053/transit/005
# -> Triggers the orchestrator to check the Hub and Delay services, calculate the math, and return the final ETA for hub 005.
```