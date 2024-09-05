package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.AdminProductController;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProductReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.AdminProductService;

@RestController
@RequiredArgsConstructor
public class AdminProductControllerImpl implements AdminProductController {

    private final AdminProductService service;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        return service.getAll(page, size, search);
    }

    @Override
    public ResponseEntity<?> add(ProductReqDTO dto) {
        return service.add(dto);
    }

    @Override
    public ResponseEntity<?> edit(Long id, ProductReqDTO dto) {
        return service.edit(id, dto);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        return service.delete(id);
    }
}
