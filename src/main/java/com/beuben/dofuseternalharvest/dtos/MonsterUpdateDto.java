package com.beuben.dofuseternalharvest.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonsterUpdateDto {
  public Integer id;
  public String etat;
  public Integer quantite;
}
