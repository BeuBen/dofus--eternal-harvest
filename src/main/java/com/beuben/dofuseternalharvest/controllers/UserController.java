package com.beuben.dofuseternalharvest.controllers;

import com.beuben.dofuseternalharvest.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(
      UserService userService
  ) {
    this.userService = userService;
  }

  @GetMapping
  public String userHome() {
    return "user/userHome";
  }

  @GetMapping("/information")
  public String getUser(@RequestParam("username") String username, ModelMap model) {
    var user = userService.getUser(username);
    model.addAttribute("user", user);
    return "user/user";
  }
}
