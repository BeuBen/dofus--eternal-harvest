package com.beuben.dofuseternalharvest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }
}
