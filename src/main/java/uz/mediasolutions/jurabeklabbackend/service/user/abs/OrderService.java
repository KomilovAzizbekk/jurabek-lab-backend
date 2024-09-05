package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;

public interface OrderService {

    ResponseEntity<Page<?>> findAll(int page, int size, String status);

    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> add(OrderReqDTO dto);
}
