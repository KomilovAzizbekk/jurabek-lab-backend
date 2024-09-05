package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "app/orders")
public interface OrderController {

    @GetMapping("/get-mine")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<Page<?>> findAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                    @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                    @RequestParam(defaultValue = "") String status);

    @GetMapping("/get/{id}")
    ResponseEntity<?> getById(@PathVariable Long id);

    @PostMapping("/add")
    ResponseEntity<?> add(@RequestBody @Valid OrderReqDTO dto);

}
