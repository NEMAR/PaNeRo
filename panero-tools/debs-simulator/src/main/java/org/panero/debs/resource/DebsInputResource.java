package org.panero.debs.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.springframework.core.io.FileSystemResource;

public class DebsInputResource extends FileSystemResource {
    private boolean compressed;

    public DebsInputResource(final File file, final boolean compressed) {
        super(file);
        this.compressed = compressed;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (compressed) {
            return new GZIPInputStream(super.getInputStream());
        }
        return super.getInputStream();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }
}
