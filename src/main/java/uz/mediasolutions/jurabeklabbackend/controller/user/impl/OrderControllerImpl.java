package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.OrderController;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.OrderService;

@RestController("userOrderController")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService service;

    @Override
    public ResponseEntity<Page<?>> findAll(int page, int size, String status) {
        return service.findAll(page, size, status);
    }

    @Override
    public ResponseEntity<?> getById(Long id) {
        return service.getById(id);
    }

    @Override
    public ResponseEntity<?> add(OrderReqDTO dto) {
        return service.add(dto);
    }
}
