package org.panero.common.model;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Measurement {
    @NotBlank
    private String name;

    @Valid
    private Set<NameValuePair<String>> tags = Sets.newHashSet();

    @NotNull
    private Long time = System.currentTimeMillis();

    @NotNull
    private TimeUnit precision = TimeUnit.MILLISECONDS;

    @NotNull
    private BigDecimal value;

    @Valid
    @JsonProperty("value_metadata")
    private Set<NameValuePair<?>> valueMetadata = Sets.newHashSet();

    public static Builder create(final String name) {
        return new Builder(name);
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
    public Long getTime() {
        return time;
    }

    public void setTime(final Long time) {
        this.time = time;
    }

    public void setTime(final Long time, final TimeUnit precision) {
        setTime(time);
        setPrecision(precision);
    }

    @XmlElement
    public TimeUnit getPrecision() {
        return precision;
    }

    public void setPrecision(final TimeUnit precision) {
        this.precision = precision;
    }

    @XmlElement
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    @XmlElement
    public Set<NameValuePair<?>> getValueMetadata() {
        return valueMetadata;
    }

    public void setValueMetadata(final Set<NameValuePair<?>> valueMetadata) {
        this.valueMetadata = valueMetadata;
    }

    public <T> void addValueMetadata(final String name, final T value) {
        Preconditions.checkArgument(name != null, "Parameter 'name' must not be null");
        Preconditions.checkArgument(value != null, "Parameter 'value' must not be null");
        if (valueMetadata == null) valueMetadata = Sets.newHashSet();
        valueMetadata.add(new NameValuePair<>(name, value));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("name", name)
                .add("tags", tags)
                .add("time", time)
                .add("precision", precision)
                .add("value", value)
                .add("value_metadata", valueMetadata)
                .toString();
    }

    public static class Builder {
        private final Measurement object = new Measurement();
        private final Set<NameValuePair<String>> tags = Sets.newHashSet();
        private final Set<NameValuePair<?>> valueMetadata = Sets.newHashSet();

        private Builder(final String name) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Parameter 'name' must not be empty");
            object.setName(name);
        }

        public Builder time(final long time, final TimeUnit precision) {
            return time(Long.valueOf(time), precision);
        }

        public Builder time(final Long time, final TimeUnit precision) {
            Preconditions.checkArgument(time != null, "Parameter 'time' must not be null");
            Preconditions.checkArgument(precision != null, "Parameter 'precision' must not be null");
            object.setTime(time);
            object.setPrecision(precision);
            return this;
        }

        public <T> Builder tag(final String name, final T value) {
            Preconditions.checkArgument(name != null, "Parameter 'name' must not be null");
            Preconditions.checkArgument(value != null, "Parameter 'value' must not be null");
            tags.add(new NameValuePair<>(name, value.toString()));
            return this;
        }

        public Builder value(final double value) {
            return value(BigDecimal.valueOf(value));
        }

        public Builder value(final Double value) {
            return value(BigDecimal.valueOf(value));
        }

        public Builder value(final BigDecimal value) {
            Preconditions.checkArgument(value != null, "Parameter 'value' must not be null");
            object.setValue(value);
            return this;
        }

        public <T> Builder valueMetadata(final String name, final T value) {
            Preconditions.checkArgument(name != null, "Parameter 'name' must not be null");
            Preconditions.checkArgument(value != null, "Parameter 'value' must not be null");
            valueMetadata.add(new NameValuePair<>(name, value));
            return this;
        }

        public Actions and() {
            return this.new Actions();
        }

        public class Actions {
            public Measurement buildIt() {
                object.setTags(tags);
                object.setValueMetadata(valueMetadata);
                if (object.getValue() == null) object.setValue(BigDecimal.ZERO);
                if (object.getTime() == null) object.setTime(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                return object;
            }

            public void consumeIt(Consumer<Measurement> consumer) {
                consumer.accept(buildIt());
            }
        }
    }
}
