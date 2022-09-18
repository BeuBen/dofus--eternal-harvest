package com.beuben.dofuseternalharvest.services;

import com.beuben.dofuseternalharvest.utils.Constants;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

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

    //TODO parse text + update call on metamob API

    //TODO before update call, call monsters to setup difference

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
}
