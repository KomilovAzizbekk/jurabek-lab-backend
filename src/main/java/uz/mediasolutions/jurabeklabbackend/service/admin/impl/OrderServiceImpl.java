package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mediasolutions.jurabeklabbackend.entity.*;
import uz.mediasolutions.jurabeklabbackend.enums.NotificationType;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Order2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderProductDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderProductReqDTO;
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
    private final PharmacyRepository pharmacyRepository;
    private final NotificationRepository notificationRepository;
    private final ConstantsRepository constantsRepository;

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
        order.setPharmacyPhoneNumber(dto.getPharmacyPhoneNumber());
        orderRepository.save(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        List<OrderProduct> existedOrderProducts = orderProductRepository.findAllByOrderId(id);

        for (OrderProductReqDTO product : dto.getProducts()) {
            boolean existed = false;
            OrderProduct op = null; // O'zgaruvchini null bilan boshlash

            Product product1 = productRepository.findByIdAndDeletedFalse(product.getProductId()).orElseThrow(
                    () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
            );

            for (OrderProduct existedOrderProduct : existedOrderProducts) {
                if (existedOrderProduct.getProductId().equals(product1.getId())) {
                    existed = true;
                    op = existedOrderProduct;
                    break; // Topilganda davom etishni to'xtating
                }
            }

            if (existed) {
                op.setQuantity(product.getQuantity());
                orderProducts.add(op); // O'chirilmagan op ni qo'shamiz
            } else {
                OrderProduct orderProduct = OrderProduct.builder()
                        .order(order)
                        .productId(product1.getId())
                        .productName(product1.getName())
                        .quantity(product.getQuantity())
                        .build();
                orderProducts.add(orderProduct);
            }
        }

        // O'chirilgan orderProductlarni o'chirish
        for (OrderProduct existedOrderProduct : existedOrderProducts) {
            if (orderProducts.stream().noneMatch(op -> op.getId().equals(existedOrderProduct.getId()))) {
                orderProductRepository.deleteById(existedOrderProduct.getId());
            }
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

        Pharmacy pharmacy = pharmacyRepository.findById(order.getPharmacyId()).orElseThrow(
                () -> RestException.restThrow("Pharmacy not found", HttpStatus.NOT_FOUND)
        );

        Constants constants = constantsRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Constants not found", HttpStatus.NOT_FOUND)
        );

        float f = (float) constants.getCashbackPercent() / 100;
        BigDecimal income = order.getTotalPrice().multiply(BigDecimal.valueOf(f));

        User user = order.getUser();

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setViewed(false);
        notification.setOrderId(order.getId().toString());
        notification.setAmount(income);

        if (accept) {
            user.setBalance(user.getBalance().add(income));
            notification.setType(NotificationType.ORDER_CONFIRMED);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setAcceptedTime(new Timestamp(System.currentTimeMillis()));
        } else {
            notification.setType(NotificationType.ORDER_CANCELLED);
            order.setStatus(OrderStatus.REJECTED);
        }
        orderRepository.save(order);
        notificationRepository.save(notification);

        Transaction transaction = Transaction.builder()
                .type(TransactionType.INCOME)
                .user(order.getUser())
                .amount(income)
                .status(TransactionStatus.DONE)
                .pharmacyId(pharmacy.getId())
                .pharmacyName(pharmacy.getName())
                .build();

        transactionRepository.save(transaction);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }

    @Override
    public ResponseEntity<Page<?>> getAllOrderProducts(Long orderId, int page, int size) {
        Page<OrderProductDTO> orderProducts = orderRepository.getOrderProducts(orderId, PageRequest.of(page, size));
        return ResponseEntity.ok(orderProducts);
    }

}
