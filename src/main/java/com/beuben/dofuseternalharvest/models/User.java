package com.beuben.dofuseternalharvest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private String pseudo;
  private String contact;
  private String presentation;
  private String image;
  private String imageUrl;
  private Integer step;
  private String server;
  private Date lastConnection;
  private String link;
}
