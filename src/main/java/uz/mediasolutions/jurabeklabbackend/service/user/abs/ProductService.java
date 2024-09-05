package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.http.ResponseEntity;

public interface ProductService {

    ResponseEntity<?> getAll(int page, int size, String search);

}
