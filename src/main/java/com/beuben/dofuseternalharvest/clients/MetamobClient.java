package com.beuben.dofuseternalharvest.clients;

import com.beuben.dofuseternalharvest.dtos.MonsterDto;
import com.beuben.dofuseternalharvest.dtos.MonsterUpdateDto;
import com.beuben.dofuseternalharvest.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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

  public List<MonsterDto> getMonsters(String username) {
    return metamobReadClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/utilisateurs/" + username + "/monstres").build())
        .retrieve()
        .bodyToFlux(MonsterDto.class)
        .collectList()
        .block();
  }

  /*public void updateMonsters(final String username, final List<MonsterUpdateDto> monstersToUpdate) {
    metamobModifyClient
        .put()
        .uri(uriBuilder -> uriBuilder.path("/utilisateurs/" + username + "/monstres"))
  }*/
}
