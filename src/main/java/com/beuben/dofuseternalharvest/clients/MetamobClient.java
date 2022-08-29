package com.beuben.dofuseternalharvest.clients;

import com.beuben.dofuseternalharvest.dto.UserDto;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MetamobClient {

  private final WebClient metamobReadClient;
  private final WebClient metamobModifyClient;

  @Autowired
  public MetamobClient(
      @Qualifier("metamobReadClient") WebClient metamobReadClient,
      @Qualifier("metamobModifyClient") WebClient metamobModifyClient
  ) {
    this.metamobReadClient = metamobReadClient;
    this.metamobModifyClient = metamobModifyClient;
  }

  public UserDto getUser(String username) {
    return metamobReadClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/utilisateurs/" + username).build())
        .retrieve()
        .bodyToMono(UserDto.class)
        .block();
  }
}
