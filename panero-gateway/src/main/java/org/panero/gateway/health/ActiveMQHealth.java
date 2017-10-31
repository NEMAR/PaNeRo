package org.panero.gateway.health;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component("ActiveMQ")
public class ActiveMQHealth extends AbstractHealthIndicator {
    @Autowired
    private BrokerViewMBean brokerView;

    @Override
    protected void doHealthCheck(final Health.Builder builder) throws Exception {
        builder.up();
        builder.withDetail("name", brokerView.getBrokerName());
        builder.withDetail("version", brokerView.getBrokerVersion());
        builder.withDetail("uptime", brokerView.getUptime());
        builder.withDetail("store used (%)", brokerView.getStorePercentUsage());
        builder.withDetail("memory used (%)", brokerView.getMemoryPercentUsage());
        builder.withDetail("temp used (%)", brokerView.getTempPercentUsage());
        builder.withDetail("producers", brokerView.getTotalProducerCount());
        builder.withDetail("consumers", brokerView.getTotalConsumerCount());
        builder.withDetail("messages", new MessageStats(brokerView.getTotalMessageCount(),
                brokerView.getTotalEnqueueCount(),
                brokerView.getTotalDequeueCount()));
    }

    private static class MessageStats {
        private long total;
        private long enqueued;
        private long dequeued;

        public MessageStats(final long total, final long enqueued, final long dequeued) {
            this.total = total;
            this.enqueued = enqueued;
            this.dequeued = dequeued;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(final long total) {
            this.total = total;
        }

        public long getEnqueued() {
            return enqueued;
        }

        public void setEnqueued(final long enqueued) {
            this.enqueued = enqueued;
        }

        public long getDequeued() {
            return dequeued;
        }

        public void setDequeued(final long dequeued) {
            this.dequeued = dequeued;
        }

        public long getDiff() {
            return enqueued - dequeued;
        }
    }
}
