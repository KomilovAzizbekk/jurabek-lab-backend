package uz.mediasolutions.jurabeklabbackend.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Order;
import uz.mediasolutions.jurabeklabbackend.entity.Pharmacy;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;
import uz.mediasolutions.jurabeklabbackend.repository.OrderRepository;
import uz.mediasolutions.jurabeklabbackend.repository.PharmacyRepository;

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

    @Scheduled(fixedRate = 1200000)
    public void scheduleRejectOrder() {
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(72);
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

        for (Order order : orders) {
            Pharmacy pharmacy = pharmacyMap.get(order.getPharmacyId());
            if (pharmacy != null) {
                pharmacy.setEnableOrder(true);
                pharmacyList.add(pharmacy);
            }
            order.setStatus(OrderStatus.REJECTED);
            orderList.add(order);
        }

        orderRepository.saveAll(orderList);
        pharmacyRepository.saveAll(pharmacyList);
    }


}
