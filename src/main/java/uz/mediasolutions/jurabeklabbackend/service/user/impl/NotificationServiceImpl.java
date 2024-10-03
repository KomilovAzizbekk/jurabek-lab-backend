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

        notification.setViewed(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok(notification);
    }
}
