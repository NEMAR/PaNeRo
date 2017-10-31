#!/usr/bin/env bash

docker-compose kill
docker-compose rm -f

pushd ..
mvn clean verify
rm panero-docker/**/*.tar.gz # remove all binaries
cp panero-platform/target/panero-platform-*-bin.tar.gz panero-docker/platform/panero-platform-bin.tar.gz
cp panero-platform/config/application.yml panero-docker/platform/conf/application.yml
cp panero-tools/dwd-radiationdata-importer/target/dwd-radiationdata-importer-*-bin.tar.gz panero-docker/radiation/dwd-radiationdata-importer.bin.tar.gz
popd

docker build \
  --build-arg http_proxy=${http_proxy} --build-arg https_proxy=${https_proxy} --build-arg ftp_proxy=${ftp_proxy} \
  -t panero/base base

docker build \
  --build-arg http_proxy=${http_proxy} --build-arg https_proxy=${https_proxy} --build-arg ftp_proxy=${ftp_proxy} \
  -t panero/flink flink

docker build \
  --build-arg http_proxy=${http_proxy} --build-arg https_proxy=${https_proxy} --build-arg ftp_proxy=${ftp_proxy} \
  -t panero/platform platform
