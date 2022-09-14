package com.beuben.dofuseternalharvest.mappers;

import com.beuben.dofuseternalharvest.dtos.MonsterDto;
import com.beuben.dofuseternalharvest.models.Monster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MonsterMapper {

  MonsterMapper INSTANCE = Mappers.getMapper(MonsterMapper.class);

  @Mapping(target = "name", source = "nom")
  @Mapping(target = "imageUrl", source = "image_url")
  @Mapping(target = "step", source = "etape")
  @Mapping(target = "quantity", source = "quantite")
  @Mapping(target = "looking", source = "recherche")
  @Mapping(target = "proposing", source = "propose")
  @Mapping(target = "normalName", source = "nom_normal")
  Monster toMonster(MonsterDto monsterDto);
}
