package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProfileReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.UUID;

@RequestMapping(Rest.BASE_PATH + "app/user-profile")
public interface UserController {

    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> edit(@PathVariable UUID id, ProfileReqDTO dto);

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> delete(@PathVariable UUID id);

}
