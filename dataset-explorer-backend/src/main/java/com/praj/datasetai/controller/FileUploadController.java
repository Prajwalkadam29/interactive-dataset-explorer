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
            // 1. Save file to disk
            Path filePath = storageService.store(file);

            // 2. Clean table name (remove extension and special chars)
            String tableName = file.getOriginalFilename()
                    .replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();

            // 3. Ingest into DuckDB
            ingestionService.ingestCsv(tableName, filePath);

            return ResponseEntity.ok(Map.of(
                    "message", "File uploaded and ingested successfully",
                    "tableName", tableName
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}