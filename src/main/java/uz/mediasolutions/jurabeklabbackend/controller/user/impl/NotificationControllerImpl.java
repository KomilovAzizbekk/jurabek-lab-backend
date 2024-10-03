package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.NotificationController;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.NotificationService;

@RestController
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

    private final NotificationService service;

    @Override
    public ResponseEntity<Page<?>> getMyNotifications(int page, int size) {
        return service.getMyNotifications(page, size);
    }

    @Override
    public ResponseEntity<?> getMyNotification(Long id) {
        return service.getMyNotification(id);
    }
}
