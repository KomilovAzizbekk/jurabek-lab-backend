package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.req.AdminReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.UUID;

@RequestMapping(Rest.BASE_PATH + "admin/users")
public interface UserController {

    @GetMapping("/get-me")
    ResponseEntity<?> getMe();

    @GetMapping("/get-all-users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                  @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                  @RequestParam(defaultValue = "") String search);

    @GetMapping("/get-all-admins")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    ResponseEntity<?> getAllAdmins(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                   @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                   @RequestParam(defaultValue = "") String search);

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
