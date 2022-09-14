package com.beuben.dofuseternalharvest.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonsterDto {
  private Integer id;
  private String nom;
  private String slug;
  private String type;
  private String image_url;
  private Integer etape;
  private String zone;
  private String souszone;
  private Integer quantite;
  private Integer recherche;
  private Integer propose;
  private String nom_normal;
}
