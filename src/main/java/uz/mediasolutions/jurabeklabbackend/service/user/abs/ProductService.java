package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ProductService {

    ResponseEntity<?> getAll(int page, int size, String search);

    ResponseEntity<Page<?>> getByOrderId(int page, int size, Long orderId);
}
