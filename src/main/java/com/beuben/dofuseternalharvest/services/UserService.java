package com.beuben.dofuseternalharvest.services;

import com.beuben.dofuseternalharvest.clients.UserClient;
import com.beuben.dofuseternalharvest.mappers.UserMapper;
import com.beuben.dofuseternalharvest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserClient userClient;

  @Autowired
  public UserService(UserClient userClient) {
    this.userClient = userClient;
  }

  public User getUser(final String username) {
    var userDto = userClient.getUser(username);
    return  UserMapper.INSTANCE.toUser(userDto);
  }
}
