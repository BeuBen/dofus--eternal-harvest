package com.beuben.dofuseternalharvest.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

  //METAMOB
  public static final String HEADER_API_KEY = "HTTP-X-APIKEY";
  public static final String HEADER_USER_KEY = "HTTP-X-USERKEY";

  //SOULSTONE READING
  public static final String TMP_PATH = "src/main/resources/static/tmp";
  public static final String TESSDATA_PATH = "src/main/resources/tessdata";
  public static final Integer SIMILARITY_MIN_DISTANCE = 3;
  public static final String ARCHI_TYPE = "archimonstre";
  public static final String STATE_NONE = "aucun";
  public static final String STATE_LOOKING = "recherche";
  public static final String STATE_PROPOSE = "propose";
  public static final List<String> MONSTERS_NOT_IN_QUEST =
      List.of(
          "Kwakere de Flamme",
          "Kwakere de Glace",
          "Kwakere de Terre",
          "Kwakere de Vent",
          "Craquelope",
          "Guerrier Mental",
          "Robot Fléau",
          "Citwouille",
          "Ricanif"
      );
}
