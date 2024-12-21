package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Product2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.ProductDTO;
import uz.mediasolutions.jurabeklabbackend.repository.ProductRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.ProductService;

@Service("userProductService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        Page<ProductDTO> allProducts = productRepository.findAllWithSearchForApp(search, PageRequest.of(page, size));
        return ResponseEntity.ok(allProducts);
    }

    @Override
    public ResponseEntity<Page<?>> getByOrderId(int page, int size, Long orderId) {
        Page<Product2DTO> product2DTOS = productRepository.findAllByOrderId(orderId, PageRequest.of(page, size));
        return ResponseEntity.ok(product2DTOS);
    }
}
