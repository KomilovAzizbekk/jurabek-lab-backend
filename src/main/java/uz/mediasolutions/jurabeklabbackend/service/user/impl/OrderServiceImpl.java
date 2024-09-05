package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mediasolutions.jurabeklabbackend.entity.*;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderProductDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.repository.OrderProductRepository;
import uz.mediasolutions.jurabeklabbackend.repository.OrderRepository;
import uz.mediasolutions.jurabeklabbackend.repository.PharmacyRepository;
import uz.mediasolutions.jurabeklabbackend.repository.ProductRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.OrderService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.ArrayList;
import java.util.List;

@Service("userOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public ResponseEntity<Page<?>> findAll(int page, int size, String status) {
        return null;
    }

    @Override
    public ResponseEntity<?> getById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<?> add(OrderReqDTO dto) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            throw RestException.restThrow("Couldn't recognize user", HttpStatus.UNAUTHORIZED);
        }

        Pharmacy pharmacy = pharmacyRepository.findById(dto.getPharmacyId()).orElseThrow(
                () -> new RestException("Pharmacy not found", HttpStatus.NOT_FOUND)
        );

        Order order = Order.builder()
                .user(user)
                .totalPrice(dto.getTotalPrice())
                .status(OrderStatus.SENT)
                .pharmacy(pharmacy)
                .pharmacyPhoneNumber(dto.getPharmacyPhoneNumber())
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderProductDTO> products = dto.getProducts();
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductDTO product : products) {

            Product product1 = productRepository.findById(product.getProductId()).orElseThrow(
                    () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
            );

            OrderProduct orderProduct = OrderProduct.builder()
                    .order(savedOrder)
                    .product(product1)
                    .quantity(product.getQuantity())
                    .build();

            orderProducts.add(orderProduct);
        }

        orderProductRepository.saveAll(orderProducts);

        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
    }
}
