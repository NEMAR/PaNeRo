# PaNeRo GateWay

> The **PaNeRo Gateway**, used as a mediator between **PaNeRo** and a tenant.

## Installation

* Extract the `panero-gateway-${version}-bin.tar.gz` to `/opt/panero-gateway`
* Create a symlink: `ln -s /opt/panero-gateway/panero-gateway.jar /etc/init.d/panero-gateway`
* Flag it to start automatically: `update-rc.d panero-gateway defaults` 
* Run as non-root: `chown -R <user>:<user> /opt/panero-gateway`
* Manage the service: `service panero-gateway start|stop|status`

**Optional steps:**

* Configure the permission so that the JAR file cannot be written and can only be read or executed by its owner:

    `chmod 500 /opt/panero-gateway/panero-gateway-${version}.jar`

* Make the JAR file immutable:

    `chattr +i /opt/panero-gateway/panero-gateway-${version}.jar`

## Usage

### Configuration

You can change the configuration parameters in `config/application.yml` in order to adapt the settings to your needs.

| Parameter | Description |
|:--------- |:----------- |
| `panero.tenant` | Your tenant name |
| `panero.client-id` | A unique identifier for your Gateway, adapt it if you want to run multiple Gateways |
| `panero.gateway.http.port` | PaNeRo Gateway: Exposed HTTP port |
| `panero.gateway.openwire.connector` | Gateway (OpenWire): The transport connector protocol to be used |
| `panero.gateway.openwire.port` | Gateway (OpenWire): Exposed port |
| `panero.gateway.openwire.options` | Gateway (OpenWire): Advanced connection parameters |
| `panero.gateway.stomp.connector` | Gateway (STOMP): The transport connector protocol to be used |
| `panero.gateway.stomp.port` | Gateway (STOMP): Exposed port |
| `panero.gateway.stomp.options` | Gateway (STOMP): Advanced connection parameters |
| `panero.gateway.mqtt.connector` | Gateway (MQTT): The transport connector protocol to be used |
| `panero.gateway.mqtt.port` | Gateway (MQTT): Exposed port |
| `panero.gateway.mqtt.options` | Gateway (MQTT): Advanced connection parameters |
| `panero.broker.connector` | PaNeRo Broker: The transport connector protocol to be used |
| `panero.broker.options` |  PaNeRo Broker: Advanced connection parameters |
| `panero.broker.hostname` | PaNeRo Broker Hostname |
| `panero.broker.port` | PaNeRo Broker Port |
| `panero.broker.username` | PaNeRo Broker: Your username |
| `panero.broker.password` | PaNeRo Broker: The user's password |
| `panero.key-store` | SSL Support: Keystore to be used |
| `panero.key-store-password` | SSL Support: Keystore password |
| `panero.trust-store` | SSL Support: Truststore to be used |
| `panero.trust-store-password` | SSL Support: Truststore password |

Default configuration:

```yml
panero:
  tenant: default
  client-id: 1
  gateway:
    http.port: 8080
    openwire:
      connector: tcp
      port: 61616
      options: ~
    stomp:
      connector: stomp
      port: 61613
      options: ~
    mqtt:
      connector: mqtt
      port: 1883
      options: ~
  broker:
    connector: ssl
    options: ~
    hostname: HOSTNAME
    port: 61616
    username: USERNAME
    password: PASSWORD
  key-store: config/gateway.ks
  key-store-password: password
  trust-store: config/gateway.ts
  trust-store-password: password
```

### Message API

Written messages to queue `Queue.PANERO.1.INPUT_PLAIN` are automatically forwarded to **PaNeRo**.

The queue `Queue.PANERO.1.INPUT_PLAIN` accepts `TextMessages` in JSON format. It is required to send the header `tenant` with an appropriate value in all of your messages.

The payload requires the following schema:

```
{
    tenant: string,
    measurements: [{
        name: string,
        time: long,
        precision: enum,
        tags: [{
            name: string,
            value: string
        }],
        value: number,
        value_metadata: [{
            name: string,
            value: string
        }]
    }]
}
```

Valid values for `precision` field:

```
NANOSECONDS
MICROSECONDS
MILLISECONDS
SECONDS
MINUTES
HOURS
DAYS
```

### HTTP API

You can use the built-in HTTP API in order to write/submit measurement data.

Endpoint: `POST /write`

| Parameter | Type | Required | Schema |
|:--------- |:---- |:-------- |:------ |
| `measurements` | `body` | `true` | `Measurements` |

| Responese | Description |
|:--------- |:----------- |
| `'201'` | Success |

`Measurements` JSON Schema:

```
{
    measurements: [{
        name: string,
        time: long,
        precision: enum,
        tags: [{
            name: string,
            value: string
        }],
        value: number,
        value_metadata: [{
            name: string,
            value: string
        }]
    }]
}
```

Valid values for `precision` field:

```
NANOSECONDS
MICROSECONDS
MILLISECONDS
SECONDS
MINUTES
HOURS
DAYS
```

## Monitoring

You can use the following HTTP endpoints in order to request monitoring information:

| Endpoint | Description |
|:-------- |:----------- |
| [`/info`](http://localhost:8080/info) | Displays arbitrary application info |
| [`/health`](http://localhost:8080/health) | Shows application health information |
| [`/metrics`](http://localhost:8080/metrics) | Shows ‘metrics’ information for the current application |

## Special Features

### Using PaNeRo Gateway behind a Firewall

**Problem:** TCP connections through port `61616` are blocked by our firewall.

**Solution:** Tunnel PaNeRo communication with HTTP.

Open file `application.yml` in subdirectory `/config` and apply the following settings:

```
panero:
  broker:
    connector: https
    port: 443
```

### Using PaNeRo Gateway behind a Proxy

You can tunnel the PaNeRo communication with HTTP and use appropriate proxy parameters.

Open file `application.yml` in subdirectory `/config` and apply the following settings:

```
panero:
  broker:
    connector: https
    options: proxyHost=<server>&proxyPort=<port>
    port: 443
```
