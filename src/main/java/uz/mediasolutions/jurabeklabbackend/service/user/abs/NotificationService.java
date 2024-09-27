package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    ResponseEntity<Page<?>> getMyNotifications(int page, int size);

    ResponseEntity<?> getMyNotification(Long id);
}
