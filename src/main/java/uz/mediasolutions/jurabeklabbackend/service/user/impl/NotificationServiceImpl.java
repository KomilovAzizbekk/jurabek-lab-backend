package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Notification;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.NotificationDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.NotificationResDTO;
import uz.mediasolutions.jurabeklabbackend.repository.NotificationRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.NotificationService;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<Page<?>> getMyNotifications(int page, int size) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<NotificationDTO> myNotifications = notificationRepository.findMyNotifications(user.getId(), PageRequest.of(page, size));
        return ResponseEntity.ok(myNotifications);
    }

    @Override
    public ResponseEntity<?> getMyNotification(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Notification not found", HttpStatus.NOT_FOUND)
        );

        if (!notification.getUser().getId().equals(user.getId())) {
            throw RestException.restThrow("Notification does not belong to you", HttpStatus.FORBIDDEN);
        }

        if (!notification.isViewed()) {
            notification.setViewed(true);
            notification = notificationRepository.save(notification);
        }

        NotificationResDTO dto = NotificationResDTO.builder()
                .id(notification.getId())
                .cardNumber(notification.getCardNumber())
                .amount(notification.getAmount())
                .viewed(notification.isViewed())
                .orderId(notification.getOrderId())
                .transactionId(notification.getTransactionId())
                .cardName(notification.getCardName())
                .type(notification.getType().name())
                .createdTime(notification.getCreatedAt())
                .build();

        return ResponseEntity.ok(dto);
    }
}
