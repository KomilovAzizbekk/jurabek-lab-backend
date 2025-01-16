package uz.mediasolutions.jurabeklabbackend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Constants;
import uz.mediasolutions.jurabeklabbackend.entity.Order;
import uz.mediasolutions.jurabeklabbackend.entity.Pharmacy;
import uz.mediasolutions.jurabeklabbackend.entity.Transaction;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.repository.ConstantsRepository;
import uz.mediasolutions.jurabeklabbackend.repository.OrderRepository;
import uz.mediasolutions.jurabeklabbackend.repository.PharmacyRepository;
import uz.mediasolutions.jurabeklabbackend.repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleOrderService {

    private final PharmacyRepository pharmacyRepository;
    private final OrderRepository orderRepository;
    private final ConstantsRepository constantsRepository;
    private final TransactionRepository transactionRepository;

    @Scheduled(fixedRate = 43200000)
    public void scheduleRejectOrder() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(1);
        Timestamp cutoffTime = Timestamp.valueOf(localDateTime);

        List<Order> orders = orderRepository.findPendingOrdersBefore(cutoffTime);
        if (orders.isEmpty()) {
            return;
        }

        Set<Long> pharmacyIds = orders.stream()
                .map(Order::getPharmacyId)
                .collect(Collectors.toSet());

        List<Pharmacy> pharmacies = pharmacyRepository.findByIdInAndDeletedFalse(pharmacyIds);

        Map<Long, Pharmacy> pharmacyMap = pharmacies.stream()
                .collect(Collectors.toMap(Pharmacy::getId, pharmacy -> pharmacy));

        List<Order> orderList = new ArrayList<>();
        List<Pharmacy> pharmacyList = new ArrayList<>();

        Constants constants = constantsRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Constants not found", HttpStatus.NOT_FOUND)
        );

        float f = (float) constants.getCashbackPercent() / 100;

        for (Order order : orders) {
            Pharmacy pharmacy = pharmacyMap.get(order.getPharmacyId());
            if (pharmacy != null) {
                pharmacy.setEnableOrder(true);
                pharmacyList.add(pharmacy);

                BigDecimal income = order.getTotalPrice().multiply(BigDecimal.valueOf(f));

                Transaction transaction = Transaction.builder()
                        .type(TransactionType.INCOME)
                        .user(order.getUser())
                        .amount(income)
                        .status(TransactionStatus.REJECTED)
                        .pharmacyId(pharmacy.getId())
                        .pharmacyName(pharmacy.getName())
                        .build();
                transactionRepository.save(transaction);
            }
            order.setStatus(OrderStatus.REJECTED);
            orderList.add(order);
        }

        orderRepository.saveAll(orderList);
        pharmacyRepository.saveAll(pharmacyList);
    }


}
