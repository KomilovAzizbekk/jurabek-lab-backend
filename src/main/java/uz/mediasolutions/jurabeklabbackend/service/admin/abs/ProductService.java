package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProductEditDTO;

public interface ProductService {

    ResponseEntity<?> getAll(int page, int size, String search);

    ResponseEntity<?> add(MultipartFile file);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> edit(Long id, ProductEditDTO dto);
}
