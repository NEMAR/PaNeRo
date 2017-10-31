package org.panero.common.activemq;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.activemq")
public class ActiveMQProperties extends org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties {
    private String prefix;

    private int queueprefetch;

    private int queuebrowserprefetch;

    private int topicprefetch;

    private int durabletopicprefetch;

    private int optimizedurabletopicprefetch;

    private boolean useCompression = false;

    private boolean useAsyncSend = false;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public boolean isUseCompression() {
        return useCompression;
    }

    public void setUseCompression(final boolean useCompression) {
        this.useCompression = useCompression;
    }

    public boolean isUseAsyncSend() {
        return useAsyncSend;
    }

    public void setUseAsyncSend(final boolean useAsyncSend) {
        this.useAsyncSend = useAsyncSend;
    }

    public int getQueueprefetch() {
        return queueprefetch;
    }

    public void setQueueprefetch(final int queueprefetch) {
        this.queueprefetch = queueprefetch;
    }

    public int getQueuebrowserprefetch() {
        return queuebrowserprefetch;
    }

    public void setQueuebrowserprefetch(final int queuebrowserprefetch) {
        this.queuebrowserprefetch = queuebrowserprefetch;
    }

    public int getTopicprefetch() {
        return topicprefetch;
    }

    public void setTopicprefetch(final int topicprefetch) {
        this.topicprefetch = topicprefetch;
    }

    public int getDurabletopicprefetch() {
        return durabletopicprefetch;
    }

    public void setDurabletopicprefetch(final int durabletopicprefetch) {
        this.durabletopicprefetch = durabletopicprefetch;
    }

    public int getOptimizedurabletopicprefetch() {
        return optimizedurabletopicprefetch;
    }

    public void setOptimizedurabletopicprefetch(final int optimizedurabletopicprefetch) {
        this.optimizedurabletopicprefetch = optimizedurabletopicprefetch;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("brokerUrl", getBrokerUrl())
                .add("inMemory", isInMemory())
                .add("user", getUser())
                .add("password", getPassword())
                .add("prefix", prefix)
                .add("useCompression", useCompression)
                .add("useAsyncSend", useAsyncSend)
                .add("QueuePrefetch", queueprefetch)
                .add("QueueBowserPrefetch", queuebrowserprefetch)
                .add("TopicPrefetch", topicprefetch)
                .add("DurableTopicPrefetch", durabletopicprefetch)
                .add("OptimizeDurableTopicPrefetch", optimizedurabletopicprefetch)
                .toString();
    }
}
