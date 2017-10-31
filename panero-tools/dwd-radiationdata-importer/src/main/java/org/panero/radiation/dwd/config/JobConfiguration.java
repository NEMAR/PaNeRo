package org.panero.radiation.dwd.config;

import org.panero.common.model.api.Measurements;
import org.panero.gateway.client.GatewayClient;
import org.panero.gateway.client.GatewayConfiguration;
import org.panero.gateway.client.http.DefaultHttpClient;
import org.panero.gateway.client.http.HttpClientConfiguration;
import org.panero.radiation.dwd.batch.MeasurementWriter;
import org.panero.radiation.dwd.batch.RecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.item.DefaultItemFailureHandler;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.separator.SuffixRecordSeparatorPolicy;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(GatewayProperties.class)
public class JobConfiguration {
  public static final String FILE_NAME_PARAMETER = "input.file";
  private static final Logger LOGGER = LoggerFactory.getLogger(JobConfiguration.class);

  @Autowired
  private GatewayProperties properties;

  @Autowired
  private JobBuilderFactory jobs;

  @Autowired
  private StepBuilderFactory steps;

  @Autowired
  private RecordProcessor processor;

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
  public FlatFileItemReader<String> reader(@Value("file://#{jobParameters['" + FILE_NAME_PARAMETER + "']}") final Resource resource) {
    LOGGER.info("Creating ItemReader for input file {}", resource.getFilename());
    final FlatFileItemReader<String> reader = new FlatFileItemReader<>();
    reader.setResource(resource);
    reader.setEncoding(StandardCharsets.UTF_8.name());
    reader.setLinesToSkip(1); // skip headers
    reader.setLineMapper((line, number) -> line.replaceAll("[\\n\\r ]+", ""));
    SuffixRecordSeparatorPolicy recordSeparatorPolicy = new SuffixRecordSeparatorPolicy();
    recordSeparatorPolicy.setSuffix("eor");
    reader.setRecordSeparatorPolicy(recordSeparatorPolicy);
    reader.setBufferedReaderFactory((file, enc) -> {
      ZipInputStream input = new ZipInputStream(file.getInputStream(), Charset.forName(enc));
      while (true) {
        ZipEntry currentEntry = input.getNextEntry();
        if (currentEntry == null || currentEntry.getName().startsWith("produkt")) {
          LOGGER.info("Using ZipEntry {}", currentEntry);
          break;
        }
      }
      return new BufferedReader(new InputStreamReader(input));
    });
    return reader;
  }

  @Bean
  public Step step(final ItemReader<String> reader) {
    return steps.get("convertInput")
        .<String, Measurements>chunk(1000)
        .faultTolerant()
        .retryLimit(0)
        .skipPolicy((throwable, skipCount) -> {
          if (NonTransientResourceException.class.isAssignableFrom(throwable.getClass())) {
            LOGGER.warn("Skipping read for file");
            return true;
          }
          if (throwable instanceof ValidationException
              || throwable instanceof StringIndexOutOfBoundsException
              || throwable instanceof NumberFormatException) {
            return true;
          }
          throw new SkipLimitExceededException(0, throwable);
        })
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .listener((ItemReadListener<Object>) new DefaultItemFailureHandler()) // because stupid generics ...
        .listener((ItemProcessListener<Object, Object>) new DefaultItemFailureHandler())
        .listener((ItemWriteListener<Object>) new DefaultItemFailureHandler())
        .listener(new SkipListener<String, Measurements>() {
          @Override
          public void onSkipInRead(Throwable throwable) {
            LOGGER.warn("Failed to read data because of {}", throwable);
          }

          @Override
          public void onSkipInWrite(Measurements measurement, Throwable throwable) {
            LOGGER.warn("Failed to write Measurement data [{}] because of {}", measurement, throwable);
          }

          @Override
          public void onSkipInProcess(String solarRecord, Throwable throwable) {
            LOGGER.warn("Failed to process Record [{}] due to {}", solarRecord, throwable);
          }
        })
        .build();
  }

  @Bean
  public Job job(final Step step) {
    return jobs.get("InputConversion")
        .start(step)
        .build();
  }
}
