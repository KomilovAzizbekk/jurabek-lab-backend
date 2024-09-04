package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.RoleController;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;

@RestController
public class RoleControllerImpl implements RoleController {

    @Override
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(RoleName.values());
    }
}
