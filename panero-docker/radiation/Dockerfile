FROM java:8-jre

ENV http_proxy  ${http_proxy:-}
ENV https_proxy ${https_proxy:-}
ENV ftp_proxy   ${ftp_proxy:-}

ADD dwd-radiationdata-importer.bin.tar.gz /usr/local/
ADD wait-for-it.sh /usr/local/wait-for-it.sh
ADD start.sh /usr/local/start.sh

RUN chmod -R 755 /usr/local/start.sh
RUN chmod -R 755 /usr/local/wait-for-it.sh
RUN chmod -R 755 /usr/local/dwd-radiationdata-importer

WORKDIR /usr/local

CMD /usr/local/start.sh 8086 8080
