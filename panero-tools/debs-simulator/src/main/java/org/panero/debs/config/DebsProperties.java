package org.panero.debs.config;

import java.io.File;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("debs")
public class DebsProperties {
    @NotNull
    private File file;

    private boolean compressed = true;

    @NotNull
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int interval;

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(final boolean compressed) {
        this.compressed = compressed;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(final int interval) {
        this.interval = interval;
    }
}
