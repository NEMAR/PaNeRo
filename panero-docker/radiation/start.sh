#!/usr/bin/env bash

params=("$@")
for arg in "${params[@]}"; do
   ./wait-for-it.sh ${arg} --timeout=0
done

java -Djava.security.egd=file:/dev/./urandom -Xms512m -Xmx2048m -jar dwd-radiationdata-importer/dwd-radiationdata-importer.jar --spring.profiles.active=docker
./start.sh & #restart application when java terminates
