package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "app/notification")
public interface NotificationController {

    @GetMapping("/get-mine")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<Page<?>> getMyNotifications(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                               @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size);

    @GetMapping("/get-mine/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> getMyNotification(@PathVariable Long id);

}
