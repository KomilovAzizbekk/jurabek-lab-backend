package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.OrderController;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.OrderService;

@RestController("adminOrderController")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService service;

    @Override
    public ResponseEntity<?> getAll(int page, int size) {
        return service.getAll(page, size);
    }

    @Override
    public ResponseEntity<?> edit(Long id, OrderReqDTO dto) {
        return service.edit(id, dto);
    }

}
