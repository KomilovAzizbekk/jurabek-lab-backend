package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "app/notification")
public interface NotificationController {

    @GetMapping("/get-mine")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<Page<?>> getMyNotifications(int page, int size);

    @GetMapping("/get-mine/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> getMyNotification(@PathVariable Long id);

}
