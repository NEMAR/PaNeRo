package org.panero.radiation.dwd.config;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("ftp")
public class FtpProperties
{
  @Valid
  private Connection connection;

  @NotEmpty
  private String filenamePattern;

  @NotNull
  private Boolean deleteRemoteFiles;

  @NotEmpty
  private String remoteDirectory;

  @NotEmpty
  private String localDirectory;

  public Connection getConnection()
  {
    return connection;
  }

  public void setConnection(final Connection connection)
  {
    this.connection = connection;
  }

  public String getFilenamePattern()
  {
    return filenamePattern;
  }

  public void setFilenamePattern(final String filenamePattern)
  {
    this.filenamePattern = filenamePattern;
  }

  public Boolean getDeleteRemoteFiles() {
    return deleteRemoteFiles;
  }

  public void setDeleteRemoteFiles(Boolean deleteRemoteFiles) {
    this.deleteRemoteFiles = deleteRemoteFiles;
  }

  public String getRemoteDirectory()
  {
    return remoteDirectory;
  }

  public void setRemoteDirectory(final String remoteDirectory)
  {
    this.remoteDirectory = remoteDirectory;
  }

  public String getLocalDirectory() {
    return localDirectory;
  }

  public void setLocalDirectory(String localDirectory) {
    this.localDirectory = localDirectory;
  }

  public static class Connection
  {
    @NotEmpty
    private String hostname;

    @Min(1)
    @Max(65535)
    @NotNull
    private Integer port;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotNull
    private Integer interval;

    public String getHostname()
    {
      return hostname;
    }

    public void setHostname(final String hostname)
    {
      this.hostname = hostname;
    }

    public Integer getPort()
    {
      return port;
    }

    public void setPort(final Integer port)
    {
      this.port = port;
    }

    public String getUsername()
    {
      return username;
    }

    public void setUsername(final String username)
    {
      this.username = username;
    }

    public String getPassword()
    {
      return password;
    }

    public void setPassword(final String password)
    {
      this.password = password;
    }

    public Integer getInterval()
    {
      return interval;
    }

    public void setInterval(final Integer interval)
    {
      this.interval = interval;
    }
  }
}
