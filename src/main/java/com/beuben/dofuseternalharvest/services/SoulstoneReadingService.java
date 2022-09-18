package com.beuben.dofuseternalharvest.services;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class SoulstoneReadingService {

  public SoulstoneReadingService() {
  }

  public String updateSoulsFromScreenshots(MultipartFile multipartFile, Boolean add) {

    var file = convertMultipartToFile(multipartFile, add);
    var tesseract = setupTesseract();
    var text = "";

    try {
      text = tesseract.doOCR(file);
    } catch (TesseractException tesseractException) {
      System.out.println(tesseractException.getMessage());
    }

    //TODO parse text + update call on metamob API

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

  public File convertMultipartToFile(MultipartFile multipartFile, Boolean add) {

    //TODO set local path dynamically + use temp folder + clean temp folder after

    var localPath = String.join("",
        "D:\\DevProjects\\dofus--eternal-harvest\\src\\main\\resources\\static\\",
        add ? "add/" : "delete/",
        multipartFile.getOriginalFilename());

    var file = new File(localPath);

    try {
      multipartFile.transferTo(file);
    } catch (IOException ioException) {
      System.out.println(ioException.getMessage());
    }

    return file;
  }
}
