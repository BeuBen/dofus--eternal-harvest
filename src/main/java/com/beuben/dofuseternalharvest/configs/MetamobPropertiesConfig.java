package com.beuben.dofuseternalharvest.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "metamob.api")
public class MetamobPropertiesConfig {

  /**
   * Metamob API key linked to user account.
   */
  private String key;

  /**
   * Metamob API URL
   */
  private String host;

  /**
   * Metamob API user id
   */
  private String user;

}
