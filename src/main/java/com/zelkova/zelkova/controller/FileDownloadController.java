package com.zelkova.zelkova.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.util.ApiResponseUtil;
import com.zelkova.zelkova.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Log4j2
public class FileDownloadController {

    private final String fileStorageLocation = "./upload"; // Directory where files are stored
    private final CustomFileUtil fileUtil;

    // 단일 pdf, excel, hwp 파일 업로드 체크
    @PostMapping("/upload")
    public ResponseEntity<CommonResponse<Object>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("uplaod file >>> " + file);

        // Check if the uploaded file is a PDF
        if (file.isEmpty()) {
            return ApiResponseUtil.error("Please select a file to upload");
        }
        String contentType = file.getContentType();
        String[] checkArray = new String[4];  
        checkArray[0] = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"; //word 파일
        checkArray[1] = "application/pdf"; // pdf
        checkArray[2] = "application/octet-stream"; // 한글
        checkArray[3] = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // 엑셀

        boolean found = Arrays.asList(checkArray).contains(contentType);

        if (!found) {
            return ApiResponseUtil.error("Only PDF files are allowed");
        }

        // Save the file using the service
        String filePath = fileUtil.saveFile(file);
        return ApiResponseUtil.success(filePath);
        // return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // Load file as Resource
            Path filePath = Paths.get(fileStorageLocation).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Set content type to application/pdf for PDF files
                String contentType = "application/pdf";

                // Set the response headers to force download
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

