package org.panero.radiation.dwd.batch;

import org.panero.common.model.api.Measurements;
import org.panero.gateway.client.GatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@StepScope
public class MeasurementWriter implements ItemWriter<Measurements> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementWriter.class);

  @Autowired
  private GatewayClient client;

  @Override
  public void write(final List<? extends Measurements> items) throws Exception {
    LOGGER.debug("Sending [{}] diffuse and global solar irradiation measurements", items.size());
    final Measurements.Builder builder = Measurements.create();
    items.stream().map(Measurements::getMeasurements)
        .flatMap(List::stream)
        // exclude error-valued data
        .filter(measurement -> measurement.getValue().compareTo(new BigDecimal(-999)) != 0)
        .forEach(builder::measurement);
    final Measurements m = builder.and().buildIt();
    LOGGER.debug("Flattened {} Measurement pairs into {} Measurements", items.size(), m.getMeasurements().size());
    if (m.getMeasurements().isEmpty()) {
      LOGGER.info("Not writing empty Measurement collection");
      return;
    }
    client.send(m);
    LOGGER.info("Successfully submitted {} measurements to panero-gateway", m.getMeasurements().size());
  }
}
