package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "app/orders")
public interface OrderController {

    @GetMapping("/get-mine")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))})
    })
    ResponseEntity<Page<?>> findAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                    @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                    @RequestParam(defaultValue = "") String status);

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))})
    })
    ResponseEntity<?> getById(@PathVariable Long id);

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> add(@RequestBody @Valid OrderReqDTO dto);

}
