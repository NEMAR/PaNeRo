package org.panero.wolfhagen.forecast.config;

import java.nio.charset.StandardCharsets;

import org.panero.common.model.Measurement;
import org.panero.gateway.client.GatewayClient;
import org.panero.gateway.client.GatewayConfiguration;
import org.panero.gateway.client.http.DefaultHttpClient;
import org.panero.gateway.client.http.HttpClientConfiguration;
import org.panero.wolfhagen.forecast.batch.InputWrapper;
import org.panero.wolfhagen.forecast.batch.InputWrapperProcessor;
import org.panero.wolfhagen.forecast.batch.MeasurementWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(GatewayProperties.class)
public class JobConfiguration {
    @Autowired
    private GatewayProperties properties;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private InputWrapperProcessor processor;

    @Autowired
    private MeasurementWriter writer;

    @Bean
    public GatewayConfiguration configuration() {
        final HttpClientConfiguration clientConfiguration = new HttpClientConfiguration();
        clientConfiguration.setHostname(properties.getHostname());
        clientConfiguration.setPort(properties.getPort());
        return clientConfiguration;
    }

    @Bean
    public GatewayClient client() {
        return new DefaultHttpClient((HttpClientConfiguration) configuration());
    }

    @Bean
    @StepScope
    public FlatFileItemReader<InputWrapper> reader(@Value("file://#{jobParameters['input.file']}") final Resource resource) {
        final FlatFileItemReader<InputWrapper> reader = new FlatFileItemReader<>();
        reader.setResource(resource);
        reader.setEncoding(StandardCharsets.UTF_8.name());
        reader.setLineMapper(new DefaultLineMapper<InputWrapper>() {{
            setLineTokenizer(new DelimitedLineTokenizer(";") {{
                setNames(new String[]{"date", "time", "value"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<InputWrapper>() {{
                setTargetType(InputWrapper.class);
            }});
        }});
        return reader;
    }

    @Bean
    public Step step(final ItemReader<InputWrapper> reader) {
        return steps.get("convertInput")
                .<InputWrapper, Measurement>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(final Step step) {
        return jobs.get("InputConversion")
                .start(step)
                .build();
    }
}
