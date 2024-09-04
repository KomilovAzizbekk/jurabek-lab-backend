package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInAdminDTO;

public interface AdminAuthService {

    ResponseEntity<?> signIn(SignInAdminDTO dto);

}
