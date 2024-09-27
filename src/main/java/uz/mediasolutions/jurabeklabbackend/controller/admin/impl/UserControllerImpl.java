package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.UserController;
import uz.mediasolutions.jurabeklabbackend.payload.req.AdminReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.UserService;

import java.util.UUID;

@RestController("adminUserController")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Override
    public ResponseEntity<?> getMe() {
        return service.getMe();
    }

    @Override
    public ResponseEntity<?> getAllUsers(int page, int size, String search) {
        return service.getAllUsers(page, size, search);
    }

    @Override
    public ResponseEntity<?> getAllAdmins(int page, int size, String search) {
        return service.getAllAdmins(page, size, search);
    }

    @Override
    public ResponseEntity<?> addAdmin(AdminReqDTO dto) {
        return service.addAdmin(dto);
    }

    @Override
    public ResponseEntity<?> editAdmin(UUID id, AdminReqDTO dto) {
        return service.editAdmin(id, dto);
    }

    @Override
    public ResponseEntity<?> deleteAdmin(UUID id) {
        return service.deleteAdmin(id);
    }
}
