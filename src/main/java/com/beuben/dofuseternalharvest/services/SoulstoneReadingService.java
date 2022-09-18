package com.beuben.dofuseternalharvest.services;

import com.beuben.dofuseternalharvest.dtos.MonsterUpdateDto;
import com.beuben.dofuseternalharvest.models.Monster;
import com.beuben.dofuseternalharvest.utils.Constants;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SoulstoneReadingService {

  private final MonstersService monstersService;

  @Autowired
  public SoulstoneReadingService(MonstersService monstersService) {
    this.monstersService = monstersService;
  }

  public String updateSoulsFromScreenshots(MultipartFile multipartFile, Boolean add) throws IOException {

    //Read content of the screenshot
    var file = convertMultipartToFile(multipartFile);
    var tesseract = setupTesseract();
    var text = "";

    try {
      text = tesseract.doOCR(file);
    } catch (TesseractException tesseractException) {
      System.out.println(tesseractException.getMessage());
    }

    //Get current monsters for user
    //TODO parametrize username
    var currentMonsters = monstersService.getUserMonsters("Brux");

    //Parse and prepare list of new monsters
    var newMonstersAndQuantities = getMonstersNameAndQuantitiesFromText(text);

    //Adapt quantities if add or delete
    newMonstersAndQuantities.replaceAll((monsterName, quantity) -> add ? quantity : quantity * -1);

    //Create MonsterUpdateDto objects to call Metamob API
    //The Collator allows to bypass the break related to the bad detection of accents
    var monstersUpdateDTOs =
        newMonstersAndQuantities
            .entrySet()
            .stream()
            .map(entry -> buildMonsterUpdateDto(entry, currentMonsters))
            .toList();


    //TODO gérer les changements de quantité et les états (recherche/propose)


    //TODO update call on metamob API

    //Clear tmp folder
    var tmpFolder = new File(Constants.TMP_PATH);
    var listFiles = Optional.ofNullable(tmpFolder.listFiles());
    listFiles.ifPresent(files ->
        Arrays.stream(files)
            .forEach(this::deleteTmpFileWrapped)
    );

    return text;
  }

  public Tesseract setupTesseract() {
    var tesseract = new Tesseract();
    tesseract.setDatapath(Constants.TESSDATA_PATH);
    tesseract.setLanguage("eng");
    tesseract.setPageSegMode(1);
    tesseract.setOcrEngineMode(1);
    return tesseract;
  }

  public File convertMultipartToFile(MultipartFile multipartFile) throws IOException {

    var tmpPath = Paths.get(Constants.TMP_PATH);
    if (!Files.exists(tmpPath)) {
      Files.createDirectory(tmpPath);
    }

    var localPath = String.join("",
        Constants.TMP_PATH,
        "/",
        multipartFile.getOriginalFilename());

    var file = new File(localPath);

    try {
      multipartFile.transferTo(file.getAbsoluteFile());
    } catch (IOException ioException) {
      System.out.println(ioException.getMessage());
    }

    return file;
  }

  public void deleteTmpFileWrapped(File file) {
    try {
      FileUtils.delete(file);
    } catch (IOException ioException) {
      System.out.println(ioException.getMessage());
    }
  }

  public Map<String, Long> getMonstersNameAndQuantitiesFromText(String text) {

    var lines = text.split("\n");

    //Keep only lines with '(<level>)'
    var monsterLines =
        Arrays.stream(lines)
            .filter(line -> line.matches("^.*\\(.*\\)$"))
            .toList();

    //Remove level at the end of line and remove accents
    List<String> monsterLinesWithoutLevels = monsterLines.stream()
        .map(monsterLine ->
            monsterLine.substring(0, monsterLine.indexOf('(') - 1))
        .toList();

    //Return map of monster name and quantities
    return monsterLinesWithoutLevels.stream()
        .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
  }

  public MonsterUpdateDto buildMonsterUpdateDto(Map.Entry<String, Long> entry, List<Monster> currentMonsters) {
    var monsterId =
        currentMonsters
            .stream()
            .filter(currentMonster -> {
              var collator = Collator.getInstance();
              collator.setStrength(Collator.PRIMARY);
              return collator.compare(currentMonster.getName(), entry.getKey()) == 0;
            })
            .toList()
            .get(0)
            .getId();
    return new MonsterUpdateDto().withId(monsterId).withQuantite(Math.toIntExact(entry.getValue()));
  }
}
