package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.AdminReqDTO;

import java.util.UUID;

public interface UserService {

    ResponseEntity<?> getAllUsers(int page, int size, String search);

    ResponseEntity<?> getAllAdmins(int page, int size, String search);

    ResponseEntity<?> addAdmin(AdminReqDTO dto);

    ResponseEntity<?> editAdmin(UUID id, AdminReqDTO dto);

    ResponseEntity<?> deleteAdmin(UUID id);

    ResponseEntity<?> getMe();

    ResponseEntity<?> blockUser(UUID id, boolean block);
}
