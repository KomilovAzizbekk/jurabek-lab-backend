package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.UserProductController;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.UserProductService;

@RestController
@RequiredArgsConstructor
public class UserProductControllerImpl implements UserProductController {

    private final UserProductService service;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        return service.getAll(page, size, search);
    }
}
