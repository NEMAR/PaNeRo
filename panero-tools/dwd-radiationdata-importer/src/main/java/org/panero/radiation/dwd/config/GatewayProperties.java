package org.panero.radiation.dwd.config;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("panero.gateway")
public class GatewayProperties
{
  @NotEmpty
  private String hostname;

  @Min(1)
  @Max(65535)
  @NotNull
  private Integer port;

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
}
