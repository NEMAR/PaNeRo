FROM java:8-jre

ENV http_proxy  ${http_proxy:-}
ENV https_proxy ${https_proxy:-}
ENV ftp_proxy   ${ftp_proxy:-}

ADD panero-platform-bin.tar.gz /usr/local/
ADD conf/application.yml /usr/local/panero-platform/config/application.yml
ADD conf/application-docker.yml /usr/local/panero-platform/config/application-docker.yml
ADD wait-for-it.sh /usr/local/wait-for-it.sh
ADD start.sh /usr/local/start.sh

RUN chmod -R 755 /usr/local/start.sh
RUN chmod -R 755 /usr/local/wait-for-it.sh
RUN chmod -R 755 /usr/local/panero-platform

EXPOSE 8080

WORKDIR /usr/local

CMD /usr/local/start.sh
