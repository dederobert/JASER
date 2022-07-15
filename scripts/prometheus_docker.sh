#!/bin/bash
docker run --rm --name prometheus \
--network prometheus-network -p 9090:9090 \
-v "$PWD"/prometheus.yml:/opt/bitnami/prometheus/conf/prometheus.yml \
bitnami/prometheus:latest