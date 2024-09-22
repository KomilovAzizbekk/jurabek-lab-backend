package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Product2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.ProductDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "app/products")
public interface ProductController {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))})
    })
    ResponseEntity<?> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                             @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                             @RequestParam(required = false) String search);

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product2DTO.class))})
    })
    ResponseEntity<Page<?>> getByOrderId(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                         @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                         @PathVariable("orderId") Long orderId);

}
