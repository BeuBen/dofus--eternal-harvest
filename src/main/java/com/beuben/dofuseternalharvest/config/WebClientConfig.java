package com.beuben.dofuseternalharvest.config;

import com.beuben.dofuseternalharvest.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

  private final MetamobPropertiesConfig metamobPropertiesConfig;

  @Autowired
  public WebClientConfig(MetamobPropertiesConfig metamobPropertiesConfig) {
    this.metamobPropertiesConfig = metamobPropertiesConfig;
  }

  @Bean
  WebClient metamobReadClient() {
    return WebClient.builder()
        .defaultHeaders(httpHeaders -> {
              httpHeaders.set(Constants.HEADER_API_KEY, metamobPropertiesConfig.getKey());
            }
        )
        .baseUrl(metamobPropertiesConfig.getHost())
        .filter(contentTypeInterceptor())
        .build();
  }

  @Bean
  WebClient metamobModifyClient() {
    return WebClient.builder()
        .defaultHeaders(httpHeaders -> {
              httpHeaders.set(Constants.HEADER_API_KEY, metamobPropertiesConfig.getKey());
              httpHeaders.set(Constants.HEADER_USER_KEY, metamobPropertiesConfig.getUser());
            }
        )
        .baseUrl(metamobPropertiesConfig.getHost())
        .filter(contentTypeInterceptor())
        .build();
  }

  /**
   * This method is used to avoid InvalidMimeTypeException in the response from Metamob API
   * @return An exchangeFilterFunction removing Content-Type in the response header to replace it by the one expected
   */
  private ExchangeFilterFunction contentTypeInterceptor() {
    return ExchangeFilterFunction.ofResponseProcessor(clientResponse ->
        Mono.just(clientResponse.mutate()
            .headers(headers -> headers.remove(HttpHeaders.CONTENT_TYPE))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()));
  }
}
