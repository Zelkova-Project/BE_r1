package com.zelkova.zelkova.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

import net.coobird.thumbnailator.Thumbnails;

@Component
@Log4j2
public class CustomFileUtil {
  // 업로드 경로 기본값을 application properties
  @Value("${com.zelkova.upload.path}")
  private String uploadPath;

  @Value("${com.zelkova.upload.filepath}")
  private String filepath;
  
  // init "@PostConstruct"를 이용해 디렉토리 없으면 생성
  // dependency injection 되는 순간 excute
  @PostConstruct
  public void init() {
    File file = new File(uploadPath);
    File file2 = new File(filepath);

    if (!file.exists()) {
      file.mkdir();
    }

    if (!file2.exists()) {
      file2.mkdir();
    }
    uploadPath = file.getAbsolutePath();
    filepath = file2.getAbsolutePath();
    log.info("---------------");
    log.info("--------------- uploadPath : " + uploadPath);
    log.info("--------------- filepath : " + filepath);
  }

  public String saveFile(MultipartFile file) {
    if (file == null) {
      return "no file";
    }
    String fileSavedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path path = Paths.get(filepath, fileSavedName);

    try {
      Files.copy(file.getInputStream(), path);
    } catch (IOException e) {
      log.info("file Except ! " + e);
    }

    return fileSavedName;
  }
  
  public List<String> saveFiles(List<MultipartFile> files) {
    if (files == null || files.size() == 0) {
      return List.of();
    }

    List<String> uploadFileNames = new ArrayList<>();

    // >>> for 시작
    for (MultipartFile file : files) {
      // 중복값 피하기 위한 UUID
      String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

      // 목적지 + 파일명 (파일 자체도 디스크의 주소이기 때문에 path가 될 수 있다.)
      Path path = Paths.get(uploadPath, savedName);

      try {
        // 바이트화 된 multipartFile을 사용할 수 있도록 InputStream에 넣어줌. 
        Files.copy(file.getInputStream(), path);

        // 썸네일 프로세스 시작
        String contentType = file.getContentType();
        if (contentType.startsWith("image")) {
          Path thumbPath = Paths.get(uploadPath, "s_" + savedName);

          Thumbnails.of(path.toFile())
              .size(200, 200)
              .toFile(thumbPath.toFile());
        }

        uploadFileNames.add(savedName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // >>> for 끝

    return uploadFileNames;
  }
  
  public ResponseEntity<Resource> getFile(String filename) {
    Resource resource = new FileSystemResource(uploadPath + File.separator + filename);

    if (!resource.isReadable()) {
      resource = new FileSystemResource(uploadPath + File.separator + "default.png");
    }

    HttpHeaders headers = new HttpHeaders();

    try {
      // probeContentType: file이면 file로 타입을 맞춰주고 아니라면 타입을 체크해 넣어줌
      headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    }

    return ResponseEntity.ok().headers(headers).body(resource);
  }
  
  public void deleteFiles(List<String> fileNames) {
    if (fileNames.size() == 0 || fileNames == null) {
      return;
    }

    for (String name : fileNames) {
      Path thumbPath = Paths.get(uploadPath, "s_" + name);
      Path originPath = Paths.get(uploadPath, name);

      try {
        Files.deleteIfExists(thumbPath);
        Files.deleteIfExists(originPath);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }
}
