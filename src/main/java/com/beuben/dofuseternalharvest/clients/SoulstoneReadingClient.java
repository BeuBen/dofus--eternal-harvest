package com.beuben.dofuseternalharvest.clients;

import com.beuben.dofuseternalharvest.dtos.MonsterUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class SoulstoneReadingClient {

  private final WebClient metamobModifyClient;

  @Autowired
  public SoulstoneReadingClient(WebClient metamobModifyClient) {
    this.metamobModifyClient = metamobModifyClient;
  }

  public Object putMonsters(String username, List<MonsterUpdateDto> monsters) {

    //TODO create return object class cf. doc Metamob

    return metamobModifyClient
        .put()
        .uri("/utilisateurs/" + username + "/monstres")
        .bodyValue(monsters)
        .retrieve()
        .bodyToMono(Object.class)
        .block();
  }
}
