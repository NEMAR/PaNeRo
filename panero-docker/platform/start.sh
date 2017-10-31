#!/usr/bin/env bash

params=("$@")
for arg in "${params[@]}"; do
   ./wait-for-it.sh ${arg} --timeout=0
done

cd panero-platform
java -Djava.security.egd=file:/dev/./urandom -Xms512m -Xmx2048m -jar panero-platform.war --spring.profiles.active=docker
exit 0
