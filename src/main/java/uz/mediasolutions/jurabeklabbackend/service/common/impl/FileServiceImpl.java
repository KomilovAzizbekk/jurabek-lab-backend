package uz.mediasolutions.jurabeklabbackend.service.common.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.service.common.abs.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${uploadDir}")
    private String uploadDir;

    @Value("${baseUrl}")
    private String baseUrl;

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw RestException.restThrow("File cannot be empty", HttpStatus.BAD_REQUEST);
        }

        try {
            // Fayl saqlash uchun papkani tekshirish va yaratish
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // UUID yaratish va fayl nomini kengaytmasi bilan birlashtirish
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String uuidFilename = UUID.randomUUID().toString() + extension;

            // Faylni yuklash va papkaga saqlash
            Path filePath = path.resolve(uuidFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL yaratish
            String fileUrl = baseUrl + "/api/files/get/" + uuidFilename;

            return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);

        } catch (Exception e) {
            throw RestException.restThrow("Error with uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getFile(String uuidFilename) {
        try {
            // UUID faylni saqlangan joydan olish
            Path filePath = Paths.get(uploadDir).resolve(uuidFilename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
            }

            // Faylning MIME turini aniqlash
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Faylni HTTP javobida qaytarish
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename())
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving file.");
        }
    }

    public void deleteFile(String uuidFilename) throws IOException {
        try {
            // Fayl yo'lini aniqlash va uni o‘chirish
            Path filePath = Paths.get(uploadDir).resolve(uuidFilename);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            System.out.println("Error deleting file: " + uuidFilename);
        }
    }
}
