package org.panero.common.model.api;

import java.util.List;
import java.util.function.Consumer;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.NotEmpty;
import org.panero.common.model.Measurement;

@XmlRootElement(name = "write")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Measurements {
    @Valid
    @NotEmpty
    private List<Measurement> measurements = Lists.newArrayList();

    public static Builder create() {
        return new Builder();
    }

    @XmlElement
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(final List<Measurement> measurements) {
        Preconditions.checkArgument(measurements != null, "Parameter 'measurements' must not be null");
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
                .add("measurements", measurements)
                .toString();
    }

    public static class Builder {
        private final Measurements object = new Measurements();

        private List<Measurement> measurements = Lists.newArrayList();

        private Builder() {

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
            public Measurements buildIt() {
                object.setMeasurements(measurements);
                return object;
            }

            public void consumeIt(Consumer<Measurements> consumer) {
                consumer.accept(buildIt());
            }
        }
    }
}
