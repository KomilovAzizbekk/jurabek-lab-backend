package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.AuthController;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignUpDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenUserDTO;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.AuthService;

@RestController("userAuthController")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService service;

    @Override
    public ResponseEntity<?> signIn(String lang, SignInDTO dto) {
        return service.signIn(lang, dto);
    }

    @Override
    public ResponseEntity<TokenUserDTO> signUp(String lang, SignUpDTO dto) {
        return service.signUp(lang, dto);
    }
}
