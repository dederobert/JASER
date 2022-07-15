#!/bin/bash
docker run -d -p 3000:3000 --network prometheus-network --rm --name grafana grafana/grafana