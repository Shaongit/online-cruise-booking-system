package com.cruise.booking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path baseUploadDir;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.baseUploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.baseUploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + this.baseUploadDir, e);
        }
    }

    /**
     * Stores a ship image file.
     * File is named: ship_{shipId}_{8-char-uuid}.{ext}
     *
     * @return the public URL path, e.g. /uploads/ships/ship_1_a1b2c3d4.jpg
     */
    public String storeShipImage(MultipartFile file, Long shipId) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null
                ? file.getOriginalFilename() : "image");
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String fileName = "ship_" + shipId + "_" + uniqueId + (ext != null ? "." + ext : "");

        Path shipsDir = baseUploadDir.resolve("ships");
        Files.createDirectories(shipsDir);

        Path target = shipsDir.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/ships/" + fileName;
    }

    /**
     * Deletes a previously uploaded file given its public URL path.
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;
        try {
            // Strip leading /uploads/ prefix to get relative path within base dir
            String relative = fileUrl.replaceFirst("^/uploads/", "");
            Path filePath = baseUploadDir.resolve(relative).normalize();
            // Safety check: ensure path is within upload directory
            if (filePath.startsWith(baseUploadDir)) {
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ignored) {
        }
    }
}
