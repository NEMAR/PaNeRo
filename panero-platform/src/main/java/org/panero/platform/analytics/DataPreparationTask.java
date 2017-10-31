package org.panero.platform.analytics;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.panero.common.model.BatchMeasurements;
import org.panero.common.model.NameValuePair;
import org.panero.platform.config.AnalyticsFlowConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.influxdb.InfluxDBProperties;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(AnalyticsProperties.class)
public class DataPreparationTask {
    private static Logger logger = LoggerFactory.getLogger(DataPreparationTask.class);

    @Autowired
    private AnalyticsProperties properties;

    @Autowired
    private InfluxDBProperties influxDBProperties;

    @Autowired
    private InfluxDBTemplate<BatchMeasurements> influxDBTemplate;

    @Autowired
    private AnalyticsFlowConfiguration.OptimizationDataGateway gateway;

    @ServiceActivator
    public NameValuePair<String> prepareData() {
        doPrepareData();
        return new NameValuePair<>("prepare_data", HttpStatus.OK.name());
    }

    @Scheduled(fixedDelayString = "${panero.analytics.short-term.interval}000")
    private void doPrepareData() {
        logger.info("=== START Data Preparation");
        properties.getShortTerm().getDataSets().forEach(dataSet -> {
            logger.info("Peparing data for tenant [{}]", dataSet.getTenant());
            final StringBuilder query = new StringBuilder();
            dataSet.getQueries().forEach(q -> query.append(q.replaceAll("\\$\\{tenant\\}", dataSet.getTenant())).append(";"));
            final List<QueryResult.Result> queryResult = doQuery(query.toString());
            if (queryResult == null) return;
            final List<QueryResult.Series> series = queryResult
                    .stream()
                    .filter(result -> result.getSeries() != null)
                    .map(QueryResult.Result::getSeries)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            gateway.send(new OptimizationData(dataSet.getTenant(), series));
        });
        logger.info("=== END Data Preparation");
    }

    private List<QueryResult.Result> doQuery(final String q) {
        logger.info("Executing query: {}", q);
        final Query query = new Query(q, influxDBProperties.getDatabase());
        final QueryResult queryResult = influxDBTemplate.query(query, TimeUnit.SECONDS);
        if (queryResult.hasError()) {
            logger.error("Could not execute query: {}", queryResult.getError());
            return null;
        }
        return queryResult.getResults();
    }
}
