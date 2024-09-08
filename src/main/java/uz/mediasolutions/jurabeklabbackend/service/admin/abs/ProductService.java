package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ResponseEntity<?> getAll(int page, int size, String search);

    ResponseEntity<?> add(MultipartFile file);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> editImage(Long id, String imageUrl);
}
