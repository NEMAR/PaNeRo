# PaNeRo

> Platform Prototype Supporting the Decentralized Market Agent

## Components

* **PaNeRo Platform**
    * Core Application: [`panero-platform`](panero-platform)
    * MOM: [`panero-activemq`](panero-activemq)
    * CEP: [`panero-flink`](panero-flink)
* **PaNeRo Gateway**
    * Application: [`panero-gateway`](panero-gateway)
    * Client Library: [`panero-gateway-client`](panero-gateway-client)

## Provisioning

By default, all components can be provisioned using *Docker Compose*, see [`panero-docker`](panero-docker).

## Building from Source

If you want to build PaNeRo from source code, use the following steps.

### Required Tools

* [Apache Maven](http://maven.apache.org/)
* A [GIT](http://git-scm.com/) client

### Clone and Build

Clone this repository locally using:

```shell
git clone https://github.com/NEMAR/PaNeRo.git
```

Navigate to the newly created directory and execute:

```shell
mvn clean install
```
