package org.panero.debs.config.batch;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.panero.debs.config.DebsProperties;
import org.panero.debs.model.Metric;
import org.panero.debs.model.converter.MetricToMeasurementsConverter;
import org.panero.debs.resource.DebsInputResource;
import org.panero.gateway.client.GatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(DebsProperties.class)
public class BatchRunner {
    private static Logger logger = LoggerFactory.getLogger(BatchRunner.class);

    @Autowired
    private DebsProperties properties;

    @Autowired
    private GatewayClient gatewayClient;

    @Bean
    public ItemReader<Metric> itemReader() {
        final FlatFileItemReader<Metric> reader = new FlatFileItemReader<>();
        reader.setResource(new DebsInputResource(properties.getFile(), properties.isCompressed()));
        reader.setLineMapper(new DefaultLineMapper<Metric>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"id", "timestamp", "value", "property", "plugId", "householdId", "houseId"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Metric>() {{
                setTargetType(Metric.class);
            }});
        }});
        reader.setEncoding(StandardCharsets.UTF_8.name());
        reader.setStrict(true);
        return reader;
    }

    @Bean
    public ItemWriter<Metric> itemWriter() {
        final MetricToMeasurementsConverter converter = new MetricToMeasurementsConverter();
        return items -> {
            // ... create ~60 chunks
            final List<? extends List<? extends Metric>> chunks = Lists.partition(items, items.size() / 60);
            logger.info("Sending [{}] messages with [{}] total items", chunks.size(), items.size());
            chunks.forEach(chunk -> gatewayClient.sendAsync(converter.convert(chunk)));
            Uninterruptibles.sleepUninterruptibly(properties.getInterval(), TimeUnit.MILLISECONDS);
        };
    }

    @Bean
    public Job job(final JobBuilderFactory jobBuilderFactory, final StepBuilderFactory stepBuilderFactory) {
        final Policy policy = new Policy();
        final Step pipeline = stepBuilderFactory.get("debs.step")
                .<Metric, Metric>chunk(policy).listener(policy)
                .reader(itemReader()).writer(itemWriter()).build();
        return jobBuilderFactory.get("debs")
                .incrementer(new RunIdIncrementer())
                .start(pipeline).build();
    }
}
