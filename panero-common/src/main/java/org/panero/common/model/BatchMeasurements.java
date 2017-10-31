package org.panero.common.model;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.panero.common.PersistenceConstants;

@XmlRootElement()
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BatchMeasurements {
    @NotBlank
    private String tenant;

    @Valid
    private Set<NameValuePair<String>> tags = Sets.newHashSet();

    @Valid
    @NotEmpty
    private List<Measurement> measurements = Lists.newArrayList();

    public static Builder create() {
        return new Builder();
    }

    public static Builder create(final String tenant) {
        return new Builder(tenant);
    }

    @XmlElement
    public String getTenant() {
        return tenant;
    }

    public void setTenant(final String tenant) {
        this.tenant = tenant;
    }

    @XmlElement
    public Set<NameValuePair<String>> getTags() {
        return tags;
    }

    public void setTags(final Set<NameValuePair<String>> tags) {
        this.tags = tags;
    }

    public void addTag(final String name, final String value) {
        Preconditions.checkArgument(name != null, "Parameter 'name' must not be null");
        Preconditions.checkArgument(value != null, "Parameter 'value' must not be null");
        if (tags == null) tags = Sets.newHashSet();
        tags.add(new NameValuePair<>(name, value));
    }

    @XmlElement
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(final List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurement(final Measurement measurement) {
        Preconditions.checkArgument(measurement != null, "Parameter 'measurement' must not be null");
        if (measurements == null) measurements = Lists.newArrayList();
        measurements.add(measurement);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("tenant", tenant)
                .add("tags", tags)
                .add("measurements", measurements)
                .toString();
    }

    public static class Builder {
        private final BatchMeasurements object = new BatchMeasurements();
        private Set<NameValuePair<String>> tags = Sets.newHashSet();
        private List<Measurement> measurements = Lists.newArrayList();

        private Builder() {
            this(PersistenceConstants.DEFAULT_TENANT);
        }

        private Builder(final String tenant) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(tenant), "Parameter 'tenant' must not be null");
            object.setTenant(tenant);
        }

        public <T> Builder tag(final String name, final T value) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Parameter 'name' must not be null");
            Preconditions.checkArgument(value != null, "Parameter 'value' must not be null");
            tags.add(new NameValuePair<>(name, value.toString()));
            return this;
        }

        public Builder measurement(final Measurement measurement) {
            Preconditions.checkArgument(measurement != null, "Parameter 'measurement' must not be null");
            measurements.add(measurement);
            return this;
        }

        public Actions and() {
            return this.new Actions();
        }

        public class Actions {
            public BatchMeasurements buildIt() {
                object.setTags(tags);
                object.setMeasurements(measurements);
                return object;
            }

            public void consumeIt(Consumer<BatchMeasurements> consumer) {
                consumer.accept(buildIt());
            }
        }
    }
}
