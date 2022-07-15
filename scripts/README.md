# Monitoring with prometheus and grafana

## Prometheus

In order to monitor the system, we use Prometheus. It is a tool that allows us to monitor the system and see the metrics that we want.

### How to start Prometheus
To run Prometheus, we use docker.

#### 1. Create network named `prometheus-network` in order to let Prometheus connect to other containers.
```bash 
    docker network create prometheus-network --driver bridge
```

#### 2. Start Prometheus.

You can run following command to start Prometheus, or run the script file `prometheus_docker.sh` _(or `prometheus_docker.bat`)_.

```bash
docker run --rm --name prometheus \
--network prometheus-network -p 9090:9090 \
-v "$PWD"/prometheus.yml:/opt/bitnami/prometheus/conf/prometheus.yml \
bitnami/prometheus:latest
```

> Details:
> - `--name`: Name of the container.
> - `--network`: Network of the container.
> - `--rm`: Remove the container after it is stopped.
> - `-p 9090:9090`: Map port 9090 to port 9090.
> - `-v "$PWD"/prometheus.yml:/opt/bitnami/prometheus/conf/prometheus.yml`: Mount configuration file.
> - `bitnami/prometheus:latest`: Image of the container.

# Grafana
In order to monitor the system, we use Grafana. It is a tool that allows us to monitor the system and see the metrics that we want. Grafana is a web application that allows us to visualize the metrics that we want.

## How to start Grafana
To run Grafana, we use docker.

#### 1. Start Grafana.
````bash
docker run -d -p 3000:3000 --network prometheus-network --rm --name grafana grafana/grafana:latest
````

> Details:
> - `-d`: Run in background.
> - `-p 3000:3000`: Map port 3000 to port 3000.
> - `--network prometheus-network`: Network of the container.
> - `--rm`: Remove the container after it is stopped.
> - `grafana/grafana:latest`: Image of the container.