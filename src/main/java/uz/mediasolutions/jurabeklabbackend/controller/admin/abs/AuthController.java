package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInAdminDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "admin/auth")
public interface AuthController {

    @PostMapping("/sign-in")
    ResponseEntity<?> signIn(@RequestBody SignInAdminDTO dto);

}
