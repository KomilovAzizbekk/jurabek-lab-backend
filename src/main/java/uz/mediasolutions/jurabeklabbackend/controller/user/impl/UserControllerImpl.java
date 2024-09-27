package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.UserController;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProfileReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.UserService;

import java.util.UUID;

@RestController("userUserController")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Override
    public ResponseEntity<?> edit(UUID id, ProfileReqDTO dto) {
        return service.edit(id, dto);
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        return service.delete(id);
    }
}
