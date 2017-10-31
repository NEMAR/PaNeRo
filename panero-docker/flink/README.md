
## Apache Flink Image

You can build it with the following command:

```shell
docker build -t panero/flink .
```

In order to use this image behind a corporate proxy, you can use the following command:

```shell
docker build \
  --build-arg http_proxy=${http_proxy:-http://web-proxy.acme.com:8088} \
  --build-arg https_proxy=${https_proxy:-http://web-proxy.acme.com:8088} \
  --build-arg ftp_proxy=${ftp_proxy:-http://web-proxy.acme.com:8088} \
  -t panero/flink .
```
