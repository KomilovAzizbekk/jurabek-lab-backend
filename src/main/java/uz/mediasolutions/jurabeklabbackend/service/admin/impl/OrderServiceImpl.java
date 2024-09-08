package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mediasolutions.jurabeklabbackend.entity.*;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Order2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderProductDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReq2DTO;
import uz.mediasolutions.jurabeklabbackend.repository.*;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.OrderService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("adminOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String status) {
        Page<Order2DTO> allOrders = orderRepository.getAllOrders(status, PageRequest.of(page, size));
        return ResponseEntity.ok(allOrders);
    }

    @Override
    @Transactional
    public ResponseEntity<?> edit(Long id, OrderReq2DTO dto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Order not found", HttpStatus.NOT_FOUND)
        );
        Optional.ofNullable(dto.getPharmacyPhoneNumber()).ifPresent(order::setPharmacyPhoneNumber);
        orderRepository.save(order);

        List<OrderProduct> orderProducts = new ArrayList<>();

        for (uz.mediasolutions.jurabeklabbackend.payload.req.OrderProductDTO product : dto.getProducts()) {
            OrderProduct orderProduct = orderProductRepository.findByOrderId(id).orElseThrow(
                    () -> RestException.restThrow("Order Product not found", HttpStatus.NOT_FOUND)
            );

            Optional.ofNullable(product.getProductId()).ifPresent(productId -> {
                Product product1 = productRepository.findByIdAndDeletedFalse(productId).orElseThrow(
                        () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
                );
                orderProduct.setProduct(product1);
            });
            Optional.ofNullable(product.getQuantity()).ifPresent(orderProduct::setQuantity);
            orderProducts.add(orderProduct);
        }
        orderProductRepository.saveAll(orderProducts);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }

    @Override
    @Transactional
    public ResponseEntity<?> accept(Long id, boolean accept) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Order not found", HttpStatus.NOT_FOUND)
        );

        if (accept) {
            order.setStatus(OrderStatus.CONFIRMED);
            order.setAcceptedTime(new Timestamp(System.currentTimeMillis()));
        } else {
            order.setStatus(OrderStatus.REJECTED);
        }
        orderRepository.save(order);

        BigDecimal income = order.getTotalPrice().divideToIntegralValue(BigDecimal.valueOf(10));

        Transaction transaction = Transaction.builder()
                .type(TransactionType.INCOME)
                .user(order.getUser())
                .amount(income)
                .status(TransactionStatus.DONE)
                .pharmacy(order.getPharmacy())
                .build();

        transactionRepository.save(transaction);

        User user = order.getUser();
        user.setBalance(user.getBalance().add(income));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }

    @Override
    public ResponseEntity<Page<?>> getAllOrderProducts(Long orderId, int page, int size) {
        Page<OrderProductDTO> orderProducts = orderRepository.getOrderProducts(orderId, PageRequest.of(page, size));
        return ResponseEntity.ok(orderProducts);
    }

}
