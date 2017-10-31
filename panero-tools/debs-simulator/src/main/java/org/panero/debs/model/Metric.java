package org.panero.debs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;

import java.util.Iterator;
import java.util.function.Consumer;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Metric {
    @NotNull
    private Long id;

    @NotNull
    private Long timestamp;

    @NotNull
    private Double value;

    @NotNull
    private Integer property;

    @NotNull
    @JsonProperty("plug_id")
    private Long plugId;

    @NotNull
    @JsonProperty("household_id")
    private Long householdId;

    @NotNull
    @JsonProperty("house_id")
    private Long houseId;

    public static Builder create() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(final Double value) {
        this.value = value;
    }

    public Integer getProperty() {
        return property;
    }

    public void setProperty(final Integer property) {
        this.property = property;
    }

    public Long getPlugId() {
        return plugId;
    }

    public void setPlugId(final Long plugId) {
        this.plugId = plugId;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(final Long householdId) {
        this.householdId = householdId;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(final Long houseId) {
        this.houseId = houseId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Metric that = (Metric) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("timestamp", timestamp)
                .add("value", value)
                .add("property", property)
                .add("plug_id", plugId)
                .add("household_id", householdId)
                .add("house_id", houseId)
                .toString();
    }

    public static class Builder {
        private final Metric object = new Metric();

        private Builder() {

        }

        /**
         * Parses a {@link String} representing a {@link Metric}, probably read from a file.
         * <p>
         * The {@link String} has to match the following format:
         * <pre>2967740702,1379879536,0,1,3,2,12</pre>
         *
         * @param line the {@link String} representing a {@link Metric}
         * @throws RuntimeException if the given {@link String} is not parsable
         */
        public Builder parseLine(final String line) {
            try {
                final Iterator<String> it = Splitter.on(',')
                        .omitEmptyStrings()
                        .trimResults()
                        .split(line)
                        .iterator();
                object.setId(Long.parseLong(it.next()));
                object.setTimestamp(Long.parseLong(it.next()));
                object.setValue(Double.parseDouble(it.next()));
                object.setProperty(Integer.parseInt(it.next()));
                object.setPlugId(Long.parseLong(it.next()));
                object.setHouseholdId(Long.parseLong(it.next()));
                object.setHouseId(Long.parseLong(it.next()));
                return this;
            } catch (Exception e) {
                throw new RuntimeException("Unable to parse string: " + line, e);
            }
        }

        public Actions and() {
            return this.new Actions();
        }

        public class Actions {
            public Metric buildIt() {
                return object;
            }

            public void consumeIt(Consumer<Metric> consumer) {
                consumer.accept(object);
            }
        }
    }
}
