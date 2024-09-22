package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.UUID;

@RequestMapping(Rest.BASE_PATH + "admin/transactions")
public interface TransactionController {

    @GetMapping("/all")
    ResponseEntity<Page<?>> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                   @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                   @RequestParam(required = false) String status);

    @PatchMapping("/paid-or-reject/{id}")
    ResponseEntity<?> paidOrRejected(@PathVariable UUID id,
                                     @RequestParam boolean paidOrRejected);

}
