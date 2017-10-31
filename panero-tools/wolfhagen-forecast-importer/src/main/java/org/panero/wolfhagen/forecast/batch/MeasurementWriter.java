package org.panero.wolfhagen.forecast.batch;

import com.google.common.base.Splitter;
import com.google.common.util.concurrent.Uninterruptibles;
import org.panero.common.model.Measurement;
import org.panero.common.model.api.Measurements;
import org.panero.gateway.client.GatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@StepScope
public class MeasurementWriter implements ItemWriter<Measurement> {
    private static Logger logger = LoggerFactory.getLogger(MeasurementWriter.class);

    @Autowired
    private GatewayClient client;

    /*
     * Current absolute file, e.g. /full/path/to/file/2016.06.21_residuallast_prognose_1.csv
     */
    private String resource;

    // type of measurement
    // e.g. 2016.06.21_residuallast_prognose_1.csv -> residuallast_prognose
    private String type;

    @Override
    public void write(final List<? extends Measurement> items) throws Exception {
        logger.info("Sending [{}] {} measurements", items.size(), type);
        final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final String forecastDate = getForecastDate();
        final String forecastFile = new File(resource).getName();
        final Measurements.Builder builder = Measurements.create();
        items.forEach(item -> {
            item.addTag("timestamp", timestamp);
            item.addTag("forecast_date", forecastDate);
            item.addTag("forecast_file", forecastFile);
            item.setName(type);
            builder.measurement(item);
        });
        builder.and().consumeIt(client::sendAsync);
        Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
    }

    @Value("#{jobParameters['input.file']}")
    public void setFile(final String resource) {
        this.resource = resource;
        String[] splitter = resource.split("_");
        if (splitter.length > 2) {
            // prognose -> forecast because it was always named like this
            this.type = String.format("%s.%s", splitter[1], splitter[2].replace("prognose", "forecast")).toLowerCase();
        } else {
            logger.error("Can not determine measurement name from {}.", resource);
        }
    }

    /**
     * Get the date for the forecast from the filename.
     *
     * @return Returns the date formatted in ISO format, such as 2011-12-03
     */
    private String getForecastDate() {
        final String name = new File(resource).getName();
        final Iterator<String> date = Splitter.on(".").trimResults().split(name.substring(0, 10)).iterator();
        final Integer year = Integer.parseInt(date.next());
        final Integer month = Integer.parseInt(date.next());
        final Integer day = Integer.parseInt(date.next());
        return LocalDate.of(year, month, day).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
