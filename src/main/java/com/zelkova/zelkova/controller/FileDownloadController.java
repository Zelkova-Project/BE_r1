package com.zelkova.zelkova.controller.files;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileDownloadController {

    private final String fileStorageLocation = "./upload"; // Directory where files are stored

    // POST endpoint to upload a file
    // @PostMapping("/upload")
    // public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    //     // Check if the uploaded file is a PDF
    //     if (file.isEmpty()) {
    //         return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
    //     }

    //     if (!file.getContentType().equals("application/pdf")) {
    //         return new ResponseEntity<>("Only PDF files are allowed.", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    //     }

    //     try {
    //         // Save the file using the service
    //         String filePath = fileStorageService.saveFile(file);
    //         return new ResponseEntity<>("File saved at: " + filePath, HttpStatus.OK);
    //     } catch (IOException e) {
    //         return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

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
