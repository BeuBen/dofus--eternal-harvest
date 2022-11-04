package com.beuben.dofuseternalharvest.services;

import com.beuben.dofuseternalharvest.clients.SoulstoneReadingClient;
import com.beuben.dofuseternalharvest.dtos.MonsterUpdateDto;
import com.beuben.dofuseternalharvest.models.Monster;
import com.beuben.dofuseternalharvest.utils.Constants;
import info.debatty.java.stringsimilarity.WeightedLevenshtein;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SoulstoneReadingService {

  private final MonstersService monstersService;

  private final SoulstoneReadingClient soulstoneReadingClient;

  @Autowired
  public SoulstoneReadingService(MonstersService monstersService, SoulstoneReadingClient soulstoneReadingClient) {
    this.monstersService = monstersService;
    this.soulstoneReadingClient = soulstoneReadingClient;
  }

  public List<MonsterUpdateDto> updateSoulsFromScreenshots(MultipartFile[] multipartFiles, Boolean add) throws IOException {

    var monsterUpdateDTOs = new ArrayList<MonsterUpdateDto>(Collections.emptyList());

    var tesseract = setupTesseract();

    //Get current monsters for user
    //TODO parametrize username
    var currentMonsters = monstersService.getUserMonsters("Brux");

    //Generate list of MonsterUpdateDto for each file
    Arrays.stream(multipartFiles)
        .forEach(multipartFile -> {
              try {
                monsterUpdateDTOs.addAll(generateMonsterUpdateDTOs(multipartFile, tesseract, add, currentMonsters));
              } catch (IOException e) {
                log.error(e.getMessage());
              }
            }
        );

    //Call to Metamob API to update the monsters
    //TODO parametrize username
    var response = soulstoneReadingClient.putMonsters("Brux", monsterUpdateDTOs);

    log.info(response.toString());

    //Clear tmp folder
    var tmpFolder = new File(Constants.TMP_PATH);
    var listFiles = Optional.ofNullable(tmpFolder.listFiles());
    listFiles.ifPresent(files ->
        Arrays.stream(files)
            .forEach(this::deleteTmpFileWrapped)
    );

    return monsterUpdateDTOs;
  }

  public Tesseract setupTesseract() {
    var tesseract = new Tesseract();
    tesseract.setDatapath(Constants.TESSDATA_PATH);
    tesseract.setLanguage("eng");
    tesseract.setPageSegMode(1);
    tesseract.setOcrEngineMode(1);
    return tesseract;
  }

  public List<MonsterUpdateDto> generateMonsterUpdateDTOs(
      MultipartFile multipartFile,
      Tesseract tesseract,
      Boolean add,
      List<Monster> currentMonsters) throws IOException {

    var file = convertMultipartToFile(multipartFile);

    var text = "";

    try {
      text = tesseract.doOCR(file);
    } catch (TesseractException tesseractException) {
      log.error(tesseractException.getMessage());
    }

    //Parse and prepare list of new monsters
    var newMonstersAndQuantities = getMonstersNameAndQuantitiesFromText(text);

    removeMonstersNotInQuest(newMonstersAndQuantities);

    //Adapt quantities if add or delete
    newMonstersAndQuantities.replaceAll((monsterName, quantity) -> add ? quantity : quantity * -1);

    //Create MonsterUpdateDto objects to call Metamob API
    return newMonstersAndQuantities
        .entrySet()
        .stream()
        .map(entry -> buildMonsterUpdateDto(entry, currentMonsters, add))
        .filter(Objects::nonNull)
        .toList();
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
      log.error(ioException.getMessage());
    }

    return file;
  }

  public void deleteTmpFileWrapped(File file) {
    try {
      FileUtils.delete(file);
    } catch (IOException ioException) {
      log.error(ioException.getMessage());
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

  public void removeMonstersNotInQuest(Map<String, Long> monstersMap) {
    monstersMap
        .keySet()
        .removeIf(Constants.MONSTERS_NOT_IN_QUEST::contains);
  }

  public MonsterUpdateDto buildMonsterUpdateDto(
      Map.Entry<String, Long> entry,
      List<Monster> currentMonsters,
      Boolean add) {

    //Weighted strategy to compare monster names
    var strategy = new WeightedLevenshtein((c, c1) -> {
      if (c == 'l' && c1 == Character.MIN_VALUE) {
        return 0.1;
      }
      return 1.0;
    });

    //Get monster id in metamob
    var monsterId =
        Optional.ofNullable(currentMonsters
            .stream()
            .filter(currentMonster ->
                //Name comparison to get id
                strategy.distance(currentMonster.getName(), entry.getKey()) < Constants.SIMILARITY_MIN_DISTANCE)
            .toList()
            .get(0)
            .getId());

    if (monsterId.isPresent()) {

      var currentQuantity = getCurrentQuantityById(monsterId.get(), currentMonsters);

      var isArchi = isArchi(monsterId.get(), currentMonsters);

      var state = resolveState(currentQuantity, Math.toIntExact(entry.getValue()), isArchi);

      return new MonsterUpdateDto()
          .withId(monsterId.get())
          .withQuantite(add ?
              String.join("", "+", String.valueOf(entry.getValue()))
              : String.valueOf(entry.getValue()))
          .withEtat(state);
    } else {
      return null;
    }
  }

  public Integer getCurrentQuantityById(Integer id, List<Monster> currentMonsters) {
    return currentMonsters.stream()
        .filter(currentMonster -> Objects.equals(currentMonster.getId(), id))
        .toList()
        .get(0)
        .getQuantity();
  }

  public Boolean isArchi(Integer id, List<Monster> currentMonsters) {
    return currentMonsters.stream()
        .filter(currentMonster -> Objects.equals(currentMonster.getId(), id))
        .toList()
        .get(0)
        .getType()
        .equals(Constants.ARCHI_TYPE);
  }

  public String resolveState(Integer currentQuantity, Integer newQuantity, Boolean isArchi) {
    if (!isArchi) {
      return Constants.STATE_NONE;
    }

    var delta = currentQuantity + newQuantity;

    if (delta == 0) {
      return Constants.STATE_LOOKING;
    } else if (delta > 1) {
      return Constants.STATE_PROPOSE;
    } else {
      return Constants.STATE_NONE;
    }
  }
}
