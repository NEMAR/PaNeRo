package org.panero.gateway.config.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.panero.common.BrokerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:activemq-context.xml")
public class ActiveMQConfiguration {
    @Bean
    public Queue plainInputQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_PLAIN_INPUT_DESTINATION);
    }

    @Bean
    public Queue commandsQueue() {
        return new ActiveMQQueue(BrokerConstants.PANERO_COMMANDS_DESTINATION);
    }
}
