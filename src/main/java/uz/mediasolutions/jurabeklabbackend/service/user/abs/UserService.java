package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProfileReqDTO;

import java.util.UUID;

public interface UserService {

    ResponseEntity<?> edit(UUID id, ProfileReqDTO dto);

    ResponseEntity<?> delete(UUID id);
}
