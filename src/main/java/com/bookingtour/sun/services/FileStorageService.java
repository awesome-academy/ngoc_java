package com.bookingtour.sun.services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    // Provide a default value to avoid startup failure if property is missing
    @Value("${file.upload-dir:uploads/tours}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String extension = "";

            String originalFileName = file.getOriginalFilename();

            if (originalFileName != null
                    && originalFileName.contains(".")) {

                extension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf("."));
            }

            String fileName =
                    UUID.randomUUID() + extension;

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/tours/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot store file", e);
        }
    }

    public void delete(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isBlank()) {
                return;
            }

            // imageUrl lưu dạng:
            // /uploads/tours/abc.jpg

            String fileName = Paths.get(imageUrl).getFileName().toString();
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Cannot delete file: " + imageUrl, e);
        }
    }
}
