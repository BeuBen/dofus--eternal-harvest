package com.beuben.dofuseternalharvest.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class MonsterUpdateDto {
  public Integer id;
  public String etat;
  //+X or -X
  public String quantite;
}
