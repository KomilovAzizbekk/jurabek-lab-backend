package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProfileReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.UUID;

@RequestMapping(Rest.BASE_PATH + "app/user-profile")
public interface UserController {

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> edit(@PathVariable UUID id,
                           @RequestBody @Valid ProfileReqDTO dto);

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> delete(@PathVariable UUID id);

}
