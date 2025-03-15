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
import java.util.stream.Collectors;

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
    public ResponseEntity<?> getById(Long id) {
        Order2DTO allOrders = orderRepository.getOrderById(id);
        return ResponseEntity.ok(allOrders);
    }

    @Override
    @Transactional
    public ResponseEntity<?> edit(Long id, OrderReq2DTO dto) {
        // Buyurtma mavjudligini tekshirish
        Order order = orderRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Order not found", HttpStatus.NOT_FOUND)
        );
        if (!order.getStatus().equals(OrderStatus.SENT)) {
            throw RestException.restThrow("You cannot edit ACCEPTED or REJECTED orders", HttpStatus.BAD_REQUEST);
        }
        BigDecimal totalPrice = new BigDecimal(0);

        // O'zgartiriladigan OrderProductlar ro'yxati
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<OrderProduct> existedOrderProducts = orderProductRepository.findAllByOrderId(id);

        // DTO ichidagi mahsulotlar ustida aylanish
        for (OrderProductReqDTO product : dto.getProducts()) {
            Product product1 = productRepository.findByIdAndDeletedFalse(product.getProductId())
                    .orElseThrow(() -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND));

            totalPrice = totalPrice.add(product1.getPrice()
                    .multiply(BigDecimal.valueOf(product.getQuantity()))
                    .multiply(BigDecimal.valueOf(1-product.getDiscountPercent()/100)));

            // OrderProduct mavjudligini tekshirish
            OrderProduct op = existedOrderProducts.stream()
                    .filter(existedOrderProduct -> existedOrderProduct.getProductId().equals(product1.getId()))
                    .findFirst()
                    .orElse(null);

            if (op != null) { // Agar mavjud bo'lsa
                op.setQuantity(product.getQuantity());
                op.setDiscountPercent(product.getDiscountPercent());
                orderProducts.add(op); // O'zgartirilgan instansiyani qo'shamiz
            } else { // Yangi OrderProduct yaratish
                OrderProduct orderProduct = OrderProduct.builder()
                        .order(order)
                        .discountPercent(product.getDiscountPercent())
                        .productId(product1.getId())
                        .productName(product1.getName())
                        .quantity(product.getQuantity())
                        .build();
                orderProducts.add(orderProduct);
            }
        }

        // Orderni tahrirlash
        order.setTotalPrice(totalPrice);
        order.setPharmacyPhoneNumber(dto.getPharmacyPhoneNumber());
        orderRepository.save(order);

        // Yangi OrderProductlarni saqlash
        orderProductRepository.saveAll(orderProducts);

        // Qolganini o'chirish
        List<OrderProduct> leftOrderProducts = existedOrderProducts.stream()
                .filter(existedOrderProduct -> !orderProducts.contains(existedOrderProduct))
                .collect(Collectors.toList());

        orderProductRepository.deleteAll(leftOrderProducts);
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

        if (!order.getStatus().equals(OrderStatus.SENT)) {
            throw RestException.restThrow("Order is already processed", HttpStatus.CONFLICT);
        }
        Transaction transaction;

        if (accept) {
            user.setBalance(user.getBalance().add(income));
            notification.setType(NotificationType.ORDER_CONFIRMED);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setAcceptedTime(new Timestamp(System.currentTimeMillis()));

            transaction = Transaction.builder()
                    .type(TransactionType.INCOME)
                    .user(order.getUser())
                    .amount(income)
                    .status(TransactionStatus.DONE)
                    .pharmacyId(pharmacy.getId())
                    .pharmacyName(pharmacy.getName())
                    .build();
        } else {
            notification.setType(NotificationType.ORDER_CANCELLED);
            order.setStatus(OrderStatus.REJECTED);

            transaction = Transaction.builder()
                    .type(TransactionType.INCOME)
                    .user(order.getUser())
                    .amount(income)
                    .status(TransactionStatus.REJECTED)
                    .pharmacyId(pharmacy.getId())
                    .pharmacyName(pharmacy.getName())
                    .build();
        }
        pharmacy.setEnableOrder(true);

        pharmacyRepository.save(pharmacy);
        orderRepository.save(order);
        notificationRepository.save(notification);

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
