package org.panero.wolfhagen.forecast.config;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@Configuration
@EnableConfigurationProperties(FtpProperties.class)
public class FtpConfiguration {
    @Autowired
    private FtpProperties properties;

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        final DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
        factory.setHost(properties.getConnection().getHostname());
        factory.setPort(properties.getConnection().getPort());
        factory.setUsername(properties.getConnection().getUsername());
        factory.setPassword(properties.getConnection().getPassword());

        factory.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
        factory.setFileType(FTP.ASCII_FILE_TYPE);
        factory.setBufferSize(100000);

        return factory;
    }
}
