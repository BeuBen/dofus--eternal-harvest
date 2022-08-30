package com.beuben.dofuseternalharvest.services;

import com.beuben.dofuseternalharvest.clients.MetamobClient;
import com.beuben.dofuseternalharvest.mappers.UserMapper;
import com.beuben.dofuseternalharvest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetamobService {

  private final MetamobClient metamobClient;

  @Autowired
  public MetamobService(
      MetamobClient metamobClient
  ) {
    this.metamobClient = metamobClient;
  }

  public User getUser(final String username) {
    var userDto = metamobClient.getUser(username);
    return  UserMapper.INSTANCE.userDtoToUser(userDto);
  }
}
