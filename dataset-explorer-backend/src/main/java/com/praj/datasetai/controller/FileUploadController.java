package com.praj.datasetai.controller;

import com.praj.datasetai.service.DataIngestionService;
import com.praj.datasetai.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final StorageService storageService;
    private final DataIngestionService ingestionService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Path filePath = storageService.store(file);

            // Clean table name (e.g., "sales data.xlsx" -> "sales_data")
            String originalName = file.getOriginalFilename();
            String tableName = originalName.substring(0, originalName.lastIndexOf("."))
                    .replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();

            ingestionService.ingestFile(tableName, filePath);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "File processed successfully",
                    "tableName", tableName,
                    "type", originalName.substring(originalName.lastIndexOf(".") + 1)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Processing failed: " + e.getMessage());
        }
    }
}