package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.ProductController;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProductEditDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.ProductService;

@RestController("adminProductController")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService service;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        return service.getAll(page, size, search);
    }

    @Override
    public ResponseEntity<?> add(MultipartFile file) {
        return service.add(file);
    }

    @Override
    public ResponseEntity<?> edit(Long id, ProductEditDTO dto) {
        return service.edit(id, dto);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        return service.delete(id);
    }
}
