package uz.mediasolutions.jurabeklabbackend.controller.common.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.common.abs.RefreshTokenController;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;
import uz.mediasolutions.jurabeklabbackend.service.common.abs.RefreshTokenService;

@RestController
@RequiredArgsConstructor
public class RefreshTokenControllerImpl implements RefreshTokenController {

    private final RefreshTokenService service;

    @Override
    public ResponseEntity<TokenDTO> refresh(HttpServletRequest request) {
        return service.refresh(request);
    }
}
