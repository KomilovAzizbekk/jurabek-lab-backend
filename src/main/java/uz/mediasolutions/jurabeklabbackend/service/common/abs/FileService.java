package uz.mediasolutions.jurabeklabbackend.service.common.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResponseEntity<?> uploadFile(MultipartFile file);

    ResponseEntity<?> getFile(String filename);
}
