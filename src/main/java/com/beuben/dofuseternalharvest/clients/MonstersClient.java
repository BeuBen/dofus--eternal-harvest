package com.beuben.dofuseternalharvest.clients;

import com.beuben.dofuseternalharvest.dtos.MonsterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MonstersClient {

  private final WebClient metamobReadClient;

  @Autowired
  public MonstersClient(
      @Qualifier("metamobReadClient") WebClient metamobReadClient
  ) {
    this.metamobReadClient = metamobReadClient;
  }

  public List<MonsterDto> getMonsters() {
    return metamobReadClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/monstres").build())
        .retrieve()
        .bodyToFlux(MonsterDto.class)
        .collectList()
        .block();
  }

  public List<MonsterDto> getUserMonsters(String username) {
    return metamobReadClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/utilisateurs/" + username + "/monstres").build())
        .retrieve()
        .bodyToFlux(MonsterDto.class)
        .collectList()
        .block();
  }
}
