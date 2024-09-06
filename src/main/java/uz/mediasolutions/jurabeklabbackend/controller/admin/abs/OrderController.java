package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "admin/orders")
public interface OrderController {

    @GetMapping
    ResponseEntity<?> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                             @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size);

    @PatchMapping("/edit/{id}")
    ResponseEntity<?> edit(@PathVariable Long id,
                           @RequestBody OrderReqDTO dto);

}
