package uz.mediasolutions.jurabeklabbackend.controller.common.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.common.abs.OrderStatusController;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderStatusControllerImpl implements OrderStatusController {

    @Override
    public ResponseEntity<List<OrderStatus>> getOrderStatuses() {
        return ResponseEntity.ok(Arrays.asList(OrderStatus.values()));
    }

}
