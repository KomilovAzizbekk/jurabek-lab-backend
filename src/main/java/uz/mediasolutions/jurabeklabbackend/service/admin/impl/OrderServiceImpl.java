package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.OrderService;

@Service("adminOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public ResponseEntity<?> getAll(int page, int size) {
        return null;
    }

    @Override
    public ResponseEntity<?> edit(Long id, OrderReqDTO dto) {
        return null;
    }

}
