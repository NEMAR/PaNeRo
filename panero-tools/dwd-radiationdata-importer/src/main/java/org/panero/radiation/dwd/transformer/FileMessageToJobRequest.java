package org.panero.radiation.dwd.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import java.io.File;

import static org.panero.radiation.dwd.config.JobConfiguration.FILE_NAME_PARAMETER;

/**
 * Created by vogel612 on 06.05.17.
 */
public class FileMessageToJobRequest implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMessageToJobRequest.class);

    private final Job job;

    public FileMessageToJobRequest(Job job) {
        this.job = job;
    }

    @Transformer
    public JobLaunchRequest transform(final Message<File> message)
    {
        LOGGER.debug("Launching job for file {}", message.getPayload().getName());
        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(FILE_NAME_PARAMETER, message.getPayload().getAbsolutePath());
        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(job);
    }
}
