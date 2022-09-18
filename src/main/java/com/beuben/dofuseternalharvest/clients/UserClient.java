package com.beuben.dofuseternalharvest.clients;

import com.beuben.dofuseternalharvest.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserClient {

  private final WebClient metamobReadClient;

  @Autowired
  public UserClient(
      @Qualifier("metamobReadClient") WebClient metamobReadClient
  ) {
    this.metamobReadClient = metamobReadClient;
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
