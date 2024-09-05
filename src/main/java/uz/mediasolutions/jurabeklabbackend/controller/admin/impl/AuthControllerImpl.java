package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.AuthController;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInAdminDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.AuthService;

@RestController("adminAuthController")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService service;

    @Override
    public ResponseEntity<?> signIn(SignInAdminDTO dto) {
        return service.signIn(dto);
    }
}
