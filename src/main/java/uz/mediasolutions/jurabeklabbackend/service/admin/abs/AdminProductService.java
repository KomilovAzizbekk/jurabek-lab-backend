package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProductReqDTO;

public interface AdminProductService {

    ResponseEntity<?> getAll(int page, int size, String search);

    ResponseEntity<?> add(ProductReqDTO dto);

    ResponseEntity<?> edit(Long id, ProductReqDTO dto);

    ResponseEntity<?> delete(Long id);
}
