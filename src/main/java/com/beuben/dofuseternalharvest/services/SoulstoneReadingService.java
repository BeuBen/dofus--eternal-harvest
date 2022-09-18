package com.beuben.dofuseternalharvest.services;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SoulstoneReadingService {

  public SoulstoneReadingService() {
  }

  public String updateSoulsFromScreenshots(MultipartFile multipartFile, Boolean add) throws IOException {

    var file = convertMultipartToFile(multipartFile);
    var tesseract = setupTesseract();
    var text = "";

    try {
      text = tesseract.doOCR(file);
    } catch (TesseractException tesseractException) {
      System.out.println(tesseractException.getMessage());
    }

    //TODO clean temp folder after

    //TODO parse text + update call on metamob API

    //TODO before update call, call monsters to setup difference

    return text;
  }

  public Tesseract setupTesseract() {
    var tesseract = new Tesseract();
    tesseract.setDatapath("src/main/resources/tessdata");
    tesseract.setLanguage("eng");
    tesseract.setPageSegMode(1);
    tesseract.setOcrEngineMode(1);
    return tesseract;
  }

  public File convertMultipartToFile(MultipartFile multipartFile) throws IOException {

    var tmpPath = Paths.get("src/main/resources/static/tmp");
    if (!Files.exists(tmpPath)) {
      Files.createDirectory(tmpPath);
    }

    var localPath = String.join("",
        "src/main/resources/static/tmp/",
        multipartFile.getOriginalFilename());

    var file = new File(localPath);

    try {
      multipartFile.transferTo(file.getAbsoluteFile());
    } catch (IOException ioException) {
      System.out.println(ioException.getMessage());
    }

    return file;
  }
}
