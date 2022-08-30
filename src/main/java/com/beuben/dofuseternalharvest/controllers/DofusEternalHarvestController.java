package com.beuben.dofuseternalharvest.controllers;

import com.beuben.dofuseternalharvest.services.MetamobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/eternal_harvest_manager")
public class DofusEternalHarvestController {

  private final MetamobService metamobService;

  @Autowired
  public DofusEternalHarvestController(
      MetamobService metamobService
  ) {
    this.metamobService = metamobService;
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }

  @GetMapping("/user")
  public String getMetamobApiKey(@RequestParam("username") String username, ModelMap model) {
    var user = metamobService.getUser(username);
    model.addAttribute("user", user);
    return "user";
  }
}
