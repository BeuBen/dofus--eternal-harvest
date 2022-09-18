package com.beuben.dofuseternalharvest.controllers;

import com.beuben.dofuseternalharvest.services.SoulstoneReadingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/soulstone_reading")
public class SoulstoneReadingController {

  private final SoulstoneReadingService soulstoneReadingService;

  @Autowired
  public SoulstoneReadingController(SoulstoneReadingService soulstoneReadingService) {
    this.soulstoneReadingService = soulstoneReadingService;
  }

  @GetMapping
  public String soulstoneReadingHome() {
    return "soulstoneReading/soulstoneReadingHome";
  }

  @GetMapping("/add_home")
  public String addSoulsHome() {
    return "soulstoneReading/add/soulstoneAdd";
  }

  @GetMapping("/delete_home")
  public String deleteSoulsHome() {
    return "soulstoneReading/delete/soulstoneDelete";
  }

  @PostMapping("/add_souls")
  public String addMonstersFromScreenshots(
      @RequestParam("soulstoneScreenshot") MultipartFile soulstoneScreenshot,
      ModelMap model) {

    var souls = soulstoneReadingService.updateSoulsFromScreenshots(soulstoneScreenshot, true);

    model.put("souls", souls);
    return "soulstoneReading/soulstoneReadingResult";
  }

  @PostMapping("/delete_souls")
  public String deleteMonstersFromScreenshots(
      @RequestParam("soulstoneScreenshot") MultipartFile soulstoneScreenshot,
      ModelMap model) {
    model.put("file", soulstoneScreenshot);
    return "soulstoneReading/soulstoneReadingResult";
  }
}
