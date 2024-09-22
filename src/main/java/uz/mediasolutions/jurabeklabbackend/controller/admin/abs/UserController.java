package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.AdminDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.UserDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.AdminReqDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.MeResDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.UUID;

@RequestMapping(Rest.BASE_PATH + "admin/users")
public interface UserController {

    @GetMapping("/get-me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeResDTO.class))})
    })
    ResponseEntity<?> getMe();

    @GetMapping("/get-all-users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))})
    })
    ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                  @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                  @RequestParam(required = false) String search);

    @GetMapping("/get-all-admins")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminDTO.class))})
    })
    ResponseEntity<?> getAllAdmins(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                   @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                   @RequestParam(required = false) String search);

    @PostMapping("/add-admin")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    ResponseEntity<?> addAdmin(@RequestBody @Valid AdminReqDTO dto);

    @PatchMapping("/edit-admin/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    ResponseEntity<?> editAdmin(@PathVariable UUID id,
                                @RequestBody AdminReqDTO dto);

    @DeleteMapping("/delete-admin/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    ResponseEntity<?> deleteAdmin(@PathVariable UUID id);
}
