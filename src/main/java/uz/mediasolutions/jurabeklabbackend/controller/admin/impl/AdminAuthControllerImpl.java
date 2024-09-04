package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.AdminAuthController;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInAdminDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.AdminAuthService;

@RestController
@RequiredArgsConstructor
public class AdminAuthControllerImpl implements AdminAuthController {

    private final AdminAuthService service;

    @Override
    public ResponseEntity<?> signIn(SignInAdminDTO dto) {
        return service.signIn(dto);
    }
}
