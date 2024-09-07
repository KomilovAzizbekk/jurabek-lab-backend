package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mediasolutions.jurabeklabbackend.entity.Order;
import uz.mediasolutions.jurabeklabbackend.entity.Transaction;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Order2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderProductDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.OrderReqDTO;
import uz.mediasolutions.jurabeklabbackend.repository.OrderRepository;
import uz.mediasolutions.jurabeklabbackend.repository.TransactionRepository;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.OrderService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service("adminOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String status) {
        Page<Order2DTO> allOrders = orderRepository.getAllOrders(status, PageRequest.of(page, size));
        return ResponseEntity.ok(allOrders);
    }

    @Override
    public ResponseEntity<?> edit(Long id, OrderReqDTO dto) {
        return null;
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
