package uz.mediasolutions.jurabeklabbackend.controller.common.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.List;

@RequestMapping(Rest.BASE_PATH + "order-statuses")
public interface OrderStatusController {

    @GetMapping
    ResponseEntity<List<OrderStatus>> getOrderStatuses();

}
