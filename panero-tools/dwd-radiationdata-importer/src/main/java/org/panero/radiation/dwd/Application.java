package org.panero.radiation.dwd;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@IntegrationComponentScan

public class Application
{
  public static void main(String[] args)
  {
    LoggerFactory.getLogger("dwd-radiationdata-import").info("Starting DWD-Radiationdata Import Application");
    SpringApplication.run(Application.class, args);
  }
}
