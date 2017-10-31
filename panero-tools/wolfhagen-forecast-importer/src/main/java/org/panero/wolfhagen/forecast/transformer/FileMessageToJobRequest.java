package org.panero.wolfhagen.forecast.transformer;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

public class FileMessageToJobRequest implements InitializingBean {
    public static final String INPUT_FILE = "input.file";

    private Job job;

    public FileMessageToJobRequest() {
    }

    public FileMessageToJobRequest(final Job job) {
        this.job = job;
    }

    public void setJob(final Job job) {
        this.job = job;
    }

    @Transformer
    public JobLaunchRequest transform(final Message<File> message) {
        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(INPUT_FILE, message.getPayload().getAbsolutePath());
        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(job);
    }
}
