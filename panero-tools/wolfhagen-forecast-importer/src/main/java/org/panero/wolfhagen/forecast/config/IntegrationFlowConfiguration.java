package org.panero.wolfhagen.forecast.config;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTPFile;
import org.panero.common.Consts;
import org.panero.wolfhagen.forecast.transformer.FileMessageToJobRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.ftp.Ftp;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableConfigurationProperties(FtpProperties.class)
public class IntegrationFlowConfiguration {
    @Autowired
    private FtpProperties properties;

    @Autowired
    private SessionFactory<FTPFile> ftpSessionFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Bean
    public PropertiesPersistingMetadataStore metadataStore() {
        final PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
        metadataStore.setBaseDirectory("target");
        return metadataStore;
    }

    @Bean
    public FileSystemPersistentAcceptOnceFileListFilter localFilter() {
        return new FileSystemPersistentAcceptOnceFileListFilter(metadataStore(), Consts.EMPTY);
    }

    @Bean
    public JobLaunchingGateway jobHandler() {
        return new JobLaunchingGateway(jobLauncher);
    }

    @Bean
    public MessageHandler logger() {
        final LoggingHandler logger = new LoggingHandler(LoggingHandler.Level.INFO.name());
        logger.setLoggerName(IntegrationFlowConfiguration.class.getName());
        return logger;
    }

    @Bean
    public IntegrationFlow ftpInboundFlow() {
        return IntegrationFlows
                .from(Ftp.inboundAdapter(ftpSessionFactory)
                                .preserveTimestamp(true)
                                .remoteDirectory(properties.getRemoteDirectory())
                                .patternFilter(properties.getFilenamePattern())
                                .deleteRemoteFiles(properties.getDeleteRemoteFiles())
                                .localFilenameExpression("#this.toLowerCase().replaceAll(' ', '_')")
                                .localDirectory(new File("target/transfers"))
                                .localFilter(localFilter()),
                        c -> c.poller(Pollers
                                .fixedDelay(properties.getConnection().getInterval(), TimeUnit.SECONDS)
                                .maxMessagesPerPoll(-1)))
                .transform(new FileMessageToJobRequest(job))
                .handle(jobHandler())
                .handle(logger())
                .get();
    }
}
