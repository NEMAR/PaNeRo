package org.panero.platform.config.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.panero.common.BrokerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class ActiveMQConfiguration {
    @Bean
    public Queue plainInputQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_PLAIN_INPUT_DESTINATION);
    }

    @Bean
    public Queue normalizedInputQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_NORMALIZED_INPUT_DESTINATION);
    }

    @Bean
    public Queue influxDBPersistenceQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_PERSISTENCE_INFLUXDB_DESTINATION);
    }

    @Bean
    public Queue hanaDBPersistenceQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_PERSISTENCE_HANADB_DESTINATION);
    }

    @Bean
    public Queue mesapPersistenceQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_PERSISTENCE_MESAP_DESTINATION);
    }

    @Bean
    public Queue csvPersistenceQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_PERSISTENCE_CSV_DESTINATION);
    }

    @Bean
    public Queue commandsQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_COMMANDS_DESTINATION);
    }
}
