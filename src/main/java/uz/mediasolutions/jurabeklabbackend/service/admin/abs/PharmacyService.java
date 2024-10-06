package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PharmacyService {

    ResponseEntity<Page<?>> getAll(int page, int size, String search);

    ResponseEntity<?> addByFile(MultipartFile file);

    ResponseEntity<?> delete(Long id);
}
