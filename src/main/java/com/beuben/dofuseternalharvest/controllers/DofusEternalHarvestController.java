package com.beuben.dofuseternalharvest.controllers;

import com.beuben.dofuseternalharvest.services.MetamobService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

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
  public String getUser(@RequestParam("username") String username, ModelMap model) {
    var user = metamobService.getUser(username);
    model.addAttribute("user", user);
    return "user";
  }

  @GetMapping("/monsters")
  public String getMonsters(@RequestParam("username") String username, ModelMap model) {
    var monsters = metamobService.getMonsters(username);
    model.addAttribute("monsters", monsters);
    return "monsters";
  }

  @GetMapping("/test")
  public String readImageText() {

    try {
      var tesseract = new Tesseract();
      tesseract.setDatapath("src/main/resources/tessdata");
      tesseract.setLanguage("eng");
      tesseract.setPageSegMode(1);
      tesseract.setOcrEngineMode(1);

      var text = tesseract.doOCR(new File("src/main/resources/static/Capture.PNG"));
      System.out.println(text);
    } catch (TesseractException tesseractException) {
      System.out.println(tesseractException.getMessage());
    }

    return "home";
  }
}
