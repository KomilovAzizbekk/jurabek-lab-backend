package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.ProductController;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.ProductService;

@RestController("userProductController")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService service;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        return service.getAll(page, size, search);
    }
}
