package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Order2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderProductDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReq2DTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "admin/orders")
public interface OrderController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order2DTO.class))})
    })
    ResponseEntity<?> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                             @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                             @RequestParam(required = false) String status);

    @GetMapping("/get-order-products/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderProductDTO.class))})
    })
    ResponseEntity<Page<?>> getAllOrderProducts(@PathVariable Long orderId,
                                                @RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size);

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<?> edit(@PathVariable Long id,
                           @RequestBody @Valid OrderReq2DTO dto);

    @PatchMapping("/accept/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<?> accept(@PathVariable Long id,
                             @RequestParam(defaultValue = "false") boolean accept);

}
