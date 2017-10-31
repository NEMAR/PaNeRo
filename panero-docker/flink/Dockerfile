FROM panero/base

ENV FLINK_VERSION 1.0.3
ENV FLINK_HADOOP hadoop1

RUN ssh-keygen -f ~/.ssh/id_rsa -t rsa -N ''
RUN cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && chmod 600 ~/.ssh/*

RUN mkdir ~/downloads && cd ~/downloads && \
    wget -q -O - http://archive.apache.org/dist/flink/flink-$FLINK_VERSION/flink-$FLINK_VERSION-bin-$FLINK_HADOOP-scala_2.10.tgz | tar -zxvf - -C /usr/local/
RUN cd /usr/local && ln -s ./flink-$FLINK_VERSION flink

ENV FLINK_HOME /usr/local/flink
ENV PATH $PATH:$FLINK_HOME/bin

ADD conf/flink-conf.yaml /usr/local/flink/conf/flink-conf.yaml
ADD conf/logback.xml /usr/local/flink/conf/logback.xml
ADD conf/logback-yarn.xml /usr/local/flink/conf/logback-yarn.xml
ADD conf/log4j.properties /usr/local/flink/conf/log4j.properties
ADD conf/log4j-cli.properties /usr/local/flink/conf/log4j-cli.properties
ADD config-flink.sh /usr/local/flink/bin/config-flink.sh

RUN chmod +x /usr/local/flink/bin/config-flink.sh

EXPOSE 22
EXPOSE 6123

CMD ["/usr/local/flink/bin/config-flink.sh", "taskmanager"]
