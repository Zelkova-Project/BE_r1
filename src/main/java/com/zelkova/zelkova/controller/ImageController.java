package com.zelkova.zelkova.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.zelkova.zelkova.dto.ImageDTO;
import com.zelkova.zelkova.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

  private final CustomFileUtil fileUtil;

  // 이미지만 등록할 때
  @PostMapping("/")
  public Map<String, String> register(ImageDTO imageDTO) {
    List<MultipartFile> list = imageDTO.getFiles();

    List<String> uploadFileNames = fileUtil.saveFiles(list);

    imageDTO.setUploadFileNames(uploadFileNames);

    log.info("uploadFileNames >>> " + uploadFileNames);

    return Map.of("RESULT", "SUCCESS");
  }

  @GetMapping("/view/{filename}")
  public ResponseEntity<Resource> viewFile(@PathVariable(name = "filename") String filename) {
    ResponseEntity<Resource> resource = fileUtil.getFile(filename);
    return resource;
  }
}
