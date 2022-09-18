package com.beuben.dofuseternalharvest.controllers;

import com.beuben.dofuseternalharvest.services.MonstersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/monsters")
public class MonstersController {

  private final MonstersService monstersService;

  @Autowired
  public MonstersController(
      MonstersService monstersService
  ) {
    this.monstersService = monstersService;
  }

  @GetMapping
  public String monstersHome() {
    return "monsters/monstersHome";
  }

  @GetMapping("/list")
  public String getMonsters(@RequestParam("username") String username, ModelMap model) {
    var monsters = monstersService.getUserMonsters(username);
    model.addAttribute("monsters", monsters);
    return "monsters/monsters";
  }
}
