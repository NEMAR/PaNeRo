package org.panero.wolfhagen.forecast.batch;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import org.panero.common.PersistenceConstants;
import org.panero.common.model.Measurement;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component
public class InputWrapperProcessor implements ItemProcessor<InputWrapper, Measurement> {
    @Override
    public Measurement process(final InputWrapper item) throws Exception {
        // <Date>;<Time>;<Value>
        // 04.07.2016;02:00;2226,6600
        final Double value = Double.parseDouble(CharMatcher.anyOf(",").replaceFrom(item.getValue(), "."));
        final Iterator<String> date = Splitter.on(".").trimResults().split(item.getDate()).iterator();
        final Iterator<String> time = Splitter.on(":").trimResults().split(item.getTime()).iterator();
        final Integer day = Integer.parseInt(date.next());
        final Integer month = Integer.parseInt(date.next());
        final Integer year = Integer.parseInt(date.next());
        final Integer hour = Integer.parseInt(time.next());
        final Integer minute = Integer.parseInt(time.next());
        final ZonedDateTime datetime = LocalDateTime.of(year, month, day, hour, minute).atZone(ZoneId.systemDefault());
        final Long timestamp = datetime.toEpochSecond();
        // ----------------------------
        return Measurement.create("residuallast.forecast")
                .tag(PersistenceConstants.TAG_TENANT, "wolfhagen")
                .time(timestamp, TimeUnit.SECONDS)
                .value(value)
                .and().buildIt();
    }
}
