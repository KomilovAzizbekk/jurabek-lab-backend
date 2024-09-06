package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;

public interface OrderService {

    ResponseEntity<?> getAll(int page, int size);

    ResponseEntity<?> edit(Long id, OrderReqDTO dto);

}
