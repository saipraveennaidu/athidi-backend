package com.athidi.upload.controller;

import com.athidi.common.response.ApiResponse;
import com.athidi.common.response.ApiResponseBuilder;
import com.athidi.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {
    private final ApiResponseBuilder responseBuilder;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidRequestException("Please select a file to upload");
        }

        try {
            // Create uploads directory if it doesn't exist in backend directory
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Generate a unique file name
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + extension;

            // Copy file to target path
            Path targetPath = uploadDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Return relative url path (e.g. /uploads/uuid.ext)
            String fileUrl = "/uploads/" + newFileName;

            return ResponseEntity.ok(
                    responseBuilder.success("File uploaded successfully", fileUrl)
            );
        } catch (IOException e) {
            throw new RuntimeException("Could not upload file: " + e.getMessage(), e);
        }
    }
}
