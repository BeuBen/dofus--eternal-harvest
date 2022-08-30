package com.beuben.dofuseternalharvest.mappers;

import com.beuben.dofuseternalharvest.dtos.UserDto;
import com.beuben.dofuseternalharvest.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "imageUrl", source = "image_url")
  @Mapping(target = "step", source = "etape")
  @Mapping(target = "server", source = "serveur")
  // TODO problème avec la date qui n'est pas bien mappée
  @Mapping(target = "lastConnection", source = "derniere_connexion", dateFormat = "dd-MM-yyyy HH:mm:ss")
  @Mapping(target = "link", source = "lien")
  User userDtoToUser(UserDto userDto);

}
