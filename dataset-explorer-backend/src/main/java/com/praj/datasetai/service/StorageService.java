package com.praj.datasetai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Service
public class StorageService {

    @Value("${storage.location}")
    private String storageLocation;

    public Path store(MultipartFile file) throws IOException {
        Path root = Paths.get(storageLocation);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destinationFile = root.resolve(Paths.get(filename)).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return destinationFile;
    }
}