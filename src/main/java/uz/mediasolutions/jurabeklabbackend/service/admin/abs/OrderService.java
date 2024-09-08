package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReq2DTO;

public interface OrderService {

    ResponseEntity<?> getAll(int page, int size, String status);

    ResponseEntity<?> edit(Long id, OrderReq2DTO dto);

    ResponseEntity<?> accept(Long id, boolean accept);

    ResponseEntity<Page<?>> getAllOrderProducts(Long orderId, int page, int size);
}
