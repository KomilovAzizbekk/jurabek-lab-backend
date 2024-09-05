package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Product;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.ProductDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProductReqDTO;
import uz.mediasolutions.jurabeklabbackend.repository.ProductRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.AdminProductService;
import uz.mediasolutions.jurabeklabbackend.service.common.impl.FileServiceImpl;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final FileServiceImpl fileService;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        Page<ProductDTO> allProducts = productRepository.findAllWithSearch(search, PageRequest.of(page, size));
        return ResponseEntity.ok(allProducts);
    }

    @Override
    public ResponseEntity<?> add(ProductReqDTO dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .build();
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
    }

    @Override
    public ResponseEntity<?> edit(Long id, ProductReqDTO dto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
        );
        Optional.ofNullable(dto.getName()).ifPresent(product::setName);
        Optional.ofNullable(dto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(dto.getImageUrl()).ifPresent(imageUrl -> {
            String oldImage = product.getImageUrl();
            if (!oldImage.equals(imageUrl)) {
                try {
                    fileService.deleteFile(oldImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                product.setImageUrl(imageUrl);
            }
        });
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
        );
        try {
            fileService.deleteFile(product.getImageUrl());
            productRepository.delete(product);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Rest.DELETED);
        } catch (Exception e) {
            throw RestException.restThrow("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
