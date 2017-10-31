package org.panero.debs.config.batch;

import org.panero.debs.model.Metric;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.context.RepeatContextSupport;

public class Policy implements CompletionPolicy, ItemReadListener<Metric> {
    private long marker;
    private long timestamp;

    @Override
    public boolean isComplete(final RepeatContext context, final RepeatStatus result) {
        return (result != null && !result.isContinuable()) || isComplete(context);
    }

    @Override
    public boolean isComplete(final RepeatContext context) {
        return marker != 0 && marker < timestamp;
    }

    @Override
    public RepeatContext start(final RepeatContext context) {
        return new RepeatContextSupport(context);
    }

    @Override
    public void update(final RepeatContext context) {
        if (marker < timestamp) marker = timestamp;
    }

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(final Metric metric) {
        timestamp = metric.getTimestamp();
    }

    @Override
    public void onReadError(final Exception e) {

    }
}
