package com.beuben.dofuseternalharvest.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
  private String pseudo;
  private String contact;
  private String presentation;
  private String image;
  private String image_url;
  private Integer etape;
  private String serveur;
  private String derniere_connexion;
  private String lien;
}
