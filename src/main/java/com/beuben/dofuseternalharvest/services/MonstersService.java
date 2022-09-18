package com.beuben.dofuseternalharvest.services;

import com.beuben.dofuseternalharvest.clients.MonstersClient;
import com.beuben.dofuseternalharvest.mappers.MonsterMapper;
import com.beuben.dofuseternalharvest.models.Monster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonstersService {

  private final MonstersClient monstersClient;

  @Autowired
  public MonstersService(MonstersClient monstersClient) {
    this.monstersClient = monstersClient;
  }

  public List<Monster> getMonsters(final String username) {
    var monstersDto = monstersClient.getMonsters(username);
    return monstersDto.stream()
        .map(MonsterMapper.INSTANCE::toMonster)
        .toList();
  }
}
