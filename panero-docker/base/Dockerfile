FROM ubuntu:xenial

ENV http_proxy  ${http_proxy:-}
ENV https_proxy ${https_proxy:-}
ENV ftp_proxy   ${ftp_proxy:-}

RUN rm /bin/sh && ln -s /bin/bash /bin/sh
RUN apt-get update && \
    apt-get install -y curl wget apt-transport-https ca-certificates && \
    apt-get install -y openjdk-8-jdk openjdk-8-jre-headless supervisor openssh-server openssh-client && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

RUN mkdir -p /var/run/sshd && \
    sed -i "s/UsePrivilegeSeparation.*/UsePrivilegeSeparation no/g" /etc/ssh/sshd_config && \
    sed -i "s/UsePAM.*/UsePAM no/g" /etc/ssh/sshd_config && \
    sed -i "s/PermitRootLogin.*/PermitRootLogin yes/g" /etc/ssh/sshd_config
RUN echo 'root:installed' | chpasswd

EXPOSE 22

ADD supervisor.conf /etc/supervisor/supervisor.conf
