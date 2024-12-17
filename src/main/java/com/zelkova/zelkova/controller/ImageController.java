package com.zelkova.zelkova.controller;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.zelkova.zelkova.dto.ImageDTO;
import com.zelkova.zelkova.util.CustomFileUtil;

import io.swagger.v3.oas.annotations.Operation;
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
  @Operation(summary = "이미지 등록", description = "이미지 등록")
  public Map<String, List<String>> register(ImageDTO imageDTO) {
    List<MultipartFile> list = imageDTO.getFiles();

    List<String> uploadFileNames = fileUtil.saveFiles(list);

    imageDTO.setUploadFileNames(uploadFileNames);

    return Map.of("imageNames", uploadFileNames);
  }

    // 이미지만 등록할 때
    // @PostMapping("/webp/")
    // public Map<String, String> registerWebp(@RequestParam("file") MultipartFile file) throws IOException {
  
    //   String webpName = fileUtil.saveFile(file);
  
    //   return Map.of("uploadFileName", webpName);
    // }

  @PostMapping("/webp/")
  @Operation(summary = "webp로 이미지 등록", description = "webp로 이미지 등록")
  public List<String> registerWebp(@RequestParam("files") List<MultipartFile> files) throws IOException {

    String webpName = "";

    List<String> loadedFileNames = new ArrayList<>();

    for (int i = 0; i < files.size(); i++) {
      MultipartFile file = files.get(i);
      webpName = fileUtil.saveFile(file);
      loadedFileNames.add(webpName);
    }
    
    return loadedFileNames;
  }

  @GetMapping("/view/{filename}")
  @Operation(summary = "이미지 경로 조회", description = "이미지 경로 조회")
  public ResponseEntity<Resource> viewFile(@PathVariable(name = "filename") String filename) {
    ResponseEntity<Resource> resource = fileUtil.getFile(filename);
    return resource;
  }
}


