package org.panero.platform.persistence.influxdb.converter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.influxdb.dto.Point;
import org.panero.common.PersistenceConstants;
import org.panero.common.model.Measurement;
import org.panero.common.model.NameValuePair;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

class MeasurementConverter implements Converter<Measurement, Point> {
    @Bean
    private List<String> getMappingList() {
        return Stream.of("il1", "il2", "il3",
                "metering_value", "metering_value_backup", "metering_value_backup2",
                "p_negative", "p_positive",
                "phase_i1_u1", "phase_i2_u2", "phase_i3_u3", "phase_u1_u2", "phase_u1_u3",
                "pl1_negative", "pl1_positive",
                "pl2_negative", "pl2_positive",
                "pl3_negative", "pl3_positive",
                "ql1_negative", "ql1_positive",
                "ql2_negative", "ql2_positive",
                "ql3_negative", "ql3_positive",
                "ul1", "ul2", "ul3").collect(Collectors.toList());
    }

    @Override
    public Point convert(final Measurement raw) {
        Measurement.Builder builder = Measurement.create(raw.getName())
                .time(raw.getTime(), raw.getPrecision())
                .value(raw.getValue());
        // Fix overwrite issue for Measurement (ilx): "message_subtype" must be a tag and not a field
        if (getMappingList().contains(raw.getName()) && raw.getTags().stream().map(NameValuePair::getName).filter("message_subtype"::equals).count() == 0) {
            raw.getTags().stream().forEach((NameValuePair pair) -> builder.tag(pair.getName(), pair.getValue()));
            builder.tag("message_subtype", raw.getValueMetadata().stream()
                    .filter(metadata -> "message_subtype".equals(metadata.getName()))
                    .filter(Objects::nonNull)
                    .map(NameValuePair::getValue)
                    .map(Objects::toString)
                    .findFirst().orElse(""));
            raw.getValueMetadata().stream().filter(pair -> !"message_subtype".equals(pair.getName())).forEach(
                    pair -> builder.valueMetadata(pair.getName(), pair.getValue())
            );
        } else {
            raw.getTags().stream().forEach((NameValuePair pair) -> builder.tag(pair.getName(), pair.getValue()));
            raw.getValueMetadata().stream().forEach(pair ->
                    builder.valueMetadata(pair.getName(), pair.getValue())
            );
        }

        final Measurement source = builder.and().buildIt();

        return Point.measurement(source.getName())
                .time(source.getTime(), source.getPrecision())
                .tag(source.getTags().stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue)))
                .addField(PersistenceConstants.FIELD_VALUE, source.getValue())
                .fields(source.getValueMetadata().stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue))).build();
    }
}
