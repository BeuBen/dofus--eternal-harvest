package com.beuben.dofuseternalharvest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Monster {
  private Integer id;
  private String name;
  private String slug;
  private String type;
  private String imageUrl;
  private Integer step;
  private String zone;
  private String souszone;
  private Integer quantity;
  private Integer looking;
  private Integer proposing;
  private String normalName;
}
