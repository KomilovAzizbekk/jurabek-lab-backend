package uz.mediasolutions.jurabeklabbackend.service.common.impl;

import jakarta.activation.MimeType;
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
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

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

            // Faylni yuklash va papkaga saqlash
            Path filePath = path.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Fayl nomini URL-kodlash (Krillcha belgilar uchun)
            String encodedFilename = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);

            // URL yaratish
            String fileUrl = baseUrl + "/api/files/get/" + encodedFilename;

            return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);

        } catch (Exception e) {
            throw RestException.restThrow("Error with uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getFile(String filename) {
        try {
            // Faylni saqlangan joydan olish
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Faylning MIME turini aniqlash
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                // MIME turini aniqlay olmasak, standart turni ishlatamiz
                contentType = "application/octet-stream";
            }

            // Faylni HTTP javobida qaytarish
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename())
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public void deleteFile(String imageUrl) throws IOException {
        String imagePath = uploadDir + imageUrl.substring(imageUrl.lastIndexOf('/'));
        Path path = Paths.get(imagePath);
        Files.deleteIfExists(path);
    }
}
