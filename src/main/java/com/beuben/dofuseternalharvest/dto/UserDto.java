package com.beuben.dofuseternalharvest.dto;

public class UserDto {
  public String pseudo;
  public String contact;
  public String presentation;
  public String image;
  public String image_url;
  public Integer etape;
  public String serveur;
  public String derniere_connexion;
  public String lien;

  public String getPseudo() {
    return pseudo;
  }

  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getPresentation() {
    return presentation;
  }

  public void setPresentation(String presentation) {
    this.presentation = presentation;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public Integer getEtape() {
    return etape;
  }

  public void setEtape(Integer etape) {
    this.etape = etape;
  }

  public String getServeur() {
    return serveur;
  }

  public void setServeur(String serveur) {
    this.serveur = serveur;
  }

  public String getDerniere_connexion() {
    return derniere_connexion;
  }

  public void setDerniere_connexion(String derniere_connexion) {
    this.derniere_connexion = derniere_connexion;
  }

  public String getLien() {
    return lien;
  }

  public void setLien(String lien) {
    this.lien = lien;
  }
}
