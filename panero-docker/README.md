
### PaNeRo

Docker Compose for PaNeRo
-------------------------

> [Get Docker Compose](https://www.docker.com/products/docker-compose)

#### Build the Images

```shell
./build.sh
```

#### Start the Docker environment:

```shell
docker-compose up -d
```

#### Update the Docker environment:

1. Stop all instances: `docker-compose stop`
2. Pull changes from Git: `cd .. && git pull origin master && cd -`
3. Build new environment: `sh build.sh`
4. Start environment: `docker-compose up -d`


#### Scale Apache Flink's Taskmanager

```shell
docker-compose scale taskmanager=<N>
```

#### Working with Apache Flink

```shell
# Login via SSH (password: installed)
ssh -p 2222 root@localhost

# Upload files
scp -P 2222 <file> root@localhost:/<path>

# Run a Apache Flink topology
ssh -p 2222 root@localhost /usr/local/flink/bin/flink run -c <class> <jar> <params>
```

#### Shutdown containers and clean up environment:

```shell
docker-compose kill
docker-compose rm -f
```

#### Ports

* **PaNeRo Platform**
  * HTTP port on `8080`
  * Health endpoint `/health`  
  * Metric endpoint `/metrics`
  * Weather endpoint `/weather`
* **Apache ActiveMQ**
  * Admin Web Console on port `8161`
  * OpenWire protocol port on `61617`
  * OpenWire SSL protocol port on `61616`
  * STOMP protocol port on `61614`
  * STOMP SSL protocol port on `61613`
  * MQTT protocol port on `1884`
  * MQTT SSL protocol port on `1883`
* **Apache Flink JobManager**
  * Web Dashboard is on port `48080`
  * Web Client is on port `48081`
  * RPC port `6123` (default, not exposed to host)
  * SSH Port `2222`
* **Apache Flink TaskManagers**
  * RPC port `6121` (default, not exposed to host)
  * Data port `6122` (default, not exposed to host)
  * Randomly assigned SSH port, check with `docker ps`
* **InfluxDB**
  * Web UI on port `8083`
  * TCP port on `8086`
  * Database: `panero`
  * Admin User: `NEMAR`
  * Admin Password: `PANERO`
* **Grafana**
  * Web UI on port `3000`
  * Admin User: `NEMAR`
  * Admin Password: `PANERO`

#### SSH

Default `root` password is `installed`.
