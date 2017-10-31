package org.panero.radiation.dwd.batch;

import org.panero.common.PersistenceConstants;
import org.panero.common.model.Measurement;
import org.panero.common.model.api.Measurements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class RecordProcessor implements ItemProcessor<String, Measurements> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecordProcessor.class);
  private static final Pattern FLOATING_REGEX = Pattern.compile("[-+]?\\d+(\\.\\d+)?");
  private static final Pattern DATE_REGEX = Pattern.compile("\\d{10}:\\d{2}");
  private static final Pattern INT_REGEX = Pattern.compile("[-+]?\\d+");

  @Override
  public Measurements process(final String item) throws Exception {
    String[] recordEntries = Arrays.stream(item.split(";")).map(String::trim).toArray(String[]::new);
    if (recordEntries.length < 9) {
      throw new ValidationException("Processing record with invalid entry count: " + item);
    }

    Measurement.Builder diffuse = Measurement.create("weather.radiation.diffuse");
    Measurement.Builder global = Measurement.create("weather.radiation.global");

    if (!validate(recordEntries)) {
      throw new ValidationException("Radiation record not format compliant: " + Arrays.toString(recordEntries));
    }

    // STATIONS_ID; MESS_DATUM; QUALITAETS_NIVEAU; SONNENSCHEINDAUER;DIFFUS_HIMMEL_KW_J;GLOBAL_KW_J;ATMOSPHAERE_LW_J;SONNENZENIT;MESS_DATUM_WOZ;eor
    diffuse.value(new BigDecimal(recordEntries[4].trim()));
    diffuse.valueMetadata("unit", "J/h");
//        diffuse.tag("rawValue", recordEntries[4]);
    global.value(new BigDecimal(recordEntries[5].trim()));
    global.valueMetadata("unit", "J/h");
//        global.tag("rawValue", recordEntries[5]);

    for (Measurement.Builder builder : new Measurement.Builder[]{diffuse, global}) {
      builder.time(parseDateTime(recordEntries[1]), TimeUnit.MINUTES);
      builder.tag(PersistenceConstants.TAG_TENANT, PersistenceConstants.DEFAULT_TENANT);
      builder.tag("station_id", recordEntries[0]);
      builder.tag("radiation.zenith", recordEntries[7].trim());
    }
    LOGGER.trace("Processed radiation data for [{},{}]", recordEntries[0], recordEntries[1]);
    Measurements.Builder batch = Measurements.create();
    Stream.of(diffuse.and().buildIt(), global.and().buildIt()).forEach(batch::measurement);
    return batch.and().buildIt();
  }

  private boolean validate(String[] recordEntries) {
    return validateDouble(recordEntries[4])
        && validateDouble(recordEntries[5])
        && validateDateTime(recordEntries[1])
        && validateInt(recordEntries[0])
        && validateDouble(recordEntries[7]);
  }

  private boolean validateInt(String recordEntry) {
    return INT_REGEX.matcher(recordEntry).matches();
  }

  private boolean validateDateTime(String recordEntry) {
    return DATE_REGEX.matcher(recordEntry).matches();
  }

  private boolean validateDouble(String recordEntry) {
    return FLOATING_REGEX.matcher(recordEntry).matches();
  }

  private long parseDateTime(String recordEntry) {
    final int year = Integer.parseInt(recordEntry.substring(0, 4));
    final int month = Integer.parseInt(recordEntry.substring(4, 6));
    final int day = Integer.parseInt(recordEntry.substring(6, 8));
    final int hour = Integer.parseInt(recordEntry.substring(8, 10));
    // 11 and not 10 because they insert a : before minute
    final int minute = Integer.parseInt(recordEntry.substring(11));
    final GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute);
    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
    return calendar.getTimeInMillis();
  }
}
