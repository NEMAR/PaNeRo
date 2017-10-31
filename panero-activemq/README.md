
### PaNeRo

ActiveMQ Broker Configuration
-----------------------------

Default configuration for the ActiveMQ Broker running inside **PaNeRo Core**.

### Destinations

The following destinations are available by default:

```
Queue.PANERO.1.INPUT_PLAIN
Queue.PANERO.1.INPUT_NORMALIZED
```

Central inbound queue is `Queue.PANERO.1.INPUT_PLAIN`.

All incoming messages via `Queue.PANERO.1.INPUT_PLAIN` are forwarded to queue `Queue.PANERO.1.INPUT_NORMALIZED` since at the moment we do not have a normalizer component.

### Transport Connectors

| Protocol | Port | Connector |
|:-------- |:----:|:---------:|
| HTTP (SSL) | 51515 | `https` |
| OpenWire (SSL) | 61616 | `ssl` |
| OpenWire | 61617 | `tcp` |
| STOMP (SSL) | 61613 | `stomp+ssl` |
| STOMP | 61614 | `stomp` |
| MQTT (SSL) | 1883 | `mqtt+ssl` |
| MQTT | 1884 | `mqtt` |

> Each transport connector is DDoS protected by limiting concurrent connections to `1000` and frame size to `100MB`.

### Secure Communication

By default, a self-signed certificate is used to encrypt the communication between a **PaNeRo Gateway** and **PaNeRo Core**.

The certificate has been issued to `nemar.iaas.uni-stuttgart.de` and is valid until 2016-06-29.
