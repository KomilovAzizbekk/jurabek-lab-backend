package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.ProductDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "admin/products")
public interface ProductController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))})
    })
    ResponseEntity<?> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                             @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                             @RequestParam(required = false) String search);

    @PostMapping(value = "/add-by-file", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<?> add(@RequestParam("file") MultipartFile file);

    @PatchMapping("/edit-image/{id}")
    ResponseEntity<?> editImage(@PathVariable("id") Long id,
                                @RequestBody @Valid String imageUrl);

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<?> delete(@PathVariable Long id);

}
